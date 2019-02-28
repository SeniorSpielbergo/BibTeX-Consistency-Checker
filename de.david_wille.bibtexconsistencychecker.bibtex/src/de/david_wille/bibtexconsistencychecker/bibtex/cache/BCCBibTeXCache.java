package de.david_wille.bibtexconsistencychecker.bibtex.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplacePatternEntry;

public class BCCBibTeXCache {

	private static volatile BCCBibTeXCache instance;
	private Map<String, BCCBibTeXProjectCache> cachedProjects;

	private BCCBibTeXCache() {
		return;
	}

	public static BCCBibTeXCache getInstance() {
		if (instance == null) {
			synchronized (BCCBibTeXCache.class) {
				if (instance == null) {
					instance = new BCCBibTeXCache();
				}
			}
		}

		return instance;
	}

	public void cacheBibTeXFile(IProject project, BCCBibTeXFile bibTeXFile) {
		String projectIdentifier = getProjectIdentifier(project);
		if (!cachedProjects.containsKey(projectIdentifier)) {
			initializeProjectCache(project, bibTeXFile);
		}
		else {
			updateProjectCache(project, bibTeXFile);
		}
	}

	private void initializeProjectCache(IProject project, BCCBibTeXFile bibTeXFile) {
		String projectIdentifier = getProjectIdentifier(project);
		BCCBibTeXProjectCache projectCache = new BCCBibTeXProjectCache(project, bibTeXFile);
		cachedProjects.put(projectIdentifier, projectCache);
	}

	private void updateProjectCache(IProject project, BCCBibTeXFile bibTeXFile) {
		String projectIdentifier = getProjectIdentifier(project);
		BCCBibTeXProjectCache projectCache = cachedProjects.get(projectIdentifier);
		projectCache.updateCache(bibTeXFile);
	}

	public List<BCCAbstractBibTeXEntry> getEntries(IProject project, String entryKey) {
		String projectIdentifier = getProjectIdentifier(project);
		BCCBibTeXProjectCache projectCache = cachedProjects.get(projectIdentifier);
		
		List<BCCAbstractBibTeXEntry> foundEntries = projectCache.getEntries(entryKey);
		
		return foundEntries;
	}

	public List<BCCAbstractBibTeXEntry> getEntries(IProject project) {
		String projectIdentifier = getProjectIdentifier(project);
		BCCBibTeXProjectCache projectCache = cachedProjects.get(projectIdentifier);
		
		List<BCCAbstractBibTeXEntry> foundEntries = projectCache.getEntries();
		
		return foundEntries;
	}

	public List<BCCReplacePatternEntry> getReplacePattern(IProject project, String patternKey) {
		String projectIdentifier = getProjectIdentifier(project);
		BCCBibTeXProjectCache projectCache = cachedProjects.get(projectIdentifier);
		
		List<BCCReplacePatternEntry> foundReplacePattern = projectCache.getReplacePattern(patternKey);
		
		return foundReplacePattern;
	}

	public List<BCCReplacePatternEntry> getReplacePattern(IProject project) {
		String projectIdentifier = getProjectIdentifier(project);
		BCCBibTeXProjectCache projectCache = cachedProjects.get(projectIdentifier);
		
		List<BCCReplacePatternEntry> foundReplacePattern = projectCache.getReplacePattern();
		
		return foundReplacePattern;
	}

	private String getProjectIdentifier(IProject project) {
		return project.getFullPath().toPortableString();
	}

	public void clearCache() {
		cachedProjects = new HashMap<>();
	}

}
