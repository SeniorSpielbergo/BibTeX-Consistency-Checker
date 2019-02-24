package de.david_wille.bibtexconsistencychecker.consistencyrule.ui.contentassist

import de.david_wille.bibtexconsistencychecker.bibtex.BCCBibTeXStandaloneSetup
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile
import de.david_wille.bibtexconsistencychecker.bibtex.util.BCCBibTeXUtil
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCEntryKeyObject
import de.david_wille.bibtexconsistencychecker.consistencyrule.services.BCCConsistencyRuleGrammarAccess
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil
import java.util.ArrayList
import java.util.List
import javax.inject.Inject
import org.eclipse.core.resources.IContainer
import org.eclipse.core.resources.IFile
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
	
	override complete_BCCConsistencyRuleKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCConsistencyRuleKeywordAccess.group.createKeywordProposalWithTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCAppliesToKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCAppliesToKeywordAccess.group.createKeywordProposalWithTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCExcludedEntryKeysKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCExcludedEntryKeysKeywordAccess.group.createKeywordProposalWithTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCExceptForKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCExceptForKeywordAccess.group.createKeywordProposalWithTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCFieldExistsKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCFieldExistsKeywordAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCFieldsExistKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCFieldsExistKeywordAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCUsesReplacePatternKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCUsesReplacePatternKeywordAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCUseReplacePatternKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCUseReplacePatternKeywordAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCIsIntegerKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCIsIntegerKeywordAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCAreIntegerKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCAreIntegerKeywordAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCUsesReplacePatternExpression(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCUsesReplacePatternExpressionAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCEntryKeyObject(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		var EObject contextNode = NodeModelUtils.findActualSemanticObjectFor(context.currentNode)
		if (contextNode instanceof BCCEntryKeyObject) {
			var String fieldValue = contextNode.entryKey
			var List<BCCAbstractBibTeXEntry> relevantBibTeXEntries = identifyAllBibTeXEntryKeysContainingValue(model, fieldValue)
			
			for (BCCAbstractBibTeXEntry relevantBibTeXEntry : relevantBibTeXEntries) {
				val proposalString = relevantBibTeXEntry.entryBody.entryKey
				acceptor.accept(createCompletionProposal(proposalString, proposalString, null, context))
			}
		}
	}
	
	protected def identifyAllBibTeXEntryKeysContainingValue(EObject model, String fieldValue) {
		var IFile modelFile = BCCResourceUtil.getIFile(model)
		var IContainer container = modelFile.parent
		
		var List<IFile> allBibTeXFiles = BCCResourceUtil.collectAllFilesInContainer(container, "bib")
		
		var List<BCCBibTeXFile> parsedBibTeXFiles = BCCResourceUtil.parseModels(new BCCBibTeXStandaloneSetup(), allBibTeXFiles)
		
		var List<BCCAbstractBibTeXEntry> allBibTeXEntries = BCCBibTeXUtil.collectAllAbstractBibTeXEntries(parsedBibTeXFiles)
		
		var List<BCCAbstractBibTeXEntry> relevantBibTeXEntries = new ArrayList<BCCAbstractBibTeXEntry>()
		
		for (BCCAbstractBibTeXEntry bibTeXEntry : allBibTeXEntries) {
			if (bibTeXEntry.entryBody.entryKey.contains(fieldValue)) {
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
