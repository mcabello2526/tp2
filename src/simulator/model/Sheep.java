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
	
	protected Sheep(Sheep p1, Animal p2) {
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
			//setState(State.NORMAL); // cambio  
			
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
			setState(State.NORMAL); 
		}
	}
	
	private void die() {
		if (this.energy <= 0.0 || this.age > MAX_AGE_SHEEP) {setState(State.DEAD);}
	}

	@Override
	protected void setNormalStateAction() {
		// TODO Auto-generated method stub
		this.dangerSource = null; 
		this.mateTarget = null; 
	}

	@Override
	protected void setMateStateAction() {
		// TODO Auto-generated method stub
		this.dangerSource = null; 
	}

	@Override
	protected void setHungerStateAction() {
		// TODO Auto-generated method stub
		// no puede estar hambriento?
	}

	@Override
	protected void setDangerStateAction() {
		// TODO Auto-generated method stub
		this.mateTarget = null; 
	}

	@Override
	protected void setDeadStateAction() {
		// TODO Auto-generated method stub
		this.dangerSource = null; 
		this.mateTarget = null; 
	}
	
	private void stateNormal(Double dt) {
		if (this.pos.distanceTo(this.dest) < 8.0) {  // COLLISION_RANGE
			double posX = Utils.RAND.nextDouble()*(this.regionMngr.getWidth()-1); 
			double posY = Utils.RAND.nextDouble()*(this.regionMngr.getHeight()-1); 
			this.dest = new Vector2D(posX, posY); 
	  	}
		this.move(this.speed*dt*Math.exp((this.energy - MAX_ENERGY)* HUNGER_DECAY_EXP_FACTOR));
		this.age +=dt; 
		this.energy = Utils.constrainValueInRange(this.energy - (FOOD_DROP_RATE_SHEEP * dt), 0.0, MAX_ENERGY); 
		this.desire = Utils.constrainValueInRange(this.desire + (DESIRE_INCREASE_RATE_SHEEP*dt), 0.0, MAX_DESIRE);
		
		if (this.dangerSource == null) {
			// no se hacerlo :)
		}
		
		if (this.dangerSource != null) {  // hay que mirarlo 
			setState(State.DANGER); 
		}
	}
	
	private void stateDanger (Double dt) {
		/*1*/
		if (this.dangerSource != null && this.dangerSource.getState() == State.DEAD) {
			dangerSource = null; 
		}
		
		/*2*/
		if (dangerSource == null) {
			if (this.pos.distanceTo(this.dest) < 8.0) {  // COLLISION_RANGE
				double posX = Utils.RAND.nextDouble()*(this.regionMngr.getWidth()-1); 
				double posY = Utils.RAND.nextDouble()*(this.regionMngr.getHeight()-1); 
				this.dest = new Vector2D(posX, posY); 
		  	}
			this.move(this.speed*dt*Math.exp((this.energy - MAX_ENERGY)* HUNGER_DECAY_EXP_FACTOR));
			this.age +=dt; 
			this.energy = Utils.constrainValueInRange(this.energy - (FOOD_DROP_RATE_SHEEP * dt), 0.0, MAX_ENERGY); 
			this.desire = Utils.constrainValueInRange(this.desire + (DESIRE_INCREASE_RATE_SHEEP*dt), 0.0, MAX_DESIRE);
		}
		else {
			this.dest = pos.plus(pos.minus(dangerSource.getPosition()).direction()); 
			this.move(2.0*speed*dt*Math.exp((energy-100.0/*MAX_ENERGY*/)*0.007/*HUNGER_DECAY_EXP_FACTOR*/));
			this.age += dt; 
			this.energy = Utils.constrainValueInRange((this.energy -(20.0*1.2*dt)), 0.0, MAX_ENERGY); 
			this.desire = Utils.constrainValueInRange(this.desire + (40.0*dt), 0.0, MAX_DESIRE); 
		}
		
		/*3*/
		
		// MIRAR Y FALTA 
		
	}

	private void stateMate(Double dt) {
		if (this.mateTarget != null && (this.mateTarget.getState().equals(State.DEAD) || this.pos.distanceTo(this.mateTarget.getPosition()) > this.sightRange)) {
			this.mateTarget = null ;
		}
		if (mateTarget == null) {
			// buscar en la lista que no se como se hace sin las lambda expressions
			// this.mateTarget = this.mateStrategy.select(this, mates); 
			if (this.mateTarget == null) {
				// avanza como en NORMAL
				if (this.pos.distanceTo(this.dest) < 8.0) {  // COLLISION_RANGE
					double posX = Utils.RAND.nextDouble()*(this.regionMngr.getWidth()-1); 
					double posY = Utils.RAND.nextDouble()*(this.regionMngr.getHeight()-1); 
					this.dest = new Vector2D(posX, posY); 
			  	}
				this.move(this.speed*dt*Math.exp((this.energy - MAX_ENERGY)* HUNGER_DECAY_EXP_FACTOR));
				this.age +=dt; 
				this.energy = Utils.constrainValueInRange(this.energy - (FOOD_DROP_RATE_SHEEP * dt), 0.0, MAX_ENERGY); 
				this.desire = Utils.constrainValueInRange(this.desire + (DESIRE_INCREASE_RATE_SHEEP*dt), 0.0, MAX_DESIRE);
			}
		}
		else {
			this.dest = this.mateTarget.getPosition(); 
			this.move(2.0*speed*dt*Math.exp((energy-100.0)*0.007));
			this.age += dt; 
			this.energy = Utils.constrainValueInRange(this.energy - (20.0*1.2*dt), 0.0, MAX_ENERGY); 
			this.desire = Utils.constrainValueInRange(this.desire + (40.0*dt), 0.0, MAX_DESIRE);
			
			if (this.pos.distanceTo(this.mateTarget.getPosition()) > this.sightRange) {
				this.desire = 0.0; 
				this.mateTarget.desire = 0.0; // mirar si esto se debe hacer o si deberiamos hacer un setter 
				if (!this.isPregnant() && Utils.RAND.nextDouble() < PREGNANT_PROBABILITY_SHEEP) {
					this.baby = new Sheep (this, mateTarget);  // como hacemos para que 
					this.mateTarget = null; 
				}
				
			}			
		}
		
		if (this.dangerSource == null) {
			// buscar un nuevo animal que se considere peligroso
		}
		
		if (this.dangerSource != null) {
			setState(State.DANGER); 
		}
		else if (this.desire < DESIRE_THRESHOLD_SHEEP){
			setState(State.NORMAL); 
		}
	}
	

	
	private void stateHunger(Double dt) {
		// no hace nada
	}
	
}
