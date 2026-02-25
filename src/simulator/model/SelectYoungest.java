package simulator.model;

import java.util.List;
import java.util.Comparator; 
import java.util.Collections; 

public class SelectYoungest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		if (as.isEmpty()) {
			return null; 
		}
		
		return Collections.min(as, new Comparator<Animal>(){

			@Override
			public int compare(Animal o1, Animal o2) {
				double age1 = o1.getAge(); 
				double age2 = o2.getAge(); 
				
				return Double.compare(age1,age2);
			}
			
		}); 
	}

}
