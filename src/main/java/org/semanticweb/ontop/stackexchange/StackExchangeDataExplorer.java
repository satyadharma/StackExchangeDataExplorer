package org.semanticweb.ontop.stackexchange;

public class StackExchangeDataExplorer {
	static OntologyInterface ontology;
	private MainFrame apps;	
	
	public StackExchangeDataExplorer () {
		apps = new MainFrame();
		ontology = new OntologyInterface();
	}
	
	public static void main(String[] args)  {
		StackExchangeDataExplorer explorer = new StackExchangeDataExplorer();
		MainFrame explorerApps = explorer.getApps();
		explorerApps.run();
	}

	public MainFrame getApps() {
		return apps;
	}

	public MainFrame setApps(MainFrame apps) {
		this.apps = apps;
		return apps;
	}
}
