package de.david_wille.bibtexconsistencychecker.wizard.pages;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

public class BCCFolderSelectionPage extends WizardPage {

	protected BCCFolderSelectionPage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		StringButtonFieldEditor fileFieldEditor = new DirectoryFieldEditor("Test", "test", parent);
		fileFieldEditor.getStringValue();
	}

}
