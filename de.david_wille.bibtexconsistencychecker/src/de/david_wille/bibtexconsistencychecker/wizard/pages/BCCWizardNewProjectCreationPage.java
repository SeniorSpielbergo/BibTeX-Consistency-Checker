package de.david_wille.bibtexconsistencychecker.wizard.pages;

import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class BCCWizardNewProjectCreationPage extends WizardNewProjectCreationPage {

	public BCCWizardNewProjectCreationPage(String pageName, String pageTitle, String pageDescription) {
		super(pageName);
		setTitle(pageTitle);
		setDescription(pageDescription);
	}

}
