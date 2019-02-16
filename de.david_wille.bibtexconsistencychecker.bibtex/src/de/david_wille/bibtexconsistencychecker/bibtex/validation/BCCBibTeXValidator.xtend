package de.david_wille.bibtexconsistencychecker.bibtex.validation

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractEntryBodyField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXPackage
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEntryBody
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCTitleField
import java.util.HashMap
import java.util.Map
import org.eclipse.xtext.validation.Check

/**
 * This class contains custom validation rules. 
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
class BCCBibTeXValidator extends AbstractBCCBibTeXValidator {
	
	public static val String NO_DOUBLE_BRACES = "NO_DOUBLE_BRACES"
	public static val String SAME_FIELD_MULTIPLE_TIMES = "SAME_FIELD_MULTIPLE_TIMES"
	private static String OPENING_BRACE = "{"
	private static String CLOSING_BRACE = "}"
	
	@Check
	def void checkTitleFieldStartsAndEndsWithDoubleParenthesis(BCCTitleField titleField) {
		if (!titleField.fieldValue.startsWith(OPENING_BRACE + OPENING_BRACE) ||
			!titleField.fieldValue.endsWith(CLOSING_BRACE + CLOSING_BRACE))
		{
			warning("To ensure case sensitivity of titles you should enclose them in double braces \"{{ ... }}\"", BCCBibTeXPackage.Literals.BCC_ABSTRACT_GENERIC_FIELD__FIELD_VALUE, NO_DOUBLE_BRACES, titleField.fieldValue)
		}
	}
	
	@Check
	def void checkEachFieldTypeOnlyExistsOnce(BCCEntryBody entryBody) {
		var Map<Class<?>, Integer> entryBodyFields = new HashMap<Class<?>, Integer>();
		
		var int i = 0
		for (BCCAbstractEntryBodyField entryBodyField : entryBody.fields) {
			if (!entryBodyFields.containsKey(entryBodyField.class)) {
				entryBodyFields.put(entryBodyField.class, i)
			}
			else {
				error("This field has already been specified for this entry. Each field can only exist once per entry.", entryBodyField.eContainingFeature, i, SAME_FIELD_MULTIPLE_TIMES)
			}
			i++;
		}
	}
	
}
