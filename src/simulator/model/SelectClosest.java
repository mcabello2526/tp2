package simulator.model;

import java.util.List;
import java.util.Collections;
import java.util.Comparator; 

public class SelectClosest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		if (as.isEmpty()) {
			return null; 
		}
		
		return Collections.min(as, new Comparator<Animal>() {

			@Override
			public int compare(Animal o1, Animal o2) {
				double distancia1 = a.getPosition().distanceTo(o1.getPosition());
				double distancia2 = a.getPosition().distanceTo(o2.getPosition()); 
				return Double.compare (distancia1, distancia2);
			}
			
		}); 
		
	}
	
	// tambien se puede hacer normal asi: 
	/*
	@Override
	public Animal select(Animal a, List<Animal> as) {
		Animal a1 = null; 
		if (!as.isEmpty()) {
			a1 = as.get(0); 
			for (int i = 1; i < as.size(); i++) {
				if (a1.getPosition().distanceTo(a.getPosition()) > as.get(i).getPosition().distanceTo(a.getPosition())) {
					a1 = as.get(i); 
				}
			}
		}
	 
		return a1; 
	}
	*/
	
	// y se puede hacer con una expresion lambda asi 
	/*
	@Override
    public Animal select(Animal a, List<Animal> as) {
        if (as.isEmpty()) {
            return null;
        }
        // Buscamos el animal 'c' que tenga la menor distancia a 'a'
        return Collections.min(as, (c1, c2) -> {
            double dist1 = a.getPosition().distanceTo(c1.getPosition());
            double dist2 = a.getPosition().distanceTo(c2.getPosition());
            return Double.compare(dist1, dist2);
        });
    }
    */
	

}
