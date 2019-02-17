package de.david_wille.bibtexconsistencychecker.executionmodel.ui.contentassist

import de.david_wille.bibtexconsistencychecker.executionmodel.services.BCCExecutionModelGrammarAccess
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil
import javax.inject.Inject
import org.eclipse.core.resources.IContainer
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IResource
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
	
	private static val String SEPARATOR = " "
	private static val String BCC_RULE_FILE_ENDING = "bcc_rule"
	
	override complete_EnsureAlphabeticOrderKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		ensureAlphabeticOrderKeywordAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_EnsureShortHarvardStyleKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		ensureShortHarvardStyleKeywordAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_ConsistencyRulesKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		consistencyRulesKeywordAccess.group.createKeywordProposalWithTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCRulePath(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		var IFile modelFile = BCCResourceUtil.getIFile(model)
		var IContainer parentResource = modelFile.parent
		
		for (IResource childResource : parentResource.members) {
			if (BCCResourceUtil.resourceIsFile(childResource)) {
				var IFile childFile = childResource as IFile
				
				if (childFile.fileExtension.equals(BCC_RULE_FILE_ENDING)) {
					val proposalString = "\"" + childFile.name + "\""
					acceptor.accept(createCompletionProposal(proposalString, proposalString, null, context))
				}
			}
		}
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
