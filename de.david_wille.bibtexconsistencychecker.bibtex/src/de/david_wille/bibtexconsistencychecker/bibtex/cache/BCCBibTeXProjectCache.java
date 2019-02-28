package de.david_wille.bibtexconsistencychecker.bibtex.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplacePatternEntry;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;

public class BCCBibTeXProjectCache {

	private IProject cachedProject;
	private Map<String, BCCBibTeXFileCache> cachedBibTeXFiles;
	
	public BCCBibTeXProjectCache(IProject project, BCCBibTeXFile bibTeXFile) {
		cachedProject = project;
		cachedBibTeXFiles = new HashMap<>();
		
		initializeCacheForBibTeXFile(bibTeXFile);
	}
	
	private void initializeCacheForBibTeXFile(BCCBibTeXFile bibTeXFile) {
		String bibTeXFileIdentifier = getBibTeXFileIdentifier(bibTeXFile);
		BCCBibTeXFileCache bibTeXFileCache = new BCCBibTeXFileCache(bibTeXFile);
		cachedBibTeXFiles.put(bibTeXFileIdentifier, bibTeXFileCache);
	}
	
	public IProject getCachedProject() {
		return cachedProject;
	}

	public void updateCache(BCCBibTeXFile bibTeXFile) {
		String bibTeXFileIdentifier = getBibTeXFileIdentifier(bibTeXFile);
		if (!cachedBibTeXFiles.containsKey(bibTeXFileIdentifier)) {
			initializeCacheForBibTeXFile(bibTeXFile);
		}
		else {
			updateCacheForBibTeXFile(bibTeXFile);
		}
	}

	private void updateCacheForBibTeXFile(BCCBibTeXFile bibTeXFile) {
		String bibTeXFileIdentifier = getBibTeXFileIdentifier(bibTeXFile);
		BCCBibTeXFileCache bibTeXFileCache = cachedBibTeXFiles.get(bibTeXFileIdentifier);
		bibTeXFileCache.updateCache(bibTeXFile);
	}


	public List<BCCAbstractBibTeXEntry> getEntries(String entryKey) {
		List<BCCAbstractBibTeXEntry> foundFileEntries = new ArrayList<>();
		
		for (Entry<String, BCCBibTeXFileCache> cacheEntry : cachedBibTeXFiles.entrySet()) {
			List<BCCAbstractBibTeXEntry> cachedFileEntries = cacheEntry.getValue().getEntries(entryKey);
			foundFileEntries.addAll(cachedFileEntries);
		}
		
		return foundFileEntries;
	}

	public List<BCCAbstractBibTeXEntry> getEntries() {
		List<BCCAbstractBibTeXEntry> foundFileEntries = new ArrayList<>();
		
		for (Entry<String, BCCBibTeXFileCache> cacheEntry : cachedBibTeXFiles.entrySet()) {
			List<BCCAbstractBibTeXEntry> cachedFileEntries = cacheEntry.getValue().getEntries();
			foundFileEntries.addAll(cachedFileEntries);
		}
		
		return foundFileEntries;
	}

	public List<BCCReplacePatternEntry> getReplacePattern(String patternKey) {
		List<BCCReplacePatternEntry> foundFileEntries = new ArrayList<>();
		
		for (Entry<String, BCCBibTeXFileCache> cacheEntry : cachedBibTeXFiles.entrySet()) {
			List<BCCReplacePatternEntry> cachedFileEntries = cacheEntry.getValue().getReplacePattern(patternKey);
			foundFileEntries.addAll(cachedFileEntries);
		}
		
		return foundFileEntries;
	}

	public List<BCCReplacePatternEntry> getReplacePattern() {
		List<BCCReplacePatternEntry> foundFileEntries = new ArrayList<>();
		
		for (Entry<String, BCCBibTeXFileCache> cacheEntry : cachedBibTeXFiles.entrySet()) {
			List<BCCReplacePatternEntry> cachedFileEntries = cacheEntry.getValue().getReplacePattern();
			foundFileEntries.addAll(cachedFileEntries);
		}
		
		return foundFileEntries;
	}

	private String getBibTeXFileIdentifier(BCCBibTeXFile bibTeXFile) {
		return BCCResourceUtil.getIFile(bibTeXFile).getFullPath().toPortableString();
	}
}
