package simulator.model;

import java.util.List;

public interface EcoSysObserver {  // AnimalInfo MapInfo RegionInfo para no permitir alterar el estado de los objetosx
	  void onRegister(double time, MapInfo map, List<AnimalInfo> animals);  
	  void onReset(double time, MapInfo map, List<AnimalInfo> animals);  
	  void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a);  
	  void onRegionSet(int row, int col, MapInfo map, RegionInfo r);  
	  void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt);  
}
