package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.launcher.Main;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class ChangeRegionsDialog extends JDialog implements EcoSysObserver {


	private static final long serialVersionUID = 1L;
	private DefaultComboBoxModel<String> regionsModel;
	private DefaultComboBoxModel<String> fromRowModel;
	private DefaultComboBoxModel<String> toRowModel;
	private DefaultComboBoxModel<String> fromColModel;
	private DefaultComboBoxModel<String> toColModel;

	private DefaultTableModel dataTableModel;
	private Controller ctrl;
	private List<JSONObject> regionsInfo;

	private String[] headers = { "Key", "Value", "Description" };

	ChangeRegionsDialog(Controller ctrl) {
		super((Frame) null, true);
		this.ctrl = ctrl;
		initGUI();
		ctrl.addObserver(this);

	}

	private void initGUI() {
		setTitle("Change Regions");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		mainPanel.setPreferredSize(new Dimension(761, 478));
		JPanel helpTextPanel = new JPanel();
		JPanel tablePanel = new JPanel(new BorderLayout());
		JPanel comboBoxPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();

		JLabel text = new JLabel(
				"<html><p> Select a region type, the rows/cols interval, and provide values for the parameter in the, <b> Value column </b> (default values</p><p> are used for parameters with no value).</p></html>");
		text.setFont(new Font("Arial", Font.PLAIN, 14));

		helpTextPanel.setLayout(new BorderLayout());
		helpTextPanel.add(text);

		JLabel regionType = new JLabel("Region type: ");
		JLabel rows = new JLabel("Row from/to: ");
		JLabel cols = new JLabel("Col from/to: ");

		// this.regionsInfo se usará para establecer la información en la tabla
		this.regionsInfo = Main.regionsFactory.getInfo();

		// this.dataTableModel es un modelo de tabla que incluye todos los parámetros de
		// la region
		this.dataTableModel = new DefaultTableModel() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1;
			}
		};

		this.dataTableModel.setColumnIdentifiers(this.headers);

		JTable dataTable = new JTable(dataTableModel);
		JScrollPane tableScrollPane = new JScrollPane(dataTable);
		tablePanel.add(tableScrollPane);

		// this.regionsModel es un modelo de combobox que incluye los tipos de regiones
		this.regionsModel = new DefaultComboBoxModel<>();

		for (JSONObject region : regionsInfo) {
			regionsModel.addElement(region.getString("desc")); // esto estaba con type pero segun es mejor con desc
		}

		JComboBox<String> dataRegions = new JComboBox<String>(regionsModel);
		dataRegions.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				modifyDataTable(dataRegions.getSelectedIndex());

			}

		});
		comboBoxPanel.add(regionType);
		comboBoxPanel.add(dataRegions);

		this.fromRowModel = new DefaultComboBoxModel<>();
		this.toRowModel = new DefaultComboBoxModel<>();
		this.fromColModel = new DefaultComboBoxModel<>();
		this.toColModel = new DefaultComboBoxModel<>();

		comboBoxPanel.add(rows);
		JComboBox<String> fromRowComboBox = new JComboBox<String>(fromRowModel);
		comboBoxPanel.add(fromRowComboBox);

		JComboBox<String> toRowComboBox = new JComboBox<String>(toRowModel);
		comboBoxPanel.add(toRowComboBox);

		comboBoxPanel.add(cols);
		JComboBox<String> fromColComboBox = new JComboBox<String>(fromColModel);
		comboBoxPanel.add(fromColComboBox);

		JComboBox<String> toColComboBox = new JComboBox<String>(toColModel);
		comboBoxPanel.add(toColComboBox);

		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				okAction(dataRegions);

			}

		});

		JButton cancel = new JButton("Cancel");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cancelAction();

			}

		});

		buttonsPanel.add(cancel);
		buttonsPanel.add(ok);

		mainPanel.add(helpTextPanel);
		mainPanel.add(tablePanel);
		mainPanel.add(comboBoxPanel);
		mainPanel.add(buttonsPanel);

		setPreferredSize(new Dimension(700, 400)); // puedes usar otro tamaño
		pack();
		setResizable(false);
		setVisible(false);
	}

	public void open(Frame parent) {
		setLocation(parent.getLocation().x + parent.getWidth() / 2 - getWidth() / 2,
				parent.getLocation().y + parent.getHeight() / 2 - getHeight() / 2);
		pack();
		setVisible(true);
	}

	private void cancelAction() {
		this.setVisible(false);
	}

	private void okAction(JComboBox<String> dataRegions) {
		JSONObject regionInfo = regionsInfo.get(dataRegions.getSelectedIndex());
		String regionType = regionInfo.getString("type"); 

		String fromRow = fromRowModel.getSelectedItem().toString();
		String toRow = toRowModel.getSelectedItem().toString();
		String fromCol = fromColModel.getSelectedItem().toString();
		String toCol = toColModel.getSelectedItem().toString();

		JSONObject regionData = new JSONObject();
		for (int i = 0; i < dataTableModel.getRowCount(); i++) {
			String key = dataTableModel.getValueAt(i, 0).toString();
			String value = dataTableModel.getValueAt(i, 1).toString();
			if (!value.isEmpty()) {
				regionData.put(key, value);
			}
		}

		JSONObject finalJSON = new JSONObject();
		JSONObject regionJSON = new JSONObject();

		JSONArray row = new JSONArray();
		row.put(Integer.parseInt(fromRow)); // esto estaba normal sin el parse
		row.put(Integer.parseInt(toRow));
		regionJSON.put("row", row);

		JSONArray col = new JSONArray();
		col.put(Integer.parseInt(fromCol)); // esto estaba normal sin el parse
		col.put(Integer.parseInt(toCol));
		regionJSON.put("col", col);

		JSONObject specJSON = new JSONObject();
		specJSON.put("type", regionType);
		specJSON.put("data", regionData);
		regionJSON.put("spec", specJSON);

		finalJSON.append("regions", regionJSON);

		try {
			ctrl.setRegions(finalJSON);
			this.setVisible(false);
		} catch (Exception e) {
			ViewUtils.showErrorMsg(e.getMessage());
		}
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		updateInfoRegions(map);

	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		updateInfoRegions(map);

	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {

	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {

	}

	@Override
	public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {

	}

	private void updateInfoRegions(MapInfo map) {
		fromRowModel.removeAllElements();
		toRowModel.removeAllElements();
		fromColModel.removeAllElements();
		toColModel.removeAllElements();

		int numRows = map.getRows();
		int numCols = map.getCols();

		for (int i = 0; i < numRows; i++) {
			fromRowModel.addElement(String.valueOf(i));
			toRowModel.addElement(String.valueOf(i));
		}

		for (int i = 0; i < numCols; i++) {
			fromColModel.addElement(String.valueOf(i));
			toColModel.addElement(String.valueOf(i));
		}

	}

	private void modifyDataTable(int i) {
		dataTableModel.setRowCount(0);
		if (i >= 0 && i < regionsInfo.size()) {
			JSONObject info = regionsInfo.get(i);
			JSONObject data = info.optJSONObject("data");
			if (data != null) {
				for (String key : data.keySet()) {
					String value = data.optString(key);
					dataTableModel.addRow(new Object[] { key, "", value });
				}
			}
		}
	}

}