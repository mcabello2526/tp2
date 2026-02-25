package simulator.model;

import simulator.model.Animal.Diet;

//representa una región que da comida sólo a animales herbívoros. 

public class DefaultRegion extends Region {
	final static double FOOD_EAT_RATE_HERBS = 60.0;
	final static double FOOD_SHORTAGE_TH_HERBS = 5.0;
	final static double FOOD_SHORTAGE_EXP_HERBS = 2.0;
	public DefaultRegion() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(double dt) {
		// TODO Auto-generated method stub
		//no hace nada

	}

	@Override
	public double getFood(AnimalInfo a, double dt) {
		if(a.getDiet() == Diet.CARNIVORE) {
			return Animal.MINIMUM_VALUE;
		}
		int n = 0; 
		for(Animal animal : animalList) {
			if(animal.getDiet() == Diet.HERVIBORE) {
				n++;
			}
		}
		
		double food = FOOD_EAT_RATE_HERBS*Math.exp(-Math.max(0,n-FOOD_SHORTAGE_TH_HERBS)*FOOD_SHORTAGE_EXP_HERBS)*dt;
		//double food = 60.0*Math.exp(-Math.max(0,n-5.0)*2.0)*dt;

		return food;
	}

}
