package de.david_wille.bibtexconsistencychecker.bibtex.ide.syntaxcoloring

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractGenericField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXPackage
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCDateField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEntryBody
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEntryKeyObject
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCMonthField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCPages
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCPagesField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplaceKeyObject
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplacePatternEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCYearField
import de.david_wille.bibtexconsistencychecker.bibtex.services.BCCBibTeXGrammarAccess
import javax.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.ide.editor.syntaxcoloring.DefaultSemanticHighlightingCalculator
import org.eclipse.xtext.ide.editor.syntaxcoloring.HighlightingStyles
import org.eclipse.xtext.ide.editor.syntaxcoloring.IHighlightedPositionAcceptor
import org.eclipse.xtext.util.CancelIndicator

class BCCBibTeXSemanticHighlightingCalculator extends DefaultSemanticHighlightingCalculator {
	@Inject package BCCBibTeXGrammarAccess grammar

	override protected boolean highlightElement(EObject object, IHighlightedPositionAcceptor acceptor,
		CancelIndicator cancelIndicator) {
		switch (object) {
			BCCAbstractGenericField: {
				highlightFeature(acceptor, object, BCCBibTeXPackage.eINSTANCE.BCCAbstractGenericField_FieldValueObject, HighlightingStyles.DEFAULT_ID)
				return true
			}
			BCCReplacePatternEntry: {
				highlightFeature(acceptor, object, BCCBibTeXPackage.eINSTANCE.BCCReplacePatternEntry_ReplacementFieldValue, HighlightingStyles.STRING_ID)
				return true
			}
			BCCYearField: {
				highlightFeature(acceptor, object, BCCBibTeXPackage.eINSTANCE.BCCYearField_Year, HighlightingStyles.DEFAULT_ID)
				return true
			}
			BCCMonthField: {
				highlightFeature(acceptor, object, BCCBibTeXPackage.eINSTANCE.BCCMonthField_Month, HighlightingStyles.DEFAULT_ID)
				return true
			}
			BCCDateField: {
				highlightFeature(acceptor, object, BCCBibTeXPackage.eINSTANCE.BCCDateField_Date, HighlightingStyles.DEFAULT_ID)
				return true
			}
			BCCPages: {
				highlightFeature(acceptor, object, BCCBibTeXPackage.eINSTANCE.BCCPages_StartPage, HighlightingStyles.DEFAULT_ID)
				highlightFeature(acceptor, object, BCCBibTeXPackage.eINSTANCE.BCCPages_EndPage, HighlightingStyles.DEFAULT_ID)
				return true
			}
			BCCPagesField: {
				highlightFeature(acceptor, object, BCCBibTeXPackage.eINSTANCE.BCCPagesField_Pages, HighlightingStyles.DEFAULT_ID)
				return true
			}
			BCCEntryBody: {
				highlightFeature(acceptor, object, BCCBibTeXPackage.eINSTANCE.BCCEntryBody_EntryKeyObject, HighlightingStyles.DEFAULT_ID)
				return true
			}
			BCCEntryKeyObject: {
				highlightFeature(acceptor, object, BCCBibTeXPackage.eINSTANCE.BCCEntryKeyObject_EntryKey, HighlightingStyles.DEFAULT_ID)
				return true
			}
			BCCReplaceKeyObject: {
				highlightFeature(acceptor, object, BCCBibTeXPackage.eINSTANCE.BCCReplaceKeyObject_ReplaceKey, HighlightingStyles.DEFAULT_ID)
				return true
			}
			default: false
		}
}
	
}
				