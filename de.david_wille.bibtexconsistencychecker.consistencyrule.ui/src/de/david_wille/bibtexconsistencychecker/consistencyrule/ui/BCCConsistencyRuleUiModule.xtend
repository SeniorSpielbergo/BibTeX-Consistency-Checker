package de.david_wille.bibtexconsistencychecker.consistencyrule.ui

import de.david_wille.bibtexconsistencychecker.consistencyrule.ui.hyperlink.BCCConsistencyRuleHyperlinkHelper
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.eclipse.xtext.ui.editor.IXtextEditorCallback
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkHelper

/**
 * Use this class to register components to be used within the Eclipse IDE.
 */
@FinalFieldsConstructor
class BCCConsistencyRuleUiModule extends AbstractBCCConsistencyRuleUiModule {
	
	def Class<? extends IHyperlinkHelper> bindIHyperlinkHelper() {
		BCCConsistencyRuleHyperlinkHelper
	}
	
	// replaces the default NatureAddingEditorCallback
	override Class<? extends IXtextEditorCallback> bindIXtextEditorCallback() {
		return BCCNatureAddingEditorCallBack
	}
	
}
