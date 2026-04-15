package simulator.model;

public interface FoodSupplier {
	// pedir comida para el animal durante dt segundos 
	double getFood(AnimalInfo a, double dt);
}
