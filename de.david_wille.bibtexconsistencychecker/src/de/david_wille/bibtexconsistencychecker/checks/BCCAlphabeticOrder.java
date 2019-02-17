package de.david_wille.bibtexconsistencychecker.checks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.resources.IResource;

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXPackage;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEntryBody;
import de.david_wille.bibtexconsistencychecker.bibtex.util.BCCBibTeXUtil;
import de.david_wille.bibtexconsistencychecker.util.BCCMarkerHandling;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;

public class BCCAlphabeticOrder {
	
	public static void checkAlphabeticOrder(List<BCCBibTeXFile> parsedBibTeXFiles) {
		for (BCCBibTeXFile bibTeXFile : parsedBibTeXFiles) {
			checkAlphabeticOrder(bibTeXFile);
		}
	}

	private static void checkAlphabeticOrder(BCCBibTeXFile bibTeXFile) {
		List<BCCAbstractBibTeXEntry> bibTeXEntries = BCCBibTeXUtil.collectAbstractBibTeXEntries(bibTeXFile);
		List<String> entryKeys = collectAllEntryKeys(bibTeXEntries);
		
		Collections.sort(entryKeys, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
		});
		
		for (int i = 0; i < entryKeys.size(); i++) {
			BCCEntryBody entryBody = bibTeXEntries.get(i).getEntryBody();
			String expectedEntryKey = entryKeys.get(i);
			String foundEntryKey = entryBody.getEntryKey();
			
			if (!entryKeysAreEqual(expectedEntryKey, foundEntryKey)) {
				String errorMessage = "The entries are not in alphabetical order. Found \"" + foundEntryKey + "\" instead of \"" + expectedEntryKey + "\".";
				IResource resource = BCCResourceUtil.getIFile(bibTeXFile);
				
				BCCMarkerHandling factory = new BCCMarkerHandling();
				factory.createErrorMarker(resource, errorMessage, entryBody, BCCBibTeXPackage.Literals.BCC_ENTRY_BODY__ENTRY_KEY);
				
				return;
			}
		}
	}

	private static boolean entryKeysAreEqual(String expectedEntryKey, String foundEntryKey) {
		return foundEntryKey.equals(expectedEntryKey);
	}
	
	private static List<String> collectAllEntryKeys(List<BCCAbstractBibTeXEntry> bibTeXEntries) {
		List<String> entryKeys = new ArrayList<>();
		
		for (BCCAbstractBibTeXEntry bibTeXEntry : bibTeXEntries) {
			entryKeys.add(bibTeXEntry.getEntryBody().getEntryKey());
		}
		
		return entryKeys;
	}

}
