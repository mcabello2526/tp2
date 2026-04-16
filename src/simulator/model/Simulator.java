package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import simulator.factories.Factory;
import simulator.model.Animal.State;
// añadir animales, modificar regiones, avanzar la simulacion 
public class Simulator implements JSONable, Observable<EcoSysObserver> {
	private Factory<Animal> animalsFactory;
	private Factory<Region> regionsFactory;
	private RegionManager regionManager;
	private List<Animal> animalsList;
	private List<EcoSysObserver> observerList; 
	private double time;

	public Simulator(int cols, int rows, int width, int height, Factory<Animal> animalsFactory,
			Factory<Region> regionsFactory) {
		if (animalsFactory == null || regionsFactory == null) {
			throw new IllegalArgumentException("One or both factories are null");
		}
		this.animalsFactory = animalsFactory;
		this.regionsFactory = regionsFactory;
		this.regionManager = new RegionManager(cols, rows, width, height);
		this.animalsList = new ArrayList<>();
		this.time = 0.0;
		this.observerList = new ArrayList<>(); 
	}

	private void setRegion(int row, int col, Region r) {
		this.regionManager.setRegion(row, col, r);
		notifyOnRegionSet(row, col, r); 
	}

	// recibe el json del controller crea la instancia y se la pasa al metodo privado
	public void setRegion(int row, int col, JSONObject rJson) {
		Region R = regionsFactory.createInstance(rJson);
		setRegion(row, col, R);
	}

	private void addAnimal(Animal a) {
		this.animalsList.add(a);
		this.regionManager.registerAnimal(a);
		notifyOnAnimalAdded(a); 
	}
	//igual que arriba este es para el controller 
	public void addAnimal(JSONObject aJson) {
		Animal a = animalsFactory.createInstance(aJson);
		addAnimal(a);
	}
// esto se puede hacer porque RegionManager implementa AnimalMapView
	// y AnimalMapView es subclase MapInfo 
	public MapInfo getMapInfo() {
		return regionManager;
	}

	// hay que poner ? extends porque la lista es de animales no de AnimalInfo 
	// luego hay que poner de AnimalInfo porque no queremos que ni el simulador ni el
	// controlador puedan acceder a los metodos de Animal
	
	public List<? extends AnimalInfo> getAnimals() {
		return Collections.unmodifiableList(animalsList);
	}

	public double getTime() {
		return this.time;
	}

	public void advance(double dt) {
		/* 1 */ time += dt;
		/* 2 */ List<Animal> deads = new ArrayList<>();
		for (Animal a : animalsList) {
			if (a.getState() == State.DEAD) {
				deads.add(a);
				regionManager.unregisterAnimal(a);
			}
		}
		animalsList.removeAll(deads);

		/* 3 */ for (Animal a : animalsList) {
			a.update(dt);
			regionManager.updateAnimalRegion(a);
		}

		/* 4 */ regionManager.updateAllRegions(dt);

		/* 5 */ List<Animal> baby = new ArrayList<>();
		for (Animal a : animalsList) {
			if (a.isPregnant() == true) {
				baby.add(a.deliverBaby());

			}
		}
		for (Animal a : baby) {
			this.addAnimal(a);
		}
		
		notifyOnAdvance(dt); 
	}
	
/*
	// streams 
	List<Animal> deads = new ArrayList<>();
	for(Animal a: animalsList) {
	    if(a.getState() == State.DEAD) {
	        deads.add(a);
	        regionManager.unregisterAnimal(a); // Efecto secundario
	    }
	}
	animalsList.removeAll(deads);
*/
	
	public JSONObject asJSON() {
		JSONObject obj = new JSONObject();
		obj.put("time", time);
		obj.put("state", regionManager.asJSON());

		return obj;
	}
	
	public void reset (int cols, int rows, int width, int height) {

		this.regionManager = new RegionManager(cols, rows , width, height); 
		this.animalsList = new ArrayList<>();
		this.time = 0.0; 
		notifyOnReset(); 
	}
	
	private void notifyOnRegister(EcoSysObserver o) {
		List<AnimalInfo>animals = new ArrayList<>(animalsList); 
		o.onRegister(time, regionManager, animals);
	}
	
	private void notifyOnReset() {
		List<AnimalInfo>animals = new ArrayList <>(animalsList); 
		for (EcoSysObserver o: observerList) {
			o.onReset(time, regionManager, animals);
		}
	}
	private void notifyOnAnimalAdded(Animal a) {
		List<AnimalInfo>animals = new ArrayList <>(animalsList); 
		for (EcoSysObserver o: observerList) {
			o.onAnimalAdded(time, regionManager, animals, a); 
		}
	}
	
	private void notifyOnRegionSet(int row, int col, Region r ) { // esto tiene que ser un region o un regionInfo?
		for (EcoSysObserver o: observerList) {
			o.onRegionSet(row, col, regionManager, r); 
		}
	}
	private void notifyOnAdvance(double dt) {
		List<AnimalInfo> animals = new ArrayList<>(animalsList); 
		for (EcoSysObserver o: observerList) {
			o.onAdvance(time, regionManager, animals, dt);
		}
	}

	@Override
	public void addObserver(EcoSysObserver o) {
		if (!observerList.contains(o)) {
			observerList.add(o); 
			notifyOnRegister(o); 
		}
		
	}

	@Override
	public void removeObserver(EcoSysObserver o) {
		Iterator<EcoSysObserver> it = observerList.iterator(); 
		while(it.hasNext() && !it.next().equals(o)) it.remove(); 
		
	}

}
