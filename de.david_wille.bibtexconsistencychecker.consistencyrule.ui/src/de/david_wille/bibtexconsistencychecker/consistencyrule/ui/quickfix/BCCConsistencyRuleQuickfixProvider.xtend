package de.david_wille.bibtexconsistencychecker.consistencyrule.ui.quickfix

import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCFieldExistsExpression
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCFieldsExistExpression
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCMultiFieldSelectionExpression
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCNoKeywordExpression
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCSingleFieldSelectionExpression
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCUseReplacePatternExpression
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCUsesReplacePatternExpression
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.Expression
import de.david_wille.bibtexconsistencychecker.consistencyrule.validation.BCCConsistencyRuleValidator
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.ui.editor.model.IXtextDocument
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext
import org.eclipse.xtext.ui.editor.model.edit.ISemanticModification
import org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider
import org.eclipse.xtext.ui.editor.quickfix.Fix
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor
import org.eclipse.xtext.validation.Issue

/**
 * Custom quickfixes.
 *
 * See https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#quick-fixes
 */
class BCCConsistencyRuleQuickfixProvider extends DefaultQuickfixProvider {
	
	@Fix(BCCConsistencyRuleValidator.USES_CORRECT_KEYWORD_COMBINATIONS)
	def replaceWithCorrectKeyword(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, 'Replace with correct keyword', 'Replace with correct keyword.', '',
			new ISemanticModification() {
				override apply(EObject element, IModificationContext context) throws Exception {
					var IXtextDocument xtextDocument = context.xtextDocument
					var String keyword = identifyCorrectKeyword(element)
					xtextDocument.replace(issue.offset, issue.length, keyword);
				}
	
				def identifyCorrectKeyword(EObject element) {
					var BCCNoKeywordExpression parentExpression = element as BCCNoKeywordExpression
					var Expression leftExpression = parentExpression.left
					var Expression rightExpression = parentExpression.right
					
					if (leftExpression instanceof BCCSingleFieldSelectionExpression) {
						if (rightExpression instanceof BCCFieldsExistExpression) {
							"field exists"
						}
						else if (rightExpression instanceof BCCUseReplacePatternExpression) {
							"uses replace pattern"
						}
					}
					else if (leftExpression instanceof BCCMultiFieldSelectionExpression) {
						if (rightExpression instanceof BCCFieldExistsExpression) {
							"fields exist"
						}
						else if (rightExpression instanceof BCCUsesReplacePatternExpression) {
							"use replace pattern"
						}
					}
				}
	
		    }
		);
	}
	
}
