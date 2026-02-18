package simulator.model;
import java.util.List;
import java.util.ArrayList;

public abstract class Region implements Entity, FoodSupplier, RegionInfo {
	protected List<Animal> animalList;  
	
	public Region() {
		animalList =  new ArrayList<>(); 
	}
	
	final void addAnimal (Animal a) {
		animalList.add(a); 
	}
	final void removeAnimal(Animal a) {
		animalList.remove(a); 
	}
	final List<Animal>getAnimals(){
		return new ArrayList(animalList); 
	}
}
