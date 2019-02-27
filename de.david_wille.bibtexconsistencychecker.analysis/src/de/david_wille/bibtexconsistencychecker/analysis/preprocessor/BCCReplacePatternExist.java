package de.david_wille.bibtexconsistencychecker.analysis.preprocessor;

import java.util.List;

import org.eclipse.core.resources.IResource;

import de.david_wille.bibtexconsistencychecker.analysis.BCCAnalysis;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXFileEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractEntryBodyField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractGenericField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXPackage;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplacePatternEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.util.BCCBibTeXUtil;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;

public class BCCReplacePatternExist {

	public static boolean checkReplacePatternExist(List<BCCBibTeXFile> parsedBibTeXFiles) {
		boolean noErrorsDetected = true;
		
		List<BCCReplacePatternEntry> allReplacePattern = BCCBibTeXUtil.collectAllReplacePattern(parsedBibTeXFiles);
		
		for (BCCBibTeXFile parsedBibTeXFile : parsedBibTeXFiles) {
			boolean noNewErrorsDetected = checkReplacePatternExist(parsedBibTeXFile, allReplacePattern);
			noErrorsDetected = noErrorsDetected && noNewErrorsDetected;
		}
		
		return noErrorsDetected;
	}

	private static boolean checkReplacePatternExist(BCCBibTeXFile parsedBibTeXFile, List<BCCReplacePatternEntry> allReplacePattern) {
		boolean noErrorsDetected = true;
		
		for (BCCAbstractBibTeXFileEntry fileEntry : parsedBibTeXFile.getEntries()) {
			if (fileEntry instanceof BCCAbstractBibTeXEntry) {
				BCCAbstractBibTeXEntry bibTeXEntry = (BCCAbstractBibTeXEntry) fileEntry;
				
				boolean noNewErrorsDetected = checkReplacePatternExist(bibTeXEntry, allReplacePattern);
				noErrorsDetected = noErrorsDetected && noNewErrorsDetected;
			}
		}
		
		return noErrorsDetected;
	}

	private static boolean checkReplacePatternExist(BCCAbstractBibTeXEntry bibTeXEntry, List<BCCReplacePatternEntry> allReplacePattern) {
		boolean noErrorsDetected = true;
		
		for (BCCAbstractEntryBodyField field : bibTeXEntry.getEntryBody().getFields()) {
			if (field instanceof BCCAbstractGenericField) {
				BCCAbstractGenericField genericField = (BCCAbstractGenericField) field;
				
				if (BCCBibTeXUtil.usesReplacePattern(genericField)) {
					boolean noNewErrorsDetected = checkReplacePatternExist(genericField, allReplacePattern);
					noErrorsDetected = noErrorsDetected && noNewErrorsDetected;
				}
			}
		}
		
		return noErrorsDetected;
	}

	private static boolean checkReplacePatternExist(BCCAbstractGenericField field, List<BCCReplacePatternEntry> allReplacePattern) {
		String fieldValue = field.getFieldValueObject().getFieldValue();
		
		if (!fieldReplacePatternExists(fieldValue, allReplacePattern)) {
			String errorMessage = "The specified replace pattern does not exist.";
			IResource resource = BCCResourceUtil.getIFile(field);
			
			BCCAnalysis.createConsistencyProblemErrorMarker(resource, errorMessage, field, BCCBibTeXPackage.Literals.BCC_ABSTRACT_GENERIC_FIELD__FIELD_VALUE_OBJECT);
			
			return false;
		}
		
		return true;
	}

	private static boolean fieldReplacePatternExists(String fieldValue, List<BCCReplacePatternEntry> allReplacePattern) {
		for (BCCReplacePatternEntry replacePattern : allReplacePattern) {
			if (replacePattern.getReplaceKeyObject().getReplaceKey().equals(fieldValue)) {
				return true;
			}
		}
		return false;
	}

}
