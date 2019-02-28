package de.david_wille.bibtexconsistencychecker.consistencyrule.ui.contentassist

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry
import de.david_wille.bibtexconsistencychecker.bibtex.cache.BCCBibTeXCache
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCEntryKeyReference
import de.david_wille.bibtexconsistencychecker.consistencyrule.services.BCCConsistencyRuleGrammarAccess
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil
import java.util.ArrayList
import java.util.List
import javax.inject.Inject
import org.eclipse.core.resources.IProject
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.Group
import org.eclipse.xtext.Keyword
import org.eclipse.xtext.RuleCall
import org.eclipse.xtext.nodemodel.util.NodeModelUtils
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor

/**
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#content-assist
 * on how to customize the content assistant.
 */
class BCCConsistencyRuleProposalProvider extends AbstractBCCConsistencyRuleProposalProvider {
	@Inject extension BCCConsistencyRuleGrammarAccess
	
	private static val String SEPARATOR = " "
	
	override complete_BCCFieldExistsExpression(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCFieldExistsExpressionAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCFieldsExistExpression(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCFieldsExistExpressionAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCUsesReplacePatternExpression(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCUsesReplacePatternExpressionAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCUseReplacePatternExpression(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCUseReplacePatternExpressionAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCIsIntegerExpression(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCIsIntegerExpressionAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCAreIntegerExpression(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCAreIntegerExpressionAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCEntryKeyReference(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		var EObject contextNode = NodeModelUtils.findActualSemanticObjectFor(context.currentNode)
		if (contextNode instanceof BCCEntryKeyReference) {
			var String fieldValue = contextNode.entryKey
			var List<BCCAbstractBibTeXEntry> relevantBibTeXEntries = identifyAllBibTeXEntryKeysContainingValue(model, fieldValue)
			
			for (BCCAbstractBibTeXEntry relevantBibTeXEntry : relevantBibTeXEntries) {
				val proposalString = relevantBibTeXEntry.entryBody.entryKeyObject.entryKey
				acceptor.accept(createCompletionProposal(proposalString, proposalString, null, context))
			}
		}
	}
	
	protected def identifyAllBibTeXEntryKeysContainingValue(EObject model, String fieldValue) {
		var IProject modelProject = BCCResourceUtil.getIProject(model)
		
		var List<BCCAbstractBibTeXEntry> allBibTeXEntries = BCCBibTeXCache.instance.getEntries(modelProject);
		
		var List<BCCAbstractBibTeXEntry> relevantBibTeXEntries = new ArrayList<BCCAbstractBibTeXEntry>()
		
		for (BCCAbstractBibTeXEntry bibTeXEntry : allBibTeXEntries) {
			if (bibTeXEntry.entryBody.entryKeyObject.entryKey.contains(fieldValue)) {
				relevantBibTeXEntries.add(bibTeXEntry)
			}
		}
		
		relevantBibTeXEntries
	}
	
	protected def createKeywordProposalWithTrailingSeparator(Group group, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		if (group === null) {
			return null
		}
		val proposalString = group.elements.filter(Keyword).map[value].join(SEPARATOR) + SEPARATOR
		acceptor.accept(createCompletionProposal(proposalString, proposalString, null, context))
	}
	
	protected def createKeywordProposalWithoutTrailingSeparator(Group group, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		if (group === null) {
			return null
		}
		val proposalString = group.elements.filter(Keyword).map[value].join(SEPARATOR)
		acceptor.accept(createCompletionProposal(proposalString, proposalString, null, context))
	}
	
}
