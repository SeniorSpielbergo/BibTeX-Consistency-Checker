package de.david_wille.bibtexconsistencychecker.executionmodel.ui

import de.david_wille.bibtexconsistencychecker.executionmodel.ui.hyperlink.BCCExecutionModelHyperlinkHelper
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkHelper

/**
 * Use this class to register components to be used within the Eclipse IDE.
 */
@FinalFieldsConstructor
class BCCExecutionModelUiModule extends AbstractBCCExecutionModelUiModule {
	
	def Class<? extends IHyperlinkHelper> bindIHyperlinkHelper() {
		BCCExecutionModelHyperlinkHelper
	}
	
}
