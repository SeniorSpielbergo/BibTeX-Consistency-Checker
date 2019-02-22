package de.david_wille.bibtexconsistencychecker.analysis.preprocessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IResource;

import de.david_wille.bibtexconsistencychecker.analysis.BCCAnalysis;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXFileEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXPackage;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEntryBody;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;

public class BCCNoConflictingEntryKeysExist {

	public static boolean checkNoConflictingEntryKeysExist(List<BCCBibTeXFile> parsedBibTeXFiles) {
		boolean noErrorsDetected = true;
		
		Map<String, List<BCCAbstractBibTeXEntry>> allEntries = identifyAllEntries(parsedBibTeXFiles);
		
		for (Entry<String, List<BCCAbstractBibTeXEntry>> bibTeXentry : allEntries.entrySet()) {
			List<BCCAbstractBibTeXEntry> entries = bibTeXentry.getValue();
			
			if (entries.size() > 1) {
				for (BCCAbstractBibTeXEntry entry : entries) {
					String errorMessage = "";
					
					if (entriesAreInSameFile(entries)) {
						errorMessage = "Another entry with the same key exists.";
					}
					else {
						errorMessage = "Another entry with the same key exists in: " + generateFilesString(entry, entries);
					}
					
					IResource resource = BCCResourceUtil.getIFile(entry);
					
					BCCAnalysis.createConsistencyProblemErrorMarker(resource, errorMessage, entry.getEntryBody(), BCCBibTeXPackage.Literals.BCC_ENTRY_BODY__ENTRY_KEY);
					
					noErrorsDetected = noErrorsDetected && false;
				}
			}
		}
		
		return noErrorsDetected;
	}

	private static String generateFilesString(BCCAbstractBibTeXEntry entry, List<BCCAbstractBibTeXEntry> entries) {
		IResource entryResource = BCCResourceUtil.getIFile(entry);
		List<IResource> allResources = identifySortedListOfAllRelatedResources(entries);
		allResources.remove(entryResource);
		
		String stringRepresentation = "";
		
		int i = 0;
		for (IResource resource : allResources) {
			if (i > 0 && i < allResources.size()-1) {
				stringRepresentation += ", ";
			}
			else if (i > 0 && i == allResources.size()-1) {
				stringRepresentation += " and ";
			}
			
			stringRepresentation += resource.getName();
			
			i++;
		}
		
		return stringRepresentation;
	}

	private static List<IResource> identifySortedListOfAllRelatedResources(List<BCCAbstractBibTeXEntry> entries) {
		List<IResource> allResources = new ArrayList<>();
		
		for (BCCAbstractBibTeXEntry currentEntry : entries) {
			IResource resource = BCCResourceUtil.getIFile(currentEntry);
			if (!allResources.contains(resource)) {
				allResources.add(resource);
			}
		}
		
		Collections.sort(allResources, new Comparator<IResource>() {
			@Override
			public int compare(IResource arg0, IResource arg1) {
				return arg0.getName().compareTo(arg1.getName());
			}
		});
		
		return allResources;
	}

	private static boolean entriesAreInSameFile(List<BCCAbstractBibTeXEntry> entries) {
		BCCAbstractBibTeXEntry referenceEntry = entries.get(0);
		IResource referenceResource = BCCResourceUtil.getIFile(referenceEntry);
		
		for (BCCAbstractBibTeXEntry entry : entries) {
			IResource resource = BCCResourceUtil.getIFile(entry);
			
			if (!resource.equals(referenceResource)) {
				return false;
			}
		}
		
		return true;
	}

	private static Map<String, List<BCCAbstractBibTeXEntry>> identifyAllEntries(List<BCCBibTeXFile> parsedBibTeXFiles) {
		Map<String, List<BCCAbstractBibTeXEntry>> allEntries = new HashMap<>();
		
		for (BCCBibTeXFile parsedBibTeXFile : parsedBibTeXFiles) {
			for (BCCAbstractBibTeXFileEntry fileEntry : parsedBibTeXFile.getEntries()) {
				if (fileEntry instanceof BCCAbstractBibTeXEntry) {
					BCCAbstractBibTeXEntry bibTeXEntry = (BCCAbstractBibTeXEntry) fileEntry;
					BCCEntryBody entryBody = bibTeXEntry.getEntryBody();
					
					List<BCCAbstractBibTeXEntry> entries = new ArrayList<>();
					if (allEntries.containsKey(entryBody.getEntryKey())) {
						entries = allEntries.get(entryBody.getEntryKey());
					}
					entries.add(bibTeXEntry);
					
					allEntries.put(entryBody.getEntryKey(), entries);
				}
			}
		}
		
		return allEntries;
	}

}
