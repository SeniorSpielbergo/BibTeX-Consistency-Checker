package de.david_wille.bibtexconsistencychecker.bibtex.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXFileEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEntryKeyObject;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplaceKeyObject;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplacePatternEntry;

public class BCCBibTeXCache {

	private static volatile BCCBibTeXCache instance;
	private Map<BCCBibTeXFile, Map<String, BCCAbstractBibTeXEntry>> entryKeyToEntryMap;
	private Map<BCCBibTeXFile, Map<String, BCCReplacePatternEntry>> replaceKeyToReplacePatternMap;
	private Map<IProject, List<BCCBibTeXFile>> cachedFiles;

	private BCCBibTeXCache() {
		entryKeyToEntryMap = new HashMap<>();
		replaceKeyToReplacePatternMap = new HashMap<>();
		cachedFiles = new HashMap<>();
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
		saveAddBibTeXFileToCachedFiles(project, bibTeXFile);
		
		Map<String, BCCAbstractBibTeXEntry> cachedBibTeXEntries = new HashMap<>();
		Map<String, BCCReplacePatternEntry> cachedReplacePattern = new HashMap<>();
		
		for (BCCAbstractBibTeXFileEntry fileEntry : bibTeXFile.getEntries()) {
			if (fileEntry instanceof BCCAbstractBibTeXEntry) {
				BCCAbstractBibTeXEntry bibTeXEntry = (BCCAbstractBibTeXEntry) fileEntry;
				BCCEntryKeyObject entryKeyObject = bibTeXEntry.getEntryBody().getEntryKeyObject();
				cachedBibTeXEntries.put(entryKeyObject.getEntryKey(), bibTeXEntry);
			}
			else if (fileEntry instanceof BCCReplacePatternEntry) {
				BCCReplacePatternEntry replacePatternEntry = (BCCReplacePatternEntry) fileEntry;
				BCCReplaceKeyObject replaceKeyObject = replacePatternEntry.getReplaceKeyObject();
				cachedReplacePattern.put(replaceKeyObject.getReplaceKey(), replacePatternEntry);
			}
		}
		
		entryKeyToEntryMap.put(bibTeXFile, cachedBibTeXEntries);
		replaceKeyToReplacePatternMap.put(bibTeXFile, cachedReplacePattern);
	}

	private void saveAddBibTeXFileToCachedFiles(IProject project, BCCBibTeXFile bibTeXFile) {
		List<BCCBibTeXFile> cachedFilesSet = null;
		if (cachedFiles.containsKey(project)) {
			cachedFilesSet = cachedFiles.get(project);
		}
		else {
			cachedFilesSet = new ArrayList<>();
		}
		cachedFilesSet.add(bibTeXFile);
		cachedFiles.put(project, cachedFilesSet);
	}

	public BCCAbstractBibTeXEntry getEntry(IProject project, String entryKey) {
		if (cachedFiles.containsKey(project)) {
			List<BCCBibTeXFile> cachedProjectFiles = cachedFiles.get(project);
			for (BCCBibTeXFile cachedProjectFile : cachedProjectFiles) {
				Map<String, BCCAbstractBibTeXEntry> entryKeyToEntryMap = this.entryKeyToEntryMap.get(cachedProjectFile);
				if (entryKeyToEntryMap.containsKey(entryKey)) {
					return entryKeyToEntryMap.get(entryKey);
				}
			}
		}
		return null;
	}

	public BCCReplacePatternEntry getReplacePattern(IProject project, String patternKey) {
		if (cachedFiles.containsKey(project)) {
			List<BCCBibTeXFile> cachedProjectFiles = cachedFiles.get(project);
			for (BCCBibTeXFile cachedProjectFile : cachedProjectFiles) {
				Map<String, BCCReplacePatternEntry> replaceKeyToReplacePatternMap = this.replaceKeyToReplacePatternMap.get(cachedProjectFile);
				if (replaceKeyToReplacePatternMap.containsKey(patternKey)) {
					return replaceKeyToReplacePatternMap.get(patternKey);
				}
			}
		}
		return null;
	}

	public void clearCache() {
		entryKeyToEntryMap = new HashMap<>();
		replaceKeyToReplacePatternMap = new HashMap<>();
		cachedFiles = new HashMap<>();
	}

}
