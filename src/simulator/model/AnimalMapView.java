package simulator.model;

import java.util.List;
import java.util.function.Predicate;

public interface AnimalMapView extends MapInfo, FoodSupplier {  
	public List<Animal> getAnimalsInRange(Animal e, Predicate<Animal> filter);  
}

//Predicate <T> sirve para delegar en un valor booleano --> filtro para buscar animales