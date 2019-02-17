package de.david_wille.bibtexconsistencychecker.checks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IResource;

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXFileEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAuthorField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXPackage;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEntryBody;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCPerson;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCTitleField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCYearField;
import de.david_wille.bibtexconsistencychecker.bibtex.util.BCCBibTeXUtil;
import de.david_wille.bibtexconsistencychecker.util.BCCMarkerHandling;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;
import de.david_wille.bibtexconsistencychecker.util.BCCSpecialCharacterHandling;

public class BCCShortHarvardStyle {
	
	private static final int SHORT_HARVARD_STYLE_MAX_SINGLE_AUTHOR_LENGTH = 3;
	private static final int SHORT_HARVARD_STYLE_MAX_NUMBER_AUTHORS = 3;
	private static final String PLUS = "+";
	private static final String VAN_DEN = "van den";
	private static final String VAN_DER = "van der";
	private static final String VAN = "van";
	private static final String DA = "da";

	public static void checkShortHarvardStyle(List<BCCBibTeXFile> parsedBibTeXFiles) {
		for (BCCBibTeXFile bibTeXFile : parsedBibTeXFiles) {
			checkShortHarvardStyle(bibTeXFile);
		}
	}

	private static void checkShortHarvardStyle(BCCBibTeXFile bibTeXFile) {
		List<BCCAbstractBibTeXFileEntry> bibTeXFileEntries = bibTeXFile.getEntries();
		Map<BCCAbstractBibTeXEntry, String> expectedBibTeXEntryShortHarvardStyles = generateExpectedShortHarvardStyles(bibTeXFileEntries);
		
		for (Entry<BCCAbstractBibTeXEntry, String> mapEntry : expectedBibTeXEntryShortHarvardStyles.entrySet()) {
			if (!entryKeyUsesCorrectShortHarvardStyle(mapEntry)) {
				String foundEntryKey = identifyEntryKey(mapEntry.getKey());
				String expectedEntryKey = mapEntry.getValue();
				BCCEntryBody entryBody = mapEntry.getKey().getEntryBody();
				
				String errorMessage = "The entry does not use the Short Harvard Style. Found \"" + foundEntryKey + "\" instead of \"" + expectedEntryKey + "\".";
				IResource resource = BCCResourceUtil.getIFile(bibTeXFile);
				
				BCCMarkerHandling factory = new BCCMarkerHandling();
				factory.createErrorMarker(resource, errorMessage, entryBody, BCCBibTeXPackage.Literals.BCC_ENTRY_BODY__ENTRY_KEY);
			}
		}
	}
	
	private static boolean entryKeyUsesCorrectShortHarvardStyle(Entry<BCCAbstractBibTeXEntry, String> mapEntry) {
		BCCAbstractBibTeXEntry currentEntry = mapEntry.getKey();
		
		String usedEntryKey = identifyEntryKey(currentEntry);
		String expectedEntryKey = mapEntry.getValue();
		
		if (usedEntryKey.equals(expectedEntryKey)) {
			return true;
		}
		
		return false;
	}

	private static Map<BCCAbstractBibTeXEntry, String> generateExpectedShortHarvardStyles(List<BCCAbstractBibTeXFileEntry> bibTeXFileEntries) {
		Map<String, List<BCCAbstractBibTeXEntry>> initialBibTeXEntryShortHarvardStyles = initializeExpectedBibTeXEntryShortHarvardStyles(bibTeXFileEntries);
		Map<BCCAbstractBibTeXEntry, String> expectedBibTeXEntryShortHarvardStyles = new HashMap<>();
		
		for (Entry<String, List<BCCAbstractBibTeXEntry>> mapEntry : initialBibTeXEntryShortHarvardStyles.entrySet()) {
			List<BCCAbstractBibTeXEntry> entries = initialBibTeXEntryShortHarvardStyles.get(mapEntry.getKey());
			
			if (entries.size() == 1) {
				expectedBibTeXEntryShortHarvardStyles.put(entries.get(0), mapEntry.getKey());
			}
			else {
				Collections.sort(entries, new Comparator<BCCAbstractBibTeXEntry>() {
					@Override
					public int compare(BCCAbstractBibTeXEntry arg0, BCCAbstractBibTeXEntry arg1) {
						if (bibTeXEntryContainsAuthorTitleAndYear(arg0) && bibTeXEntryContainsAuthorTitleAndYear(arg1)) {
							BCCTitleField titleField0 = BCCBibTeXUtil.identifyField(arg0, BCCTitleField.class);
							BCCTitleField titleField1 = BCCBibTeXUtil.identifyField(arg1, BCCTitleField.class);
							
							return titleField0.getFieldValue().compareToIgnoreCase(titleField1.getFieldValue());
						}
						
						return 0;
					}
				});
				
				for (int i = 0; i < entries.size(); i++) {
					String unambiguousEntryKey = mapEntry.getKey() + toAlphabetic(i);
					
					expectedBibTeXEntryShortHarvardStyles.put(entries.get(i), unambiguousEntryKey);
				}
			}
		}
		
		return expectedBibTeXEntryShortHarvardStyles;
	}
	
	private static String toAlphabetic(int i) {
	    if (i < 0) {
	        return "-" + toAlphabetic(-i-1);
	    }

	    int quotient = i/26;
	    int remainder = i%26;
	    
	    char letter = (char)((int)'a' + remainder);
	    
	    if (quotient == 0) {
	        return "" + letter;
	    }
	    else {
	        return toAlphabetic(quotient-1) + letter;
	    }
	}

	private static Map<String, List<BCCAbstractBibTeXEntry>> initializeExpectedBibTeXEntryShortHarvardStyles(List<BCCAbstractBibTeXFileEntry> bibTeXFileEntries) {
		Map<String, List<BCCAbstractBibTeXEntry>> expectedBibTeXEntryShortHarvardStyles = new HashMap<>();
		
		for (BCCAbstractBibTeXFileEntry abstractFileEntry : bibTeXFileEntries) {
			if (abstractFileEntry instanceof BCCAbstractBibTeXEntry) {
				BCCAbstractBibTeXEntry bibTeXEntry = (BCCAbstractBibTeXEntry) abstractFileEntry;
				
				if (bibTeXEntryContainsAuthorAndYear(bibTeXEntry)) {
					BCCAuthorField authorField = BCCBibTeXUtil.identifyField(bibTeXEntry, BCCAuthorField.class);
					BCCYearField yearField = BCCBibTeXUtil.identifyField(bibTeXEntry, BCCYearField.class);
					
					String expectedShortHarvardStyle = generateShortHarvardStyle(authorField, yearField);
					safeAddExpectedBibTeXEntryShortHarvardStyle(expectedBibTeXEntryShortHarvardStyles, expectedShortHarvardStyle, bibTeXEntry);
				}
			}
		}
		
		return expectedBibTeXEntryShortHarvardStyles;
	}

	private static void safeAddExpectedBibTeXEntryShortHarvardStyle(Map<String, List<BCCAbstractBibTeXEntry>> expectedBibTeXEntryShortHarvardStyles,
			String expectedShortHarvardStyle, BCCAbstractBibTeXEntry bibTeXEntry)
	{
		List<BCCAbstractBibTeXEntry> entries = new ArrayList<>();
		
		if (expectedBibTeXEntryShortHarvardStyles.containsKey(expectedShortHarvardStyle)) {
			entries = expectedBibTeXEntryShortHarvardStyles.get(expectedShortHarvardStyle);
		}
		
		entries.add(bibTeXEntry);
		
		expectedBibTeXEntryShortHarvardStyles.put(expectedShortHarvardStyle, entries);
	}

	private static boolean bibTeXEntryContainsAuthorAndYear(BCCAbstractBibTeXEntry bibTeXEntry) {
		return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCAuthorField.class) && BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCYearField.class);
	}

	private static String generateShortHarvardStyle(BCCAuthorField authorField, BCCYearField yearField) {
		String authorToken = generateAuthorToken(authorField);
		String yearToken = generateYearToken(yearField);
		String plusToken = generatePlusToken(authorField);
		
		return authorToken + plusToken + yearToken;
	}

	private static String generatePlusToken(BCCAuthorField authorField) {
		List<BCCPerson> persons = authorField.getPersonFieldBody().getPersons();
		
		if (persons.size() > SHORT_HARVARD_STYLE_MAX_NUMBER_AUTHORS) {
			return PLUS;
		}
		
		return "";
	}

	private static String generateAuthorToken(BCCAuthorField authorField) {
		List<BCCPerson> persons = authorField.getPersonFieldBody().getPersons();
		
		if (persons.size() == 1) {
			return generateAuthorTokenForSingleAuthor(persons.get(0));
		}
		else {
			return generateAuthorTokenForMultipleAuthors(persons);
		}
	}

	private static String generateAuthorTokenForMultipleAuthors(List<BCCPerson> persons) {
		String authorToken = "";
		
		for (int i = 0; i < Math.min(persons.size(), SHORT_HARVARD_STYLE_MAX_NUMBER_AUTHORS); i++) {
			BCCPerson person = persons.get(i);
			
			String lastName = retrieveAndPreProcessLastName(person);
			
			authorToken += lastName.substring(0, 1);
		}
		
		return authorToken;
	}

	private static String generateAuthorTokenForSingleAuthor(BCCPerson person) {
		String lastName = retrieveAndPreProcessLastName(person);
		int lastSubstringIndex = identifyLastSubstringIndex(lastName);
		
		return lastName.substring(0, lastSubstringIndex);
	}

	private static int identifyLastSubstringIndex(String lastName) {
		return Math.min(lastName.length(), SHORT_HARVARD_STYLE_MAX_SINGLE_AUTHOR_LENGTH);
	}

	private static String retrieveAndPreProcessLastName(BCCPerson person) {
		String lastName = BCCSpecialCharacterHandling.replaceSpecialLatexCharacters(person.getLastName());
		
		if (lastName.startsWith(VAN_DEN)) {
			lastName = lastName.replace(VAN_DEN, "").trim();
		}
		else if (lastName.startsWith(VAN_DER)) {
			lastName = lastName.replace(VAN_DER, "").trim();
		}
		else if (lastName.startsWith(VAN)) {
			lastName = lastName.replace(VAN, "").trim();
		}
		else if (lastName.startsWith(DA)) {
			lastName = lastName.replace(DA, "").trim();
		}
		return lastName;
	}

	private static String generateYearToken(BCCYearField yearField) {
		String yearString = "" + yearField.getYear();
		return yearString.substring(2, 4);
	}

	private static String identifyEntryKey(BCCAbstractBibTeXEntry bibTeXEntry) {
		return bibTeXEntry.getEntryBody().getEntryKey();
	}
	
	private static boolean bibTeXEntryContainsAuthorTitleAndYear(BCCAbstractBibTeXEntry bibTeXEntry) {
		return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCTitleField.class) && bibTeXEntryContainsAuthorAndYear(bibTeXEntry);
	}
	
}
