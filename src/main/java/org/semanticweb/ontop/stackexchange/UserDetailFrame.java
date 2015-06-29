package org.semanticweb.ontop.stackexchange;

import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLResultSet;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;

import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import java.awt.Insets;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


/**
 * User detail frame is a frame used to show detail of a user that has been
 * chosen in main frame.
 */
@SuppressWarnings("serial")
public class UserDetailFrame extends JFrame {
	private String userId;
	private OntologyInterface ontology;
	private JPanel contentPane;
	private JTextField userIdTextField;
	private JTextField reputationTextField;
	private JTextField creationDateTextField;
	private JTextField displayNameTextField;
	private JTextField lastAccessDateTextField;
	private JTextField websiteUrlTextField;
	private JTextField locationTextField;
	private JTextField viewsTextField;
	private JTextField upvotesTextField;
	private JTextField downvotesTextField;
	private JTextField ageTextField;
	private JTextField badgeTf;
	private JTextField questionTf;
	private JTextField answerTf;
	private JTextField tagTf;
	private JPanel postListPanel;

	public UserDetailFrame(String user, OntologyInterface ontologyI) {
		ontology = ontologyI;
		this.setTitle(user.substring(65, user.length() - 1));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 614 };
		gbl_contentPane.rowHeights = new int[] { 431 };
		gbl_contentPane.columnWeights = new double[] { 1.0 };
		gbl_contentPane.rowWeights = new double[] { 1.0 };
		contentPane.setLayout(gbl_contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		contentPane.add(tabbedPane, gbc_tabbedPane);

		JPanel panel = new JPanel();
		tabbedPane.addTab("User Detail", null, panel, null);
		panel.setLayout(new MigLayout("", "[50][grow]",
				"[][][][][][][][][][][][]"));

		JLabel userIdLabel = new JLabel("User Id");
		panel.add(userIdLabel, "cell 0 0,alignx trailing");

		userIdTextField = new JTextField();
		panel.add(userIdTextField, "cell 1 0,growx");
		userIdTextField.setColumns(10);

		JLabel reputationLabel = new JLabel("Reputation");
		panel.add(reputationLabel, "cell 0 1,alignx trailing");

		reputationTextField = new JTextField();
		panel.add(reputationTextField, "cell 1 1,growx");
		reputationTextField.setColumns(10);

		JLabel creationDateLabel = new JLabel("Creation Date");
		panel.add(creationDateLabel, "cell 0 2,alignx trailing");

		creationDateTextField = new JTextField();
		panel.add(creationDateTextField, "cell 1 2,growx");
		creationDateTextField.setColumns(10);

		JLabel displayNameLabel = new JLabel("Display Name");
		panel.add(displayNameLabel, "cell 0 3,alignx trailing");

		displayNameTextField = new JTextField();
		panel.add(displayNameTextField, "cell 1 3,growx");
		displayNameTextField.setColumns(10);

		JLabel LastAccessDateLabel = new JLabel("Last Access Date");
		panel.add(LastAccessDateLabel, "cell 0 4,alignx trailing");

		lastAccessDateTextField = new JTextField();
		panel.add(lastAccessDateTextField, "cell 1 4,growx");
		lastAccessDateTextField.setColumns(10);

		JLabel websiteUrlLabel = new JLabel("Website URL");
		panel.add(websiteUrlLabel, "cell 0 5,alignx trailing");

		websiteUrlTextField = new JTextField();
		panel.add(websiteUrlTextField, "cell 1 5,growx");
		websiteUrlTextField.setColumns(10);

		JLabel locationLabel = new JLabel("Location");
		panel.add(locationLabel, "cell 0 6,alignx trailing");

		locationTextField = new JTextField();
		panel.add(locationTextField, "cell 1 6,growx");
		locationTextField.setColumns(10);

		JLabel aboutMeLabel = new JLabel("About Me");
		panel.add(aboutMeLabel, "cell 0 7,alignx right");

		JEditorPane aboutMeEditPane = new JEditorPane();
		panel.add(aboutMeEditPane, "cell 1 7,growx,aligny baseline");

		JLabel viewsLabel = new JLabel("Views");
		panel.add(viewsLabel, "cell 0 8,alignx trailing");

		viewsTextField = new JTextField();
		panel.add(viewsTextField, "cell 1 8,growx");
		viewsTextField.setColumns(10);

		JLabel upvotesLabel = new JLabel("Upvotes");
		panel.add(upvotesLabel, "cell 0 9,alignx trailing");

		upvotesTextField = new JTextField();
		panel.add(upvotesTextField, "cell 1 9,growx");
		upvotesTextField.setColumns(10);

		JLabel downvotesLabel = new JLabel("Downvotes");
		panel.add(downvotesLabel, "cell 0 10,alignx trailing");

		downvotesTextField = new JTextField();
		panel.add(downvotesTextField, "cell 1 10,growx");
		downvotesTextField.setColumns(10);

		JLabel ageLabel = new JLabel("Age");
		panel.add(ageLabel, "cell 0 11,alignx trailing");

		ageTextField = new JTextField();
		panel.add(ageTextField, "cell 1 11,growx");
		ageTextField.setColumns(10);

		// Fetch query result
		userId = user;
		String detailQuery = "PREFIX : <http://www.semanticweb.org/satyadharma/ontologies/stackoverflow/>"
				+ "SELECT * where { "
				+ userId
				+ " a :User ; "
				+ ":userReputation ?reputation ; :userCreationDate ?creationDate ; "
				+ ":userDisplayName ?name ; :userLastAccessDate ?date ; "
				+ ":userWebsiteUrl ?url ; :userLocation ?location ; "
				+ ":userAboutMe ?about ; :userViews ?views ; "
				+ ":userUpVotes ?upvote ; :userDownVotes ?downvote ; "
				+ ":userAge ?age . } ";
		Object[][] rowData = query(detailQuery);

		userIdTextField.setText(userId);
		reputationTextField.setText((String) rowData[0][0]);
		creationDateTextField.setText((String) rowData[0][1]);
		displayNameTextField.setText((String) rowData[0][2]);
		lastAccessDateTextField.setText((String) rowData[0][3]);
		websiteUrlTextField.setText((String) rowData[0][4]);
		locationTextField.setText((String) rowData[0][5]);
		viewsTextField.setText((String) rowData[0][7]);
		upvotesTextField.setText((String) rowData[0][8]);
		downvotesTextField.setText((String) rowData[0][9]);
		ageTextField.setText((String) rowData[0][10]);

		aboutMeEditPane.setEditable(false);
		HTMLEditorKit kit = new HTMLEditorKit();
		aboutMeEditPane.setEditorKit(kit);

		String aboutMe = (String) rowData[0][6];
		Document doc = kit.createDefaultDocument();
		aboutMeEditPane.setDocument(doc);
		aboutMeEditPane.setText(aboutMe);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("User Posts", null, panel_1, null);
		panel_1.setLayout(new MigLayout("", "[grow]", "[grow][]"));

		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, "cell 0 0,grow");

		JPanel panel_2 = new JPanel();
		scrollPane.setViewportView(panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.rowHeights = new int[] { 0 };
		gbl_panel_2.columnWeights = new double[] {};
		gbl_panel_2.rowWeights = new double[] { Double.MIN_VALUE };
		panel_2.setLayout(gbl_panel_2);

		postListPanel = panel_2;

		JButton btnNewButton = new JButton("Show Posts");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showPost();
			}
		});
		panel_1.add(btnNewButton, "cell 0 1,alignx right,aligny center");

		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Additional Information", null, panel_3, null);
		panel_3.setLayout(new MigLayout("", "[grow]", "[grow][]"));

		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4, "cell 0 0,grow");
		panel_4.setLayout(new MigLayout("", "[][grow]", "[][][][]"));

		JLabel lblNewLabel = new JLabel("Badge Count");
		panel_4.add(lblNewLabel, "cell 0 0,alignx trailing");

		badgeTf = new JTextField();
		panel_4.add(badgeTf, "cell 1 0,growx");
		badgeTf.setColumns(10);

		JLabel lblQuestionCount = new JLabel("Question Count");
		panel_4.add(lblQuestionCount, "cell 0 1,alignx trailing");

		questionTf = new JTextField();
		questionTf.setColumns(10);
		panel_4.add(questionTf, "cell 1 1,growx");

		JLabel lblAnswerCount = new JLabel("Answer Count");
		panel_4.add(lblAnswerCount, "cell 0 2,alignx trailing");

		answerTf = new JTextField();
		answerTf.setColumns(10);
		panel_4.add(answerTf, "cell 1 2,growx");

		JLabel lblTagWithMost = new JLabel("Tag with Most Posts");
		panel_4.add(lblTagWithMost, "cell 0 3,alignx trailing");

		tagTf = new JTextField();
		tagTf.setColumns(10);
		panel_4.add(tagTf, "cell 1 3,growx");

		JButton btnNewButton_1 = new JButton("Show");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showMoreDetail();
			}
		});
		panel_3.add(btnNewButton_1, "cell 0 1,alignx right");
		aboutMeEditPane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent hle) {
				if (HyperlinkEvent.EventType.ACTIVATED.equals(hle
						.getEventType())) {
					System.out.println(hle.getURL());
					Desktop desktop = Desktop.getDesktop();
					try {
						desktop.browse(hle.getURL().toURI());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
	}

	public void showPost() {
		String postQuery = "PREFIX : <http://www.semanticweb.org/satyadharma/ontologies/stackoverflow/>"
				+ "SELECT * where { ?post a :Post ; :PostOwnerUser "
				+ userId
				+ " ; :postBody ?body . }";

		Object[][] rowData = query(postQuery);

			for (int ii = 0; ii < rowData.length; ii++) {
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.BOTH;
				c.gridx = 0;
				c.gridy = ii;
				String postIdString = (String) rowData[ii][0];
				postIdString = postIdString.substring(65,
						postIdString.length() - 1);
				JLabel postLabel = new JLabel(postIdString);
				postLabel.setVerticalAlignment(JLabel.TOP);
				postLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				Border edge = BorderFactory.createRaisedBevelBorder();
				postLabel.setBorder(edge);
				postListPanel.add(postLabel, c);
				c.gridx = 1;
				c.gridy = ii;

				JTextPane postPane = new JTextPane();
				postPane.setEditable(false);
				postPane.setBorder(edge);
				HTMLEditorKit kit = new HTMLEditorKit();
				postPane.setEditorKit(kit);
				StyleSheet styleSheet = kit.getStyleSheet();
				styleSheet.addRule("p {margin:0;padding:0;}");
				String postText = (String) rowData[ii][1];
				Document doc = kit.createDefaultDocument();
				postPane.setDocument(doc);
				postPane.setText(postText);
				postPane.setSize(new Dimension(800, Integer.MAX_VALUE));
				postPane.setPreferredSize(new Dimension(800, (int) postPane
						.getPreferredSize().getHeight()));
				postPane.addHyperlinkListener(new HyperlinkListener() {
					public void hyperlinkUpdate(HyperlinkEvent hle) {
						if (HyperlinkEvent.EventType.ACTIVATED.equals(hle
								.getEventType())) {
							System.out.println(hle.getURL());
							Desktop desktop = Desktop.getDesktop();
							try {
								desktop.browse(hle.getURL().toURI());
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				});
				postListPanel.add(postPane, c);
			}
			postListPanel.update(getGraphics());
			postListPanel.repaint();
			this.validate();
			this.repaint();
	}

	public void showMoreDetail() {
		String badgeQuery = "PREFIX : <http://www.semanticweb.org/satyadharma/ontologies/stackoverflow/>"
				+ "SELECT ?badge where { ?badge a :Badge ; :BadgeUser "
				+ userId + " . }";
		Object[][] rowData = query(badgeQuery);
		badgeTf.setText(String.valueOf(rowData.length));

		String questionQuery = "PREFIX : <http://www.semanticweb.org/satyadharma/ontologies/stackoverflow/>"
				+ "SELECT ?post where { ?post a :Question ; :PostOwnerUser "
				+ userId + " . }";
		rowData = query(questionQuery);
		questionTf.setText(String.valueOf(rowData.length));

		String answerQuery = "PREFIX : <http://www.semanticweb.org/satyadharma/ontologies/stackoverflow/>"
				+ "SELECT ?post where { ?post a :Answer ; :PostOwnerUser "
				+ userId + " . }";
		rowData = query(answerQuery);
		answerTf.setText(String.valueOf(rowData.length));

		String questionTagQuery = "PREFIX : <http://www.semanticweb.org/satyadharma/ontologies/stackoverflow/>"
				+ "SELECT ?post ?tagName where { ?post :PostOwnerUser "
				+ userId + " ; :PostTag ?tag . ?tag :tagName ?tagName . }";
		rowData = query(questionTagQuery);
		HashMap<String, Integer> hash = new HashMap<String, Integer>();
		for (int ii = 0; ii < rowData.length; ii++) {
			String tag = (String) rowData[ii][1];
			if (!hash.containsKey(tag)) {
				hash.put(tag, 1);
			} else {
				hash.put(tag, hash.get(tag) + 1);
			}
		}

		String answerTagQuery = "PREFIX : <http://www.semanticweb.org/satyadharma/ontologies/stackoverflow/>"
				+ "SELECT ?answer ?tagName where { ?answer :AnswerParent ?post ; :PostOwnerUser "
				+ userId
				+ " . { ?post :PostTag ?tag . ?tag :tagName ?tagName } }";
		rowData = query(answerTagQuery);
		for (int ii = 0; ii < rowData.length; ii++) {
			String tag = (String) rowData[ii][1];
			if (!hash.containsKey(tag)) {
				hash.put(tag, 1);
			} else {
				hash.put(tag, hash.get(tag) + 1);
			}
		}
		int max = 0;
		String maxTag = "Nothing";
		for (String key : hash.keySet()) {
			if (hash.get(key) > max) {
				max = hash.get(key);
				maxTag = key;
			}
		}
		tagTf.setText(String.format("%s (%s)", maxTag, max));
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
			e.printStackTrace();
		}
		return rowData;
	}
}
