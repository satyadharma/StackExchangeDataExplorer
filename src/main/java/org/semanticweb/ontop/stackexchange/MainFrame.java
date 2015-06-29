package org.semanticweb.ontop.stackexchange;

import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLResultSet;

import java.awt.EventQueue;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JButton;

import java.awt.Insets;

import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Font;

public class MainFrame {
	private JFrame frmProgrammerstackexchangeapps;
	private JTable userTable;
	private JTable postTable;
	private Object rowData[][];
	private Object columnNames[];
	private DefaultTableModel tableModel;
	private DefaultTableModel questionTableModel;
	private OntologyInterface ontology;
	private JTextField displayNameTf;
	private JTextField reputationLtTf;
	private JTextField reputationGtTf;
	private JTextField websiteUrlTf;
	private JTextField locationTf;
	private JTextField questionFilterTf;

	/**
	 * Launch the application.
	 */
	public void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frmProgrammerstackexchangeapps.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		ontology = StackExchangeDataExplorer.ontology;
		frmProgrammerstackexchangeapps = new JFrame();
		frmProgrammerstackexchangeapps.setTitle("ProgrammerStackExchangeApps");
		frmProgrammerstackexchangeapps.setBounds(100, 100, 800, 600);
		frmProgrammerstackexchangeapps
				.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProgrammerstackexchangeapps.getContentPane().setLayout(
				new MigLayout("", "[grow]", "[grow]"));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmProgrammerstackexchangeapps.getContentPane().add(tabbedPane,
				"cell 0 0,grow");

		JPanel panel = new JPanel();
		tabbedPane.addTab("User Explorer", null, panel, null);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 600, 100 };
		gbl_panel.rowHeights = new int[] { 375, 23 };
		gbl_panel.columnWeights = new double[] { 1.0, 1.0 };
		gbl_panel.rowWeights = new double[] { 1.0, 0.0 };
		panel.setLayout(gbl_panel);

		rowData = new Object[][] {};
		columnNames = new Object[] {};
		tableModel = new DefaultTableModel();
		questionTableModel = new DefaultTableModel();

		userTable = new JTable(tableModel) {
			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == this.getColumnCount() - 1) {
					return true;
				} else {
					return false;
				}
			}
		};

		userTable.setAutoCreateRowSorter(true);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel.add(scrollPane, gbc_scrollPane);

		scrollPane.setViewportView(userTable);
		JButton btnNewButton = new JButton("Search");
		btnNewButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String nameFilter = displayNameTf.getText();
				String reputationLtFilter = reputationLtTf.getText();
				String reputationGtFilter = reputationGtTf.getText();
				String webisteUrlFilter = websiteUrlTf.getText();
				String locationFilter = locationTf.getText();
				String sparqlQuery = "PREFIX : <http://www.semanticweb.org/satyadharma/ontologies/stackoverflow/>"
						+ "SELECT ?user ?name ?reputation ?website ?location "
						+ "WHERE {?user a :User ; :userDisplayName ?name ; :userReputation ?reputation ; "
						+ ":userWebsiteUrl ?website ; :userLocation ?location . ";

				if (!nameFilter.equals("")) {
					sparqlQuery += String.format(
							"FILTER regex(?name, \"%s\", \"i\")", nameFilter);
				}
				;
				if (!reputationLtFilter.equals("")) {
					sparqlQuery += String.format("FILTER (?reputation >= %s)",
							reputationLtFilter);
				}
				;
				if (!reputationGtFilter.equals("")) {
					sparqlQuery += String.format("FILTER (?reputation >= %s)",
							reputationGtFilter);
				}
				;
				if (!webisteUrlFilter.equals("")) {
					sparqlQuery += String.format(
							"FILTER regex(?website, \"%s\", \"i\")",
							webisteUrlFilter);
				}
				;
				if (!locationFilter.equals("")) {
					sparqlQuery += String.format(
							"FILTER regex(?location, \"%s\", \"i\")",
							locationFilter);
				}
				;
				sparqlQuery += " } ";
				updateData(ontology.query(sparqlQuery));
			}
		});

		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 0;
		panel.add(panel_3, gbc_panel_3);
		panel_3.setLayout(new MigLayout("", "[grow 50]",
				"[][][][][][][][][][][]"));

		JLabel lblFilter = new JLabel("FILTER");
		lblFilter.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_3.add(lblFilter, "cell 0 0,alignx center");

		JLabel lblNewLabel = new JLabel("Display Name");
		panel_3.add(lblNewLabel, "cell 0 1");

		displayNameTf = new JTextField();
		panel_3.add(displayNameTf, "cell 0 2,growx");
		displayNameTf.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Reputation (<)");
		panel_3.add(lblNewLabel_1, "cell 0 3");

		reputationLtTf = new JTextField();
		panel_3.add(reputationLtTf, "cell 0 4,growx");
		reputationLtTf.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Reputation (>)");
		panel_3.add(lblNewLabel_2, "cell 0 5");

		reputationGtTf = new JTextField();
		panel_3.add(reputationGtTf, "cell 0 6,growx");
		reputationGtTf.setColumns(10);

		JLabel lblNewLabel_3 = new JLabel("Website Url");
		panel_3.add(lblNewLabel_3, "cell 0 7");

		websiteUrlTf = new JTextField();
		panel_3.add(websiteUrlTf, "cell 0 8,growx");
		websiteUrlTf.setColumns(10);

		JLabel lblNewLabel_4 = new JLabel("Location");
		panel_3.add(lblNewLabel_4, "cell 0 9");

		locationTf = new JTextField();
		panel_3.add(locationTf, "cell 0 10,growx");
		locationTf.setColumns(10);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 1;
		panel.add(btnNewButton, gbc_btnNewButton);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Question Explorer", null, panel_1, null);
		panel_1.setLayout(new MigLayout("", "[][grow][]", "[grow][]"));

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_1.add(scrollPane_1, "cell 0 0 3 1,grow");

		postTable = new JTable(questionTableModel);
		scrollPane_1.setViewportView(postTable);

		JLabel lblNewLabel_5 = new JLabel("Keyword (use comma to separate)");
		panel_1.add(lblNewLabel_5, "cell 0 1,alignx trailing");

		questionFilterTf = new JTextField();
		panel_1.add(questionFilterTf, "cell 1 1,growx");
		questionFilterTf.setColumns(10);

		JButton btnNewButton_1 = new JButton("Search");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String filter = questionFilterTf.getText();
				filter.replaceAll("\\s+", "");
				String[] filterList = filter.split(",");

				String sparqlQuery = "PREFIX : <http://www.semanticweb.org/satyadharma/ontologies/stackoverflow/>"
						+ "SELECT ?question ?body ?view ?score "
						+ "WHERE {?question a :Question ; :postBody ?body ; :postViewCount ?view ; :postScore ?score . ";
				for (int ii = 0; ii < filterList.length; ii++) {
					if (!filterList[ii].equals("")) {
						sparqlQuery += String.format(
								"FILTER regex(?body, \"%s\", \"i\") ",
								filterList[ii]);
					}
				}
				sparqlQuery += " } ";

				Object[][] res = query(sparqlQuery);
				Object[][] tableData = new Object[res.length][];
				for (int ii = 0; ii < res.length; ii++) {
					tableData[ii] = new Object[res[ii].length + 1];
					for (int jj = 0; jj < res[ii].length; jj++) {
						tableData[ii][jj] = res[ii][jj];
					}
					tableData[ii][res[ii].length] = "Click for Answers";
				}

				columnNames = new Object[] { "Question Id", "Body",
						"View Count", "Score", "Answers" };
				questionTableModel.setDataVector(tableData, columnNames);
				postTable.getColumn("Answers").setCellRenderer(
						new ButtonRenderer());
				postTable.getColumn("Answers").setCellEditor(
						new ButtonAnswerEditor(new JCheckBox()));
				questionTableModel.fireTableDataChanged();
			}
		});
		panel_1.add(btnNewButton_1, "cell 2 1");
	}

	public void updateData(QuestOWLResultSet queryResult) {
		int columnSize;
		columnNames = new Object[] { "User Id", "Display Name", " Reputation",
				"Website Url", "Location", "Details" };
		try {
			columnSize = queryResult.getColumnCount();
			ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
			while (queryResult.nextRow()) {
				ArrayList<String> row = new ArrayList<String>();
				for (int idx = 1; idx <= columnSize; idx++) {
					OWLObject binding = queryResult.getOWLObject(idx);
					String value;
					if (binding instanceof OWLLiteral) {
						value = ((OWLLiteral) binding).getLiteral();
					} else {
						value = binding.toString();
					}
					row.add(value);
				}
				data.add(row);
			}

			rowData = new Object[data.size()][columnSize + 1];
			for (int ii = 0; ii < data.size(); ii++) {
				for (int jj = 0; jj < columnSize; jj++) {
					if (jj != 2) {
						rowData[ii][jj] = data.get(ii).get(jj);
					} else {
						rowData[ii][jj] = Integer
								.parseInt(data.get(ii).get(jj));
					}
				}
				rowData[ii][columnSize] = "Click for Details";
			}
			queryResult.close();
			tableModel.setDataVector(rowData, columnNames);
			userTable.getColumn("Details")
					.setCellRenderer(new ButtonRenderer());
			userTable.getColumn("Details").setCellEditor(
					new ButtonEditor(new JCheckBox()));
			tableModel.fireTableDataChanged();
		} catch (OWLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Object[][] query(String q) {
		QuestOWLResultSet rs = ontology.query(q);
		Object[][] rowData = null;
		int columnSize = 0;
		try {
			columnSize = rs.getColumnCount();
			ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

			while (rs.nextRow()) {
				ArrayList<String> row = new ArrayList<String>();
				for (int idx = 1; idx <= columnSize; idx++) {
					OWLObject binding = rs.getOWLObject(idx);
					String value;
					if (binding instanceof OWLLiteral) {
						value = ((OWLLiteral) binding).getLiteral();
					} else {
						value = binding.toString();
					}
					row.add(value);
				}
				data.add(row);
			}
			rowData = new Object[data.size()][columnSize];
			for (int ii = 0; ii < data.size(); ii++) {
				for (int jj = 0; jj < columnSize; jj++) {
					rowData[ii][jj] = data.get(ii).get(jj);
				}
			}
		} catch (OWLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rowData;
	}
}

@SuppressWarnings("serial")
class ButtonRenderer extends JButton implements TableCellRenderer {
	private String id;

	public ButtonRenderer() {
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(UIManager.getColor("Button.background"));
		}
		setText((value == null) ? "" : value.toString());
		return this;
	}

	public String getId() {
		return id;
	}
}

@SuppressWarnings("serial")
class ButtonEditor extends DefaultCellEditor {
	protected JButton button;
	private boolean isPushed;
	private String label;
	private String id;
	private OntologyInterface ontology;

	public ButtonEditor(JCheckBox checkBox) {
		super(checkBox);
		ontology = StackExchangeDataExplorer.ontology;
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if (isSelected) {
			button.setForeground(table.getSelectionForeground());
			button.setBackground(table.getSelectionBackground());
		} else {
			button.setForeground(table.getForeground());
			button.setBackground(table.getBackground());
		}
		label = (value == null) ? "" : value.toString();
		id = table.getValueAt(row, 0).toString();
		button.setText(label);
		isPushed = true;
		return button;
	}

	@Override
	public Object getCellEditorValue() {
		if (isPushed) {
			UserDetailFrame frame = new UserDetailFrame(id, ontology);
			frame.pack();
			frame.setVisible(true);
		}
		isPushed = false;
		return new String(label);
	}

	@Override
	public boolean stopCellEditing() {
		isPushed = false;
		return super.stopCellEditing();
	}

	@Override
	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
}

@SuppressWarnings("serial")
class ButtonAnswerEditor extends DefaultCellEditor {
	protected JButton button;
	private String label;
	private boolean isPushed;
	private String questionId;
	private String questionBody;
	private OntologyInterface ontology;

	public ButtonAnswerEditor(JCheckBox checkBox) {
		super(checkBox);
		ontology = StackExchangeDataExplorer.ontology;
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if (isSelected) {
			button.setForeground(table.getSelectionForeground());
			button.setBackground(table.getSelectionBackground());
		} else {
			button.setForeground(table.getForeground());
			button.setBackground(table.getBackground());
		}
		label = (value == null) ? "" : value.toString();
		questionId = table.getValueAt(row, 0).toString();
		questionBody = table.getValueAt(row, 1).toString();
		button.setText(label);
		isPushed = true;
		return button;
	}

	@Override
	public Object getCellEditorValue() {
		if (isPushed) {
			QuestionDetailFrame frame = new QuestionDetailFrame(questionId,
					questionBody, ontology);
			frame.pack();
			frame.setVisible(true);
		}
		isPushed = false;
		return new String(label);
	}

	@Override
	public boolean stopCellEditing() {
		isPushed = false;
		return super.stopCellEditing();
	}

	@Override
	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
}
