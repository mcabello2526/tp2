package simulator.model;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Animal.Diet;
import simulator.model.Animal.State;


public class Wolf extends Animal{
	final static String WOLF_GENETIC_CODE = "Wolf";
	final static double INIT_SIGHT_WOLF = 50;
	final static double INIT_SPEED_WOLF = 60;
	final static double BOOST_FACTOR_WOLF = 3.0;
	final static double MAX_AGE_WOLF = 14.0;
	final static double FOOD_THRESHOLD_WOLF = 50.0;
	final static double FOOD_DROP_BOOST_FACTOR_WOLF = 1.2;
	final static double FOOD_DROP_RATE_WOLF = 18.0;
	final static double FOOD_DROP_DESIRE_WOLF = 10.0;
	final static double FOOD_EAT_VALUE_WOLF = 50.0;
	final static double DESIRE_THRESHOLD_WOLF = 65.0;
	final static double DESIRE_INCREASE_RATE_WOLF = 30.0;
	final static double PREGNANT_PROBABILITY_WOLF = 0.75;
	
	private Animal huntTarget; 
	private SelectionStrategy huntingStrategy; 

	public Wolf(SelectionStrategy mateStrategy, SelectionStrategy huntingStrategy, Vector2D pos) {
		super(WOLF_GENETIC_CODE, Diet.CARNIVORE, INIT_SIGHT_WOLF, INIT_SPEED_WOLF, mateStrategy, pos);
		// TODO Auto-generated constructor stub
		this.huntingStrategy = huntingStrategy; 
	}
	
	protected Wolf (Wolf p1, Animal p2) {
		super(p1, p2); 
		this.huntingStrategy = p1.huntingStrategy; 
		this.huntTarget = null;
	}

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
		return this.baby != null;
	}

	
	
	@Override
	public void update(double dt) {
		// TODO Auto-generated method stub
/*1*/		if (!this.state.equals(State.DEAD)) {
/*2*/		switch (state) { 
			case MATE: 
				stateMate(dt); 
				break; 
			case HUNGER: 
				stateHunger(dt); 
				break; 
			case DANGER: 
				stateDanger(dt); 
				break; 
			default: 
				stateNormal(dt); 
				break; 
			}
		
/*3*/    	posIsOut(); 
			 
			
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
			
			this.pos = new Vector2D(newX, newY); 
			setState(State.NORMAL); 
		
	    }
	}
	
	private void die() {
		if (this.energy <= 0.0 || this.age > MAX_AGE_WOLF) {setState(State.DEAD);}
	}
	
	
	
	private void stateNormal(Double dt) {
		if (this.pos.distanceTo(dest) < 8.0) {
			double posX = Utils.RAND.nextDouble()*(this.regionMngr.getWidth()-1); 
			double posY = Utils.RAND.nextDouble()*(this.regionMngr.getHeight()-1); 
			this.dest = new Vector2D(posX, posY); 
		}
		this.move(speed*dt*Math.exp((energy-100.0)*0.007/*HUNGER_DECAY_EXP_FACTOR*/));
		this.age += dt; 
		this.energy = Utils.constrainValueInRange((this.energy - (18.0*dt)), 0.0, MAX_ENERGY); 
		this.desire = Utils.constrainValueInRange((this.desire + (30.0*dt)) , 0.0, MAX_DESIRE); 
		
		if (this.energy < 50.0 /*FOOD_THRESHOLD_WOLF*/ ) {
			setState(State.HUNGER); 
			
		}
		else if (this.desire > DESIRE_THRESHOLD_WOLF) {
			setState(State.MATE); 
		}
	}
	
	private void stateHunger(Double dt) {
		if (this.huntTarget == null || this.huntTarget != null &&
				(this.huntTarget.getState().equals(State.DEAD) || this.pos.distanceTo(this.huntTarget.getPosition())> this.sightRange)) {
			huntTarget = huntingStrategy.select(this, regionMngr.getAnimalsInRange(this, (Animal a) -> a.getDiet().equals(Diet.CARNIVORE)));	

		}
		
		if (this.huntTarget == null) {
			if (this.pos.distanceTo(dest) < 8.0) {
				double posX = Utils.RAND.nextDouble()*(this.regionMngr.getWidth()-1); 
				double posY = Utils.RAND.nextDouble()*(this.regionMngr.getHeight()-1); 
				this.dest = new Vector2D(posX, posY); 
			}
			this.move(speed*dt*Math.exp((energy-100.0)*0.007/*HUNGER_DECAY_EXP_FACTOR*/));
			this.age += dt; 
			this.energy = Utils.constrainValueInRange((this.energy - (18.0*dt)), 0.0, MAX_ENERGY); 
			this.desire = Utils.constrainValueInRange((this.desire + (30.0*dt)) , 0.0, MAX_DESIRE); 
		}
		else {
			this.dest = huntTarget.getPosition(); 
			this.move(3.0*speed*dt*Math.exp((energy-100.0)*0.007/*HUNGER_DECAY_EXP_FACTOR*/));
			this.age += dt; 
			this.energy = Utils.constrainValueInRange(this.energy - (18.0*1.2*dt), 0.0, MAX_ENERGY); 
			this.desire = Utils.constrainValueInRange(this.desire + (30.0*dt), 0.0, MAX_DESIRE); 
			
			if (this.pos.distanceTo(this.huntTarget.getPosition()) < 8.0) {
				setState(State.DEAD);
				huntTarget = null; 
				this.energy = Utils.constrainValueInRange(this.energy + FOOD_EAT_VALUE_WOLF, 0.0, MAX_ENERGY); 
			}
		}
		
		if (this.energy > 50.0) {
			if (this.desire < DESIRE_THRESHOLD_WOLF) {
				setState(State.NORMAL); 
			}
			else {
				setState(State.MATE); 
			}
		}
	}
	
	private void stateMate(double dt) {
		if (this.mateTarget != null && (this.state== State.DEAD || this.pos.distanceTo(this.mateTarget.getPosition())> this.sightRange)) {
			this.mateTarget = null; 
		}
		
		if (this.mateTarget == null) {
			// buscar animal para emparejarse con lambda 
			this.mateTarget = this.mateStrategy.select(this, regionMngr.getAnimalsInRange(this, (Animal a) -> a.getGeneticCode().equals(this.getGeneticCode()))); 

			if (mateTarget == null) {
				if (this.pos.distanceTo(dest) < 8.0) {
					double posX = Utils.RAND.nextDouble()*(this.regionMngr.getWidth()-1); 
					double posY = Utils.RAND.nextDouble()*(this.regionMngr.getHeight()-1); 
					this.dest = new Vector2D(posX, posY); 
				}
				this.move(speed*dt*Math.exp((energy-100.0)*0.007/*HUNGER_DECAY_EXP_FACTOR*/));
				this.age += dt; 
				this.energy = Utils.constrainValueInRange((this.energy - (18.0*dt)), 0.0, MAX_ENERGY); 
				this.desire = Utils.constrainValueInRange((this.desire + (30.0*dt)) , 0.0, MAX_DESIRE);
			}
		}
		else {
			this.dest = mateTarget.getPosition(); 
			this.move(3.0*speed*dt*Math.exp((energy-100.0)*0.007));
			this.age += dt; 
			this.energy = Utils.constrainValueInRange(this.energy -(18.0*1.2*dt), 0.0, MAX_ENERGY); 
			this.desire = Utils.constrainValueInRange((this.desire + (30.0*dt)) , 0.0, MAX_DESIRE);
			
			if (this.pos.distanceTo(this.mateTarget.getPosition()) < 8.0) {
				this.desire = 0.0;
				this.mateTarget.desire= 0.0; 
				if (!this.isPregnant() && Utils.RAND.nextDouble() < PREGNANT_PROBABILITY_WOLF) {
					this.baby = new Wolf (this, mateTarget);   
				}
				this.energy = Utils.constrainValueInRange((this.energy - (10.0)), 0.0, MAX_ENERGY); 
				this.mateTarget = null; 
				
			}

		}
		
		if (this.energy < 50.0) {
			setState(State.HUNGER); 
		}else if (this.desire < 65.0){
			setState(State.NORMAL); 
		}
	}
	
	private void stateDanger(double dt) {
		//no puede estar en danger
	}

	
	
	@Override
	protected void setNormalStateAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setMateStateAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setHungerStateAction() {
		// TODO Auto-generated method stub
		this.mateTarget = null; 
	}

	@Override
	protected void setDangerStateAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setDeadStateAction() {
		// TODO Auto-generated method stub
		
	}


}
