package de.david_wille.bibtexconsistencychecker.bibtex.ui.contentassist

import de.david_wille.bibtexconsistencychecker.bibtex.BCCBibTeXStandaloneSetup
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractGenericField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplacePatternEntry
import de.david_wille.bibtexconsistencychecker.bibtex.util.BCCBibTeXUtil
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil
import java.util.ArrayList
import java.util.List
import org.eclipse.core.resources.IContainer
import org.eclipse.core.resources.IFile
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.RuleCall
import org.eclipse.xtext.nodemodel.util.NodeModelUtils
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor

/**
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#content-assist
 * on how to customize the content assistant.
 */
class BCCBibTeXProposalProvider extends AbstractBCCBibTeXProposalProvider {
	
	override complete_BCCGenericFieldValue(EObject model, RuleCall ruleCall, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		var EObject contextNode = NodeModelUtils.findActualSemanticObjectFor(context.currentNode)
		if (contextNode instanceof BCCAbstractGenericField) {
			if (BCCBibTeXUtil.usesReplacePattern(contextNode)) {
				var String fieldValue = contextNode.fieldValue
				var List<BCCReplacePatternEntry> relevantReplacePattern = identifyAllReplacePatternContainingValue(model, fieldValue)
				
				for (BCCReplacePatternEntry replacePattern : relevantReplacePattern) {
					val proposalString = replacePattern.replaceKey
					acceptor.accept(createCompletionProposal(proposalString, proposalString, null, context))
				}
			}
		}
	}
	
	def identifyAllReplacePatternContainingValue(EObject model, String fieldValue) {
		var IFile modelFile = BCCResourceUtil.getIFile(model)
		var IContainer container = modelFile.parent
		
		var List<IFile> allBibTeXFiles = BCCResourceUtil.collectAllFilesInContainer(container, "bib")
		
		var List<BCCBibTeXFile> parsedBibTeXFiles = BCCResourceUtil.parseModels(new BCCBibTeXStandaloneSetup(), allBibTeXFiles)
		
		var List<BCCReplacePatternEntry> allReplacePattern = BCCBibTeXUtil.collectAllReplacePattern(parsedBibTeXFiles)
		
		var List<BCCReplacePatternEntry> relevantReplacePattern = new ArrayList<BCCReplacePatternEntry>()
		
		for (BCCReplacePatternEntry replacePattern : allReplacePattern) {
			if (replacePattern.replaceKey.contains(fieldValue)) {
				relevantReplacePattern.add(replacePattern)
			}
		}
		
		relevantReplacePattern
	}
	
}
