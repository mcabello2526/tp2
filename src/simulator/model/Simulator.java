package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import simulator.factories.Factory;
import simulator.model.Animal.State;

public class Simulator implements JSONable {
	private Factory<Animal> animalsFactory;
	private Factory<Region> regionsFactory;
	private RegionManager regionManager;
	private List<Animal> animalsList;
	private double time;

	public Simulator(int cols, int rows, int width, int height, Factory<Animal> animalsFactory, Factory<Region> regionsFactory) {
		this.animalsFactory = animalsFactory;
		this.regionsFactory = regionsFactory;
		this.regionManager = new RegionManager(cols, rows, width, height);
		this.animalsList = new ArrayList<>();
		this.time = 0.0;
	}
	
	//recibe obj --> private
	private void setRegion(int row, int col, Region r) {
		this.regionManager.setRegion(row, col, r);
	}
	
	public void setRegion(int row, int col, JSONObject rJson) {
		Region R = regionsFactory.createInstance(rJson);
		setRegion(row, col, R);	
	}
	
	//recibe obj --> private
	private void addAnimal(Animal a) {
		this.animalsList.add(a);
		this.regionManager.registerAnimal(a);
	}
	
	public void addAnimal(JSONObject aJson) {
		Animal a = animalsFactory.createInstance(aJson);
		addAnimal(a);
	}
	
	public MapInfo getMapInfo() {
		return regionManager;
	}
	
	public List<? extends AnimalInfo> getAnimals(){
		return Collections.unmodifiableList(animalsList);
	}
	
	public double getTime() {
		return this.time;
	}
	
	public void advance(double dt) {
/*1*/	time += dt;
/*2*/	List<Animal> deads = new ArrayList<>();
		for(Animal a: animalsList) {
			if(a.getState() == State.DEAD) {
				deads.add(a);
				regionManager.unregisterAnimal(a);
			}
		}
		animalsList.removeAll(deads);
		
/*3*/	for(Animal a: animalsList) {
			a.update(dt);
			regionManager.updateAnimalRegion(a);
		}
		
/*4*/	regionManager.updateAllRegions(dt);

/*5*/	List<Animal> baby = new ArrayList<>();
		for(Animal a: animalsList) {
			if(a.isPregnant() == true) {
				baby.add(a.deliverBaby());
				
			}
		}
		for(Animal a: baby) {
			this.addAnimal(a);
		}
	}
	
	public JSONObject asJSON() {
		JSONObject obj = new JSONObject(); 
		obj.put("time", time);
		obj.put("state", regionManager.asJSON());

		return obj;
	}

}
