package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class StatusBar extends JPanel implements EcoSysObserver {

	private static final long serialVersionUID = 1L;
	private JLabel animalsLabel; 
	private JLabel dimensionLabel; 
	private JLabel timeLabel; 
	

	StatusBar(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		
		this.timeLabel = new JLabel(String.format("%.3f", 0.0)); 
		
		this.add(new JLabel ("Time: ")); 
		this.add(timeLabel);
		
		JSeparator s = new JSeparator(JSeparator.VERTICAL);  //separador vertical
		s.setPreferredSize(new Dimension(10, 20));
		this.add(s);
		
		animalsLabel = new JLabel (Integer.toString(0)); 
		
		this.add(new JLabel ("Total Animals: ")); 
		this.add(animalsLabel); 
		
		JSeparator s1 = new JSeparator(JSeparator.VERTICAL);
		s1.setPreferredSize(new Dimension(10, 20));
		this.add(s1);
		
		dimensionLabel = new JLabel(0+"x"+0+" "+0+"x"+0); 
		this.add(new JLabel ("Dimension: ")); 
		this.add(dimensionLabel); 
		
		
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		timeLabel.setText(String.format("%.3f", time));
		animalsLabel.setText(Integer.toString(animals.size()));
		dimensionLabel.setText(map.getWidth()+"x"+map.getHeight()+" "+map.getCols()+"x"+map.getRows());

	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		dimensionLabel.setText(map.getWidth()+"x"+map.getHeight()+" "+map.getCols()+"x"+map.getRows());

	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		animalsLabel.setText(Integer.toString(animals.size()));


	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		dimensionLabel.setText(map.getWidth()+"x"+map.getHeight()+" "+map.getCols()+"x"+map.getRows());


	}

	@Override
	public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		timeLabel.setText(String.format("%.3f", time));
		animalsLabel.setText(Integer.toString(animals.size()));


	}

}
