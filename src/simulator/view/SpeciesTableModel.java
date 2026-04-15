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
import simulator.model.RegionInfo;

class SpeciesTableModel extends AbstractTableModel implements EcoSysObserver {

	private static final long serialVersionUID = 1L;

	private Map<String, Map<Animal.State, Integer>> speciesData;
	private List<String> codeGeneticsHeaders;
	private List<String> stateHeaders;

	SpeciesTableModel(Controller ctrl) {
		this.speciesData = new HashMap<>();
		this.codeGeneticsHeaders = new ArrayList<>();
		this.stateHeaders = new ArrayList<>();

		this.stateHeaders.add("Species");
		for (Animal.State s : Animal.State.values()) {
			this.stateHeaders.add(s.toString());
		}
		ctrl.addObserver(this);

	}
	
	//este no aparece de normal solo lo pongo porque esta puesto
	@Override 
	public String getColumnName(int col) {
		return stateHeaders.get(col);	
	}
	
	@Override
	public int getRowCount() {
		return codeGeneticsHeaders.size();
	}

	@Override
	public int getColumnCount() {
		return Animal.State.values().length+1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String specie = codeGeneticsHeaders.get(rowIndex); 
		if (columnIndex > 0) return speciesData.get(specie).getOrDefault(Animal.State.values()[columnIndex-1], 0); 
		return specie;
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		speciesData = new HashMap<>(); 
		codeGeneticsHeaders = new ArrayList<>(); 
		for (AnimalInfo a: animals) {
			animalsForState(a); 
		}
		fireTableDataChanged(); 
		

	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		speciesData = new HashMap<>(); 
		codeGeneticsHeaders = new ArrayList<>(); 
		fireTableDataChanged(); 

	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		animalsForState(a); 
		fireTableDataChanged(); 

	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		

	}

	@Override
	public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		speciesData = new HashMap<>(); 
		codeGeneticsHeaders = new ArrayList<>(); 
		for (AnimalInfo a : animals) {
			animalsForState(a); 
		}
		fireTableDataChanged(); 

	}
	
	private void animalsForState(AnimalInfo a) {
		String specie = a.getGeneticCode();
			Animal.State stateSpecie = a.getState();
			Map<Animal.State,Integer> m;
			if(!codeGeneticsHeaders.contains(specie)) {
				m = new HashMap<>();
				m.put(stateSpecie,1);
				codeGeneticsHeaders.add(specie);
				speciesData.put(specie,m);
		    }
			else{
				 m = speciesData.get(specie);
		     	m.put(stateSpecie,m.getOrDefault(stateSpecie,0)+1);
			}
	}
}
