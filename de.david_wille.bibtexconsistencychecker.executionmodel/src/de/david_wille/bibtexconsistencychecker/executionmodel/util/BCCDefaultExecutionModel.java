package de.david_wille.bibtexconsistencychecker.executionmodel.util;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;

import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCBibTeXFilesEntry;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCConsistencyRulesEntry;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCEnsureSettingsEntry;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModelFactory;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCFilePathEntry;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCSettingsEntry;

public class BCCDefaultExecutionModel {
	
	public static final String RULES_FOLDER = "rules";
	public static final String BIBLIOGRAPHY_FOLDER = "bibliography";
	
	public static BCCExecutionModel generate(IContainer parentContainer, String name) {
		BCCExecutionModelFactory executionModelFactory = BCCExecutionModelFactory.eINSTANCE;
		
		BCCExecutionModel executionModel = executionModelFactory.createBCCExecutionModel();
		
		BCCSettingsEntry settingsEntry = generateDefaultSettingsEntry(executionModelFactory, name);
		executionModel.setSettingsEntry(settingsEntry);
		
		if (parentContainer instanceof IProject) {
			BCCBibTeXFilesEntry bibTeXFilesEntry = generateDefaultBibTeXFilesEntry(executionModelFactory);
			executionModel.setBibTeXFilesEntry(bibTeXFilesEntry);
			
			BCCConsistencyRulesEntry consistencyRulesEntry = generateDefaultConsistencyRulesEntry(executionModelFactory);
			executionModel.setRulesEntry(consistencyRulesEntry);
		}
		
		return executionModel;
	}

	private static BCCBibTeXFilesEntry generateDefaultBibTeXFilesEntry(BCCExecutionModelFactory executionModelFactory) {
		BCCBibTeXFilesEntry bibTeXFilesEntry = executionModelFactory.createBCCBibTeXFilesEntry();
		
		BCCFilePathEntry bibliographyFolderEntry = generateBCCFilePathEntry(executionModelFactory, BIBLIOGRAPHY_FOLDER);
		
		List<BCCFilePathEntry> entries = bibTeXFilesEntry.getEntries();
		entries.add(bibliographyFolderEntry);
		
		return bibTeXFilesEntry;
	}

	private static BCCConsistencyRulesEntry generateDefaultConsistencyRulesEntry(BCCExecutionModelFactory executionModelFactory) {
		BCCConsistencyRulesEntry consistencyRulesEntry = executionModelFactory.createBCCConsistencyRulesEntry();
		
		BCCFilePathEntry rulesFolderEntry = generateBCCFilePathEntry(executionModelFactory, RULES_FOLDER);
		
		List<BCCFilePathEntry> entries = consistencyRulesEntry.getEntries();
		entries.add(rulesFolderEntry);
		
		return consistencyRulesEntry;
	}
	
	private static BCCFilePathEntry generateBCCFilePathEntry(BCCExecutionModelFactory executionModelFactory, String path) {
		BCCFilePathEntry filePathEntry = executionModelFactory.createBCCFilePathEntry();
		
		filePathEntry.setPath(path + "/");
		
		return filePathEntry;
	}

	private static BCCSettingsEntry generateDefaultSettingsEntry(BCCExecutionModelFactory executionModelFactory, String name) {
		BCCSettingsEntry settingsEntry = executionModelFactory.createBCCSettingsEntry();
		
		settingsEntry.setName(name);
		
		BCCEnsureSettingsEntry ensureSettingsEntry = generateDefaultEnsureSettingsEntry(executionModelFactory);
		settingsEntry.setEnsureSettingsEntry(ensureSettingsEntry);
		
		return settingsEntry;
	}
	
	private static BCCEnsureSettingsEntry generateDefaultEnsureSettingsEntry(BCCExecutionModelFactory executionModelFactory) {
		BCCEnsureSettingsEntry ensureSettingsEntry = executionModelFactory.createBCCEnsureSettingsEntry();
		
		ensureSettingsEntry.setEnsureAlphbeticOrderActivated(true);
		
		ensureSettingsEntry.setEnsureShortHarvardStyleActivated(true);
		
		return ensureSettingsEntry;
	}
	
}
