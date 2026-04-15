package simulator.model;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class SelectClosest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		// compruebo si la lista esta vacía
		if (as.isEmpty()) {
			return null;
		}

		// llamo al metodo min de la interfaz Collections min (Collection <? extends T>
		// coll, Comparator < ? super T> comp)
		// en todo T = Animal --> la colleccion puede ser de Animal o de cualquier
		// subclase (sheep / wolf)
		// --> el comparator puede ser de animal o cualquier super como Object
		return Collections.min(as, new Comparator<Animal>() {

			// sobrescribimos el método de la interfaz Comparator para hacer lo que nosotros
			// queremos
			@Override
			public int compare(Animal o1, Animal o2) {
				double distancia1 = a.getPosition().distanceTo(o1.getPosition());
				double distancia2 = a.getPosition().distanceTo(o2.getPosition());
				return Double.compare(distancia1, distancia2);
				// negativo si min = distancia1
				// positivo si min = distancia2
				// 0 si son iguales
			}

		});

	}

	/*
	 * @Override public Animal select(Animal a, List<Animal> as) { Animal a1 = null;
	 * if (!as.isEmpty()) { a1 = as.get(0); for (int i = 1; i < as.size(); i++) { if
	 * (a1.getPosition().distanceTo(a.getPosition()) >
	 * as.get(i).getPosition().distanceTo(a.getPosition())) { a1 = as.get(i); } } }
	 * 
	 * return a1; }
	 */

}
