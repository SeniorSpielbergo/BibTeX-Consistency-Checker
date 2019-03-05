package de.david_wille.bibtexconsistencychecker.executionmodel.ui.quickfix

import de.david_wille.bibtexconsistencychecker.executionmodel.validation.BCCExecutionModelValidator
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil
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
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext
import org.eclipse.xtext.ui.editor.model.edit.ISemanticModification
import org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider
import org.eclipse.xtext.ui.editor.quickfix.Fix
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor
import org.eclipse.xtext.validation.Issue
import de.david_wille.bibtexconsistencychecker.util.BCCUtil

/**
 * Custom quickfixes.
 *
 * See https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#quick-fixes
 */
class BCCExecutionModelQuickfixProvider extends DefaultQuickfixProvider {

	@Fix(BCCExecutionModelValidator.RULES_FOLDER_DOES_NOT_EXIST)
	def addRulesFolder(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, "Add \"" + BCCExecutionModelValidator.RULES_FOLDER + "\" folder", "Add \"" + BCCExecutionModelValidator.RULES_FOLDER + "\" folder.", '',
			new ISemanticModification() {
				override apply(EObject element, IModificationContext context) throws Exception {
					var IProject project = BCCResourceUtil.getIProject(element)
					
					var IFolder rulesFolder = project.getFolder(BCCExecutionModelValidator.RULES_FOLDER)
					rulesFolder.create(false, false, null)
				}
		    }
		);
	}

	@Fix(BCCExecutionModelValidator.BIBLIOGRAPHY_FOLDER_DOES_NOT_EXIST)
	def addBibliographyFolder(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, "Add \"" + BCCExecutionModelValidator.BIBLIOGRAPHY_FOLDER + "\" folder", "Add \"" + BCCExecutionModelValidator.BIBLIOGRAPHY_FOLDER + "\" folder.", '',
			new ISemanticModification() {
				override apply(EObject element, IModificationContext context) throws Exception {
					var IProject project = BCCResourceUtil.getIProject(element)
					
					var IFolder bibliographyFolder = project.getFolder(BCCExecutionModelValidator.BIBLIOGRAPHY_FOLDER)
					bibliographyFolder.create(false, false, null)
				}
		    }
		);
	}
	
	@Fix(BCCExecutionModelValidator.EXECUTION_MODEL_STORED_IN_PROJECT_ROOT)
	def moveFileToCorrectFolder(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, 'Move file to project root', 'Move file to project root.', '',
			new ISemanticModification() {
				override apply(EObject element, IModificationContext context) throws Exception {
					var IFile file = BCCResourceUtil.getIFile(element)
					var IProject project = file.project
					
					var IPath newFilePath = new Path(project.fullPath + "/" + file.name)
					
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
		);
	}
	
	@Fix(BCCExecutionModelValidator.ONLY_SINGLE_EXECUTION_MODEL_EXISTS)
	def deleteDuplicateFile(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, 'Delete file', 'Delete file.', '',
			new ISemanticModification() {
				override apply(EObject element, IModificationContext context) throws Exception {
					var IFile file = BCCResourceUtil.getIFile(element)
					file.delete(true, true, null)
				}
			}
		);
	}

}
