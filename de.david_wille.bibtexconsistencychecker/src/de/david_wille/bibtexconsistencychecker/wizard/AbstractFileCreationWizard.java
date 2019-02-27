package de.david_wille.bibtexconsistencychecker.wizard;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ISetup;

import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;
import de.david_wille.bibtexconsistencychecker.wizard.pages.BCCWizardNewFileCreationPage;

public abstract class AbstractFileCreationWizard extends Wizard implements INewWizard {
	
	private WizardNewFileCreationPage newFileCreationPage;
	
	protected abstract String getFileCreationPageName();
	
	protected abstract String getFileCreationPageTitle();
	
	protected abstract String getFileCreationPageDescription();
	
	protected abstract String getFileCreationWindowTitle();
	
	protected abstract String getFileExtension();
	
	protected abstract ISetup getFileCreationSetup();
	
	protected abstract EObject getStoredModel(String name);
	
	protected abstract String getIconPath();

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		ImageDescriptor image = AbstractUIPlugin.imageDescriptorFromPlugin("de.david_wille.bibtexconsistencychecker", getIconPath());
		setDefaultPageImageDescriptor(image);
		
		newFileCreationPage = new BCCWizardNewFileCreationPage(getFileCreationPageName(), getFileCreationPageTitle(), getFileCreationPageDescription(), selection);
		newFileCreationPage.setFileExtension(getFileExtension());
	}

	@Override
	public String getWindowTitle() {
		return getFileCreationWindowTitle();
	}

	@Override
	public void addPages() {
		addPage(newFileCreationPage);
	}


	@Override
	public boolean performFinish() {
		IContainer container = (IContainer) ResourcesPlugin.getWorkspace().getRoot().findMember(newFileCreationPage.getContainerFullPath());
		
		String fileName = newFileCreationPage.getFileName().replace("." + getFileExtension(), "");
		
		EObject eObject = getStoredModel(fileName);
		BCCResourceUtil.storeModel(getFileCreationSetup(), eObject, container, fileName, getFileExtension());
		
		return true;
	}

}
