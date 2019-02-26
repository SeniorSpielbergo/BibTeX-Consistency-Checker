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

	public void updateBibTeXFileCache(IProject project) {
//		cachedFiles.add(bibTeXFile);
//		
//		Map<String, BCCAbstractBibTeXEntry> entryKeyToEntryMap = new HashMap<>();
//		Map<String, BCCReplacePatternEntry> replaceKeyToReplacePatternMap = new HashMap<>();
//		
//		for (BCCAbstractBibTeXFileEntry fileEntry : bibTeXFile.getEntries()) {
//			if (fileEntry instanceof BCCAbstractBibTeXEntry) {
//				BCCAbstractBibTeXEntry bibTeXEntry = (BCCAbstractBibTeXEntry) fileEntry;
//				entryKeyToEntryMap.put(bibTeXEntry.getEntryBody().getEntryKey(), bibTeXEntry);
//			}
//			else if (fileEntry instanceof BCCReplacePatternEntry) {
//				BCCReplacePatternEntry replacePatternEntry = (BCCReplacePatternEntry) fileEntry;
//				replaceKeyToReplacePatternMap.put(replacePatternEntry.getReplaceKey(), replacePatternEntry);
//			}
//		}
//		
//		this.entryKeyToEntryMap.put(bibTeXFile, entryKeyToEntryMap);
//		this.replaceKeyToReplacePatternMap.put(bibTeXFile, replaceKeyToReplacePatternMap);
	}
	
	public void updateBibTeXFileCache(IProject project, BCCBibTeXFile bibTeXFile) {
		System.out.println(bibTeXFile);
	}

	public BCCAbstractBibTeXEntry getEntry(String entryKey) {
		for (Map<String, BCCAbstractBibTeXEntry> entryKeyToEntryMap : entryKeyToEntryMap.values()) {
			if (entryKeyToEntryMap.containsKey(entryKey)) {
				return entryKeyToEntryMap.get(entryKey);
			}
		}
		return null;
	}

	public BCCReplacePatternEntry getReplacePattern(String patternKey) {
		for (Map<String, BCCReplacePatternEntry> replaceKeyToReplacePatternMap : replaceKeyToReplacePatternMap.values()) {
			if (replaceKeyToReplacePatternMap.containsKey(patternKey)) {
				return replaceKeyToReplacePatternMap.get(patternKey);
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
