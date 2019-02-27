package de.david_wille.bibtexconsistencychecker.wizard;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ISetup;

import de.david_wille.bibtexconsistencychecker.executionmodel.BCCExecutionModelStandaloneSetup;
import de.david_wille.bibtexconsistencychecker.executionmodel.util.BCCDefaultExecutionModel;

public class BCCNewExecutionFileWizard extends AbstractFileCreationWizard {
	
	private static final String FILE_EXTENSION = "bcc";
	private static final String FILE_CREATION_PAGE_NAME = "New BibTeX Consistency Checker Execution File";
	private static final String FILE_CREATION_PAGE_TITLE = "BibTeX Consistency Checker Execution File";
	private static final String FILE_CREATION_PAGE_DESCRIPTION = "Create a new BibTeX Consistency Checker Execution file.";

	private static final String ICON_PATH = "icons/bcc_icon96.png";
	private static final String WINDOW_TITLE = "New BibTeX Consistency Checker Execution File";

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
		return new BCCExecutionModelStandaloneSetup();
	}

	@Override
	protected EObject getStoredModel(String name) {
		return BCCDefaultExecutionModel.generate(name);
	}

	@Override
	protected String getIconPath() {
		return ICON_PATH;
	}

}
