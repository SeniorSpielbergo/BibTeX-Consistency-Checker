package de.david_wille.bibtexconsistencychecker.bibtex.ide.syntaxcoloring

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractGenericField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXPackage
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplacePatternEntry
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
				highlightFeature(acceptor, object, BCCBibTeXPackage.eINSTANCE.BCCAbstractGenericField_FieldValue, HighlightingStyles.DEFAULT_ID)
				return true
			}
			BCCReplacePatternEntry: {
				highlightFeature(acceptor, object, BCCBibTeXPackage.eINSTANCE.BCCReplacePatternEntry_ReplacementFieldValue, HighlightingStyles.DEFAULT_ID)
				return true
			}
			default: false
		}
}
	
}
				