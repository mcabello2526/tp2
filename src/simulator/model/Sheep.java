package simulator.model;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Animal.Diet;

public class Sheep extends Animal{
	
	final static String SHEEP_GENETIC_CODE = "Sheep";
	final static double INIT_SIGHT_SHEEP = 40;
	final static double INIT_SPEED_SHEEP = 35;
	final static double BOOST_FACTOR_SHEEP = 2.0;
	final static double MAX_AGE_SHEEP = 8;
	final static double FOOD_DROP_BOOST_FACTOR_SHEEP = 1.2;
	final static double FOOD_DROP_RATE_SHEEP = 20.0;
	final static double DESIRE_THRESHOLD_SHEEP = 65.0;
	final static double DESIRE_INCREASE_RATE_SHEEP = 40.0;
	final static double PREGNANT_PROBABILITY_SHEEP = 0.9;
	
	private Animal dangerSource ;  // animal que se considera peligro
	private SelectionStrategy dangerStrategy; // peligro de la lista de animal en la lista de la region 
	
	
	public Sheep(SelectionStrategy mateStrategy, SelectionStrategy dangerStrategy,  Vector2D pos) {
		super (SHEEP_GENETIC_CODE, Diet.HERVIBORE, INIT_SIGHT_SHEEP, INIT_SPEED_SHEEP, mateStrategy, pos ); 
		this.dangerStrategy = dangerStrategy; 
		
	}
	
	protected Sheep(Sheep p1, Sheep p2) {
		super(p1, p2);
		this.dangerStrategy = p1.dangerStrategy; 
		this.dangerSource = null; 
	}
	

// hay que preguntar si estos metodos pueden ser implementados en Animal
	@Override
	public State getState() {
		// TODO Auto-generated method stub
		return this.state;
	}

	@Override
	public Vector2D getPosition() {
		// TODO Auto-generated method stub
		return this.pos;
	}

	@Override
	public String getGeneticCode() {
		// TODO Auto-generated method stub
		return this.geneticCode;
	}

	@Override
	public Diet getDiet() {
		// TODO Auto-generated method stub
		return this.diet;
	}

	@Override
	public double getSpeed() {
		// TODO Auto-generated method stub
		return this.speed;
	}

	@Override
	public double getSightRange() {
		// TODO Auto-generated method stub
		return this.sightRange;
	}

	@Override
	public double getEnergy() {
		// TODO Auto-generated method stub
		return this.energy;
	}

	@Override
	public double getAge() {
		// TODO Auto-generated method stub
		return this.age;
	}

	@Override
	public Vector2D getDestination() {
		// TODO Auto-generated method stub
		return this.dest;
	}

	@Override
	public boolean isPregnant() {
		// TODO Auto-generated method stub
		return this.baby != null ;
	}

	@Override
	public void update(double dt) {
		// TODO Auto-generated method stub
/*1*/		if (!this.state.equals(State.DEAD)) {
/*2*/		switch (state) {
			case NORMAL: 
				stateNormal(dt); 
				break; 
			case MATE: 
				stateMate(dt); 
				break; 
			case HUNGER: 
				stateHunger(dt); 
				break; 
			case DANGER: 
				stateDanger(dt); 
				break; 
			case DEAD: 
				stateDead(dt); 
				break; 
			}
		
/*3*/    	posIsOut(); 
			setState(State.NORMAL); 
			
/*4*/		die(); 

/*5*/		if (this.state != State.DEAD) {
				double food = this.regionMngr.getFood(this, dt); 
				this.energy = Utils.constrainValueInRange(this.energy + food, 0.0, 100.0); 
			}
		}
	}
	
	private void posIsOut() {
		double posX = this.pos.getX(); 
		double posY = this.pos.getY(); 
		double maxWidth = this.regionMngr.getWidth(); 
		double maxHeight = this.regionMngr.getHeight(); 
		
		if (posX < 0 || posX >= maxWidth || posY < 0 || posY >= maxHeight) {
			double newX = Utils.constrainValueInRange(posX, 0, maxWidth -1); 
			double newY = Utils.constrainValueInRange(posY, 0, maxHeight -1); 
			
			this.pos = new Vector2D(posX, posY); 
			
		}
	}
	
	private void die() {
		if (this.energy <= 0.0 || this.age > MAX_AGE_SHEEP) {setState(State.DEAD);}
	}
	

	// implementar los metodos state
}
