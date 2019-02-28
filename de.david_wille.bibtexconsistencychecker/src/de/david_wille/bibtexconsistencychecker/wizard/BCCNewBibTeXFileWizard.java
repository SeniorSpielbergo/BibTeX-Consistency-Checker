package de.david_wille.bibtexconsistencychecker.wizard;

import org.eclipse.core.resources.IContainer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ISetup;

import com.google.inject.Injector;

import de.david_wille.bibtexconsistencychecker.bibtex.BCCBibTeXStandaloneSetup;
import de.david_wille.bibtexconsistencychecker.bibtex.ui.internal.BibtexActivator;
import de.david_wille.bibtexconsistencychecker.bibtex.util.BCCDefaultBibTeXFile;

public class BCCNewBibTeXFileWizard extends BCCAbstractFileInSpecificFolderCreationWizard {

	private static final String FILE_EXTENSION = "bib";
	private static final String FILE_NAME = "BibTeX";
	private static final String FILE_CREATION_PAGE_NAME = "New BibTeX File";
	private static final String FILE_CREATION_PAGE_TITLE = "BibTeX File";
	private static final String FILE_CREATION_PAGE_DESCRIPTION = "Create a new BibTeX file.";
	
	private static final String REQUIRED_FOLDER = "bibliography";

	private static final String ICON_PATH = "icons/bib_icon96.png";
	private static final String WINDOW_TITLE = "New BibTeX File";

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
		return new BCCBibTeXStandaloneSetup();
	}

	@Override
	protected EObject getStoredModel(IContainer parentContainer, String name) {
		return BCCDefaultBibTeXFile.generate();
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
		return BibtexActivator.getInstance().getInjector(BibtexActivator.DE_DAVID_WILLE_BIBTEXCONSISTENCYCHECKER_BIBTEX_BCCBIBTEX);
	}

	@Override
	protected String getRequiredFolder() {
		return REQUIRED_FOLDER;
	}

}
