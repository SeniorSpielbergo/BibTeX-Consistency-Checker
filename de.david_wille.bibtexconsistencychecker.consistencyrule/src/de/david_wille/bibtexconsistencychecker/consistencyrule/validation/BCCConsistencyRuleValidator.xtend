package de.david_wille.bibtexconsistencychecker.consistencyrule.validation

import de.david_wille.bibtexconsistencychecker.bibtex.cache.BCCBibTeXCache
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAbstractSelector
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAllSelector
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRule
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRulePackage
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCEntryKeyReference
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCEntrySelectorBody
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCExcludedEntryKeysBody
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCRuleBody
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
	public static val String EACH_SELECTOR_ONLY_EXISTS_ONCE = "EACH_SELECTOR_ONLY_EXISTS_ONCE"
	public static val String EACH_EXCLUDED_KEY_ONLY_EXISTS_ONCE = "EACH_EXCLUDED_KEY_ONLY_EXISTS_ONCE"
	public static val String ALL_SELECTOR_USED_IN_EXCEPT_FOR_RULE = "ALL_SELECTOR_USED_IN_EXCEPT_FOR_RULE"
	public static val String EXCEPT_FOR_SPECIFIES_SAME_SELECTORS_AS_APPLIES_TO = "EXCEPT_FOR_SPECIFIES_SAME_SELECTORS_AS_APPLIES_TO"
	public static val String FILE_NOT_IN_CORRECT_FOLDER = "FILE_NOT_IN_CORRECT_FOLDER"
	public static val String EXCLUDED_KEY_NOT_FOUND = "EXCLUDED_KEY_NOT_FOUND"
	
	@Check(NORMAL)
	def checkExcludedKeyExists(BCCExcludedEntryKeysBody excludedKeyBody) {
		var IProject project = BCCResourceUtil.getIProject(excludedKeyBody);
		
		var int i = 0
		for (BCCEntryKeyReference entryKeyReference : excludedKeyBody.excludedEntryKeys) {
			var String excludedKey = entryKeyReference.entryKey
			
			if (BCCBibTeXCache.instance.getEntries(project, excludedKey).size() == 0) {
				warning("An entry with key \"" + excludedKey + "\" does not exist.", BCCConsistencyRulePackage.Literals.BCC_EXCLUDED_ENTRY_KEYS_BODY__EXCLUDED_ENTRY_KEYS, i, EXCLUDED_KEY_NOT_FOUND)
			}
			i++
		}
	}
	
	@Check
	def checkFileIsStoredInRulesFolder(BCCConsistencyRule consistencyRule) {
		var IFile file = BCCResourceUtil.getIFile(consistencyRule)
		var IProject project = BCCResourceUtil.getIProject(consistencyRule)
		
		if (!file.fullPath.toPortableString.startsWith(project.fullPath.toPortableString + "/rules")) {
			warning("This BibTeX Consistency Rule file will never be processed as it is not a sub file of the \"rules\" folder.", BCCConsistencyRulePackage.Literals.BCC_CONSISTENCY_RULE__RULE_BODY, FILE_NOT_IN_CORRECT_FOLDER)
		}
	}
	
	@Check
	def checkAppliesToAllIsNotCombinedWithOtherSelectors(BCCEntrySelectorBody entryBody) {
		if (bodyRepresentsAppliesToBody(entryBody)) {
			if (appliesToAllEntryExists(entryBody)) {
				var int i = 0
				for (BCCAbstractSelector selector : entryBody.selectors) {
					if (!selectorIsApplyToAllSelector(selector)) {
						warning("This selector is shadowed by another selector specifying that this rule should be applied to all BibTeX entries.", selector.eContainingFeature, i, APPLIES_TO_ALL_SELECTOR_COMBINED_WITH_OTHER_SELECTORS)
					}
					i++
				}
			}
		}
	}

	@Check
	def checkEachSelectorIsUsedOnlyOnce(BCCEntrySelectorBody entryBody) {
		var Map<Class<?>, Integer> selectors = new HashMap<Class<?>, Integer>();
		
		var int i = 0
		for (BCCAbstractSelector selector : entryBody.selectors) {
			if (!selectors.containsKey(selector.class)) {
				selectors.put(selector.class, i)
			}
			else {
				if (bodyRepresentsAppliesToBody(entryBody)) {
					error("This selector was previously used. Each selector can only be used once per applies to rule.", selector.eContainingFeature, i, EACH_SELECTOR_ONLY_EXISTS_ONCE)
				}
				else if (bodyRepresentsExceptForBody(entryBody)) {
					error("This selector was previously used. Each selector can only be used once per except for rule.", selector.eContainingFeature, i, EACH_SELECTOR_ONLY_EXISTS_ONCE)
				}
			}
			i++;
		}
	}

	@Check
	def checkEachExcludedKeyIsUsedOnlyOnce(BCCExcludedEntryKeysBody entryBody) {
		var Map<String, Integer> selectors = new HashMap<String, Integer>();
		
		var int i = 0
		for (BCCEntryKeyReference reference : entryBody.excludedEntryKeys) {
			if (!selectors.containsKey(reference.entryKey)) {
				selectors.put(reference.entryKey, i)
			}
			else {
				warning("This entry key was previously excluded.", reference.eContainingFeature, i, EACH_EXCLUDED_KEY_ONLY_EXISTS_ONCE)
			}
			i++;
		}
	}
	
	@Check
	def checkExceptForDoesNotUseAllSelector(BCCEntrySelectorBody entryBody) {
		if (bodyRepresentsExceptForBody(entryBody)) {
			if (appliesToAllEntryExists(entryBody)) {
				var int i = 0
				for (BCCAbstractSelector selector : entryBody.selectors) {
					if (selectorIsApplyToAllSelector(selector)) {
						error("This selector cannot be used for except for rules. Otherwise, the specified rule will never be applied to any BibTeX entries.", selector.eContainingFeature, i, ALL_SELECTOR_USED_IN_EXCEPT_FOR_RULE)
					}
					i++;
				}
			}
		}
	}
	
	@Check
	def checkExceptForBodyDoesNotSpecifySameSelectorsAsAppliesToBody(BCCEntrySelectorBody exceptForEntryBody) {
		if (bodyRepresentsExceptForBody(exceptForEntryBody)) {
			var BCCRuleBody ruleBody = exceptForEntryBody.eContainer as BCCRuleBody
			var BCCEntrySelectorBody appliesToEntryBody = ruleBody.appliesToBody
			
			checkExceptForBodySpecifiesSameSelectorAsAppliesToBody(exceptForEntryBody, appliesToEntryBody)
		}
	}
	
	protected def checkExceptForBodySpecifiesSameSelectorAsAppliesToBody(BCCEntrySelectorBody exceptForEntryBody, BCCEntrySelectorBody appliesToEntryBody) {
		var List<BCCAbstractSelector> appliesToSelectors = identifySelectors(appliesToEntryBody)
		
		var int i = 0
		for (BCCAbstractSelector selector : exceptForEntryBody.selectors) {
			if (sameSelectorContained(appliesToSelectors, selector)) {
				warning("This selector overrides the same applies to selector. Thus, the applies to selector will have no effect.", selector.eContainingFeature, i, EXCEPT_FOR_SPECIFIES_SAME_SELECTORS_AS_APPLIES_TO)
			}
			i++
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
	
	protected def identifySelectors(BCCEntrySelectorBody entryBody) {
		if (entryBody !== null) {
			return entryBody.selectors
		}
		else {
			return new ArrayList<BCCAbstractSelector>()
		}
	}
	
	protected def appliesToAllEntryExists(BCCEntrySelectorBody entryBody) {
		for (BCCAbstractSelector selector : entryBody.selectors) {
			if (selector instanceof BCCAllSelector) {
				return true
			}
		}
		
		false
	}
	
	protected def bodyRepresentsAppliesToBody(BCCEntrySelectorBody body) {
		var BCCRuleBody ruleBody = body.eContainer as BCCRuleBody
		ruleBody.appliesToBody.equals(body)
	}
	
	protected def bodyRepresentsExceptForBody(BCCEntrySelectorBody body) {
		var BCCRuleBody ruleBody = body.eContainer as BCCRuleBody
		ruleBody.exceptForBody.equals(body)
	}
	
	protected def selectorIsApplyToAllSelector(BCCAbstractSelector selector) {
		selector instanceof BCCAllSelector
	}
	
}
