package de.david_wille.bibtexconsistencychecker.bibtex.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.bibtex.cache.BCCBibTeXCache;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;

public class BCCBibTeXFileResourceChangeListener implements IResourceChangeListener {

	private BCCBibTeXCache cache;
	
	public BCCBibTeXFileResourceChangeListener() {
		cache = BCCBibTeXCache.getInstance();
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		IResourceDelta currentResourceDelta = event.getDelta();
		
		if (currentResourceDelta != null) {
			List<IResourceDelta> resourceDeltas = identifyChangedResource(currentResourceDelta);
			
			for (IResourceDelta resourceDelta : resourceDeltas) {
				IResource resource = resourceDelta.getResource();
				
				if (resourceDelta.getKind() == IResourceDelta.ADDED) {
					handleFileWasAdded(resource);
				}
				else if (resourceDelta.getKind() == IResourceDelta.REMOVED) {
					handleResourceWasRemoved(resource);
				}
			}
		}
	}

	private void handleFileWasAdded(IResource resource) {
		if (resource instanceof IFile) {
			IFile file = (IFile) resource;
			
			if (BCCResourceUtil.fileIsBibTeXFile(file)) {
				BCCBibTeXFile bibTeXFile = BCCResourceUtil.parseModel(file);
				cache.cacheBibTeXFile(resource.getProject(), bibTeXFile);
			}
		}
	}

	private void handleResourceWasRemoved(IResource resource) {
		if (resource instanceof IFile) {
			IFile file = (IFile) resource;
			handleFileWasRemoved(file);
		}
		else if (resource instanceof IProject) {
			IProject project = (IProject) resource;
			handleProjectWasRemoved(project);
		}
	}

	private void handleFileWasRemoved(IFile file) {
		if (BCCResourceUtil.fileIsBibTeXFile(file)) {
			IProject project = file.getProject();
			try {
				if (project.exists() && project.hasNature(BCCBibTeXStartup.NATURE_ID)) {
					cache.removeBibTeXFile(project, file);
				}
			}
			catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleProjectWasRemoved(IProject project) {
		try {
			if (project.hasNature(BCCBibTeXStartup.NATURE_ID)) {
				cache.removeProject(project);
			}
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private List<IResourceDelta> identifyChangedResource(IResourceDelta resourceDelta) {
		List<IResourceDelta> allResourceDeltas = new ArrayList<>();
		
		if (resourceDelta.getAffectedChildren().length != 0) {
			for (IResourceDelta childResourceDelta : resourceDelta.getAffectedChildren()) {
				List<IResourceDelta> childResourceDeltas = identifyChangedResource(childResourceDelta);
				allResourceDeltas.addAll(childResourceDeltas);
			}
		}
		else {
			allResourceDeltas.add(resourceDelta);
		}
			
		return allResourceDeltas;
	}
	
	

}
