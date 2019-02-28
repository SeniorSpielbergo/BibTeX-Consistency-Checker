package de.david_wille.bibtexconsistencychecker.wizard;

import org.eclipse.core.resources.IContainer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ISetup;

import com.google.inject.Injector;

import de.david_wille.bibtexconsistencychecker.consistencyrule.BCCConsistencyRuleStandaloneSetup;
import de.david_wille.bibtexconsistencychecker.consistencyrule.ui.internal.ConsistencyruleActivator;
import de.david_wille.bibtexconsistencychecker.consistencyrule.util.BCCDefaultConsistencyRule;

public class BCCNewConsistencyRuleFileWizard extends BCCAbstractFileInSpecificFolderCreationWizard {
	
	private static final String FILE_EXTENSION = "bcc_rule";
	private static final String FILE_NAME = "BibTeX Consistency Rule";
	private static final String FILE_CREATION_PAGE_NAME = "New BibTeX Consistency Rule";
	private static final String FILE_CREATION_PAGE_TITLE = "BibTeX Consistency Rule";
	private static final String FILE_CREATION_PAGE_DESCRIPTION = "Create a new BibTeX Consistency Rule.";
	
	private static final String REQUIRED_FOLDER = "rules";
	
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
	protected EObject getStoredModel(IContainer parentContainer, String name) {
		return BCCDefaultConsistencyRule.generate(name);
	}

	@Override
	protected String getIconPath() {
		return ICON_PATH;
	}

	@Override
	protected String getFileName() {
		return FILE_NAME;
	}

	@Override
	protected Injector getInjector() {
		return ConsistencyruleActivator.getInstance().getInjector(ConsistencyruleActivator.DE_DAVID_WILLE_BIBTEXCONSISTENCYCHECKER_CONSISTENCYRULE_BCCCONSISTENCYRULE);
	}

	@Override
	protected String getRequiredFolder() {
		return REQUIRED_FOLDER;
	}

}
