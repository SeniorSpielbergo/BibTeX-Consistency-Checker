package de.david_wille.bibtexconsistencychecker.analysis;

import java.util.List;

import de.david_wille.bibtexconsistencychecker.analysis.collector.BCCCollector;
import de.david_wille.bibtexconsistencychecker.analysis.evaluator.BCCExpressionEvaluator;
import de.david_wille.bibtexconsistencychecker.analysis.preprocessor.BCCPreprocessor;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRule;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel;

public class BCCAnalysis {
	
	private List<BCCConsistencyRule> parsedConsistencyRules;
	private BCCPreprocessor preprocessor;
	private BCCCollector collector;

	public BCCAnalysis(BCCExecutionModel executionModel, List<BCCConsistencyRule> parsedConsistencyRules, List<BCCBibTeXFile> parsedBibTeXFiles) {
		this.parsedConsistencyRules = parsedConsistencyRules;
		
		preprocessor = new BCCPreprocessor(executionModel, parsedBibTeXFiles);
		collector = new BCCCollector(parsedBibTeXFiles);
	}

	public void startAnalysis() {
		preprocessor.execute();
		
		evaluateSelectedConsistencyRules();
	}

	private void evaluateSelectedConsistencyRules() {
		for (BCCConsistencyRule consistencyRule : parsedConsistencyRules) {
			evaluateSelectedConsistencyRule(consistencyRule);
		}
	}

	private void evaluateSelectedConsistencyRule(BCCConsistencyRule consistencyRule) {
		List<BCCAbstractBibTeXEntry> bibTeXEntries = collector.collectMatchingEntries(consistencyRule);
		
		for (BCCAbstractBibTeXEntry bibTeXEntry : bibTeXEntries) {
			BCCExpressionEvaluator evaluator = new BCCExpressionEvaluator();
			evaluator.evaluateExpression(consistencyRule, bibTeXEntry);
		}
	}

}
