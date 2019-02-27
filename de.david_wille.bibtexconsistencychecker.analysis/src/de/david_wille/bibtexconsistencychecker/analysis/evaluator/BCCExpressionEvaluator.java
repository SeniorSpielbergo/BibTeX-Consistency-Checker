package de.david_wille.bibtexconsistencychecker.analysis.evaluator;

import org.eclipse.core.resources.IResource;

import de.david_wille.bibtexconsistencychecker.analysis.BCCAnalysis;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractEntryBodyField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractGenericField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAddressField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAuthorField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXPackage;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBooktitleField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCChapterField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCDateField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCDoiField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEditionField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEditorField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEntryBody;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCHowPublishedField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCInstitutionField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCIsbnField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCJournalField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCKeywordsField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCMonthField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCNoteField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCNumberField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCOrganizationField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCPagesField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCPublisherField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCRevisionField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCSchoolField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCSeriesField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCTitleField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCTypeField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCUrlField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCVersionField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCVolumeField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCYearField;
import de.david_wille.bibtexconsistencychecker.bibtex.util.BCCBibTeXUtil;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAbstractFieldSelectionExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAbstractMarkerExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAbstractSpecificFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAddressFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAndExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAreIntegerExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAuthorFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCBooktitleFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCChapterFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRule;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCDateFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCDoiFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCEditionFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCEditorFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCErrorMarkerExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCEvaluationExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCFieldExistsExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCFieldsExistExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCHowPublishedFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCIfThenElseExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCIfThenExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCInstitutionFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCIntegerExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCIntegerRelationalExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCIntegerRelationalOperator;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCIsIntegerExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCIsbnFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCJournalFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCKeywordsFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCMonthFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCMultiFieldSelectionExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCNoKeywordExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCNotExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCNoteFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCNumberFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCOrExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCOrganizationFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCPagesFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCParenthesesExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCPublisherFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCReturnExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCRevisionFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCSchoolFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCSeriesFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCSingleFieldSelectionExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCStringExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCStringRelationalExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCStringRelationalOperator;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCTitleFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCTypeFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCUrlFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCUseReplacePatternExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCUsesReplacePatternExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCVersionFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCVolumeFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCWarningMarkerExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCYearFieldSelector;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;

public class BCCExpressionEvaluator {
	
	private BCCConsistencyRule evaluatedConsistencyRule;
	private boolean returnFromRule;
	
	public void evaluateExpression(BCCConsistencyRule evaluatedConsistencyRule, BCCAbstractBibTeXEntry bibTeXEntry) {
		this.evaluatedConsistencyRule = evaluatedConsistencyRule;
		returnFromRule = false;
		
		for (BCCIfThenElseExpression ifThenElseExpression : evaluatedConsistencyRule.getRuleBody().getIfThenElseExpressions()) {
			evaluateIfThenElseExpression(ifThenElseExpression, bibTeXEntry);
		}
	}

	private void evaluateIfThenElseExpression(BCCIfThenElseExpression ifThenElseExpression, BCCAbstractBibTeXEntry bibTeXEntry) {
		if (!returnFromRule) {
			BCCIfThenExpression ifThenExpression = ifThenElseExpression.getIfThenExpression();
			
			BCCExpression ifExpression = ifThenExpression.getIfExpression();
			
			// check the if expression
			if (evaluateExpression(ifExpression, bibTeXEntry)) {
				for (BCCEvaluationExpression thenExpression : ifThenExpression.getThenExpressions()) {
					evaluateEvaluationExpression(thenExpression, bibTeXEntry);
				}
				return;
			}
			else {
				// process all else if expressions
				for (BCCIfThenExpression elseIfThenExpression : ifThenElseExpression.getElseIfThenExpressions()) {
					BCCExpression elseIfExpression = elseIfThenExpression.getIfExpression();
					
					// check the else if expression
					if (evaluateExpression(elseIfExpression, bibTeXEntry)) {
						for (BCCEvaluationExpression thenExpression : elseIfThenExpression.getThenExpressions()) {
							evaluateEvaluationExpression(thenExpression, bibTeXEntry);
						}
						return;
					}
				}
				
				// execute the else expression
				for (BCCEvaluationExpression elseExpression : ifThenElseExpression.getElseExpressions()) {
					evaluateEvaluationExpression(elseExpression, bibTeXEntry);
				}
			}
		}
	}

	private void evaluateEvaluationExpression(BCCEvaluationExpression evaluationExpression, BCCAbstractBibTeXEntry bibTeXEntry) {
		if (!returnFromRule) {
			if (evaluationExpression instanceof BCCIfThenElseExpression) {
				BCCIfThenElseExpression ifThenElseExpression = (BCCIfThenElseExpression) evaluationExpression;
				evaluateIfThenElseExpression(ifThenElseExpression, bibTeXEntry);
			}
			else if (evaluationExpression instanceof BCCAbstractMarkerExpression) {
				BCCAbstractMarkerExpression markerExpression = (BCCAbstractMarkerExpression) evaluationExpression;
				generateMarker(markerExpression, bibTeXEntry);
			}
			else if (evaluationExpression instanceof BCCReturnExpression) {
				returnFromRule = true;
			}
			else {
				throw new UnsupportedOperationException("Encountered a situation that was not yet implemented!");
			}
		}
	}

	private void generateMarker(BCCAbstractMarkerExpression markerExpression, BCCAbstractBibTeXEntry bibTeXEntry)
	{
		IResource problematicResource = BCCResourceUtil.getIFile(bibTeXEntry);
		IResource identifyingRuleResource = BCCResourceUtil.getIFile(evaluatedConsistencyRule);
		BCCSingleFieldSelectionExpression fieldSelectionExpression = markerExpression.getCausingElement();
		BCCEntryBody causingEntryBody = bibTeXEntry.getEntryBody();
		String message = identifyingRuleResource.getName() + ": " + markerExpression.getMessage();
		
		if (markerExpression instanceof BCCWarningMarkerExpression) {
			if (fieldSelectionExpression == null) {
				BCCAnalysis.createConsistencyProblemWarningMarker(problematicResource, message, causingEntryBody, BCCBibTeXPackage.Literals.BCC_ENTRY_BODY__ENTRY_KEY_OBJECT);
			}
			else {
				BCCAbstractEntryBodyField field = identifyField(fieldSelectionExpression.getSelection(), bibTeXEntry);
				int index = BCCBibTeXUtil.identifyFieldPosition(bibTeXEntry, field);
				BCCAnalysis.createConsistencyProblemWarningMarker(problematicResource, message, causingEntryBody, BCCBibTeXPackage.Literals.BCC_ENTRY_BODY__FIELDS, index);
			}
		}
		else if (markerExpression instanceof BCCErrorMarkerExpression) {
			if (fieldSelectionExpression == null) {
				BCCAnalysis.createConsistencyProblemErrorMarker(problematicResource, message, causingEntryBody, BCCBibTeXPackage.Literals.BCC_ENTRY_BODY__ENTRY_KEY_OBJECT);
			}
			else {
				BCCAbstractEntryBodyField field = identifyField(fieldSelectionExpression.getSelection(), bibTeXEntry);
				int index = BCCBibTeXUtil.identifyFieldPosition(bibTeXEntry, field);
				BCCAnalysis.createConsistencyProblemErrorMarker(problematicResource, message, causingEntryBody, BCCBibTeXPackage.Literals.BCC_ENTRY_BODY__FIELDS, index);
			}
		}
	}

	private boolean evaluateExpression(BCCExpression expression, BCCAbstractBibTeXEntry bibTeXEntry) {
		if (!returnFromRule) {
			if (expression instanceof BCCNoKeywordExpression) {
				BCCNoKeywordExpression noKeywordExpression = (BCCNoKeywordExpression) expression;
				return evaluateNoKeywordExpression(noKeywordExpression, bibTeXEntry);
			}
			else if (expression instanceof BCCAndExpression) {
				BCCAndExpression andExpression = (BCCAndExpression) expression;
				return evaluateAndExpression(andExpression, bibTeXEntry);
			}
			else if (expression instanceof BCCOrExpression) {
				BCCOrExpression orExpression = (BCCOrExpression) expression;
				return evaluateOrExpression(orExpression, bibTeXEntry);
			}
			else if (expression instanceof BCCParenthesesExpression) {
				BCCParenthesesExpression parenthesesExpression = (BCCParenthesesExpression) expression;
				return evaluateExpression(parenthesesExpression.getExpression(), bibTeXEntry);
			}
			else if (expression instanceof BCCNotExpression) {
				BCCNotExpression notExpression = (BCCNotExpression) expression;
				return !evaluateExpression(notExpression.getExpression(), bibTeXEntry);
			}
			else if (expression instanceof BCCStringRelationalExpression) {
				BCCStringRelationalExpression stringRelationalExpression = (BCCStringRelationalExpression) expression;
				return evaluateStringRelationalExpression(stringRelationalExpression, bibTeXEntry);
			}
			else if (expression instanceof BCCIntegerRelationalExpression) {
				BCCIntegerRelationalExpression integerRelationalExpression = (BCCIntegerRelationalExpression) expression;
				return evaluateIntegerRelationalExpression(integerRelationalExpression, bibTeXEntry);
			}
			
			throw new UnsupportedOperationException("Encountered a situation that was not yet implemented!");
		}
		else {
			return false;
		}
	}

	private boolean evaluateNoKeywordExpression(BCCNoKeywordExpression noKeywordExpression, BCCAbstractBibTeXEntry bibTeXEntry) {
		BCCExpression leftExpression = noKeywordExpression.getLeft();
		BCCExpression rightExpression = noKeywordExpression.getRight();
		
		if (leftExpression instanceof BCCAbstractFieldSelectionExpression) {
			if (rightExpression instanceof BCCFieldExistsExpression || rightExpression instanceof BCCFieldsExistExpression) {
				BCCAbstractFieldSelectionExpression fieldSelectionExpression = (BCCAbstractFieldSelectionExpression) leftExpression;
				
				return evaluateFieldSelectionExpression(fieldSelectionExpression, bibTeXEntry);
			}
			else if (rightExpression instanceof BCCUsesReplacePatternExpression || rightExpression instanceof BCCUseReplacePatternExpression) {
				BCCAbstractFieldSelectionExpression fieldSelectionExpression = (BCCAbstractFieldSelectionExpression) leftExpression;
				
				return evaluateUsesReplacePatternExpression(fieldSelectionExpression, bibTeXEntry);
			}
			else if (rightExpression instanceof BCCIsIntegerExpression || rightExpression instanceof BCCAreIntegerExpression) {
				BCCAbstractFieldSelectionExpression fieldSelectionExpression = (BCCAbstractFieldSelectionExpression) leftExpression;
				
				return evaluateIsDigitExpression(fieldSelectionExpression, bibTeXEntry);
			}
		}
		
		throw new UnsupportedOperationException("Encountered a situation that was not yet implemented!");
	}

	private boolean evaluateOrExpression(BCCOrExpression orExpression, BCCAbstractBibTeXEntry bibTeXEntry) {
		BCCExpression leftExpression = orExpression.getLeft();
		BCCExpression rightExpression = orExpression.getRight();
		
		if (evaluateExpression(leftExpression, bibTeXEntry)) {
			return true;
		}
		else if (evaluateExpression(rightExpression, bibTeXEntry)) {
			return true;
		}
		else {
			return false;
		}
	}

	private boolean evaluateAndExpression(BCCAndExpression andExpression, BCCAbstractBibTeXEntry bibTeXEntry) {
		BCCExpression leftExpression = andExpression.getLeft();
		BCCExpression rightExpression = andExpression.getRight();
		
		if (evaluateExpression(leftExpression, bibTeXEntry)) {
			return evaluateExpression(rightExpression, bibTeXEntry);
		}
		else {
			return false;
		}
	}

	private boolean evaluateIntegerRelationalExpression(BCCIntegerRelationalExpression integerRelationalExpression,
			BCCAbstractBibTeXEntry bibTeXEntry)
	{
		BCCExpression leftExpression = integerRelationalExpression.getLeft();
		BCCExpression rightExpression = integerRelationalExpression.getRight();
		
		if (leftExpression instanceof BCCSingleFieldSelectionExpression && rightExpression instanceof BCCIntegerExpression) {
			BCCSingleFieldSelectionExpression singleFieldSelectionExpression = (BCCSingleFieldSelectionExpression) leftExpression;
			BCCIntegerExpression integerExpression = (BCCIntegerExpression) rightExpression;
			
			BCCAbstractSpecificFieldSelector fieldSelector = singleFieldSelectionExpression.getSelection();
			if (identifyField(fieldSelector, bibTeXEntry) != null) {
				int leftSideInteger = identifyIntegerFieldValue(fieldSelector, bibTeXEntry);
				int rightSideInteger = integerExpression.getValue();
				
				return evaluateIntegerRelation(leftSideInteger, integerRelationalExpression.getOperator(), rightSideInteger);
			}
		}
		
		throw new UnsupportedOperationException("Encountered a situation that was not yet implemented!");
	}

	private boolean evaluateStringRelationalExpression(BCCStringRelationalExpression stringRelationalExpression,
			BCCAbstractBibTeXEntry bibTeXEntry)
	{
		BCCExpression leftExpression = stringRelationalExpression.getLeft();
		BCCExpression rightExpression = stringRelationalExpression.getRight();
		
		if (leftExpression instanceof BCCSingleFieldSelectionExpression && rightExpression instanceof BCCStringExpression) {
			BCCSingleFieldSelectionExpression singleFieldSelectionExpression = (BCCSingleFieldSelectionExpression) leftExpression;
			BCCStringExpression stringExpression = (BCCStringExpression) rightExpression;
			
			BCCAbstractSpecificFieldSelector fieldSelector = singleFieldSelectionExpression.getSelection();
			if (identifyField(fieldSelector, bibTeXEntry) != null) {
				String leftSideString = identifyStringFieldValue(fieldSelector, bibTeXEntry);
				String rightSideString = stringExpression.getValue();
				
				return evaluateStringRelation(leftSideString, stringRelationalExpression.getOperator(), rightSideString);
			}
		}
		
		throw new UnsupportedOperationException("Encountered a situation that was not yet implemented!");
	}

	private boolean evaluateFieldSelectionExpression(BCCAbstractFieldSelectionExpression leftExpression,
			BCCAbstractBibTeXEntry bibTeXEntry)
	{
		if (leftExpression instanceof BCCSingleFieldSelectionExpression) {
			BCCSingleFieldSelectionExpression singleSelectionElement = (BCCSingleFieldSelectionExpression) leftExpression;
			BCCAbstractSpecificFieldSelector selector = singleSelectionElement.getSelection();
			
			return evaluateSelectedFieldExistsInBibTeXEntry(selector, bibTeXEntry);
		}
		else if (leftExpression instanceof BCCMultiFieldSelectionExpression) {
			BCCMultiFieldSelectionExpression multiSelectionElement = (BCCMultiFieldSelectionExpression) leftExpression;
			
			boolean returnValue = true;
			for (BCCAbstractSpecificFieldSelector selector : multiSelectionElement.getSelections()) {
				returnValue = returnValue && evaluateSelectedFieldExistsInBibTeXEntry(selector, bibTeXEntry);
			}
			
			return returnValue;
		}
		
		throw new UnsupportedOperationException("Encountered a situation that was not yet implemented!");
	}

	private boolean evaluateUsesReplacePatternExpression(BCCAbstractFieldSelectionExpression leftExpression,
			BCCAbstractBibTeXEntry bibTeXEntry)
	{
		if (leftExpression instanceof BCCSingleFieldSelectionExpression) {
			BCCSingleFieldSelectionExpression singleSelectionElement = (BCCSingleFieldSelectionExpression) leftExpression;
			BCCAbstractSpecificFieldSelector selector = singleSelectionElement.getSelection();
			
			return evaluateUsesReplacePatternExpression(selector, bibTeXEntry);
		}
		else if (leftExpression instanceof BCCMultiFieldSelectionExpression) {
			BCCMultiFieldSelectionExpression multiSelectionElement = (BCCMultiFieldSelectionExpression) leftExpression;
			
			boolean returnValue = true;
			for (BCCAbstractSpecificFieldSelector selector : multiSelectionElement.getSelections()) {
				returnValue = returnValue && evaluateUsesReplacePatternExpression(selector, bibTeXEntry);
			}
			
			return returnValue;
		}
		
		throw new UnsupportedOperationException("Encountered a situation that was not yet implemented!");
	}

	private boolean evaluateIsDigitExpression(BCCAbstractFieldSelectionExpression leftExpression,
			BCCAbstractBibTeXEntry bibTeXEntry)
	{
		if (leftExpression instanceof BCCSingleFieldSelectionExpression) {
			BCCSingleFieldSelectionExpression singleSelectionElement = (BCCSingleFieldSelectionExpression) leftExpression;
			BCCAbstractSpecificFieldSelector selector = singleSelectionElement.getSelection();
			
			return evaluateIsDigitExpression(selector, bibTeXEntry);
		}
		else if (leftExpression instanceof BCCMultiFieldSelectionExpression) {
			BCCMultiFieldSelectionExpression multiSelectionElement = (BCCMultiFieldSelectionExpression) leftExpression;
			
			boolean returnValue = true;
			for (BCCAbstractSpecificFieldSelector selector : multiSelectionElement.getSelections()) {
				returnValue = returnValue && evaluateIsDigitExpression(selector, bibTeXEntry);
			}
			
			return returnValue;
		}
		
		throw new UnsupportedOperationException("Encountered a situation that was not yet implemented!");
	}

	private boolean evaluateSelectedFieldExistsInBibTeXEntry(BCCAbstractSpecificFieldSelector selector,
			BCCAbstractBibTeXEntry bibTeXEntry)
	{
		return identifyWhetherSelectedFieldExists(selector, bibTeXEntry);
	}

	private boolean evaluateUsesReplacePatternExpression(BCCAbstractSpecificFieldSelector selector,
			BCCAbstractBibTeXEntry bibTeXEntry)
	{
		boolean exists = identifyWhetherSelectedFieldExists(selector, bibTeXEntry);
		
		if (exists) {
			BCCAbstractEntryBodyField field = identifyField(selector, bibTeXEntry);
			
			if (field instanceof BCCAbstractGenericField) {
				BCCAbstractGenericField genericField = (BCCAbstractGenericField) field;
				
				if (!BCCBibTeXUtil.usesReplacePattern(genericField)) {
					return false;
				}
			}
		}
		
		return true;
	}

	private boolean evaluateIsDigitExpression(BCCAbstractSpecificFieldSelector selector,
			BCCAbstractBibTeXEntry bibTeXEntry)
	{
		boolean exists = identifyWhetherSelectedFieldExists(selector, bibTeXEntry);
		
		if (exists) {
			BCCAbstractEntryBodyField field = identifyField(selector, bibTeXEntry);
			
			if (field instanceof BCCAbstractGenericField) {
				BCCAbstractGenericField genericField = (BCCAbstractGenericField) field;
				
				if (!BCCBibTeXUtil.isInteger(genericField)) {
					return false;
				}
			}
		}
		
		return true;
	}

	private boolean identifyWhetherSelectedFieldExists(BCCAbstractSpecificFieldSelector selector,
			BCCAbstractBibTeXEntry bibTeXEntry)
	{
		if (selector instanceof BCCTitleFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCTitleField.class);
		}
		else if (selector instanceof BCCEditionFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCEditionField.class);
		}
		else if (selector instanceof BCCUrlFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCUrlField.class);
		}
		else if (selector instanceof BCCInstitutionFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCInstitutionField.class);
		}
		else if (selector instanceof BCCOrganizationFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCOrganizationField.class);
		}
		else if (selector instanceof BCCRevisionFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCRevisionField.class);
		}
		else if (selector instanceof BCCIsbnFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCIsbnField.class);
		}
		else if (selector instanceof BCCDoiFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCDoiField.class);
		}
		else if (selector instanceof BCCAddressFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCAddressField.class);
		}
		else if (selector instanceof BCCTypeFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCTypeField.class);
		}
		else if (selector instanceof BCCNumberFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCNumberField.class);
		}
		else if (selector instanceof BCCVolumeFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCVolumeField.class);
		}
		else if (selector instanceof BCCKeywordsFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCKeywordsField.class);
		}
		else if (selector instanceof BCCNoteFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCNoteField.class);
		}
		else if (selector instanceof BCCPublisherFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCPublisherField.class);
		}
		else if (selector instanceof BCCJournalFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCJournalField.class);
		}
		else if (selector instanceof BCCBooktitleFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCBooktitleField.class);
		}
		else if (selector instanceof BCCSchoolFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCSchoolField.class);
		}
		else if (selector instanceof BCCSeriesFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCSeriesField.class);
		}
		else if (selector instanceof BCCChapterFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCChapterField.class);
		}
		else if (selector instanceof BCCVersionFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCVersionField.class);
		}
		else if (selector instanceof BCCHowPublishedFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCHowPublishedField.class);
		}
		else if (selector instanceof BCCMonthFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCMonthField.class);
		}
		else if (selector instanceof BCCDateFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCDateField.class);
		}
		else if (selector instanceof BCCYearFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCYearField.class);
		}
		else if (selector instanceof BCCPagesFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCPagesField.class);
		}
		else if (selector instanceof BCCAuthorFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCAuthorField.class);
		}
		else if (selector instanceof BCCEditorFieldSelector) {
			return BCCBibTeXUtil.bibTeXEntryContainsField(bibTeXEntry, BCCEditorField.class);
		}
		
		throw new UnsupportedOperationException("Encountered a situation that was not yet implemented!");
	}

	private BCCAbstractEntryBodyField identifyField(BCCAbstractSpecificFieldSelector selector, BCCAbstractBibTeXEntry bibTeXEntry) {
		if (selector instanceof BCCTitleFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCTitleField.class);
		}
		else if (selector instanceof BCCEditionFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCEditionField.class);
		}
		else if (selector instanceof BCCUrlFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCUrlField.class);
		}
		else if (selector instanceof BCCInstitutionFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCInstitutionField.class);
		}
		else if (selector instanceof BCCOrganizationFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCOrganizationField.class);
		}
		else if (selector instanceof BCCRevisionFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCRevisionField.class);
		}
		else if (selector instanceof BCCIsbnFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCIsbnField.class);
		}
		else if (selector instanceof BCCDoiFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCDoiField.class);
		}
		else if (selector instanceof BCCAddressFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCAddressField.class);
		}
		else if (selector instanceof BCCTypeFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCTypeField.class);
		}
		else if (selector instanceof BCCNumberFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCNumberField.class);
		}
		else if (selector instanceof BCCVolumeFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCVolumeField.class);
		}
		else if (selector instanceof BCCKeywordsFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCKeywordsField.class);
		}
		else if (selector instanceof BCCNoteFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCNoteField.class);
		}
		else if (selector instanceof BCCPublisherFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCPublisherField.class);
		}
		else if (selector instanceof BCCJournalFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCJournalField.class);
		}
		else if (selector instanceof BCCBooktitleFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCBooktitleField.class);
		}
		else if (selector instanceof BCCSchoolFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCSchoolField.class);
		}
		else if (selector instanceof BCCSeriesFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCSeriesField.class);
		}
		else if (selector instanceof BCCChapterFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCChapterField.class);
		}
		else if (selector instanceof BCCVersionFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCVersionField.class);
		}
		else if (selector instanceof BCCHowPublishedFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCHowPublishedField.class);
		}
		else if (selector instanceof BCCMonthFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCMonthField.class);
		}
		else if (selector instanceof BCCDateFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCDateField.class);
		}
		else if (selector instanceof BCCYearFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCYearField.class);
		}
		else if (selector instanceof BCCPagesFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCPagesField.class);
		}
		else if (selector instanceof BCCAuthorFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCAuthorField.class);
		}
		else if (selector instanceof BCCEditorFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCEditorField.class);
		}
		
		throw new UnsupportedOperationException("Encountered a situation that was not yet implemented!");
	}

	private int identifyIntegerFieldValue(BCCAbstractSpecificFieldSelector selector, BCCAbstractBibTeXEntry bibTeXEntry) {
		if (selector instanceof BCCMonthFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCMonthField.class).getMonth();
		}
		else if (selector instanceof BCCDateFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCDateField.class).getDate();
		}
		else if (selector instanceof BCCYearFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCYearField.class).getYear();
		}
		
		throw new UnsupportedOperationException("Encountered a situation that was not yet implemented!");
	}

	private String identifyStringFieldValue(BCCAbstractSpecificFieldSelector selector, BCCAbstractBibTeXEntry bibTeXEntry) {
		if (selector instanceof BCCTitleFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCTitleField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCEditionFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCEditionField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCUrlFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCUrlField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCInstitutionFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCInstitutionField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCOrganizationFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCOrganizationField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCRevisionFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCRevisionField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCIsbnFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCIsbnField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCDoiFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCDoiField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCAddressFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCAddressField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCTypeFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCTypeField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCNumberFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCNumberField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCVolumeFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCVolumeField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCKeywordsFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCKeywordsField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCNoteFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCNoteField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCPublisherFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCPublisherField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCJournalFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCJournalField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCBooktitleFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCBooktitleField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCSchoolFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCSchoolField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCSeriesFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCSeriesField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCChapterFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCChapterField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCVersionFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCVersionField.class).getFieldValueObject().getFieldValue();
		}
		else if (selector instanceof BCCHowPublishedFieldSelector) {
			return BCCBibTeXUtil.identifyField(bibTeXEntry, BCCHowPublishedField.class).getFieldValueObject().getFieldValue();
		}
		
		throw new UnsupportedOperationException("Encountered a situation that was not yet implemented!");
	}
	
	private boolean evaluateIntegerRelation(int leftSideInteger, BCCIntegerRelationalOperator operator, int rightSideInteger) {
		if (operator == BCCIntegerRelationalOperator.GREATER) {
			return leftSideInteger > rightSideInteger;
		}
		else if (operator == BCCIntegerRelationalOperator.SMALLER) {
			return leftSideInteger < rightSideInteger;
		}
		else if (operator == BCCIntegerRelationalOperator.GREATER_OR_EQUAL) {
			return leftSideInteger >= rightSideInteger;
		}
		else if (operator == BCCIntegerRelationalOperator.SMALLER_OR_EQUAL) {
			return leftSideInteger <= rightSideInteger;
		}
		else if (operator == BCCIntegerRelationalOperator.EQUAL) {
			return leftSideInteger == rightSideInteger;
		}
		else {
			return leftSideInteger != rightSideInteger;
		}
	}

	private boolean evaluateStringRelation(String leftSideString, BCCStringRelationalOperator operator, String rightSideString) {
		if (operator == BCCStringRelationalOperator.EQUALS) {
			return leftSideString.equals(rightSideString);
		}
		else if (operator == BCCStringRelationalOperator.STARTS_WITH) {
			return leftSideString.startsWith(rightSideString);
		}
		else if (operator == BCCStringRelationalOperator.ENDS_WITH) {
			return leftSideString.endsWith(rightSideString);
		}
		else {
			return leftSideString.contains(rightSideString);
		}
	}

}
