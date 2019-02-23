package de.david_wille.bibtexconsistencychecker.consistencyrule.ui.outline

import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRule
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRulePackage
import org.eclipse.swt.graphics.Image
import org.eclipse.xtext.ui.editor.outline.IOutlineNode
import org.eclipse.xtext.ui.editor.outline.impl.DefaultOutlineTreeProvider
import org.eclipse.xtext.ui.editor.outline.impl.DocumentRootNode
import org.eclipse.xtext.ui.editor.outline.impl.EObjectNode

/**
 * Customization of the default outline structure.
 *
 * See https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#outline
 */
class BCCConsistencyRuleOutlineTreeProvider extends DefaultOutlineTreeProvider {
	
	protected def _createNode(DocumentRootNode parentNode, BCCConsistencyRule rule) {
		var String stringRepresentation = "Consistency Rule \"" + rule.ruleBody.name + "\""
		var Image image = imageDispatcher.invoke(rule)
		var IOutlineNode childNode = new EObjectNode(rule, parentNode, image, stringRepresentation, false)
		createChildren(childNode, rule)
		
		childNode
	}
	
	protected def _createChildren(IOutlineNode parentNode, BCCConsistencyRule rule) {
		var Image image = imageDispatcher.invoke(rule)
		createEStructuralFeatureNode(parentNode, rule.ruleBody, BCCConsistencyRulePackage.Literals.BCC_RULE_BODY__APPLIES_TO_BODY,
					image, "applies to", true);
		
		if (rule.ruleBody.excludedEntryKeysBody !== null) {
			createEStructuralFeatureNode(parentNode, rule.ruleBody, BCCConsistencyRulePackage.Literals.BCC_RULE_BODY__EXCLUDED_ENTRY_KEYS_BODY,
						image, "excluded entry keys", true);
		}
		
		createEStructuralFeatureNode(parentNode, rule.ruleBody, BCCConsistencyRulePackage.Literals.BCC_RULE_BODY__IF_THEN_ELSE_EXPRESSIONS,
						image, "rule description", true);
	}

}
