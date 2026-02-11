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
		
		
	}
	
	protected Animal (Animal p1, Animal p2) {  // constructora para cuando nazca un bebe a partir de otro 
		
	}
	
	// con HashMap <Animal, Region> podemos acceder a la region de los animales en tiempo consteante => usamos el equals y hash de Object (todos los animales son distintos) 
}
