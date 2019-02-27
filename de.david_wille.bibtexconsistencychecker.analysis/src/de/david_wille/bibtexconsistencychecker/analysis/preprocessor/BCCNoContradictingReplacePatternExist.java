package de.david_wille.bibtexconsistencychecker.analysis.preprocessor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;

import de.david_wille.bibtexconsistencychecker.analysis.BCCAnalysis;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXPackage;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplacePatternEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.util.BCCBibTeXUtil;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;

public class BCCNoContradictingReplacePatternExist {

	public static boolean checkNoContradictingReplacePatternExist(List<BCCBibTeXFile> parsedBibTeXFiles) {
		boolean noErrorsDetected = true;
		
		List<BCCReplacePatternEntry> overallReplacePattern = new ArrayList<>();
		
		for (BCCBibTeXFile parsedBibTeXFile : parsedBibTeXFiles) {
			List<BCCReplacePatternEntry> fileReplacePattern = BCCBibTeXUtil.collectAllReplacePattern(parsedBibTeXFile);
			
			for (BCCReplacePatternEntry replacePattern : fileReplacePattern) {
				String key = replacePattern.getReplaceKeyObject().getReplaceKey();
				
				if (replacePatternAlreadyExists(overallReplacePattern, replacePattern)) {
					BCCReplacePatternEntry existingPattern = identifyReplacePattern(overallReplacePattern, key);

					boolean noNewErrorsDetected = checkNoContradictingReplacePatternExist(replacePattern, existingPattern);
					noErrorsDetected = noErrorsDetected && noNewErrorsDetected;
				}
				else {
					overallReplacePattern.add(replacePattern);
				}
			}
		}
		
		return noErrorsDetected;
	}

	private static boolean replacePatternAlreadyExists(List<BCCReplacePatternEntry> overallReplacePattern,
			BCCReplacePatternEntry replacePattern)
	{
		for (BCCReplacePatternEntry entry : overallReplacePattern) {
			if (entry.getReplaceKeyObject().getReplaceKey().equals(replacePattern.getReplaceKeyObject().getReplaceKey())) {
				return true;
			}
		}
		return false;
	}

	private static BCCReplacePatternEntry identifyReplacePattern(List<BCCReplacePatternEntry> overallReplacePattern,
			String replaceKey)
	{
		for (BCCReplacePatternEntry entry : overallReplacePattern) {
			if (entry.getReplaceKeyObject().getReplaceKey().equals(replaceKey)) {
				return entry;
			}
		}
		return null;
	}

	private static boolean checkNoContradictingReplacePatternExist(BCCReplacePatternEntry pattern,
			BCCReplacePatternEntry existingPattern)
	{
		String value = pattern.getReplacementFieldValue();
		String existingValue = existingPattern.getReplacementFieldValue();
		
		if (!value.equals(existingValue)) {
			IResource resource1 = BCCResourceUtil.getIFile(pattern);
			IResource resource2 = BCCResourceUtil.getIFile(existingPattern);
			
			if (resource1.equals(resource2)) {
				String errorMessage = "Another replace pattern specifies a different value for this key.";
				
				BCCAnalysis.createConsistencyProblemErrorMarker(resource1, errorMessage, pattern, BCCBibTeXPackage.Literals.BCC_REPLACE_PATTERN_ENTRY__REPLACE_KEY_OBJECT);
				BCCAnalysis.createConsistencyProblemErrorMarker(resource2, errorMessage, existingPattern, BCCBibTeXPackage.Literals.BCC_REPLACE_PATTERN_ENTRY__REPLACE_KEY_OBJECT);
			}
			else {
				String errorMessage1 = "Another replace pattern in \"" + resource2.getName() + "\" specifies a different value for this key.";
				String errorMessage2 = "Another replace pattern in \"" + resource1.getName() + "\" specifies a different value for this key.";
				
				BCCAnalysis.createConsistencyProblemErrorMarker(resource1, errorMessage1, pattern, BCCBibTeXPackage.Literals.BCC_REPLACE_PATTERN_ENTRY__REPLACE_KEY_OBJECT);
				BCCAnalysis.createConsistencyProblemErrorMarker(resource2, errorMessage2, existingPattern, BCCBibTeXPackage.Literals.BCC_REPLACE_PATTERN_ENTRY__REPLACE_KEY_OBJECT);
			}
			
			return false;
		}
		
		return true;
	}

}
