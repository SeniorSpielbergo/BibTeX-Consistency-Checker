package de.david_wille.bibtexconsistencychecker.executionmodel.ui.outline

import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCBibTeXFilesEntry
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCConsistencyRulesEntry
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModelPackage
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCFilePathEntry
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
		var IOutlineNode executionModelNode = new EObjectNode(executionModel.settingsEntry, parentNode, image, stringRepresentation, false);
		
		createChildren(executionModelNode, executionModel.settingsEntry)
		
		if (executionModel.rulesEntry !== null) {
			var Image rulesEntryImage = imageDispatcher.invoke(executionModel.rulesEntry);
			var IOutlineNode rulesEntryNode = new EObjectNode(executionModel.rulesEntry, executionModelNode, rulesEntryImage, "Rules", false);
			
			createChildren(rulesEntryNode, executionModel.rulesEntry)
		}
		
		if (executionModel.bibTeXFilesEntry !== null) {
			var Image bibTeXFilesEntryImage = imageDispatcher.invoke(executionModel.bibTeXFilesEntry);
			var IOutlineNode bibTeXFilesEntryNode = new EObjectNode(executionModel.bibTeXFilesEntry, executionModelNode, bibTeXFilesEntryImage, "BibTeX Files", false);
			
			createChildren(bibTeXFilesEntryNode, executionModel.bibTeXFilesEntry)
		}
		
		executionModelNode
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
	
	protected def _createNode(IOutlineNode parentNode, BCCConsistencyRulesEntry rulesEntry) {
		if (rulesEntry !== null) {
			var Image image = imageDispatcher.invoke(rulesEntry);
			var IOutlineNode childNode = new EObjectNode(rulesEntry, parentNode, image, "Rules", false);
			
			createChildren(childNode, rulesEntry)
		}
	}
	
	protected def _createNode(IOutlineNode parentNode, BCCBibTeXFilesEntry filesEntry) {
		if (filesEntry !== null) {
			var Image image = imageDispatcher.invoke(filesEntry);
			var IOutlineNode childNode = new EObjectNode(filesEntry, parentNode, image, "BibTeX Files", false);
			
			createChildren(childNode, filesEntry)
		}
	}
	
	protected def _createChildren(IOutlineNode parentNode, BCCConsistencyRulesEntry rulesEntry) {
		for (BCCFilePathEntry ruleEntry : rulesEntry.entries) {
			if (ruleEntry.path !== null) {
				var Image pathImage = imageDispatcher.invoke(ruleEntry)
				createEStructuralFeatureNode(parentNode, ruleEntry, BCCExecutionModelPackage.Literals.BCC_FILE_PATH_ENTRY__PATH,
					pathImage, ruleEntry.path, true);
			}
		}
	}
	
	protected def _createChildren(IOutlineNode parentNode, BCCBibTeXFilesEntry filesEntry) {
		for (BCCFilePathEntry fileEntry : filesEntry.entries) {
			if (fileEntry.path !== null) {
				var Image pathImage = imageDispatcher.invoke(fileEntry)
				createEStructuralFeatureNode(parentNode, fileEntry, BCCExecutionModelPackage.Literals.BCC_FILE_PATH_ENTRY__PATH,
					pathImage, fileEntry.path, true);
			}
		}
	}
	
}
