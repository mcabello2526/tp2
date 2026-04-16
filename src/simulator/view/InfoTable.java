package simulator.view;

import java.awt.BorderLayout;



import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

public class InfoTable extends JPanel {

	private static final long serialVersionUID = 1L;

	private String title;
	private TableModel tableModel;

	InfoTable(String title, TableModel tableModel) {
		this.title = title;
		this.tableModel = tableModel;
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		/*
		TitledBorder titleBorder = BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK), 
				title, 
				TitledBorder.LEFT, 
				TitledBorder.TOP, 
				new Font("Arial", Font.BOLD, 14) 
		);
		this.setBorder(titleBorder);
		*/
		
		TitledBorder titleBorder = new TitledBorder(title); 
		this.setBorder(titleBorder);
		
		JTable table = new JTable (this.tableModel); 
		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane); 
		
	}
}