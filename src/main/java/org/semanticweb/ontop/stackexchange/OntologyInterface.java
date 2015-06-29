package org.semanticweb.ontop.stackexchange;

import it.unibz.krdb.obda.exception.InvalidMappingException;
import it.unibz.krdb.obda.exception.InvalidPredicateDeclarationException;
import it.unibz.krdb.obda.io.ModelIOManager;
import it.unibz.krdb.obda.model.OBDADataFactory;
import it.unibz.krdb.obda.model.OBDAException;
import it.unibz.krdb.obda.model.OBDAModel;
import it.unibz.krdb.obda.model.impl.OBDADataFactoryImpl;
import it.unibz.krdb.obda.owlrefplatform.core.QuestConstants;
import it.unibz.krdb.obda.owlrefplatform.core.QuestPreferences;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWL;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLConnection;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLFactory;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLResultSet;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLStatement;

import java.io.File;
import java.io.IOException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

public class OntologyInterface {
	// Path for ontology file
	final String owlfile = "resources/StackOverflow.owl";
	// Path for mapping file
	final String obdafile = "resources/StackOverflow.obda";

	private OWLOntologyManager manager;
	private OWLOntology ontology;
	private OBDADataFactory fac;
	private OBDAModel obdaModel;
	private ModelIOManager ioManager;
	private QuestPreferences preference;
	private QuestOWLFactory factory;
	private QuestOWL reasoner;
	private QuestOWLConnection conn;
	private QuestOWLStatement st;

	public OntologyInterface () {
		/*
		 * Load the ontology from an external .owl file.
		 */
		manager = OWLManager.createOWLOntologyManager();
		try {
			ontology = manager.loadOntologyFromOntologyDocument(new File(owlfile));
		} catch (OWLOntologyCreationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/*
		 * Load the OBDA model from an external .obda file
		 */
		fac = OBDADataFactoryImpl.getInstance();
		obdaModel = fac.getOBDAModel();
		ioManager = new ModelIOManager(obdaModel);
		try {
			ioManager.load(obdafile);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InvalidPredicateDeclarationException e1) {
			e1.printStackTrace();
		} catch (InvalidMappingException e1) {
			e1.printStackTrace();
		}

		/*
		 * Prepare the configuration for the Quest instance. The example below shows the setup for
		 * "Virtual ABox" mode
		 */
		preference = new QuestPreferences();
		preference.setCurrentValueOf(QuestPreferences.ABOX_MODE, QuestConstants.VIRTUAL);

		/*
		 * Create the instance of Quest OWL reasoner.
		 */
		factory = new QuestOWLFactory();
		factory.setOBDAController(obdaModel);
		factory.setPreferenceHolder(preference);
		reasoner = factory.createReasoner(ontology, new SimpleConfiguration());

		/*
		 * Prepare the data connection for querying.
		 */
		try {
			conn = reasoner.getConnection();
			st = conn.createStatement();
		} catch (OBDAException e) {
			e.printStackTrace();
		} catch (OWLException e) {
			e.printStackTrace();
		}
	}

	public QuestOWLResultSet query (String sparqlQuery) {
		QuestOWLResultSet result = null;
		try {
			result = st.executeTuple(sparqlQuery);
			QuestOWLStatement qst = st;
			String sqlQuery = qst.getUnfolding(sparqlQuery);
			System.out.println();
			System.out.println("The input SPARQL query:");
			System.out.println("=======================");
			System.out.println(sparqlQuery);
			System.out.println();
			System.out.println("The output SQL query:");
			System.out.println("=====================");
			System.out.println(sqlQuery);
		} catch (OWLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void close () {
		try {
			st.close();
			conn.close();
		} catch (OWLException e) {
			e.printStackTrace();
		}
		reasoner.dispose();
	}
}
