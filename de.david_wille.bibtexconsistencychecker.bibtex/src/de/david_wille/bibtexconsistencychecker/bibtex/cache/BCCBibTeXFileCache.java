package de.david_wille.bibtexconsistencychecker.bibtex.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXFileEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEntryKeyObject;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplaceKeyObject;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplacePatternEntry;

public class BCCBibTeXFileCache {
	
	private BCCBibTeXFile cachedBibTeXFile;
	private Map<String, List<BCCEntryKeyObject>> stringToEntryKeyObjectMap;
	private Map<BCCEntryKeyObject, BCCAbstractBibTeXEntry> entryKeyObjectToEntryMap;
	private Map<String, List<BCCReplaceKeyObject>> stringToReplaceKeyObjectMap;
	private Map<BCCReplaceKeyObject, BCCReplacePatternEntry> replaceKeyObjectToEntryMap;

	public BCCBibTeXFileCache(BCCBibTeXFile bibTeXFile) {
		updateCache(bibTeXFile);
	}

	private void cacheBibTeXFile(BCCBibTeXFile bibTeXFile) {
		cachedBibTeXFile = bibTeXFile;
		
		for (BCCAbstractBibTeXFileEntry fileEntry : bibTeXFile.getEntries()) {
			if (fileEntry instanceof BCCAbstractBibTeXEntry) {
				cacheBibTeXFileEntry(fileEntry);
			}
			else if (fileEntry instanceof BCCReplacePatternEntry) {
				cacheReplacePatternEntry(fileEntry);
			}
		}
	}

	private void cacheBibTeXFileEntry(BCCAbstractBibTeXFileEntry fileEntry) {
		BCCAbstractBibTeXEntry bibTeXEntry = (BCCAbstractBibTeXEntry) fileEntry;
		BCCEntryKeyObject entryKeyObject = bibTeXEntry.getEntryBody().getEntryKeyObject();
		
		entryKeyObjectToEntryMap.put(entryKeyObject, bibTeXEntry);
		
		saveCacheEntryKey(entryKeyObject);
	}

	private void cacheReplacePatternEntry(BCCAbstractBibTeXFileEntry fileEntry) {
		BCCReplacePatternEntry replacePatternEntry = (BCCReplacePatternEntry) fileEntry;
		BCCReplaceKeyObject replaceKeyObject = replacePatternEntry.getReplaceKeyObject();
		
		replaceKeyObjectToEntryMap.put(replaceKeyObject, replacePatternEntry);
		
		saveCacheEntryKey(replaceKeyObject);
	}

	private void saveCacheEntryKey(BCCEntryKeyObject entryKeyObject) {
		String entryKey = entryKeyObject.getEntryKey();
		List<BCCEntryKeyObject> entryKeyObjects = null;
		
		if (stringToEntryKeyObjectMap.containsKey(entryKey)) {
			entryKeyObjects = stringToEntryKeyObjectMap.get(entryKey);
		}
		else {
			entryKeyObjects = new ArrayList<>();
		}
		
		entryKeyObjects.add(entryKeyObject);
		stringToEntryKeyObjectMap.put(entryKey, entryKeyObjects);
	}

	private void saveCacheEntryKey(BCCReplaceKeyObject replaceKeyObject) {
		String replaceKey = replaceKeyObject.getReplaceKey();
		List<BCCReplaceKeyObject> replaceKeyObjects = null;
		
		if (stringToReplaceKeyObjectMap.containsKey(replaceKey)) {
			replaceKeyObjects = stringToReplaceKeyObjectMap.get(replaceKey);
		}
		else {
			replaceKeyObjects = new ArrayList<>();
		}
		
		replaceKeyObjects.add(replaceKeyObject);
		stringToReplaceKeyObjectMap.put(replaceKey, replaceKeyObjects);
	}

	public void updateCache(BCCBibTeXFile bibTeXFile) {
		clearCache();
		
		cacheBibTeXFile(bibTeXFile);
	}

	private void clearCache() {
		stringToEntryKeyObjectMap = new HashMap<>();
		entryKeyObjectToEntryMap = new HashMap<>();
		replaceKeyObjectToEntryMap = new HashMap<>();
		stringToReplaceKeyObjectMap = new HashMap<>();
	}

	public List<BCCAbstractBibTeXEntry> getEntries(String entryKey) {
		List<BCCAbstractBibTeXEntry> matchingEntries = new ArrayList<>();
		
		if (stringToEntryKeyObjectMap.containsKey(entryKey)) {
			List<BCCEntryKeyObject> matchingEntryKeyObjects = stringToEntryKeyObjectMap.get(entryKey);
			
			for (BCCEntryKeyObject entryKeyObject : matchingEntryKeyObjects) {
				matchingEntries.add(entryKeyObjectToEntryMap.get(entryKeyObject));
			}
		}
		
		return matchingEntries;
	}

	public List<BCCAbstractBibTeXEntry> getEntries() {
		List<BCCAbstractBibTeXEntry> matchingEntries = new ArrayList<>();
		
		for (String entryKey : stringToEntryKeyObjectMap.keySet()) {
			List<BCCEntryKeyObject> matchingEntryKeyObjects = stringToEntryKeyObjectMap.get(entryKey);
			
			for (BCCEntryKeyObject entryKeyObject : matchingEntryKeyObjects) {
				matchingEntries.add(entryKeyObjectToEntryMap.get(entryKeyObject));
			}
		}
		
		return matchingEntries;
	}

	public List<BCCReplacePatternEntry> getReplacePattern(String patternKey) {
		List<BCCReplacePatternEntry> matchingEntries = new ArrayList<>();
		
		if (stringToReplaceKeyObjectMap.containsKey(patternKey)) {
			List<BCCReplaceKeyObject> matchingReplaceKeyObjects = stringToReplaceKeyObjectMap.get(patternKey);
			
			for (BCCReplaceKeyObject patternKeyObject : matchingReplaceKeyObjects) {
				matchingEntries.add(replaceKeyObjectToEntryMap.get(patternKeyObject));
			}
		}
		
		return matchingEntries;
	}

	public List<BCCReplacePatternEntry> getReplacePattern() {
		List<BCCReplacePatternEntry> matchingEntries = new ArrayList<>();
		
		for (String patternKey : stringToReplaceKeyObjectMap.keySet()) {
			List<BCCReplaceKeyObject> matchingReplaceKeyObjects = stringToReplaceKeyObjectMap.get(patternKey);
			
			for (BCCReplaceKeyObject replaceKeyObject : matchingReplaceKeyObjects) {
				matchingEntries.add(replaceKeyObjectToEntryMap.get(replaceKeyObject));
			}
		}
		
		return matchingEntries;
	}
	
	public BCCBibTeXFile getCachedBibTeXFile() {
		return cachedBibTeXFile;
	}

}
