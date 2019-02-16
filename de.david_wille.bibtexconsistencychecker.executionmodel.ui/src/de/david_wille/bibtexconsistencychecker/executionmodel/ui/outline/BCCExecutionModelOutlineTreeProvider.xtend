package de.david_wille.bibtexconsistencychecker.executionmodel.ui.outline

import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModelPackage
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCSettingsEntry
import org.eclipse.swt.graphics.Image
import org.eclipse.xtext.ui.editor.outline.IOutlineNode
import org.eclipse.xtext.ui.editor.outline.impl.DefaultOutlineTreeProvider
import org.eclipse.xtext.ui.editor.outline.impl.DocumentRootNode
import org.eclipse.xtext.ui.editor.outline.impl.EObjectNode

/**
 * Customization of the default outline structure.
 *
 * See https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#outline
 */
class BCCExecutionModelOutlineTreeProvider extends DefaultOutlineTreeProvider {
	
	protected def _createNode(DocumentRootNode parentNode, BCCExecutionModel executionModel) {
		var String stringRepresentation = "Execution Model \"" + executionModel.settingsEntry.name + "\""
		var Image image = imageDispatcher.invoke(executionModel.settingsEntry);
		var IOutlineNode childNode = new EObjectNode(executionModel.settingsEntry, parentNode, image, stringRepresentation, false);
		
		createChildren(childNode, executionModel.settingsEntry)
		
		childNode
	}
	
	protected def _createChildren(IOutlineNode parentNode, BCCSettingsEntry settingsEntry) {
		if (settingsEntry.ensureAlphbeticOrderActivated) {
			var Image ensureAlphbeticOrderActivatedImage = imageDispatcher.invoke(settingsEntry)
			createEStructuralFeatureNode(parentNode, settingsEntry, BCCExecutionModelPackage.Literals.BCC_SETTINGS_ENTRY__ENSURE_ALPHBETIC_ORDER_ACTIVATED,
				ensureAlphbeticOrderActivatedImage, "ensure alphabetic order", true);
		}
		
		if (settingsEntry.ensureShortHarvardStyleActivated) {
			var Image ensureShortHarvardStyleActivatedImage = imageDispatcher.invoke(settingsEntry)
			createEStructuralFeatureNode(parentNode, settingsEntry, BCCExecutionModelPackage.Literals.BCC_SETTINGS_ENTRY__ENSURE_SHORT_HARVARD_STYLE_ACTIVATED,
				ensureShortHarvardStyleActivatedImage, "ensure short harvard style", true);
		}
	}
	
}
