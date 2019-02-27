package de.david_wille.bibtexconsistencychecker.analysis.collector;

import java.util.ArrayList;
import java.util.List;

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXFileEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCArticleEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBookEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBookletEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCConferenceEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCInBookEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCInCollectionEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCInProceedingsEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCManualEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCMasterthesisEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCMiscEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCPhdThesisEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCProceedingsEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReportEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCStandardEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCTechReportEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCThesisEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCUnpublishedEntry;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAbstractExcludedEntryKeyBody;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAbstractSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAbstractSelectorBody;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAllSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCArticleEntrySelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCBookEntrySelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCBookletEntrySelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConferenceEntrySelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRule;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCEntryKeyReference;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCInBookEntrySelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCInCollectionEntrySelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCInProceedingsEntrySelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCManualEntrySelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCMasterthesisEntrySelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCMiscEntrySelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCMultiEntryExcludedEntryKeysBody;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCMultiEntrySelectorBody;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCPhdThesisEntrySelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCProceedingsEntrySelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCReportEntrySelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCRuleBody;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCSingleEntryExcludedEntryKeysBody;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCSingleEntrySelectorBody;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCStandardEntrySelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCTechReportEntrySelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCThesisEntrySelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCUnpublishedEntrySelector;

public class BCCCollector {
	
	private List<BCCBibTeXFile> parsedBibTeXFiles;

	public BCCCollector(List<BCCBibTeXFile> parsedBibTeXFiles) {
		this.parsedBibTeXFiles = parsedBibTeXFiles;
	}

	public List<BCCAbstractBibTeXEntry> collectMatchingEntries(BCCConsistencyRule consistencyRule) {
		List<BCCAbstractBibTeXEntry> allMatchingEntries = new ArrayList<>();
		
		for (BCCBibTeXFile bibTeXFile : parsedBibTeXFiles) {
			List<BCCAbstractBibTeXEntry> matchingFileEntries = collectMatchingEntries(bibTeXFile, consistencyRule);
			allMatchingEntries.addAll(matchingFileEntries);
		}
		
		return allMatchingEntries;
	}

	private List<BCCAbstractBibTeXEntry> collectMatchingEntries(BCCBibTeXFile bibTeXFile, BCCConsistencyRule consistencyRule) {
		List<BCCAbstractBibTeXEntry> matchingFileEntries = new ArrayList<>();
		
		for (BCCAbstractBibTeXFileEntry fileEntry : bibTeXFile.getEntries()) {
			if (fileEntry instanceof BCCAbstractBibTeXEntry) {
				BCCAbstractBibTeXEntry bibTeXEntry = (BCCAbstractBibTeXEntry) fileEntry;
				
				if (ruleSelectsEntry(consistencyRule, bibTeXEntry)) {
					matchingFileEntries.add(bibTeXEntry);
				}
			}
		}
		
		return matchingFileEntries;
	}

	private boolean ruleSelectsEntry(BCCConsistencyRule consistencyRule, BCCAbstractBibTeXEntry bibTeXEntry) {
		BCCRuleBody bCCRuleBody = consistencyRule.getRuleBody();
		BCCAbstractSelectorBody appliesToBody = bCCRuleBody.getAppliesToBody();
		BCCAbstractSelectorBody exceptForBody = bCCRuleBody.getExceptForBody();
		
		List<BCCAbstractSelector> appliesToSelectors = identifyAllSelectors(appliesToBody);
		List<BCCAbstractSelector> exceptForSelectors = identifyAllSelectors(exceptForBody);
		List<String> excludedEntryKeys = identifyExcludedEntryKeys(bCCRuleBody);
		
		for (BCCAbstractSelector appliesToSelector : appliesToSelectors) {
			boolean selectedByRule = ruleSelectsEntry(bibTeXEntry, appliesToSelector, exceptForSelectors, excludedEntryKeys);
			
			if (selectedByRule) {
				return true;
			}
		}
		
		return false;
	}

	private boolean ruleSelectsEntry(BCCAbstractBibTeXEntry bibTeXEntry, BCCAbstractSelector appliesToSelector,
			List<BCCAbstractSelector> exceptForSelectors, List<String> excludedEntryKeys)
	{
		if (!entryKeyIsExcluded(bibTeXEntry, excludedEntryKeys)) {
			if (appliesToSelector instanceof BCCAllSelector) {
				if (!isBibTeXEntryTypeInExceptForList(bibTeXEntry, exceptForSelectors)) {
					return true;
				}
			}
			else if (selectorAndBibTeXEntryHaveMatchingTypes(appliesToSelector, bibTeXEntry)) {
				if (!isBibTeXEntryTypeInExceptForList(bibTeXEntry, exceptForSelectors)) {
					return true;
				}
			}
		}
	
		return false;
	}

	private boolean isBibTeXEntryTypeInExceptForList(BCCAbstractBibTeXEntry bibTeXEntry,
			List<BCCAbstractSelector> exceptForSelectors)
	{
		for (BCCAbstractSelector exceptForSelector : exceptForSelectors) {
			if (selectorAndBibTeXEntryHaveMatchingTypes(exceptForSelector, bibTeXEntry)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean entryKeyIsExcluded(BCCAbstractBibTeXEntry bibTeXEntry, List<String> excludedEntryKeys) {
		return excludedEntryKeys.contains(bibTeXEntry.getEntryBody().getEntryKeyObject().getEntryKey());
	}
	
	private boolean selectorAndBibTeXEntryHaveMatchingTypes(BCCAbstractSelector selector, BCCAbstractBibTeXEntry bibTeXEntry) {
		if (selector instanceof BCCInProceedingsEntrySelector) {
			return bibTeXEntry instanceof BCCInProceedingsEntry;
		}
		else if (selector instanceof BCCBookEntrySelector) {
			return bibTeXEntry instanceof BCCBookEntry;
		}
		else if (selector instanceof BCCArticleEntrySelector) {
			return bibTeXEntry instanceof BCCArticleEntry;
		}
		else if (selector instanceof BCCStandardEntrySelector) {
			return bibTeXEntry instanceof BCCStandardEntry;
		}
		else if (selector instanceof BCCInBookEntrySelector) {
			return bibTeXEntry instanceof BCCInBookEntry;
		}
		else if (selector instanceof BCCReportEntrySelector) {
			return bibTeXEntry instanceof BCCReportEntry;
		}
		else if (selector instanceof BCCThesisEntrySelector) {
			return bibTeXEntry instanceof BCCThesisEntry;
		}
		else if (selector instanceof BCCInCollectionEntrySelector) {
			return bibTeXEntry instanceof BCCInCollectionEntry;
		}
		else if (selector instanceof BCCProceedingsEntrySelector) {
			return bibTeXEntry instanceof BCCProceedingsEntry;
		}
		else if (selector instanceof BCCBookletEntrySelector) {
			return bibTeXEntry instanceof BCCBookletEntry;
		}
		else if (selector instanceof BCCConferenceEntrySelector) {
			return bibTeXEntry instanceof BCCConferenceEntry;
		}
		else if (selector instanceof BCCManualEntrySelector) {
			return bibTeXEntry instanceof BCCManualEntry;
		}
		else if (selector instanceof BCCMasterthesisEntrySelector) {
			return bibTeXEntry instanceof BCCMasterthesisEntry;
		}
		else if (selector instanceof BCCMiscEntrySelector) {
			return bibTeXEntry instanceof BCCMiscEntry;
		}
		else if (selector instanceof BCCPhdThesisEntrySelector) {
			return bibTeXEntry instanceof BCCPhdThesisEntry;
		}
		else if (selector instanceof BCCTechReportEntrySelector) {
			return bibTeXEntry instanceof BCCTechReportEntry;
		}
		else if (selector instanceof BCCUnpublishedEntrySelector) {
			return bibTeXEntry instanceof BCCUnpublishedEntry;
		}
		
		return false;
	}
	
	private List<String> identifyExcludedEntryKeys(BCCRuleBody bCCRuleBody) {
		List<String> excludedEntryKeys = new ArrayList<>();
		BCCAbstractExcludedEntryKeyBody excludedEntryKeyBody = bCCRuleBody.getExcludedEntryKeysBody();
		
		if (excludedEntryKeyBody instanceof BCCSingleEntryExcludedEntryKeysBody) {
			BCCSingleEntryExcludedEntryKeysBody singleEntryBody = (BCCSingleEntryExcludedEntryKeysBody) excludedEntryKeyBody;
			excludedEntryKeys.add(singleEntryBody.getExcludedEntryKey().getEntryKey());
		}
		else if (excludedEntryKeyBody instanceof BCCMultiEntryExcludedEntryKeysBody) {
			BCCMultiEntryExcludedEntryKeysBody multiEntryBody = (BCCMultiEntryExcludedEntryKeysBody) excludedEntryKeyBody;
			for (BCCEntryKeyReference entryKeyReference : multiEntryBody.getExcludedEntryKeys()) {
				excludedEntryKeys.add(entryKeyReference.getEntryKey());
			}
		}
		
		return excludedEntryKeys;
	}

	private List<BCCAbstractSelector> identifyAllSelectors(BCCAbstractSelectorBody appliesToBody) {
		List<BCCAbstractSelector> selectors = new ArrayList<>();
		
		if (appliesToBody instanceof BCCSingleEntrySelectorBody) {
			BCCSingleEntrySelectorBody singleBody = (BCCSingleEntrySelectorBody) appliesToBody;
			BCCAbstractSelector selector = singleBody.getSelector();
			
			selectors.add(selector);
		}
		else if (appliesToBody instanceof BCCMultiEntrySelectorBody) {
			BCCMultiEntrySelectorBody multiBody = (BCCMultiEntrySelectorBody) appliesToBody;
			return multiBody.getSelectors();
		}
		
		return selectors;
	}

}
