package de.david_wille.bibtexconsistencychecker.executionmodel.ui.contentassist

import de.david_wille.bibtexconsistencychecker.executionmodel.services.BCCExecutionModelGrammarAccess
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
class BCCExecutionModelProposalProvider extends AbstractBCCExecutionModelProposalProvider {
	@Inject extension BCCExecutionModelGrammarAccess
	
	private static String SEPARATOR = " "
	
	override complete_EnsureAlphabeticOrderKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		ensureAlphabeticOrderKeywordAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_EnsureShortHarvardStyle(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		ensureShortHarvardStyleAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
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
