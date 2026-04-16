package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Animal;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.MapInfo.RegionData;
import simulator.model.RegionInfo;

class RegionsTableModel extends AbstractTableModel implements EcoSysObserver {

	private static final long serialVersionUID = 1L;
	
	static class Info{
		Map <Animal.Diet, Integer> animalsPerRegion; 
		int cols; 
		int rows; 
		String regionType; 
		
		public Info(RegionData r) {
			animalsPerRegion = new HashMap<>();
			List<AnimalInfo> animalList = r.r().getAnimalsInfo();
			
			for (Animal.Diet d: Animal.Diet.values()) {
				animalsPerRegion.put(d, 0); 
			}
			
			for(AnimalInfo a: animalList) {
				animalsPerRegion.put(a.getDiet(), animalsPerRegion.getOrDefault(a.getDiet(), 0)+1); 
			}
			
			this.cols = r.col(); 
			this.rows = r.row();
			this.regionType = r.r().toString();
		}
		
	
	}
	private List<Info> regionsDataList; 
	private List<String> dietHeaders; 

	RegionsTableModel(Controller ctrl) {  
		regionsDataList = new ArrayList<>();
		dietHeaders = new ArrayList<>(); 
		ctrl.addObserver(this);
		
        dietHeaders.add("Row");
        dietHeaders.add("Col");
        dietHeaders.add("Desc.");
        
        for (Animal.Diet diet: Animal.Diet.values()) {
        	dietHeaders.add(diet.toString()); 
        }
		
	}  

	
	@Override 
	public String getColumnName(int col) {
		return dietHeaders.get(col); 
	}

	@Override
	public int getRowCount() {
		return regionsDataList.size();
	}

	@Override
	public int getColumnCount() {
		return dietHeaders.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return regionsDataList.get(rowIndex).rows; 
		}
		else if (columnIndex ==  1) {
			return regionsDataList.get(rowIndex).cols;
		}
		else if(columnIndex == 2){
            return regionsDataList.get(rowIndex).regionType;
        }
        else {
            return regionsDataList.get(rowIndex).animalsPerRegion.get(Animal.Diet.values()[columnIndex-3]);
        }
		
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		changeRegion(map); 
		
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		regionsDataList = new ArrayList<>(); 
		fireTableDataChanged(); 
		
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		changeRegion(map); 
		
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		changeRegion(map); 
		
	}

	@Override
	public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		changeRegion(map); 
		
	}
	
	private void changeRegion(MapInfo map) {
		// lo hacemos con el iterator o con el bucle normal
		regionsDataList.clear(); 
		for (RegionData r: map) {
			regionsDataList.add(new Info(r)); 
		}
		fireTableDataChanged(); 
	}
}
