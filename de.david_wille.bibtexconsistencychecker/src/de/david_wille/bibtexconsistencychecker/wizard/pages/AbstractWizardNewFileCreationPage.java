package de.david_wille.bibtexconsistencychecker.wizard.pages;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public abstract class AbstractWizardNewFileCreationPage extends WizardNewFileCreationPage {
	
	public AbstractWizardNewFileCreationPage(String fileName, String pageName, String pageTitle,
			String pageDescription, IStructuredSelection selection)
	{
		super(pageName, selection);
		setTitle(pageTitle);
		setDescription(pageDescription);
	}

	@Override
	protected void createAdvancedControls(Composite parent) {
		return;
	}
	
	@Override
	protected IStatus validateLinkedResource() {
		return Status.OK_STATUS;
	}
	
	@Override
	protected void createLinkTarget() {
		return;
	}

}
