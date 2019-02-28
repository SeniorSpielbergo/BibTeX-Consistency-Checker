package de.david_wille.bibtexconsistencychecker.bibtex.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IStartup;

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.bibtex.cache.BCCBibTeXCache;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;

public class BCCBibTeXStartup implements IStartup {
	
	public static final String NATURE_ID = "de.david_wille.bibtexconsistencychecker.bccnature";
	public static final String BIBLIOGRAPHY_FOLDER = "bibliography";

	@Override
	public void earlyStartup() {
		BCCBibTeXCache.getInstance().clearCache();
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		try {
			for (IResource resource : workspace.getRoot().members()) {
				if (resource instanceof IProject) {
					IProject project = (IProject) resource;
					
					if (project.hasNature(NATURE_ID)) {
						identifyAndCacheBibliographyFolder(project);
					}
				}
			}
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void identifyAndCacheBibliographyFolder(IProject project) throws CoreException
	{
		for (IResource resource : project.members()) {
			if (resource instanceof IFolder) {
				IFolder folder = (IFolder) resource;
				
				if (folder.getName().equals(BIBLIOGRAPHY_FOLDER)) {
					identifyAndCacheBibliographyFiles(project, folder);
				}
			}
		}
	}

	private void identifyAndCacheBibliographyFiles(IProject project, IFolder folder) throws CoreException
	{
		for (IResource resource : folder.members()) {
			if (resource instanceof IFile) {
				IFile file = (IFile) resource;
				
				if (BCCResourceUtil.fileIsBibTeXFile(file)) {
					BCCBibTeXFile bibTeXFile = BCCResourceUtil.parseModel(file);
					BCCBibTeXCache.getInstance().cacheBibTeXFile(project, bibTeXFile);
				}
			}
		}
	}

}
