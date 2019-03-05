package de.david_wille.bibtexconsistencychecker.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;

import de.david_wille.bibtexconsistencychecker.wizard.pages.AbstractWizardNewFileCreationPage;
import de.david_wille.bibtexconsistencychecker.wizard.pages.BCCWizardFileInSpecificFolderCreationPage;

public abstract class AbstractFileInSpecificFolderCreationWizard extends AbstractFileCreationWizard {
	
	protected abstract String getRequiredFolder();

	@Override
	protected AbstractWizardNewFileCreationPage initializeNewFileCreationPage(IStructuredSelection selection) {
		return new BCCWizardFileInSpecificFolderCreationPage(getRequiredFolder(), getFileName(), getFileCreationPageName(),
				getFileCreationPageTitle(), getFileCreationPageDescription(), selection);
	}

}
