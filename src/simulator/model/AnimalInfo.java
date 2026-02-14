package simulator.model;

import simulator.misc.*;
import simulator.model.Animal.Diet;
import simulator.model.Animal.State;


// informacion visible del animal (inmutable) --> si queremos pasar una instancia de Animal a una parte 
// que no puede alterar el estado pasamos AnimalInfo

// Añadir más metodos si no modifican el estado de Animal

public interface AnimalInfo extends JSONable { // Note that it extends JSONable  
	public State getState();  		
	public Vector2D getPosition();  
	public String getGeneticCode();  
	public Diet getDiet();  
	public double getSpeed();  
	public double getSightRange();  
	public double getEnergy();  
	public double getAge();
	public Vector2D getDestination();  
	public boolean isPregnant();  
}
