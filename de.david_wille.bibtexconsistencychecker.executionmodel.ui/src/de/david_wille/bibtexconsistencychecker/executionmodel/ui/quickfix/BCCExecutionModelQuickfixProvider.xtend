package de.david_wille.bibtexconsistencychecker.executionmodel.ui.quickfix

import de.david_wille.bibtexconsistencychecker.executionmodel.validation.BCCExecutionModelValidator
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil
import org.eclipse.core.resources.IFolder
import org.eclipse.core.resources.IProject
import org.eclipse.emf.ecore.EObject
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
					
					var IFolder rulesFolder = project.getFolder(BCCExecutionModelValidator.BIBLIOGRAPHY_FOLDER)
					rulesFolder.create(false, false, null)
				}
		    }
		);
	}

}
