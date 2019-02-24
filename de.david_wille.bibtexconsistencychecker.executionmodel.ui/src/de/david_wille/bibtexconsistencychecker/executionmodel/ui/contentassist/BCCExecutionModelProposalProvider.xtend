package de.david_wille.bibtexconsistencychecker.executionmodel.ui.contentassist

import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCBibTeXFilesEntry
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCConsistencyRulesEntry
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCFilePathEntry
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
	
	override complete_BCCConsistencyRulesKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCConsistencyRulesKeywordAccess.group.createKeywordProposalWithTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCBibTeXFilesKeyword(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		BCCBibTeXFilesKeywordAccess.group.createKeywordProposalWithTrailingSeparator(context, acceptor)
	}
	
	override complete_BCCValidPath(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		var IFile modelFile = BCCResourceUtil.getIFile(model)
		var IContainer parentResource = modelFile.parent
		
		var EObject contextNode = NodeModelUtils.findActualSemanticObjectFor(context.currentNode)
		if (isBibTeXFilesEntryContext(contextNode)) {
			var List<IResource> allFoundSubResources = collectAllSubResources(parentResource, BIB_FILE_ENDING)
			var List<String> existingConsistencyRuleSelections = collectExistingBibTeXFileSelections(contextNode)
			
			for (IResource resource : allFoundSubResources) {
				val String proposalString = getProposalString(resource)
				if (!existingConsistencyRuleSelections.contains(proposalString)) {
					acceptor.accept(createCompletionProposal(proposalString, proposalString, null, context))
				}
			}
		}
		else if (isConsistencyRulesEntryContext(contextNode)) {
			var List<IResource> allFoundSubResources = collectAllSubResources(parentResource, BCC_RULE_FILE_ENDING)
			var List<String> existingConsistencyRuleSelections = collectExistingConsistencyRuleSelections(contextNode)
			
			for (IResource resource : allFoundSubResources) {
				val String proposalString = getProposalString(resource)
				if (!existingConsistencyRuleSelections.contains(proposalString)) {
					acceptor.accept(createCompletionProposal(proposalString, proposalString, null, context))
				}
			}
		}
	}
	
	protected def getProposalString(IResource resource) {
		var String propsalString = resource.projectRelativePath.toPortableString
		
		if (resource instanceof IContainer) {
			propsalString += "/"
		}
		
		propsalString
	}
	
	protected def isBibTeXFilesEntryContext(EObject object) {
		object instanceof BCCBibTeXFilesEntry || object.eContainer instanceof BCCBibTeXFilesEntry
	}
	
	protected def isConsistencyRulesEntryContext(EObject object) {
		object instanceof BCCConsistencyRulesEntry || object.eContainer instanceof BCCConsistencyRulesEntry
	}
	
	protected def List<IResource> collectAllSubResources(IContainer container, String fileExtension) {
		var List<IResource> allFoundSubResources = new ArrayList<IResource>();
		
		var List<IResource> subResources = BCCResourceUtil.getChildResources(container);
		for (IResource subResource : subResources) {
			if (subResource instanceof IContainer) {
				var List<IResource> nextLevelSubResources = collectAllSubResources(subResource, fileExtension);
				if (nextLevelSubResources.size > 0) {
					allFoundSubResources.addAll(nextLevelSubResources);
					allFoundSubResources.add(subResource)
				}
			}
			else if (subResource instanceof IFile && subResource.fileExtension.equals(fileExtension)) {
				allFoundSubResources.add(subResource)
			}
		}
		
		allFoundSubResources
	}
	
	protected def List<String> collectExistingConsistencyRuleSelections(EObject eObject) {
		var BCCConsistencyRulesEntry rulesEntry = null
		if (eObject instanceof BCCConsistencyRulesEntry) {
			rulesEntry = eObject as BCCConsistencyRulesEntry
		}
		else if (eObject.eContainer instanceof BCCConsistencyRulesEntry) {
			rulesEntry = eObject.eContainer as BCCConsistencyRulesEntry
		}
		
		var List<String> existingConsistencyRuleSelections = new ArrayList<String>()
		for (BCCFilePathEntry ruleEntry : rulesEntry.entries) {
			if (ruleEntry.path !== null) {
				existingConsistencyRuleSelections.add(ruleEntry.path)
			}
		}
		existingConsistencyRuleSelections
	}
	
	protected def List<String> collectExistingBibTeXFileSelections(EObject eObject) {
		var BCCBibTeXFilesEntry filesEntry = null
		if (eObject instanceof BCCBibTeXFilesEntry) {
			filesEntry = eObject as BCCBibTeXFilesEntry
		}
		else if (eObject.eContainer instanceof BCCBibTeXFilesEntry) {
			filesEntry = eObject.eContainer as BCCBibTeXFilesEntry
		}
		
		var List<String> existingConsistencyRuleSelections = new ArrayList<String>()
		for (BCCFilePathEntry fileEntry : filesEntry.entries) {
			if (fileEntry.path !== null) {
				existingConsistencyRuleSelections.add(fileEntry.path)
			}
		}
		existingConsistencyRuleSelections
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
