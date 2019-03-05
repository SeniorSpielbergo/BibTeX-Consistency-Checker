package de.david_wille.bibtexconsistencychecker.bibtex.util;

import java.util.ArrayList;
import java.util.List;

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXFileEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractEntryBodyField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractGenericField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCGenericFieldValueObject;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplacePatternEntry;
import de.david_wille.bibtexconsistencychecker.util.BCCSpecialCharacterHandling;

public class BCCBibTeXUtil {
	
	private static String OPENING_BRACE = "{";
	private static String CLOSING_BRACE = "}";

	@SuppressWarnings("unchecked")
	public static <T> T identifyField(BCCAbstractBibTeXEntry bibTeXEntry, Class<T> type) {
		for (BCCAbstractEntryBodyField bodyField : bibTeXEntry.getEntryBody().getFields()) {
			if (type.isInstance(bodyField)) {
				return (T) bodyField;
			}
		}
		return null;
	}
	
	public static int identifyFieldPosition(BCCAbstractBibTeXEntry bibTeXEntry, BCCAbstractEntryBodyField field) {
		int i = 0;
		for (BCCAbstractEntryBodyField bodyField : bibTeXEntry.getEntryBody().getFields()) {
			if (bodyField.equals(field)) {
				return i;
			}
			i++;
		}
		
		return -1;
	}

	public static <T> boolean bibTeXEntryContainsField(BCCAbstractBibTeXEntry bibTeXEntry, Class<T> type) {
		for (BCCAbstractEntryBodyField bodyField : bibTeXEntry.getEntryBody().getFields()) {
			if (type.isInstance(bodyField)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean usesReplacePattern(BCCAbstractGenericField genericField) {
		String fieldValue = genericField.getFieldValueObject().getFieldValue();
		BCCSpecialCharacterHandling.replaceSpecialLatexCharacters(fieldValue);
		
		return !fieldValue.startsWith(OPENING_BRACE) && !fieldValue.endsWith(CLOSING_BRACE);
	}
	
	public static boolean usesReplacePattern(BCCGenericFieldValueObject fieldValueObject) {
		String fieldValue = fieldValueObject.getFieldValue();
		BCCSpecialCharacterHandling.replaceSpecialLatexCharacters(fieldValue);
		
		return !fieldValue.startsWith(OPENING_BRACE) && !fieldValue.endsWith(CLOSING_BRACE);
	}
	
	public static boolean isInteger(BCCAbstractGenericField genericField) {
		String fieldValue = getFieldValueWithoutFieldBraces(genericField);

		try {
			Integer.parseInt(fieldValue);
		}
		catch(NumberFormatException nfe){
			return false;
		}
		
		return true;
	}

	public static List<BCCAbstractBibTeXEntry> collectAllAbstractBibTeXEntries(List<BCCBibTeXFile> parsedBibTeXFiles) {
		List<BCCAbstractBibTeXEntry> bibTeXEntries = new ArrayList<>();
		for (BCCBibTeXFile parsedBibTeXFile : parsedBibTeXFiles) {
			List<BCCAbstractBibTeXEntry> fileReplacePattern = collectAllAbstractBibTeXEntries(parsedBibTeXFile);
			bibTeXEntries.addAll(fileReplacePattern);
		}
		
		return bibTeXEntries;
	}

	public static List<BCCAbstractBibTeXEntry> collectAllAbstractBibTeXEntries(BCCBibTeXFile bibTeXFile) {
		List<BCCAbstractBibTeXEntry> bibTeXEntries = new ArrayList<>();
		
		for (BCCAbstractBibTeXFileEntry abstractFileEntry : bibTeXFile.getEntries()) {
			if (abstractFileEntry instanceof BCCAbstractBibTeXEntry) {
				BCCAbstractBibTeXEntry bibTeXEntry = (BCCAbstractBibTeXEntry) abstractFileEntry;
				bibTeXEntries.add(bibTeXEntry);
			}
		}
		
		return bibTeXEntries;
	}

	public static List<BCCReplacePatternEntry> collectAllReplacePattern(List<BCCBibTeXFile> parsedBibTeXFiles) {
		List<BCCReplacePatternEntry> allReplacePattern = new ArrayList<>();
		for (BCCBibTeXFile parsedBibTeXFile : parsedBibTeXFiles) {
			List<BCCReplacePatternEntry> fileReplacePattern = collectAllReplacePattern(parsedBibTeXFile);
			allReplacePattern.addAll(fileReplacePattern);
		}
		
		return allReplacePattern;
	}

	public static List<BCCReplacePatternEntry> collectAllReplacePattern(BCCBibTeXFile parsedBibTeXFile) {
		List<BCCReplacePatternEntry> allReplacePattern = new ArrayList<>();
		for (BCCAbstractBibTeXFileEntry fileEntry : parsedBibTeXFile.getEntries()) {
			if (fileEntry instanceof BCCReplacePatternEntry) {
				BCCReplacePatternEntry replacePatternEntry = (BCCReplacePatternEntry) fileEntry;
				allReplacePattern.add(replacePatternEntry);
			}
		}
		
		return allReplacePattern;
	}
	
	public static String getFieldValueWithoutFieldBraces(BCCAbstractGenericField genericField) {
		String fieldValue = genericField.getFieldValueObject().getFieldValue();
		
		while (fieldValue.startsWith(OPENING_BRACE) && fieldValue.endsWith(CLOSING_BRACE)) {
			fieldValue = fieldValue.substring(1, fieldValue.length());
			
			fieldValue = fieldValue.substring(0, fieldValue.length()-1);
		}
		
		return fieldValue;
	}

	public static List<String> collectAllEntryKeys(List<BCCBibTeXFile> bibTeXFiles) {
		List<String> allEntryKeys = new ArrayList<>();
		
		for (BCCBibTeXFile bibTeXFile : bibTeXFiles) {
			allEntryKeys.addAll(collectAllEntryKeys(bibTeXFile));
		}
		
		return allEntryKeys;
	}

	public static List<String> collectAllEntryKeys(BCCBibTeXFile bibTeXFile) {
		List<String> allEntryKeys = new ArrayList<>();
		
		List<BCCAbstractBibTeXEntry> allBibTeXEntries = collectAllAbstractBibTeXEntries(bibTeXFile);
		for (BCCAbstractBibTeXEntry bibTeXEntry : allBibTeXEntries) {
			allEntryKeys.add(bibTeXEntry.getEntryBody().getEntryKeyObject().getEntryKey());
		}
		
		return allEntryKeys;
	}
	
}
