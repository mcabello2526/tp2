package simulator.model;

import java.util.List;
import java.util.function.Predicate;
// lo que el animal puede ver del gestor de regiones 
//  mapa, pedir comida, y ver la lista de animales en su campo visual 
public interface AnimalMapView extends MapInfo, FoodSupplier {
	public List<Animal> getAnimalsInRange(Animal e, Predicate<Animal> filter);
	

}
