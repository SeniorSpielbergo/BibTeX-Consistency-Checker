package de.david_wille.bibtexconsistencychecker.consistencyrule.validation

import de.david_wille.bibtexconsistencychecker.bibtex.cache.BCCBibTeXCache
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAbstractExcludedEntryKeysBody
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAbstractSelector
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAbstractSelectorBody
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAllSelector
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAreIntegerExpression
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRule
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRulePackage
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCEntryKeyReference
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCExpression
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCFieldExistsExpression
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCFieldsExistExpression
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCIsIntegerExpression
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCMultiEntryExcludedEntryKeysBody
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCMultiEntrySelectorBody
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCMultiFieldSelectionExpression
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCNoKeywordExpression
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCRuleBody
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCSingleEntryExcludedEntryKeysBody
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCSingleEntrySelectorBody
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCSingleFieldSelectionExpression
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCUseReplacePatternExpression
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCUsesReplacePatternExpression
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil
import java.util.ArrayList
import java.util.HashMap
import java.util.List
import java.util.Map
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProject
import org.eclipse.xtext.validation.Check

/**
 * This class contains custom validation rules. 
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
class BCCConsistencyRuleValidator extends AbstractBCCConsistencyRuleValidator {
	
	public static val String APPLIES_TO_ALL_SELECTOR_COMBINED_WITH_OTHER_SELECTORS = "APPLIES_TO_ALL_SELECTOR_COMBINED_WITH_OTHER_SELECTORS"
	public static val String EACH_APPLIES_TO_SELECTOR_ONLY_EXISTS_ONCE = "EACH_APPLIES_TO_SELECTOR_ONLY_EXISTS_ONCE"
	public static val String EACH_EXCEPT_FOR_SELECTOR_ONLY_EXISTS_ONCE = "EACH_EXCEPT_FOR_SELECTOR_ONLY_EXISTS_ONCE"
	public static val String ALL_SELECTOR_USED_IN_EXCEPT_FOR_RULE = "ALL_SELECTOR_USED_IN_EXCEPT_FOR_RULE"
	public static val String EXCEPT_FOR_SPECIFIES_SAME_SELECTORS_AS_APPLIES_TO = "EXCEPT_FOR_SPECIFIES_SAME_SELECTORS_AS_APPLIES_TO"
	public static val String USES_CORRECT_KEYWORD_COMBINATIONS = "USES_CORRECT_KEYWORD_COMBINATIONS"
	public static val String FILE_NOT_IN_CORRECT_FOLDER = "FILE_NOT_IN_CORRECT_FOLDER"
	public static val String EXCLUDED_KEY_NOT_FOUND = "EXCLUDED_KEY_NOT_FOUND"
	
	@Check(NORMAL)
	def checkExcludedKeyExists(BCCAbstractExcludedEntryKeysBody excludedKeyBody) {
		var IProject project = BCCResourceUtil.getIProject(excludedKeyBody);
		
		if (excludedKeyBody instanceof BCCSingleEntryExcludedEntryKeysBody) {
			var String excludedKey = excludedKeyBody.excludedEntryKey.entryKey
			
			if (BCCBibTeXCache.instance.getEntries(project, excludedKey).size() == 0) {
				warning("An entry with this key does not exist.", BCCConsistencyRulePackage.Literals.BCC_SINGLE_ENTRY_EXCLUDED_ENTRY_KEYS_BODY__EXCLUDED_ENTRY_KEY, EXCLUDED_KEY_NOT_FOUND)
			}
		}
		else if (excludedKeyBody instanceof BCCMultiEntryExcludedEntryKeysBody) {
			var int i = 0
			for (BCCEntryKeyReference entryKeyReference : excludedKeyBody.excludedEntryKeys) {
				var String excludedKey = entryKeyReference.entryKey
				
				if (BCCBibTeXCache.instance.getEntries(project, excludedKey).size() == 0) {
					warning("An entry with this key does not exist.", BCCConsistencyRulePackage.Literals.BCC_MULTI_ENTRY_EXCLUDED_ENTRY_KEYS_BODY__EXCLUDED_ENTRY_KEYS, i, EXCLUDED_KEY_NOT_FOUND)
				}
				i++
			}
		}
	}
	
	@Check
	def checkFileIsStoredInBibliographyFolder(BCCConsistencyRule consistencyRule) {
		var IFile file = BCCResourceUtil.getIFile(consistencyRule)
		var IProject project = BCCResourceUtil.getIProject(consistencyRule)
		
		if (!file.fullPath.toPortableString.startsWith(project.fullPath.toPortableString + "/rules")) {
			warning("This BibTeX Consistency Rule file will never be processed as it is not a sub file of the \"rules\" folder.", BCCConsistencyRulePackage.Literals.BCC_CONSISTENCY_RULE__RULE_BODY, FILE_NOT_IN_CORRECT_FOLDER)
		}
	}
	
	@Check
	def checkAppliesToAllIsNotCombinedWithOtherSelectors(BCCMultiEntrySelectorBody multiEntryBody) {
		if (bodyRepresentsAppliesToBody(multiEntryBody)) {
			if (appliesToAllEntryExists(multiEntryBody)) {
				var int i = 0
				for (BCCAbstractSelector selector : multiEntryBody.selectors) {
					if (!selectorIsApplyToAllSelector(selector)) {
						warning("This selector is shadowed by another selector specifying that this rule should be applied to all BibTeX entries.", selector.eContainingFeature, i, APPLIES_TO_ALL_SELECTOR_COMBINED_WITH_OTHER_SELECTORS)
					}
					i++
				}
			}
		}
	}

	@Check
	def checkEachSelectorIsUsedOnlyOnce(BCCMultiEntrySelectorBody multiEntryBody) {
		var Map<Class<?>, Integer> selectors = new HashMap<Class<?>, Integer>();
		
		var int i = 0
		for (BCCAbstractSelector selector : multiEntryBody.selectors) {
			if (!selectors.containsKey(selector.class)) {
				selectors.put(selector.class, i)
			}
			else {
				if (bodyRepresentsAppliesToBody(multiEntryBody)) {
					error("This selector was previously used. Each selector can only be used once per applies to rule.", selector.eContainingFeature, i, EACH_APPLIES_TO_SELECTOR_ONLY_EXISTS_ONCE)
				}
				else if (bodyRepresentsExceptForBody(multiEntryBody)) {
					error("This selector was previously used. Each selector can only be used once per except for rule.", selector.eContainingFeature, i, EACH_EXCEPT_FOR_SELECTOR_ONLY_EXISTS_ONCE)
				}
			}
			i++;
		}
	}
	
	@Check
	def checkExceptForDoesNotUseAllSelector(BCCAbstractSelectorBody entryBody) {
		if (bodyRepresentsExceptForBody(entryBody)) {
			if (appliesToAllEntryExists(entryBody)) {
				if (entryBody instanceof BCCSingleEntrySelectorBody) {
					var BCCSingleEntrySelectorBody singleEntryBody = entryBody as BCCSingleEntrySelectorBody
					
					if (selectorIsApplyToAllSelector(singleEntryBody.selector)) {
						error("This selector cannot be used for except for rules. Otherwise, the specified rule will never be applied to any BibTeX entries.", singleEntryBody.selector.eContainingFeature, ALL_SELECTOR_USED_IN_EXCEPT_FOR_RULE)
					}
				}
				else if (entryBody instanceof BCCMultiEntrySelectorBody) {
					var BCCMultiEntrySelectorBody multiEntryBody = entryBody as BCCMultiEntrySelectorBody
					
					var int i = 0
					for (BCCAbstractSelector selector : multiEntryBody.selectors) {
						if (selectorIsApplyToAllSelector(selector)) {
							error("This selector cannot be used for except for rules. Otherwise, the specified rule will never be applied to any BibTeX entries.", selector.eContainingFeature, i, ALL_SELECTOR_USED_IN_EXCEPT_FOR_RULE)
						}
						i++;
					}
				}
			}
		}
	}
	
	@Check
	def checkExceptForBodyDoesNotSpecifySameSelectorsAsAppliesToBody(BCCAbstractSelectorBody exceptForEntryBody) {
		if (bodyRepresentsExceptForBody(exceptForEntryBody)) {
			var BCCRuleBody ruleBody = exceptForEntryBody.eContainer as BCCRuleBody
			var BCCAbstractSelectorBody appliesToEntryBody = ruleBody.appliesToBody
			
			checkExceptForBodySpecifiesSameSelectorAsAppliesToBody(exceptForEntryBody, appliesToEntryBody)
		}
	}
	
	@Check
	def checkForCorrectKeywordCombinations(BCCNoKeywordExpression noKeywordExpression) {
		var BCCExpression leftExpression = noKeywordExpression.getLeft();
		var BCCExpression rightExpression = noKeywordExpression.getRight();
		
		if (leftExpression instanceof BCCSingleFieldSelectionExpression) {
			if (rightExpression instanceof BCCFieldsExistExpression) {
				error("Incorrect grammar. Please use the \"field exists\" keyword instead.", rightExpression.eContainingFeature, USES_CORRECT_KEYWORD_COMBINATIONS)
			}
			else if (rightExpression instanceof BCCUseReplacePatternExpression) {
				error("Incorrect grammar. Please use the \"uses replace pattern\" keyword instead.", rightExpression.eContainingFeature, USES_CORRECT_KEYWORD_COMBINATIONS)
			}
			else if (rightExpression instanceof BCCAreIntegerExpression) {
				error("Incorrect grammar. Please use the \"is integer\" keyword instead.", rightExpression.eContainingFeature, USES_CORRECT_KEYWORD_COMBINATIONS)
			}
		}
		else if (leftExpression instanceof BCCMultiFieldSelectionExpression) {
			if (rightExpression instanceof BCCFieldExistsExpression) {
				error("Incorrect grammar. Please use the \"fields exist\" keyword instead.", rightExpression.eContainingFeature, USES_CORRECT_KEYWORD_COMBINATIONS)
			}
			else if (rightExpression instanceof BCCUsesReplacePatternExpression) {
				error("Incorrect grammar. Please use the \"use replace pattern\" keyword instead.", rightExpression.eContainingFeature, USES_CORRECT_KEYWORD_COMBINATIONS)
			}
			else if (rightExpression instanceof BCCIsIntegerExpression) {
				error("Incorrect grammar. Please use the \"are integer\" keyword instead.", rightExpression.eContainingFeature, USES_CORRECT_KEYWORD_COMBINATIONS)
			}
		}
	}
	
	protected def checkExceptForBodySpecifiesSameSelectorAsAppliesToBody(BCCAbstractSelectorBody exceptForEntryBody, BCCAbstractSelectorBody appliesToEntryBody) {
		var List<BCCAbstractSelector> appliesToSelectors = identifySelectors(appliesToEntryBody)
		
		if (exceptForEntryBody instanceof BCCSingleEntrySelectorBody) {
			if (sameSelectorContained(appliesToSelectors, exceptForEntryBody.selector)) {
				var BCCSingleEntrySelectorBody singleEntryBody = exceptForEntryBody as BCCSingleEntrySelectorBody
				
				warning("This selector overrides the same applies to selector. Thus, the applies to selector will have no effect.", singleEntryBody.selector.eContainingFeature, EXCEPT_FOR_SPECIFIES_SAME_SELECTORS_AS_APPLIES_TO)
			}
		}
		else if (exceptForEntryBody instanceof BCCMultiEntrySelectorBody) {
			var BCCMultiEntrySelectorBody multiEntryBody = exceptForEntryBody as BCCMultiEntrySelectorBody
			
			var int i = 0
			for (BCCAbstractSelector selector : multiEntryBody.selectors) {
				if (sameSelectorContained(appliesToSelectors, selector)) {
					warning("This selector overrides the same applies to selector. Thus, the applies to selector will have no effect.", selector.eContainingFeature, i, EXCEPT_FOR_SPECIFIES_SAME_SELECTORS_AS_APPLIES_TO)
				}
				i++
			}
		}
	}
	
	protected def sameSelectorContained(List<BCCAbstractSelector> selectors, BCCAbstractSelector selector) {
		for (BCCAbstractSelector currentSelector : selectors) {
			if (currentSelector.class.equals(selector.class)) {
				return true
			}
		}
		
		false
	}
	
	protected def identifySelectors(BCCAbstractSelectorBody entryBody) {
		var List<BCCAbstractSelector> selectors = new ArrayList<BCCAbstractSelector>();
		
		if (entryBody instanceof BCCMultiEntrySelectorBody) {
			var BCCMultiEntrySelectorBody multiEntryBody = entryBody as BCCMultiEntrySelectorBody
			
			return multiEntryBody.selectors
		}
		else if (entryBody instanceof BCCSingleEntrySelectorBody) {
			var BCCSingleEntrySelectorBody singleEntryBody = entryBody as BCCSingleEntrySelectorBody
			
			selectors.add(singleEntryBody.selector)
		}
		
		selectors
	}
	
	protected def appliesToAllEntryExists(BCCAbstractSelectorBody entryBody) {
		if (entryBody instanceof BCCMultiEntrySelectorBody) {
			var BCCMultiEntrySelectorBody multiEntryBody = entryBody as BCCMultiEntrySelectorBody
			
			for (BCCAbstractSelector selector : multiEntryBody.selectors) {
				if (selector instanceof BCCAllSelector) {
					return true
				}
			}
		}
		else if (entryBody instanceof BCCSingleEntrySelectorBody) {
			var BCCSingleEntrySelectorBody singleEntryBody = entryBody as BCCSingleEntrySelectorBody
			
			if (singleEntryBody.selector instanceof BCCAllSelector) {
				return true
			}
		}
		
		false
	}
	
	protected def bodyRepresentsAppliesToBody(BCCMultiEntrySelectorBody body) {
		var BCCRuleBody ruleBody = body.eContainer as BCCRuleBody
		ruleBody.appliesToBody.equals(body)
	}
	
	protected def bodyRepresentsExceptForBody(BCCAbstractSelectorBody body) {
		var BCCRuleBody ruleBody = body.eContainer as BCCRuleBody
		ruleBody.exceptForBody.equals(body)
	}
	
	protected def selectorIsApplyToAllSelector(BCCAbstractSelector selector) {
		selector instanceof BCCAllSelector
	}
	
}
