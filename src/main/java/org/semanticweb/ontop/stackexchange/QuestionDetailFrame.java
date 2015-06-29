package org.semanticweb.ontop.stackexchange;

import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLResultSet;

import java.awt.Desktop;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import java.awt.GridBagLayout;

import javax.swing.JScrollPane;

import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;

import java.awt.GridBagConstraints;
import java.util.ArrayList;

public class QuestionDetailFrame extends JFrame {
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public QuestionDetailFrame(String questionId, String questionBody, OntologyInterface ontologyI) {
		this.setTitle(questionId);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPane.add(scrollPane, gbc_scrollPane);

		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0};
		gbl_panel.rowHeights = new int[]{0};
		gbl_panel.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{Double.MIN_VALUE};
		panel.setLayout(gbl_panel);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		String questionIdString = questionId.substring(65, questionId.length() - 1);
		JLabel questionLabel = new JLabel(questionIdString);
		questionLabel.setVerticalAlignment(JLabel.TOP);
		questionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		Border edge = BorderFactory.createRaisedBevelBorder();
		questionLabel.setBorder(edge);
		panel.add(questionLabel, c);

		{
		c.gridx = 1;
		c.gridy = 0;
		JTextPane postPane = new JTextPane();
		postPane.setEditable(false);
		postPane.setBorder(edge);
		HTMLEditorKit kit = new HTMLEditorKit();
		postPane.setEditorKit(kit);
		StyleSheet styleSheet = kit.getStyleSheet();
		styleSheet.addRule("p {margin:0;padding:0;}");
		Document doc = kit.createDefaultDocument();
		postPane.setDocument(doc);
		postPane.setText(questionBody);
		postPane.setSize(new Dimension(800, Integer.MAX_VALUE));
		postPane.setPreferredSize(new Dimension(800, (int)postPane.getPreferredSize().getHeight()));
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
		panel.add(postPane, c);
		}

		String answerQuery = "PREFIX : <http://www.semanticweb.org/satyadharma/ontologies/stackoverflow/>"
				+ "SELECT ?answer ?body ?score where { ?answer :AnswerParent " + questionId + " ; :postBody ?body ; :postScore ?score . } ORDER BY DESC(?score)" ;
		QuestOWLResultSet rs = ontologyI.query(answerQuery);
		Object[][] rowData = null;
		int columnSize = 0;
		try {
			columnSize = rs.getColumnCount();
			ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

			while (rs.nextRow()) {
				ArrayList<String> row = new ArrayList<String>();
				for (int idx = 1; idx <= columnSize; idx++) {
					OWLObject binding = rs.getOWLObject(idx);
					String value ;
					if(binding instanceof OWLLiteral){
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
					System.out.println(rowData[ii][jj]);
				}
				c = new GridBagConstraints();
				c.fill = GridBagConstraints.BOTH;
				c.gridx = 0;
				c.gridy = ii + 1;
				String postIdString = (String) rowData[ii][0];
				postIdString = postIdString.substring(65, postIdString.length() - 1);
				JLabel postLabel = new JLabel(postIdString);
				postLabel.setVerticalAlignment(JLabel.TOP);
				postLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				postLabel.setBorder(edge);
				panel.add(postLabel, c);
				c.gridx = 1;
				c.gridy = ii + 1;

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
				postPane.setPreferredSize(new Dimension(800, (int)postPane.getPreferredSize().getHeight()));
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
				panel.add(postPane, c);
			}

			panel.update(getGraphics());
			panel.repaint();
			this.validate();
			this.repaint();
			//rs.close();
		} catch (OWLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
