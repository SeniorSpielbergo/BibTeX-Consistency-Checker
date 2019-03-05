package de.david_wille.bibtexconsistencychecker.consistencyrule.ui.quickfix

import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAbstractSelector
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCEntryKeyReference
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCEntrySelectorBody
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCExcludedEntryKeysBody
import de.david_wille.bibtexconsistencychecker.consistencyrule.validation.BCCConsistencyRuleValidator
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil
import de.david_wille.bibtexconsistencychecker.util.BCCUtil
import java.util.List
import java.util.regex.Matcher
import java.util.regex.Pattern
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IFolder
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.IWorkspace
import org.eclipse.core.resources.IWorkspaceRoot
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.IPath
import org.eclipse.core.runtime.Path
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.resource.EObjectAtOffsetHelper
import org.eclipse.xtext.resource.XtextResource
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
	
	@Fix(BCCConsistencyRuleValidator.EACH_SELECTOR_ONLY_EXISTS_ONCE)
	@Fix(BCCConsistencyRuleValidator.EACH_EXCLUDED_KEY_ONLY_EXISTS_ONCE)
	def removeDuplicateSelector(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, 'Remove duplicate entry', 'Remove duplicate entry.', '',
			new ISemanticModification() {
				override apply(EObject element, IModificationContext context) throws Exception {
					var IXtextDocument xtextDocument = context.xtextDocument
					
					var int issueOffset = issue.offset
					var int issueLength = issue.length
					var String stringToBeRemoved = xtextDocument.get(issueOffset, issueLength)
					
					while (!stringToBeRemoved.startsWith(",")) {
						issueOffset--
						issueLength++
						stringToBeRemoved = xtextDocument.get(issueOffset, issueLength)
					}
					
					xtextDocument.replace(issueOffset, issueLength, "")
				}
		    }
		);
	}
	
	@Fix(BCCConsistencyRuleValidator.ALL_SELECTOR_USED_IN_EXCEPT_FOR_RULE)
	@Fix(BCCConsistencyRuleValidator.EXCEPT_FOR_SPECIFIES_SAME_SELECTORS_AS_APPLIES_TO)
	@Fix(BCCConsistencyRuleValidator.APPLIES_TO_ALL_SELECTOR_COMBINED_WITH_OTHER_SELECTORS)
	@Fix(BCCConsistencyRuleValidator.EXCLUDED_KEY_NOT_FOUND)
	def removeSelectorFromRule(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, 'Remove selector', 'Remove selector.', '',
			new ISemanticModification() {
				override apply(EObject element, IModificationContext context) throws Exception {
					if (element instanceof BCCEntrySelectorBody) {
						if (onlySingleElementContained(element.selectors)) {
							removeCompleteExpression(context, element)
						}
						else {
							var EObjectAtOffsetHelper helper = new EObjectAtOffsetHelper()
							var BCCAbstractSelector selector = helper.resolveElementAt(element.eResource as XtextResource, issue.offset) as BCCAbstractSelector
							
							if (selectorIsFirstEntry(selector, element)) {
								removeElementIncludingTrailingComma(context)
							}
							else {
								removeElementIncludingPrecedingComma(context)
							}
						}
					}
					else if (element instanceof BCCExcludedEntryKeysBody) {
						if (onlySingleElementContained(element.excludedEntryKeys)) {
							removeCompleteExpression(context, element)
						}
						else {
							var EObjectAtOffsetHelper helper = new EObjectAtOffsetHelper()
							var BCCEntryKeyReference keyReference = helper.resolveElementAt(element.eResource as XtextResource, issue.offset) as BCCEntryKeyReference
							
							if (excludedKeyIsFirstEntry(keyReference, element)) {
								removeElementIncludingTrailingComma(context)
							}
							else {
								removeElementIncludingPrecedingComma(context)
							}
						}
					}
				}
				
				def removeCompleteExpression(IModificationContext context, EObject element) {
					var IXtextDocument xtextDocument = context.xtextDocument
					var int issueOffset = issue.offset
					var int issueLength = issue.length
					var String stringToBeRemoved = xtextDocument.get(issueOffset, issueLength)
					
					var Pattern pattern = null
					if (element instanceof BCCEntrySelectorBody) {
						pattern = Pattern.compile("except\\s+for.*")
					}
					else if (element instanceof BCCExcludedEntryKeysBody) {
						pattern = Pattern.compile("excluded\\s+entry\\s+keys.*")
					}
					var Matcher matcher = pattern.matcher(stringToBeRemoved)
					
					while (!matcher.lookingAt) {
						issueOffset--
						issueLength++
						stringToBeRemoved = xtextDocument.get(issueOffset, issueLength)
						matcher = pattern.matcher(stringToBeRemoved)
					}
					
					xtextDocument.replace(issueOffset, issueLength, "")
				}
	
				def removeElementIncludingTrailingComma(IModificationContext context) {
					var IXtextDocument xtextDocument = context.xtextDocument
					var int issueOffset = issue.offset
					var int issueLength = issue.length
					var String stringToBeRemoved = xtextDocument.get(issueOffset, issueLength)
					
					while (!stringToBeRemoved.endsWith(",")) {
						issueLength++
						stringToBeRemoved = xtextDocument.get(issueOffset, issueLength)
					}
					
					xtextDocument.replace(issueOffset, issueLength, "")
				}
	
				def removeElementIncludingPrecedingComma(IModificationContext context) {
					var IXtextDocument xtextDocument = context.xtextDocument
					var int issueOffset = issue.offset
					var int issueLength = issue.length
					var String stringToBeRemoved = xtextDocument.get(issueOffset, issueLength)
					
					while (!stringToBeRemoved.startsWith(",")) {
						issueOffset--
						issueLength++
						stringToBeRemoved = xtextDocument.get(issueOffset, issueLength)
					}
					
					xtextDocument.replace(issueOffset, issueLength, "")
				}
	
				def onlySingleElementContained(List<? extends EObject> list) {
					list.size == 1
				}
	
				def selectorIsFirstEntry(BCCAbstractSelector selector, BCCEntrySelectorBody body) {
					body.selectors.get(0).equals(selector)
				}
				
				def excludedKeyIsFirstEntry(BCCEntryKeyReference keyReference, BCCExcludedEntryKeysBody body) {
					body.excludedEntryKeys.get(0).equals(keyReference)
				}
		    }
		);
	}
	
	@Fix(BCCConsistencyRuleValidator.FILE_NOT_IN_CORRECT_FOLDER)
	def moveFileToCorrectFolder(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, 'Move file to correct folder', 'Move file to correct folder.', '',
			new ISemanticModification() {
				override apply(EObject element, IModificationContext context) throws Exception {
					var IFile file = BCCResourceUtil.getIFile(element)
					var IProject project = file.project
					
					var IFolder folder = identifyFolder(project, "rules")
					if (folder !== null) {
						var IPath newFilePath = new Path(folder.fullPath + "/" + file.name)
						
						var IWorkspace workspace = ResourcesPlugin.getWorkspace();
						var IWorkspaceRoot root = workspace.getRoot();
						var IResource resource = root.findMember(newFilePath);
						
						if (resource === null) {
							file.move(newFilePath, false, null)
						}
						else {
							BCCUtil.openErrorDialog("The could not be moved, because another file with this name already exists!")
						}
					}
				}
	
				def identifyFolder(IProject project, String folderName) {
					for (IResource resource : BCCResourceUtil.getChildResources(project)) {
						if (resource instanceof IFolder) {
							if (resource.name.equals(folderName)) {
								return resource
							}
						}
					}
					
					return null
				}
	
			}
		);
	}
	
}
