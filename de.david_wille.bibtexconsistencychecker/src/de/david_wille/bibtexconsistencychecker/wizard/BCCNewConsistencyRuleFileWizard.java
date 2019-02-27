package de.david_wille.bibtexconsistencychecker.wizard;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ISetup;

import de.david_wille.bibtexconsistencychecker.consistencyrule.BCCConsistencyRuleStandaloneSetup;
import de.david_wille.bibtexconsistencychecker.consistencyrule.util.BCCDefaultConsistencyRule;

public class BCCNewConsistencyRuleFileWizard extends AbstractFileCreationWizard {
	
	private static final String FILE_EXTENSION = "bcc_rule";
	private static final String FILE_CREATION_PAGE_NAME = "New BibTeX Consistency Rule";
	private static final String FILE_CREATION_PAGE_TITLE = "BibTeX Consistency Checker Rule";
	private static final String FILE_CREATION_PAGE_DESCRIPTION = "Create a new BibTeX Consistency Checker Rule.";
	
	private static final String ICON_PATH = "icons/bcc_rule_icon96.png";
	private static final String WINDOW_TITLE = "New BibTeX Consistency Rule";

	@Override
	protected String getFileCreationPageName() {
		return FILE_CREATION_PAGE_NAME;
	}

	@Override
	protected String getFileCreationPageTitle() {
		return FILE_CREATION_PAGE_TITLE;
	}

	@Override
	protected String getFileCreationPageDescription() {
		return FILE_CREATION_PAGE_DESCRIPTION;
	}

	@Override
	protected String getFileCreationWindowTitle() {
		return WINDOW_TITLE;
	}

	@Override
	protected String getFileExtension() {
		return FILE_EXTENSION;
	}

	@Override
	protected ISetup getFileCreationSetup() {
		return new BCCConsistencyRuleStandaloneSetup();
	}

	@Override
	protected EObject getStoredModel(String name) {
		return BCCDefaultConsistencyRule.generate(name);
	}

	@Override
	protected String getIconPath() {
		return ICON_PATH;
	}

}
