package de.david_wille.bibtexconsistencychecker.executionmodel.ui.contentassist

import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCBibTeXFileEntry
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCBibTeXFilesEntry
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCBibTeXPath
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCConsistencyRuleEntry
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCConsistencyRulesEntry
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCRulePath
import de.david_wille.bibtexconsistencychecker.executionmodel.services.BCCExecutionModelGrammarAccess
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil
import java.util.ArrayList
import java.util.List
import javax.inject.Inject
import org.eclipse.core.resources.IContainer
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IResource
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
class BCCExecutionModelProposalProvider extends AbstractBCCExecutionModelProposalProvider {
	@Inject extension BCCExecutionModelGrammarAccess
	
	private static val String SEPARATOR = " "
	private static val String BCC_RULE_FILE_ENDING = "bcc_rule"
	private static val String BIB_FILE_ENDING = "bib"
	
	override complete_BCCAlphabeticOrderKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCAlphabeticOrderKeywordAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCShortHarvardStyleKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCShortHarvardStyleKeywordAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCReplacePatternExistKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCReplacePatternExistKeywordAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCNoConflictingEntryKeysExistKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCNoConflictingEntryKeysExistKeywordAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCNoContradictingReplacePatternExistKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCNoContradictingReplacePatternExistKeywordAccess.group.createKeywordProposalWithoutTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCConsistencyRulesKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCConsistencyRulesKeywordAccess.group.createKeywordProposalWithTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCBibTeXFilesKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCBibTeXFilesKeywordAccess.group.createKeywordProposalWithTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCRulePath(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		var IFile modelFile = BCCResourceUtil.getIFile(model)
		var IContainer parentResource = modelFile.parent
		
		var EObject contextNode = NodeModelUtils.findActualSemanticObjectFor(context.currentNode)
		if (contextNode instanceof BCCConsistencyRulesEntry) {
			var BCCConsistencyRulesEntry rulesEntry = contextNode as BCCConsistencyRulesEntry
			var List<String> existingConsistencyRuleSelections = collectExistingConsistencyRuleSelections(rulesEntry)
			
			for (IResource childResource : parentResource.members) {
				if (BCCResourceUtil.resourceIsFile(childResource)) {
					var IFile childFile = childResource as IFile
					
					if (childFile.fileExtension.equals(BCC_RULE_FILE_ENDING)) {
						var String childName = childFile.name
						if (!existingConsistencyRuleSelections.contains(childName)) {
							val proposalString = childName
							acceptor.accept(createCompletionProposal(proposalString, proposalString, null, context))
						}
					}
				}
			}
		}
	}
	
	def collectExistingConsistencyRuleSelections(BCCConsistencyRulesEntry rulesEntry) {
		var List<String> existingConsistencyRuleSelections = new ArrayList<String>()
		for (BCCConsistencyRuleEntry ruleEntry : rulesEntry.consistencyRuleEntries) {
			var BCCRulePath rulePath = ruleEntry.rulePath
			if (rulePath !== null && rulePath.path !== null) {
				existingConsistencyRuleSelections.add(rulePath.path)
			}
		}
		existingConsistencyRuleSelections
	}
	
	override complete_BCCBibTeXPath(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		var IFile modelFile = BCCResourceUtil.getIFile(model)
		var IContainer parentResource = modelFile.parent
		
		var EObject contextNode = NodeModelUtils.findActualSemanticObjectFor(context.currentNode)
		if (contextNode instanceof BCCBibTeXFilesEntry) {
			var BCCBibTeXFilesEntry filesEntry = contextNode as BCCBibTeXFilesEntry
			var List<String> existingBibTeXFileSelections = collectExistingConsistencyRuleSelections(filesEntry)
			
			for (IResource childResource : parentResource.members) {
				if (BCCResourceUtil.resourceIsFile(childResource)) {
					var IFile childFile = childResource as IFile
					
					if (childFile.fileExtension.equals(BIB_FILE_ENDING)) {
						var String childName = childFile.name
						if (!existingBibTeXFileSelections.contains(childName)) {
							val proposalString = childName
							acceptor.accept(createCompletionProposal(proposalString, proposalString, null, context))
						}
					}
				}
			}
		}
	}
	
	def collectExistingConsistencyRuleSelections(BCCBibTeXFilesEntry filesEntry) {
		var List<String> existingConsistencyRuleSelections = new ArrayList<String>()
		for (BCCBibTeXFileEntry fileEntry : filesEntry.bibTeXFileEntries) {
			var BCCBibTeXPath filePath = fileEntry.filePath
			if (filePath !== null && filePath.path !== null) {
				existingConsistencyRuleSelections.add(filePath.path)
			}
		}
		existingConsistencyRuleSelections
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
