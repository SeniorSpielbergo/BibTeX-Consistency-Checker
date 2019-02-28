package de.david_wille.bibtexconsistencychecker.wizard.pages;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;

import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;

public class BCCWizardSingleFileInProjectRootCreationPage extends AbstractWizardNewFileCreationPage {
	
	private String fileName;
	private IPath currentContainerFullPath;

	public BCCWizardSingleFileInProjectRootCreationPage(String fileName, String pageName, String pageTitle,
			String pageDescription, IStructuredSelection selection)
	{
		super(fileName, pageName, pageTitle, pageDescription, selection);
		this.fileName = fileName;
	}
	
	@Override
	public boolean isPageComplete() {
		currentContainerFullPath = getContainerFullPath();
		
		if (currentContainerFullPath != null) {
			IContainer container = (IContainer) ResourcesPlugin.getWorkspace().getRoot().findMember(currentContainerFullPath);
			IProject project = container.getProject();
			
			if (selectedContainerIsProjectRoot(container, project)) {
				List<IResource> foundResources = BCCResourceUtil.getChildResources(project);
				
				for (IResource foundResource : foundResources) {
					if (foundResource instanceof IFile) {
						IFile foundFile = (IFile) foundResource;
						
						if (foundFile.getFileExtension().equals(getFileExtension())) {
							setErrorMessage("There already exists a " + fileName + " file in the selected project root. Only one file of this type is allowed.");
							return false;
						}
					}
				}
			}
			else {
				setErrorMessage(fileName + " files can only be stored in the root of a project.");
				return false;
			}
			
			setErrorMessage(null);
		}
		
		return super.isPageComplete();
	}

	private boolean selectedContainerIsProjectRoot(IContainer container, IProject project) {
		return container.equals(project);
	}

}
