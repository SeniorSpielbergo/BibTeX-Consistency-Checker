package de.david_wille.bibtexconsistencychecker.wizard.pages;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;

public class BCCWizardFileInSpecificFolderCreationPage extends AbstractWizardNewFileCreationPage {
	
	private String fileName;
	private IPath currentContainerFullPath;
	private String requiredFolder;

	public BCCWizardFileInSpecificFolderCreationPage(String requiredFolder, String fileName, String pageName, String pageTitle,
			String pageDescription, IStructuredSelection selection)
	{
		super(fileName, pageName, pageTitle, pageDescription, selection);
		this.fileName = fileName;
		
		this.requiredFolder = requiredFolder;
	}
	
	@Override
	public boolean isPageComplete() {
		currentContainerFullPath = getContainerFullPath();
		
		if (currentContainerFullPath != null) {
			IContainer container = (IContainer) ResourcesPlugin.getWorkspace().getRoot().findMember(currentContainerFullPath);
			IProject project = container.getProject();
			
			if (!requiredFolderSelected(project, container)) {
				setErrorMessage(fileName + " files have to be stored in the \"" + requiredFolder + "\" folder.");
				return false;
			}
			
			setErrorMessage(null);
		}
		
		return super.isPageComplete();
	}

	private boolean requiredFolderSelected(IProject project, IContainer container) {
		String expectedFolderPath = project.getFullPath().toPortableString() + "/" + requiredFolder;
		String selectedFolderPath = container.getFullPath().toPortableString();
		return selectedFolderPath.equals(expectedFolderPath);
	}

}
