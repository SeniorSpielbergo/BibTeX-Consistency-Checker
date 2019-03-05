package de.david_wille.bibtexconsistencychecker.bibtex.ui.quickfix

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXFileEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEntryBody
import de.david_wille.bibtexconsistencychecker.bibtex.cache.BCCBibTeXCache
import de.david_wille.bibtexconsistencychecker.bibtex.validation.BCCBibTeXValidator
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil
import de.david_wille.bibtexconsistencychecker.util.BCCUtil
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
class BCCBibTeXQuickfixProvider extends DefaultQuickfixProvider {
	
	@Fix(BCCBibTeXValidator.ENTRY_KEY_EXISTS_MULTIPLE_TIMES)
	@Fix(BCCBibTeXValidator.REPLACE_KEY_EXISTS_MULTIPLE_TIMES)
	def removeEntry(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, 'Remove entry', 'Remove entry.', '',
			new ISemanticModification() {
				override apply(EObject element, IModificationContext context) throws Exception {
					if (!singleEntryExistsInFile(element)) {
						if (isFirstEntry(element)) {
							removeEntry(context)
						}
						else if (isLastEntry(element)) {
							removeLastEntry(context)
						}
						else {
							removeEntry(context)
						}
					}
					else {
						removeSingleEntry(element, context)
					}
				}
	
				def removeSingleEntry(EObject element, IModificationContext context) {
					var BCCBibTeXFile bibTeXFile = element.eContainer.eContainer as BCCBibTeXFile
						
					var IXtextDocument xtextDocument = context.xtextDocument
					xtextDocument.replace(0, xtextDocument.length, "")
					
					BCCBibTeXCache.instance.cacheBibTeXFile(BCCResourceUtil.getIProject(bibTeXFile), bibTeXFile)
				}
				
				def removeEntry(IModificationContext context) {
					var IXtextDocument xtextDocument = context.xtextDocument
					
					var int issueOffset = issue.offset
					var int issueLength = issue.length
					var String stringToBeRemoved = xtextDocument.get(issueOffset, issueLength)
					
					while (!stringToBeRemoved.startsWith("@")) {
						issueOffset--
						issueLength++
						stringToBeRemoved = xtextDocument.get(issueOffset, issueLength)
					}
					
					while (!stringToBeRemoved.endsWith("@") && !stringToBeRemoved.endsWith("%")) {
						issueLength++
						stringToBeRemoved = xtextDocument.get(issueOffset, issueLength)
					}
					
					xtextDocument.replace(issueOffset, issueLength-1, "")
				}
				
				def removeLastEntry(IModificationContext context) {
					var IXtextDocument xtextDocument = context.xtextDocument
					
					var int issueOffset = issue.offset
					var int issueLength = issue.length
					var String stringToBeRemoved = xtextDocument.get(issueOffset, issueLength)
					
					while (!stringToBeRemoved.startsWith("}") && !stringToBeRemoved.endsWith("%")) {
						issueOffset--
						issueLength++
						stringToBeRemoved = xtextDocument.get(issueOffset, issueLength)
					}
					issueOffset++
					issueLength--
					
					while (stringToBeRemoved.contains("%")) {
						var int oldLength = stringToBeRemoved.length
						stringToBeRemoved = stringToBeRemoved.substring(stringToBeRemoved.lastIndexOf("%"))
						issueOffset = issueOffset + (oldLength - stringToBeRemoved.length)
						
						oldLength = stringToBeRemoved.length
						stringToBeRemoved = stringToBeRemoved.substring(stringToBeRemoved.indexOf(System.getProperty("line.separator"))-1)
						issueOffset = issueOffset + (oldLength - stringToBeRemoved.length)
					}
					
					while (!stringToBeRemoved.endsWith("%") && issueOffset+issueLength < xtextDocument.length) {
						issueLength++
						stringToBeRemoved = xtextDocument.get(issueOffset, issueLength)
					}
					
					if (stringToBeRemoved.endsWith("%")) {
						while (!stringToBeRemoved.endsWith("}")) {
							issueLength--
							stringToBeRemoved = xtextDocument.get(issueOffset, issueLength)
						}
					}
					
					xtextDocument.replace(issueOffset, issueLength, "")
				}
	
				def singleEntryExistsInFile(EObject element) {
					var BCCBibTeXFile file = null
					if (element instanceof BCCEntryBody) {
						file = element.eContainer.eContainer as BCCBibTeXFile
					}
					else {
						file = element.eContainer as BCCBibTeXFile
					}
					
					file.entries.size == 1
				}
	
				def isFirstEntry(EObject element) {
					var BCCBibTeXFile file = null
					if (element instanceof BCCEntryBody) {
						file = element.eContainer.eContainer as BCCBibTeXFile
					}
					else {
						file = element.eContainer as BCCBibTeXFile
					}
					
					var BCCAbstractBibTeXFileEntry firstFileEntry = file.entries.get(0)
					
					firstFileEntry.equals(element.eContainer)
				}
	
				def isLastEntry(EObject element) {
					var BCCBibTeXFile file = null
					if (element instanceof BCCEntryBody) {
						file = element.eContainer.eContainer as BCCBibTeXFile
					}
					else {
						file = element.eContainer as BCCBibTeXFile
					}
					
					var BCCAbstractBibTeXFileEntry lastFileEntry = file.entries.get(file.entries.size-1)
					
					lastFileEntry.equals(element.eContainer)
				}
			}
		);
	}
	
	@Fix(BCCBibTeXValidator.FILE_NOT_IN_CORRECT_FOLDER)
	def moveFileToCorrectFolder(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, 'Move file to correct folder', 'Move file to correct folder.', '',
			new ISemanticModification() {
				override apply(EObject element, IModificationContext context) throws Exception {
					var IFile file = BCCResourceUtil.getIFile(element)
					var IProject project = file.project
					
					var IFolder folder = identifyFolder(project, "bibliography")
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
	
	@Fix(BCCBibTeXValidator.SAME_FIELD_MULTIPLE_TIMES)
	def removeField(Issue issue, IssueResolutionAcceptor acceptor) {
		
	}
	
	@Fix(BCCBibTeXValidator.REPLACE_PATTERN_DOES_NOT_EXIST)
	def addEmptyReplacePattern(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, 'Initialize new replace pattern', 'Initialize new replace pattern.', '',
			new ISemanticModification() {
				override apply(EObject element, IModificationContext context) throws Exception {
					var IXtextDocument xtextDocument = context.xtextDocument
					var String currentDocumentContents = xtextDocument.get(0, xtextDocument.length)
					
					var String replacementKey = xtextDocument.get(issue.offset, issue.length)
					
					var String replacementPattern = "\n\n@string{" + replacementKey + " = \"" + replacementKey + "\"}"
					
					currentDocumentContents += replacementPattern

					xtextDocument.replace(0, xtextDocument.length, currentDocumentContents)
				}
			}
		);
	}
}
