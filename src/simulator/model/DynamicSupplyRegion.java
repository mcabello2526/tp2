package simulator.model;

import simulator.misc.Utils;
import simulator.model.Animal.Diet;

//representa una región que da comida sólo a animales herbívoros
//la cantidad de comida puede decrecer/crecer
public class DynamicSupplyRegion extends Region {
	private double food;
	private double factor;
	final static double FOOD_EAT_RATE_HERBS = 60.0;
	final static double FOOD_SHORTAGE_TH_HERBS = 5.0;
	final static double FOOD_SHORTAGE_EXP_HERBS = 2.0;
	final static double INIT_FOOD = 100.0;
	final static double FACTOR = 2.0;

	public DynamicSupplyRegion(double initFood, double growthFactor) {
		super();
		this.food = initFood;
		this.factor = growthFactor;
	}

	@Override
	public void update(double dt) {
		if(Utils.RAND.nextDouble() < 0.5) {
			this.food += dt*factor;
		}
	}

	@Override
	public double getFood(AnimalInfo a, double dt) {
		if(a.getDiet() == Diet.CARNIVORE) {
			return Animal.MINIMUM_VALUE;
		}
		int n = 0; 
		for(Animal animal : animalList) {
			if(animal.getDiet() == Diet.HERBIVORE) {
				n++;
			}
		}
		double finalFood = Math.min(food,60.0*Math.exp(-Math.max(0,n-5.0)*2.0)*dt);
		this.food -= finalFood;
		return finalFood;
	}
}