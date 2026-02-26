package simulator.model;
import java.util.List;

import org.json.JSONObject;
import org.json.*;
import java.util.ArrayList;
import java.util.Collections;

public abstract class Region implements Entity, FoodSupplier, RegionInfo {
	protected List<Animal> animalList;  
	
	public Region() {
		this.animalList =  new ArrayList<>(); 
	}
	
	public final void addAnimal (Animal a) {
		animalList.add(a); 
	}
	public final void removeAnimal(Animal a) {
		animalList.remove(a); 
	}
	public final List<Animal>getAnimals(){
		return Collections.unmodifiableList(animalList); 
	}
	
	public JSONObject asJSON() {
		JSONArray animalArray = new JSONArray();
		JSONObject obj = new JSONObject();

		for(Animal a: animalList) {
			animalArray.put(a.asJSON());
		}
		obj.put("animals", animalArray);
		return obj;
	}
	
}
