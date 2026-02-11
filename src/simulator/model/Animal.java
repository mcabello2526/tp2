package simulator.model;

import simulator.misc.*;

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
		if (mateStrategy == null) {throw new IllegalArgumentException("invalid mate strategy. It cannot be null"); }
		
		this.geneticCode = geneticCode; 
		this.diet = diet; 
		this.sightRange = sightRange; 
		this.mateStrategy = mateStrategy; 
		
		speed = Utils.getRandomizedParameter(initSpeed, 0.1); 
		state = State.NORMAL; 
		energy = INIT_ENERGY; 
		desire = 0.0; 
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
	
	
	
	
	
	// con HashMap <Animal, Region> podemos acceder a la region de los animales en tiempo consteante => usamos el equals y hash de Object (todos los animales son distintos) 
}
