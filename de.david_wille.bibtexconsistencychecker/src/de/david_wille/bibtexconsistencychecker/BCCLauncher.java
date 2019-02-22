package de.david_wille.bibtexconsistencychecker;

import java.util.List;

import org.eclipse.core.runtime.CoreException;

import de.david_wille.bibtexconsistencychecker.analysis.BCCAnalysis;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRule;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel;
import de.david_wille.bibtexconsistencychecker.util.BCCProblemMarkerHandling;
import de.david_wille.bibtexconsistencychecker.util.BCCUtil;

public class BCCLauncher {
	
	public void launch(BCCExecutionModel executionModel) {
		try {
			if (!BCCProblemMarkerHandling.resourceHasProblemMarkers(executionModel)) {
				List<BCCBibTeXFile> parsedBibTeXFiles = BCCLauncherResourceUtil.identifyAndParseAllRelevantBibTeXFiles(executionModel);
				
				if (!BCCProblemMarkerHandling.resourcesHaveProblemMarkers(parsedBibTeXFiles)) {
					List<BCCConsistencyRule> parsedConsistencyRules = BCCLauncherResourceUtil.identifyAndParseAllRelevantConsistencyRules(executionModel);
					
					if (!BCCProblemMarkerHandling.resourcesHaveProblemMarkers(parsedConsistencyRules)) {
						BCCAnalysis analysis = new BCCAnalysis(executionModel, parsedConsistencyRules, parsedBibTeXFiles);
						analysis.startAnalysis();
					}
					else {
						BCCUtil.openErrorDialog("Cannot execute the consistency checks due to errors in the consistency rules!");
					}
				}
				else {
					BCCUtil.openErrorDialog("Cannot execute the consistency checks due to errors in the BibTeX files!");
				}
			}
			else {
				BCCUtil.openErrorDialog("Cannot execute the consistency checks due to errors in the execution model!");
			}
		}
		catch (CoreException e) {
			BCCUtil.openErrorDialog("An unexpected error occurred while reading the files!");
		}
	}

}
