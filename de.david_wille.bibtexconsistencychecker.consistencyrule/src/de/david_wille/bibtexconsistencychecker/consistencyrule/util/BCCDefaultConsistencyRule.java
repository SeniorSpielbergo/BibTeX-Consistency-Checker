package de.david_wille.bibtexconsistencychecker.consistencyrule.util;

import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAbstractSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRule;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRuleFactory;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCEntrySelectorBody;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCRuleBody;

public class BCCDefaultConsistencyRule {
	
	public static BCCConsistencyRule generate(String name) {
		BCCConsistencyRuleFactory consistencyRuleFactory = BCCConsistencyRuleFactory.eINSTANCE;
		
		BCCConsistencyRule consistencyRule = consistencyRuleFactory.createBCCConsistencyRule();
		
		BCCRuleBody ruleBody = generateRuleBody(consistencyRuleFactory, name);
		consistencyRule.setRuleBody(ruleBody);
		
		return consistencyRule;
	}
	
	public static BCCRuleBody generateRuleBody(BCCConsistencyRuleFactory consistencyRuleFactory, String name) {
		BCCRuleBody ruleBody = consistencyRuleFactory.createBCCRuleBody();
		
		ruleBody.setName(name);
		
		BCCEntrySelectorBody selectorBody = generateEntrySelectorBody(consistencyRuleFactory);
		ruleBody.setAppliesToBody(selectorBody);
		
		return ruleBody;
	}
	
	public static BCCEntrySelectorBody generateEntrySelectorBody(BCCConsistencyRuleFactory consistencyRuleFactory) {
		BCCEntrySelectorBody selectorBody = consistencyRuleFactory.createBCCEntrySelectorBody();
		
		BCCAbstractSelector selector = generateAbstractSelector(consistencyRuleFactory);
		selectorBody.getSelectors().add(selector);
		
		return selectorBody;
	}

	private static BCCAbstractSelector generateAbstractSelector(BCCConsistencyRuleFactory consistencyRuleFactory) {
		return consistencyRuleFactory.createBCCAllSelector();
	}

}
