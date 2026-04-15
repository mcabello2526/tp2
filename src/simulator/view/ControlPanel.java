package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.control.Controller;
import simulator.launcher.Main;

class ControlPanel extends JPanel {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	private ChangeRegionsDialog changeRegionsDialog;

	private JToolBar toolaBar;
	private JFileChooser fc;
	private boolean stopped = true; // utilizado en los botones de run/stop

	private JButton quitButton;
	private JButton loadButton;
	private JButton mapWindowButton;
	private JButton regionsButton;
	private JButton runButton;
	private JButton stopButton;

	private JSpinner spinner;
	private JTextField dtTextField;

	// TODO añade más atributos aquí …

	ControlPanel(Controller ctrl) {
		this.ctrl = ctrl;
		initGUI();
	}

	private void initGUI() { // mirar los separadores para ver si sale como tiene que salir
		setLayout(new BorderLayout());
		toolaBar = new JToolBar();
		add(toolaBar, BorderLayout.PAGE_START);

		// TODO crear los diferentes botones/atributos y añadirlos a la toolBar.
		// Todos ellos han de tener su correspondiente tooltip. Puedes utilizar
		// this.toolaBar.addSeparator() para añadir la línea de separación vertical
		// entre las componentes que lo necesiten.

		// Load button
		this.loadButton = new JButton();
		this.loadButton.setToolTipText("Load an input file (JSON)");
		this.loadButton.setIcon(new ImageIcon("resources/icons/open.png"));
		this.loadButton.addActionListener((e) -> openFile());
		this.toolaBar.add(loadButton);
		this.toolaBar.addSeparator();

		// MapWindow Button
		this.mapWindowButton = new JButton();
		this.mapWindowButton.setToolTipText("Map viewer");
		this.mapWindowButton.setIcon(new ImageIcon("resources/icons/viewer.png"));
		this.mapWindowButton.addActionListener((e) -> createView()); // new MapWindow(ctrl);
		this.toolaBar.add(mapWindowButton);
		this.toolaBar.addSeparator();

		// Regions Button (cambiar de regiones)
		this.regionsButton = new JButton();
		this.regionsButton.setToolTipText("Change regions");
		this.regionsButton.setIcon(new ImageIcon("resources/icons/regions.png"));
		this.regionsButton.addActionListener((e) -> openRegions());
		this.toolaBar.add(regionsButton);
		this.toolaBar.addSeparator();

		// Run Button
		this.runButton = new JButton();
		this.runButton.setToolTipText("Run Simulator");
		this.runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		this.runButton.addActionListener((e) -> runSimulator());
		this.toolaBar.add(runButton);
		this.toolaBar.addSeparator();

		// Stop Button
		this.stopButton = new JButton();
		this.stopButton.setToolTipText("Stop Simulator");
		this.stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		this.stopButton.addActionListener((e) -> stopSimulator());
		this.toolaBar.addSeparator();

		// Spinner
		this.spinner = new JSpinner(new SpinnerNumberModel(10000, 0, 10000, 100));
		this.spinner.setMaximumSize(new Dimension(70, 30));
		this.spinner.setMinimumSize(new Dimension(70, 30));
		this.spinner.setPreferredSize(new Dimension(70, 30));
		this.toolaBar.add(spinner);
		JLabel textSpinner = new JLabel("Steps: ");
		this.toolaBar.add(textSpinner);
		this.toolaBar.addSeparator();

		// deltaTextField
		this.dtTextField = new JTextField(Main.deltaTime.toString());
		this.dtTextField.setMaximumSize(new Dimension(70, 30));
		this.dtTextField.setMinimumSize(new Dimension(70, 30));
		this.dtTextField.setPreferredSize(new Dimension(70, 30));
		this.toolaBar.add(dtTextField);
		JLabel dtText = new JLabel("Delta time: ");
		this.toolaBar.add(dtText);
		this.toolaBar.addSeparator();

		// Quit Button
		this.toolaBar.add(Box.createGlue()); // this aligns the button to the right
		this.toolaBar.addSeparator();
		this.quitButton = new JButton();
		this.quitButton.setToolTipText("Quit");
		this.quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		this.quitButton.addActionListener((e) -> ViewUtils.quit(this));
		this.toolaBar.add(quitButton);
		this.toolaBar.addSeparator();

		// TODO Inicializar this.fc con una instancia de JFileChooser. Para que siempre
		// abre en la carpeta de ejemplos puedes usar:
		//
		// this.fc.setCurrentDirectory(new File(System.getProperty("user.dir") +
		// "/resources/examples"));

		this.fc = new JFileChooser();
		this.fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));

		// TODO Inicializar this.changeRegionsDialog con instancias del diálogo de
		// cambio
		// de regiones

		//this.changeRegionsDialog = new ChangeRegionsDialog(ctrl);

	}

	// TODO el resto de métodos van aquí…
	private void openFile() {
		// load data del main

	}

	private void createView() {
		// new MapWindow(ViewUtils.getWindow(ControlPanel.this), _ctrl);
		// o
		// new MapWindow(ctrl);
	}

	private void openRegions() {
		//this.changeRegionsDialog.open(ViewUtils.getWindow(this));
	}
	
	private void enableButtonsAndText(boolean enable) {
		this.quitButton.setEnabled(enable);
		this.loadButton.setEnabled(enable);
		this.mapWindowButton.setEnabled(enable);
		this.regionsButton.setEnabled(enable);
		this.runButton.setEnabled(enable);
		this.dtTextField.setEnabled(enable);
		this.spinner.setEnabled(enable);
		
		this.stopButton.setEnabled(!enable); 
	}

	private void runSim(int n, double dt) {
		if (n > 0 && !this.stopped) {
			try {
				this.ctrl.advance(dt);
				SwingUtilities.invokeLater(() -> runSim(n - 1, dt));
			} catch (Exception e) {
				// TODO llamar a ViewUtils.showErrorMsg con el mensaje de error
				// que corresponda
				// TODO activar todos los botones
			
				ViewUtils.showErrorMsg(this, e.getMessage());
				this.stopped = true;
				enableButtonsAndText(stopped);
				
			}
		} else {
			// TODO activar todos los botones
			this.stopped = true;
			enableButtonsAndText(stopped);
		}
	}
	
	private void runSimulator() {
		this.stopped = false; 
		enableButtonsAndText(stopped); 
		Main.deltaTime = Double.parseDouble(dtTextField.getText()); 
		try {
			Main.deltaTime = Double.parseDouble(dtTextField.getText());
			int n = Integer.parseInt(spinner.getValue().toString()); 
			runSim(n,Main.deltaTime); 
		}catch(Exception e) {
			ViewUtils.showErrorMsg("Not a valid delta time");
		}
	}
	
	private void stopSimulator() {
		stopped = true; 
	}
	


}