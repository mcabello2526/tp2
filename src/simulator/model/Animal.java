package simulator.model;

import simulator.misc.*;
import simulator.misc.Utils;  
import simulator.misc.Vector2D;
import org.json.*;

// no esta permitido añadir un atributo para la region --> usamos RegionManager

public abstract class Animal implements AnimalInfo, Entity{
	public enum Diet{CARNIVORE, HERVIBORE}; // alimentacion 
	public enum State{NORMAL, MATE, HUNGER, DANGER, DEAD}; // estados 
	
	final static double INIT_ENERGY = 100.0;
	final static double MUTATION_TOLERANCE = 0.2;
	final static double NEARBY_FACTOR = 60.0;
	final static double COLLISION_RANGE = 8;
	final static double HUNGER_DECAY_EXP_FACTOR = 0.007;
	final static double MAX_ENERGY = 100.0;
	final static double MAX_DESIRE = 100.0;
	final static double MINIMUM_VALUE = 0.0; //ns si se puede????? para varis
	
	protected String geneticCode; 
	protected Diet diet; 
	protected State state; 
	protected Vector2D pos; 
	protected Vector2D dest; 
	protected double energy; 
	protected double speed; 
	protected double age; 
	protected double desire; 
	protected double sightRange; 
	protected Animal mateTarget; 
	protected Animal baby; 
	protected AnimalMapView regionMngr; 
	protected SelectionStrategy mateStrategy; 
	
	protected Animal (String geneticCode, Diet diet, double sightRange, double initSpeed, SelectionStrategy mateStrategy, Vector2D pos) throws IllegalArgumentException{
		//solo hay que comprobar que no sea vacía? o hay que comprobar tb que sea valida 
		
		if (geneticCode == null || geneticCode.equals ("")) {throw new IllegalArgumentException ("Invalid genetic code. It cannot be empty.");}
		if (initSpeed < 0 || sightRange < 0) {throw new IllegalArgumentException ("Invalid intial speed. It cannot be below 0"); }  // puede ser = 0?
		if (mateStrategy == null) {throw new IllegalArgumentException("Invalid mate strategy. It cannot be null"); }
		
		this.geneticCode = geneticCode; 
		this.diet = diet; 
		this.sightRange = sightRange; 
		this.pos = pos;
		this.mateStrategy = mateStrategy; 
		
		speed = Utils.getRandomizedParameter(initSpeed, 0.1); 
		state = State.NORMAL; 
		energy = INIT_ENERGY; 
		desire = 0.0; 	/////
		dest = null; 
		mateTarget = null; 
		baby = null; 
		regionMngr = null; 
	
		// pos se incializa en el metodo init
		

		
	}
			
	protected Animal (Animal p1, Animal p2) {  // constructora para cuando nazca un bebe a partir de otro 
		dest = null; 
		mateTarget = null; 
		baby = null; 
		regionMngr = null; 
		
		state = State.NORMAL;
		desire = 0.0; 
		geneticCode = p1.geneticCode; 
		diet = p1.diet; 
		mateStrategy = p2.mateStrategy; 
		energy = (p1.energy + p2.energy)/2; 
		
		pos = p1.getPosition().plus(Vector2D.get_random_vector(-1,1).scale(60.0*(Utils.RAND.nextGaussian()+1))); 
		sightRange = Utils.getRandomizedParameter((p1.getSightRange()+p2.getSightRange())/2, 0.2); 
		speed = Utils.getRandomizedParameter((p1.getSpeed()+p2.getSpeed())/2, 0.2); 
	}
	
	
	void init(AnimalMapView regMngr) {
		regionMngr = regMngr; 
		double x, y; 
		double width = regMngr.getWidth(); 
		double height = regMngr.getHeight(); 
		if (pos == null) {
			x = Utils.RAND.nextDouble()*(width-1);
			y = Utils.RAND.nextDouble()*(height-1); 
			this.pos = new Vector2D(x, y); 
		}
		else {
			x = pos.getX(); 
			y = pos.getY(); 
			
			while (x >= width) x = (x - width);  
			while (x < 0) x = (x + width);  
			while (y >= height) y = (y - height);  
			while (y < 0) y = (y + height);
			
			this.pos = new Vector2D(x,y); 
			
			//x = Utils.constrainValueInRange(pos.getX(), 0, width -1); 
			//y = Utils.constrainValueInRange(pos.getY(), 0, height -1); 
		}
		
		double destX = Utils.RAND.nextDouble()*(width -1); 
		double destY = Utils.RAND.nextDouble()*(width -1); 
		
		this.dest = new Vector2D(destX, destY); 
	}
	//public??
	Animal deliverBaby() {
		Animal b = this.baby; 
	    this.baby = null;     
	    return b;
	}
	
	protected void move (double speed) {
		pos = pos.plus(dest.minus(pos).direction().scale(speed)); 
	}
	
	protected void setState (State state) {  // cambia el estado y realiza acciones complmentarias del estado 
		this.state = state; 
		switch(state) {
		case NORMAL: 
			setNormalStateAction(); 
			break; 
		case MATE: 
			setMateStateAction(); 
			break; 
		case HUNGER: 
			setHungerStateAction(); 
			break; 
		case DANGER: 
			setDangerStateAction(); 
			break; 
		case DEAD: 
			setDeadStateAction(); 
			break; 		
		}
	
	}
	
	abstract protected void setNormalStateAction(); 
	abstract protected void setMateStateAction(); 
	abstract protected void setHungerStateAction(); 
	abstract protected void setDangerStateAction(); 
	abstract protected void setDeadStateAction(); 
	
	public JSONObject asJSON() {
		JSONObject animalJSON = new JSONObject(); 
		animalJSON.put("pos", pos);
		animalJSON.put("gcode", geneticCode);
		animalJSON.put("diet", diet.toString().toUpperCase());
		animalJSON.put("state", state.toString().toUpperCase());

		return animalJSON;
	}
	
	// con HashMap <Animal, Region> podemos acceder a la region de los animales en tiempo consteante => usamos el equals y hash de Object (todos los animales son distintos) 


// factorias con genericos que devuelvan algo de tipo T
// principio de responsabilidad unica para algo de los comandos construir Builders de Animals delegando responsabilidad en una clase Builder 

}














