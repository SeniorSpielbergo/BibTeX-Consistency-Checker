package de.david_wille.bibtexconsistencychecker.analysis;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import de.david_wille.bibtexconsistencychecker.analysis.collector.BCCCollector;
import de.david_wille.bibtexconsistencychecker.analysis.evaluator.BCCExpressionEvaluator;
import de.david_wille.bibtexconsistencychecker.analysis.preprocessor.BCCPreprocessor;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRule;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel;
import de.david_wille.bibtexconsistencychecker.util.BCCProblemMarkerHandling;

public class BCCAnalysis {

	public static final String CONSISTENCY_PROBLEM_MARKER_ID = "de.david_wille.bibtexconsistencychecker.analysis.problemmarker";
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
	
	public static void clearAllExistingConsistencyProblemMarkers(List<IFile> files) {
		BCCProblemMarkerHandling.clearAllExistingCustomProblemMarkers(CONSISTENCY_PROBLEM_MARKER_ID, files);
	}
	
	public static void clearAllExistingConsistencyProblemMarkers(IFile file) {
		BCCProblemMarkerHandling.clearAllExistingCustomProblemMarkers(CONSISTENCY_PROBLEM_MARKER_ID, file);
	}

	public static void createConsistencyProblemErrorMarker(IResource resource, String errorMessage, EObject eObject, EStructuralFeature eStructuralFeature) {
		BCCProblemMarkerHandling factory = new BCCProblemMarkerHandling();
		factory.createCustomProblemErrorMarker(CONSISTENCY_PROBLEM_MARKER_ID, resource, errorMessage, eObject, eStructuralFeature);
	}

	public static void createConsistencyProblemWarningMarker(IResource resource, String errorMessage, EObject eObject, EStructuralFeature eStructuralFeature) {
		BCCProblemMarkerHandling factory = new BCCProblemMarkerHandling();
		factory.createCustomProblemWarningMarker(CONSISTENCY_PROBLEM_MARKER_ID, resource, errorMessage, eObject, eStructuralFeature);
	}

}
