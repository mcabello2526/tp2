package simulator.model;

import java.util.List;


// para seleccionar con que animal se relacionan en una region 

public interface SelectionStrategy {  
	Animal select(Animal a, List<Animal> as);  // el objeto del animal a invoca a select  
	
	// la lista ya viene filtrada por lo que a no puede ser parte de la lista as 
}