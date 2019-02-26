package de.david_wille.bibtexconsistencychecker.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;

public class BCCProjectNature implements IProjectNature {

	public static final String NATURE_ID = "de.david_wille.bibtexconsistencychecker.bccnature";
	public static final String RULES_FOLDER = "rules";
	public static final String BIBLIOGRAPHY_FOLDER = "bibliography";
	private IProject project;

	@Override
	public void configure() throws CoreException {
		if (!folderExists(RULES_FOLDER)) {
			BCCResourceUtil.createFolder(project, RULES_FOLDER);
		}
		
		if (!folderExists(BIBLIOGRAPHY_FOLDER)) {
			BCCResourceUtil.createFolder(project, BIBLIOGRAPHY_FOLDER);
		}
	}

	private boolean folderExists(String folderName) {
		try {
			for (IResource resource : project.members()) {
				if (resource.getName().equals(folderName)) {
					return true;
				}
			}
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public void deconfigure() throws CoreException {
		return;
	}

	@Override
	public IProject getProject() {
		return project;
	}

	@Override
	public void setProject(IProject project) {
		this.project = project;
	}

}
