package de.david_wille.bibtexconsistencychecker.bibtex.ui

import de.david_wille.bibtexconsistencychecker.bibtex.ide.syntaxcoloring.BCCBibTeXSemanticHighlightingCalculator
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.eclipse.xtext.ide.editor.syntaxcoloring.ISemanticHighlightingCalculator
import org.eclipse.xtext.ui.editor.IXtextEditorCallback

/**
 * Use this class to register components to be used within the Eclipse IDE.
 */
@FinalFieldsConstructor
class BCCBibTeXUiModule extends AbstractBCCBibTeXUiModule {
	
	def Class<? extends ISemanticHighlightingCalculator> bindISemanticHighlightingCalculator () {
		BCCBibTeXSemanticHighlightingCalculator
	}
	
	// replaces the default NatureAddingEditorCallback
	override Class<? extends IXtextEditorCallback> bindIXtextEditorCallback() {
		return BCCNatureAddingEditorCallBack
	}
	
}
