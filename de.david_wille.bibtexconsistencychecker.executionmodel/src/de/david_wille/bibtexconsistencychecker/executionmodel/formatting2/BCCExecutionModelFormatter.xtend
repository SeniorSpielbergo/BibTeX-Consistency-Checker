package de.david_wille.bibtexconsistencychecker.executionmodel.formatting2

import com.google.inject.Inject
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCBibTeXFilesEntry
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCConsistencyRulesEntry
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCEnsureSettingsEntry
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModelPackage
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCFilePathEntry
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCSettingsEntry
import de.david_wille.bibtexconsistencychecker.executionmodel.services.BCCExecutionModelGrammarAccess
import org.eclipse.xtext.formatting2.AbstractFormatter2
import org.eclipse.xtext.formatting2.IFormattableDocument
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegion

class BCCExecutionModelFormatter extends AbstractFormatter2 {
	
	@Inject extension BCCExecutionModelGrammarAccess

	def dispatch void format(BCCExecutionModel bCCExecutionModel, extension IFormattableDocument document) {
		bCCExecutionModel.getSettingsEntry.format;
		bCCExecutionModel.getBibTeXFilesEntry.format;
		bCCExecutionModel.getRulesEntry.format;
	}
	
	def dispatch void format(BCCSettingsEntry settingsEntry, extension IFormattableDocument document) {
		var ISemanticRegion settingsEntryStart = settingsEntry.regionFor.keyword("{")
		settingsEntryStart.append[newLine]
		var ISemanticRegion settingsEntryEnd = settingsEntry.regionFor.keyword("}")
		
		var BCCExecutionModel bCCExecutionModel = settingsEntry.eContainer as BCCExecutionModel
		if (bCCExecutionModel.getBibTeXFilesEntry !== null || bCCExecutionModel.getRulesEntry !== null) {
			settingsEntryEnd.append[newLines = 2]
		}
		else {
			settingsEntryEnd.append[newLine]
		}
		
		interior(settingsEntryStart, settingsEntryEnd)[indent]
		
		settingsEntry.regionFor.feature(BCCExecutionModelPackage.Literals.BCC_SETTINGS_ENTRY__NAME).append[newLine]
		
		settingsEntry.getEnsureSettingsEntry.format
	}
	
	def dispatch void format(BCCEnsureSettingsEntry ensureSettingsEntry, extension IFormattableDocument document) {
		var ISemanticRegion ensureSettingsEntryStart = ensureSettingsEntry.regionFor.keyword("{")
		ensureSettingsEntryStart.append[newLine]
		var ISemanticRegion ensureSettingsEntryEnd = ensureSettingsEntry.regionFor.keyword("}")
		ensureSettingsEntryEnd.append[newLine]
		
		interior(ensureSettingsEntryStart, ensureSettingsEntryEnd)[indent]
		
		ensureSettingsEntry.regionFor.keyword(BCCEnsureSettingsEntryAccess.orderKeyword_3_0_1).append[newLine]
		
		ensureSettingsEntry.regionFor.keyword(BCCEnsureSettingsEntryAccess.styleKeyword_3_1_2).append[newLine]
	}

	def dispatch void format(BCCBibTeXFilesEntry bCCBibTeXFilesEntry, extension IFormattableDocument document) {
		var ISemanticRegion bCCBibTeXFilesEntryStart = bCCBibTeXFilesEntry.regionFor.keyword("{")
		bCCBibTeXFilesEntryStart.append[newLine]
		var ISemanticRegion bCCBibTeXFilesEntryEnd = bCCBibTeXFilesEntry.regionFor.keyword("}")
		bCCBibTeXFilesEntryEnd.append[newLines = 2]
		
		interior(bCCBibTeXFilesEntryStart, bCCBibTeXFilesEntryEnd)[indent]
		
		for (BCCFilePathEntry bCCFilePathEntry : bCCBibTeXFilesEntry.getEntries()) {
			bCCFilePathEntry.format;
		}
	}

	def dispatch void format(BCCConsistencyRulesEntry bCCConsistencyRuleFilesEntry, extension IFormattableDocument document) {
		var ISemanticRegion bCCConsistencyRuleFilesEntryStart = bCCConsistencyRuleFilesEntry.regionFor.keyword("{")
		bCCConsistencyRuleFilesEntryStart.append[newLine]
		var ISemanticRegion bCCConsistencyRuleFilesEntryEnd = bCCConsistencyRuleFilesEntry.regionFor.keyword("}")
		bCCConsistencyRuleFilesEntryEnd.append[newLine]
		
		interior(bCCConsistencyRuleFilesEntryStart, bCCConsistencyRuleFilesEntryEnd)[indent]
		
		for (BCCFilePathEntry bCCFilePathEntry : bCCConsistencyRuleFilesEntry.getEntries()) {
			bCCFilePathEntry.format;
		}
	}
	
	def dispatch void format(BCCFilePathEntry bCCFilePathEntry, extension IFormattableDocument document) {
		bCCFilePathEntry.regionFor.feature(BCCExecutionModelPackage.Literals.BCC_FILE_PATH_ENTRY__PATH).append[newLine]
	}
	
}
