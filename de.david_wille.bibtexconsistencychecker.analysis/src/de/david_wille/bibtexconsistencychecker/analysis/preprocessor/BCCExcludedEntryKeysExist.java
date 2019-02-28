package de.david_wille.bibtexconsistencychecker.analysis.preprocessor;

import java.util.List;

import org.eclipse.core.resources.IResource;

import de.david_wille.bibtexconsistencychecker.analysis.BCCAnalysis;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.bibtex.util.BCCBibTeXUtil;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAbstractExcludedEntryKeysBody;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRule;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRulePackage;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCEntryKeyReference;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCMultiEntryExcludedEntryKeysBody;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCSingleEntryExcludedEntryKeysBody;
import de.david_wille.bibtexconsistencychecker.statistics.BCCStatistics;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;

public class BCCExcludedEntryKeysExist {

	public static boolean checkExcludedEntryKeysExist(List<BCCBibTeXFile> parsedBibTeXFiles,
			List<BCCConsistencyRule> parsedConsistencyRules)
	{
		boolean noErrorsDetected = true;
		
		List<String> allReplacePattern = BCCBibTeXUtil.collectAllEntryKeys(parsedBibTeXFiles);
		
		for (BCCConsistencyRule parsedConsistencyRule : parsedConsistencyRules) {
			boolean noNewErrorsDetected = checkExcludedEntryKeysExist(parsedConsistencyRule, allReplacePattern);
			noErrorsDetected = noErrorsDetected && noNewErrorsDetected;
		}

		return noErrorsDetected;
	}

	private static boolean checkExcludedEntryKeysExist(BCCConsistencyRule parsedConsistencyRule,
			List<String> allReplacePattern)
	{
		BCCAbstractExcludedEntryKeysBody excludedEntryKeyBody = parsedConsistencyRule.getRuleBody().getExcludedEntryKeysBody();
		if (excludedEntryKeyBody != null) {
			if (excludedEntryKeyBody instanceof BCCSingleEntryExcludedEntryKeysBody) {
				BCCSingleEntryExcludedEntryKeysBody singleEntryKeyBody = (BCCSingleEntryExcludedEntryKeysBody) excludedEntryKeyBody;
				
				BCCEntryKeyReference excludedKeyReference = singleEntryKeyBody.getExcludedEntryKey();
				String excludedKey = excludedKeyReference.getEntryKey();
				if (!allReplacePattern.contains(excludedKey)) {
					String warningMessage = "The excluded key does not exist.";
					IResource resource = BCCResourceUtil.getIFile(excludedKeyReference);
					
					BCCStatistics.getInstance().increaseWarningCounter();
					
					BCCAnalysis.createConsistencyProblemWarningMarker(resource, warningMessage, excludedKeyReference, BCCConsistencyRulePackage.Literals.BCC_SINGLE_ENTRY_EXCLUDED_ENTRY_KEYS_BODY__EXCLUDED_ENTRY_KEY);
					return false;
				}
			}
			else if (excludedEntryKeyBody instanceof BCCMultiEntryExcludedEntryKeysBody) {
				BCCMultiEntryExcludedEntryKeysBody multiEntryKeyBody = (BCCMultiEntryExcludedEntryKeysBody) excludedEntryKeyBody;
				boolean noProblemDetected = true;
				
				int i = 0;
				for (BCCEntryKeyReference excludedKeyReference : multiEntryKeyBody.getExcludedEntryKeys()) {
					String excludedKey = excludedKeyReference.getEntryKey();
					if (!allReplacePattern.contains(excludedKey)) {
						String warningMessage = "The excluded key does not exist.";
						IResource resource = BCCResourceUtil.getIFile(excludedKeyReference);
						
						BCCStatistics.getInstance().increaseWarningCounter();
						
						BCCAnalysis.createConsistencyProblemWarningMarker(resource, warningMessage, excludedKeyReference, BCCConsistencyRulePackage.Literals.BCC_MULTI_ENTRY_EXCLUDED_ENTRY_KEYS_BODY__EXCLUDED_ENTRY_KEYS, i);
						
						noProblemDetected = false;
					}
					i++;
				}
				
				return noProblemDetected;
			}
		}
		
		return true;
	}

}
