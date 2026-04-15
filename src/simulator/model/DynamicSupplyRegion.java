package simulator.model;

import simulator.misc.Utils;
import simulator.model.Animal.Diet;

public class DynamicSupplyRegion extends Region {
	private double food;  // cantidad de comida 
	private double factor; // factor de crecimiento
	final static double FOOD_EAT_RATE_HERBS = 60.0;
	final static double FOOD_SHORTAGE_TH_HERBS = 5.0;
	final static double FOOD_SHORTAGE_EXP_HERBS = 2.0;
	final static double INIT_FOOD = 100.0;
	final static double FACTOR = 2.0;

	public DynamicSupplyRegion(double initFood, double growthFactor) {
		super();
		if (initFood <= 0.0 || growthFactor < 0.0) {
			throw new IllegalArgumentException("The amount of food and growth factor need to be a double over 0.0");
		}
		this.food = initFood;
		this.factor = growthFactor;
	}

	@Override
	public void update(double dt) {
		if (Utils.RAND.nextDouble() < 0.5) {
			this.food += dt * factor;
		}
	}

	@Override
	public double getFood(AnimalInfo a, double dt) {
		if (a.getDiet() == Diet.CARNIVORE) {
			return Animal.MINIMUM_VALUE;
		}
		int n = 0;
		for (Animal animal : animalList) {
			if (animal.getDiet() == Diet.HERBIVORE) {
				n++;
			}
		}
		double finalFood = Math.min(food, 60.0 * Math.exp(-Math.max(0, n - 5.0) * 2.0) * dt);
		this.food -= finalFood;
		return finalFood;
	}
	
	public String toString () {
		return "Dynamic Suplly Region"; 
	}
}