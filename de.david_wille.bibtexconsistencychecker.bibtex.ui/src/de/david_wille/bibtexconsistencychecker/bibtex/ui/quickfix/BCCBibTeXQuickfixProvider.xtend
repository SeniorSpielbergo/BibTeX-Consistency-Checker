package de.david_wille.bibtexconsistencychecker.bibtex.ui.quickfix

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCTitleField
import de.david_wille.bibtexconsistencychecker.bibtex.validation.BCCBibTeXValidator
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
class BCCBibTeXQuickfixProvider extends DefaultQuickfixProvider {

	@Fix(BCCBibTeXValidator.NO_DOUBLE_BRACES)
	def addAdditionalBraces(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, 'Add opening and closing braces', 'Add opening and closing braces.', '',
			new ISemanticModification() {
				override apply(EObject element, IModificationContext context) throws Exception {
					(element as BCCTitleField).fieldValueObject.fieldValue = "{" + (element as BCCTitleField).fieldValueObject.fieldValue + "}"
				}
		    }
		);
	}
	
}
