package de.david_wille.bibtexconsistencychecker.bibtex.ui

import de.david_wille.bibtexconsistencychecker.bibtex.ide.syntaxcoloring.BCCBibTeXSemanticHighlightingCalculator
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.eclipse.xtext.ide.editor.syntaxcoloring.ISemanticHighlightingCalculator

/**
 * Use this class to register components to be used within the Eclipse IDE.
 */
@FinalFieldsConstructor
class BCCBibTeXUiModule extends AbstractBCCBibTeXUiModule {
	
	def Class<? extends ISemanticHighlightingCalculator> bindISemanticHighlightingCalculator () {
		BCCBibTeXSemanticHighlightingCalculator
	}
	
}
