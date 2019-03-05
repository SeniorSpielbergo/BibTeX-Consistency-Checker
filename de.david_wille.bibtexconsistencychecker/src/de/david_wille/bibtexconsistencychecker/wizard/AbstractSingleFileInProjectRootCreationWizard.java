package de.david_wille.bibtexconsistencychecker.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;

import de.david_wille.bibtexconsistencychecker.wizard.pages.AbstractWizardNewFileCreationPage;
import de.david_wille.bibtexconsistencychecker.wizard.pages.BCCWizardSingleFileInProjectRootCreationPage;

public abstract class AbstractSingleFileInProjectRootCreationWizard extends AbstractFileCreationWizard {
	
	@Override
	protected AbstractWizardNewFileCreationPage initializeNewFileCreationPage(IStructuredSelection selection) {
		return new BCCWizardSingleFileInProjectRootCreationPage(getFileName(), getFileCreationPageName(),
				getFileCreationPageTitle(), getFileCreationPageDescription(), selection);
	}

}
