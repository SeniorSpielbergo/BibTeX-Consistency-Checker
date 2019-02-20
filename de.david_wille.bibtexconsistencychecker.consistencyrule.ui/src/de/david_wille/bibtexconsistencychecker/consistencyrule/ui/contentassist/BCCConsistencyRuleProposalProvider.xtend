package de.david_wille.bibtexconsistencychecker.consistencyrule.ui.contentassist

import de.david_wille.bibtexconsistencychecker.consistencyrule.services.BCCConsistencyRuleGrammarAccess
import javax.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.Group
import org.eclipse.xtext.Keyword
import org.eclipse.xtext.RuleCall
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor

/**
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#content-assist
 * on how to customize the content assistant.
 */
class BCCConsistencyRuleProposalProvider extends AbstractBCCConsistencyRuleProposalProvider {
	@Inject extension BCCConsistencyRuleGrammarAccess
	
	private static val String SEPARATOR = " "
	
	override complete_ConsistencyRuleKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		consistencyRuleKeywordAccess.group.createKeywordProposalWithTrailingSeparator(context, acceptor)
	}
	
	override complete_AppliesToKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		appliesToKeywordAccess.group.createKeywordProposalWithTrailingSeparator(context, acceptor)
	}
	
	override complete_ChecksThatKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		checksThatKeywordAccess.group.createKeywordProposalWithTrailingSeparator(context, acceptor)
	}
	
	override complete_ExcludedEntryKeysKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		excludedEntryKeysKeywordAccess.group.createKeywordProposalWithTrailingSeparator(context, acceptor)
	}
	
	override complete_ExceptForKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		exceptForKeywordAccess.group.createKeywordProposalWithTrailingSeparator(context, acceptor)
	}
	
	override complete_FieldExistsKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		fieldExistsKeywordAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_FieldsExistKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		fieldsExistKeywordAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_UsesReplacePatternKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		usesReplacePatternKeywordAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_UseReplacePatternKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		useReplacePatternKeywordAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCUsesReplacePatternExpression(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCUsesReplacePatternExpressionAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	def createKeywordProposalWithTrailingSeparator(Group group, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		if (group === null) {
			return null
		}
		val proposalString = group.elements.filter(Keyword).map[value].join(SEPARATOR) + SEPARATOR
		acceptor.accept(createCompletionProposal(proposalString, proposalString, null, context))
	}
	
	def createKeywordProposalWithoutTrailingSeparator(Group group, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		if (group === null) {
			return null
		}
		val proposalString = group.elements.filter(Keyword).map[value].join(SEPARATOR)
		acceptor.accept(createCompletionProposal(proposalString, proposalString, null, context))
	}
	
}
