package de.david_wille.bibtexconsistencychecker.consistencyrule.formatting2

import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRule
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRulePackage
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCRuleBody
import org.eclipse.xtext.formatting2.AbstractFormatter2
import org.eclipse.xtext.formatting2.IFormattableDocument
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegion

class BCCConsistencyRuleFormatter extends AbstractFormatter2 {
	
	def dispatch void format(BCCConsistencyRule bCCConsistencyRule, extension IFormattableDocument document) {
		var ISemanticRegion ruleConsistencyRuleStart = bCCConsistencyRule.regionFor.keyword("{")
		ruleConsistencyRuleStart.append[newLine]
		var ISemanticRegion ruleConsistencyRuleEnd = bCCConsistencyRule.regionFor.keyword("}")
		ruleConsistencyRuleEnd.prepend[newLine]
		ruleConsistencyRuleEnd.append[newLine]
		
		interior(ruleConsistencyRuleStart, ruleConsistencyRuleEnd)[indent]
		
		bCCConsistencyRule.getRuleBody.format;
	}

	def dispatch void format(BCCRuleBody bCCRuleBody, extension IFormattableDocument document) {
		bCCRuleBody.regionFor.feature(BCCConsistencyRulePackage.Literals.BCC_RULE_BODY__NAME).append[newLine]
	}
	
}
