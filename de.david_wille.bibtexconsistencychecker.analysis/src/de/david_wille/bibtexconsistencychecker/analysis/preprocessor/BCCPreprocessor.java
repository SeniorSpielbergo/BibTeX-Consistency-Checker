package de.david_wille.bibtexconsistencychecker.analysis.preprocessor;

import java.util.List;

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel;

public class BCCPreprocessor {
	
	private BCCExecutionModel executionModel;
	private List<BCCBibTeXFile> parsedBibTeXFiles;

	public BCCPreprocessor(BCCExecutionModel executionModel, List<BCCBibTeXFile> parsedBibTeXFiles) {
		this.executionModel = executionModel;
		this.parsedBibTeXFiles = parsedBibTeXFiles;
	}

	public void execute() {
		if (ensureShortHarvardStyleActivated(executionModel)) {
			boolean noErrorsDetected = BCCShortHarvardStyle.checkShortHarvardStyle(parsedBibTeXFiles);
			
			if (noErrorsDetected) {
				if (ensureAlphabeticOrderActivated(executionModel)) {
					BCCAlphabeticOrder.checkAlphabeticOrder(parsedBibTeXFiles);
				}
			}
		}
		else {
			if (ensureAlphabeticOrderActivated(executionModel)) {
				BCCAlphabeticOrder.checkAlphabeticOrder(parsedBibTeXFiles);
			}
		}
		
		// TODO: move to validator as this has to be ensured in all cases
		if (ensureNoConflictingEntryKeysExistActivated(executionModel)) {
			BCCNoConflictingEntryKeysExist.checkNoConflictingEntryKeysExist(parsedBibTeXFiles);
		}
		
		// TODO: move to validator as this has to be ensured in all cases
		if (ensureNoContradictingReplacePatternExistActivated(executionModel)) {
			BCCNoContradictingReplacePatternExist.checkNoContradictingReplacePatternExist(parsedBibTeXFiles);
		}
		
		// TODO: move to validator as this has to be ensured in all cases
		if (ensureReplacePatternExistActivated(executionModel)) {
			BCCReplacePatternExist.checkReplacePatternExist(parsedBibTeXFiles);
		}
	}
	
	private boolean ensureAlphabeticOrderActivated(BCCExecutionModel executionModel) {
		return executionModel.getSettingsEntry().isEnsureAlphbeticOrderActivated();
	}

	private boolean ensureShortHarvardStyleActivated(BCCExecutionModel executionModel) {
		return executionModel.getSettingsEntry().isEnsureShortHarvardStyleActivated();
	}

	private boolean ensureNoConflictingEntryKeysExistActivated(BCCExecutionModel executionModel) {
		return executionModel.getSettingsEntry().isEnsureNoConflictingEntryKeysExistActivated();
	}

	private boolean ensureReplacePatternExistActivated(BCCExecutionModel executionModel) {
		return executionModel.getSettingsEntry().isEnsureReplacePatternExistActivated();
	}

	private boolean ensureNoContradictingReplacePatternExistActivated(BCCExecutionModel executionModel) {
		return executionModel.getSettingsEntry().isEnsureNoContradictingReplacePatternExistActivated();
	}

}
