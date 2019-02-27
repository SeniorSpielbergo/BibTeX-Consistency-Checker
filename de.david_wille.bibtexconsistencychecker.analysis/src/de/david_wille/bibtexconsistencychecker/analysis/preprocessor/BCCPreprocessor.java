package de.david_wille.bibtexconsistencychecker.analysis.preprocessor;

import java.util.List;

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRule;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel;

public class BCCPreprocessor {
	
	private BCCExecutionModel executionModel;
	private List<BCCBibTeXFile> parsedBibTeXFiles;
	private List<BCCConsistencyRule> parsedConsistencyRules;

	public BCCPreprocessor(BCCExecutionModel executionModel, List<BCCBibTeXFile> parsedBibTeXFiles,
			List<BCCConsistencyRule> parsedConsistencyRules)
	{
		this.executionModel = executionModel;
		this.parsedBibTeXFiles = parsedBibTeXFiles;
		this.parsedConsistencyRules = parsedConsistencyRules;
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
		
		BCCNoConflictingEntryKeysExist.checkNoConflictingEntryKeysExist(parsedBibTeXFiles);
		
		BCCNoContradictingReplacePatternExist.checkNoContradictingReplacePatternExist(parsedBibTeXFiles);
		
		BCCReplacePatternExist.checkReplacePatternExist(parsedBibTeXFiles);
		
		BCCExcludedEntryKeysExist.checkExcludedEntryKeysExist(parsedBibTeXFiles, parsedConsistencyRules);
	}
	
	private boolean ensureAlphabeticOrderActivated(BCCExecutionModel executionModel) {
		return executionModel.getSettingsEntry().getEnsureSettingsEntry().isEnsureAlphbeticOrderActivated();
	}

	private boolean ensureShortHarvardStyleActivated(BCCExecutionModel executionModel) {
		return executionModel.getSettingsEntry().getEnsureSettingsEntry().isEnsureShortHarvardStyleActivated();
	}

}
