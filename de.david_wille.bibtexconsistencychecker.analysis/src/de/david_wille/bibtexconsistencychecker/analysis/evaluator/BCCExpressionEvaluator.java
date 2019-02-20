package de.david_wille.bibtexconsistencychecker.analysis.evaluator;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

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
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAbstractSpecificFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAddressFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAndExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCAuthorFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCBooktitleFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCChapterFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRule;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCDateFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCDoiFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCEditionFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCEditorFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCFieldExistsExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCFieldsExistExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCHowPublishedFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCInstitutionFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCIntegerRelationalExpression;
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
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCRevisionFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCSchoolFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCSeriesFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCSingleFieldSelectionExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCStringRelationalExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCTitleFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCTypeFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCUrlFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCUseReplacePatternExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCUsesReplacePatternExpression;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCVersionFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCVolumeFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCYearFieldSelector;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.IntegerRelationalOperator;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.StringRelationalOperator;
import de.david_wille.bibtexconsistencychecker.util.BCCMarkerHandling;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;

public class BCCExpressionEvaluator {
	
	private BCCMarkerHandling markerFactory;
	private BCCConsistencyRule evaluatedConsistencyRule;
	
	public BCCExpressionEvaluator() {
		markerFactory = new BCCMarkerHandling();
	}

	public void evaluateExpression(BCCConsistencyRule evaluatedConsistencyRule, BCCAbstractBibTeXEntry bibTeXEntry) {
		this.evaluatedConsistencyRule = evaluatedConsistencyRule;
		BCCExpression expression = evaluatedConsistencyRule.getRuleBody().getChecksThatExpression();
		
		boolean positivelyEvaluated = evaluateExpression(expression, bibTeXEntry);
		
//		System.out.println(positivelyEvaluated);
	}

	private boolean evaluateExpression(BCCExpression expression, BCCAbstractBibTeXEntry bibTeXEntry) {
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
		}
		
		throw new UnsupportedOperationException("Encountered a situation that was not yet implemented!");
	}

	private boolean evaluateOrExpression(BCCOrExpression orExpression, BCCAbstractBibTeXEntry bibTeXEntry) {
		BCCExpression leftExpression = orExpression.getLeft();
		BCCExpression rightExpression = orExpression.getRight();
		
		boolean leftExpressionEvaluationResult = evaluateExpression(leftExpression, bibTeXEntry);
		boolean rightExpressionEvaluationResult = evaluateExpression(rightExpression, bibTeXEntry);
		
		return leftExpressionEvaluationResult || rightExpressionEvaluationResult;
	}

	private boolean evaluateAndExpression(BCCAndExpression andExpression, BCCAbstractBibTeXEntry bibTeXEntry) {
		BCCExpression leftExpression = andExpression.getLeft();
		BCCExpression rightExpression = andExpression.getRight();
		
		boolean leftExpressionEvaluationResult = evaluateExpression(leftExpression, bibTeXEntry);
		boolean rightExpressionEvaluationResult = evaluateExpression(rightExpression, bibTeXEntry);
		
		return leftExpressionEvaluationResult && rightExpressionEvaluationResult;
	}

	private boolean evaluateStringRelationalExpression(BCCStringRelationalExpression stringRelationalExpression,
			BCCAbstractBibTeXEntry bibTeXEntry)
	{
		throw new UnsupportedOperationException("Encountered a situation that was not yet implemented!");
	}

	private boolean evaluateIntegerRelationalExpression(BCCIntegerRelationalExpression integerRelationalExpression,
			BCCAbstractBibTeXEntry bibTeXEntry)
	{
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

	private boolean evaluateSelectedFieldExistsInBibTeXEntry(BCCAbstractSpecificFieldSelector selector,
			BCCAbstractBibTeXEntry bibTeXEntry)
	{
		boolean exists = identifyWhetherSelectedFieldExists(selector, bibTeXEntry);
		
		if (!exists) {
			generateErrorMarker(bibTeXEntry, bibTeXEntry.getEntryBody(), BCCBibTeXPackage.Literals.BCC_ENTRY_BODY__ENTRY_KEY, "The required \"" + selector.getFieldKeyword() + "\" field does not exist.");
		}
		
		return exists;
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
					int fieldPosition = BCCBibTeXUtil.identifyFieldPosition(bibTeXEntry, genericField);
					generateErrorMarker(bibTeXEntry, bibTeXEntry.getEntryBody(), BCCBibTeXPackage.Literals.BCC_ENTRY_BODY__FIELDS, fieldPosition, "The field \"" + selector.getFieldKeyword() + "\" does not use a replace pattern.");
					return false;
				}
			}
		}
		
		return true;
	}

	private void generateErrorMarker(BCCAbstractBibTeXEntry bibTeXEntry, EObject errorMarkerEObject, EStructuralFeature markedFeature, String errorMessage) {
		IResource problematicResource = BCCResourceUtil.getIFile(bibTeXEntry);
		IResource identifyingRuleResource = BCCResourceUtil.getIFile(evaluatedConsistencyRule);
		markerFactory.createErrorMarker(problematicResource, identifyingRuleResource.getName() + ": " + errorMessage, errorMarkerEObject, markedFeature);
	}

	private void generateErrorMarker(BCCAbstractBibTeXEntry bibTeXEntry, EObject errorMarkerEObject, EStructuralFeature markedFeature, int listPosition, String errorMessage) {
		IResource problematicResource = BCCResourceUtil.getIFile(bibTeXEntry);
		IResource identifyingRuleResource = BCCResourceUtil.getIFile(evaluatedConsistencyRule);
		markerFactory.createErrorMarker(problematicResource, identifyingRuleResource.getName() + ": " + errorMessage, errorMarkerEObject, markedFeature, listPosition);
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
		else {
			throw new UnsupportedOperationException("Encountered a situation that was not yet implemented!");
		}
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
		else {
			throw new UnsupportedOperationException("Encountered a situation that was not yet implemented!");
		}
	}
	
	private boolean evaluateIntegerRelation(int leftSideInteger, IntegerRelationalOperator operator, int rightSideInteger) {
		if (operator == IntegerRelationalOperator.GREATER) {
			return leftSideInteger > rightSideInteger;
		}
		else if (operator == IntegerRelationalOperator.SMALLER) {
			return leftSideInteger < rightSideInteger;
		}
		else if (operator == IntegerRelationalOperator.GREATER_OR_EQUAL) {
			return leftSideInteger >= rightSideInteger;
		}
		else if (operator == IntegerRelationalOperator.SMALLER_OR_EQUAL) {
			return leftSideInteger <= rightSideInteger;
		}
		else if (operator == IntegerRelationalOperator.EQUAL) {
			return leftSideInteger == rightSideInteger;
		}
		else {
			return leftSideInteger != rightSideInteger;
		}
	}

	private boolean evaluateStringRelation(String leftSideString, StringRelationalOperator operator, String rightSideString) {
		if (operator == StringRelationalOperator.EQUALS) {
			return leftSideString.equals(rightSideString);
		}
		else if (operator == StringRelationalOperator.STARTS_WITH) {
			return !leftSideString.startsWith(rightSideString);
		}
		else if (operator == StringRelationalOperator.STARTS_WITH) {
			return !leftSideString.endsWith(rightSideString);
		}
		else {
			return !leftSideString.contains(rightSideString);
		}
	}

}
