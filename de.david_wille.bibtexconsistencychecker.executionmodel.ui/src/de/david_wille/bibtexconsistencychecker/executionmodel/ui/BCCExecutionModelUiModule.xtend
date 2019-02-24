package de.david_wille.bibtexconsistencychecker.executionmodel.ui

import de.david_wille.bibtexconsistencychecker.executionmodel.ide.syntaxcoloring.BCCExecutionModelSemanticHighlightingCalculator
import de.david_wille.bibtexconsistencychecker.executionmodel.ui.hyperlink.BCCExecutionModelHyperlinkHelper
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.eclipse.xtext.ide.editor.syntaxcoloring.ISemanticHighlightingCalculator
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkHelper

/**
 * Use this class to register components to be used within the Eclipse IDE.
 */
@FinalFieldsConstructor
class BCCExecutionModelUiModule extends AbstractBCCExecutionModelUiModule {
	
	def Class<? extends IHyperlinkHelper> bindIHyperlinkHelper() {
		BCCExecutionModelHyperlinkHelper
	}
	
	def Class<? extends ISemanticHighlightingCalculator> bindISemanticHighlightingCalculator () {
		BCCExecutionModelSemanticHighlightingCalculator
	}
	
}
