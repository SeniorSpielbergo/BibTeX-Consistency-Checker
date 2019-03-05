package de.david_wille.bibtexconsistencychecker.bibtex.validation

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractEntryBodyField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractGenericField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXPackage
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEntryBody
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCMonthField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplacePatternEntry
import de.david_wille.bibtexconsistencychecker.bibtex.cache.BCCBibTeXCache
import de.david_wille.bibtexconsistencychecker.bibtex.util.BCCBibTeXUtil
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil
import java.util.HashSet
import java.util.List
import java.util.Set
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProject
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.validation.Check

/**
 * This class contains custom validation rules. 
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
class BCCBibTeXValidator extends AbstractBCCBibTeXValidator {
	
	public static val String SAME_FIELD_MULTIPLE_TIMES = "SAME_FIELD_MULTIPLE_TIMES"
	public static val String MONTH_FIELD_GREATER_TWELVE = "MONTH_FIELD_GREATER_TWELVE"
	public static val String FILE_NOT_IN_CORRECT_FOLDER = "FILE_NOT_IN_CORRECT_FOLDER"
	public static val String REPLACE_PATTERN_DOES_NOT_EXIST = "REPLACE_PATTERN_DOES_NOT_EXIST"
	public static val String ENTRY_KEY_EXISTS_MULTIPLE_TIMES = "ENTRY_KEY_EXISTS_MULTIPLE_TIMES"
	public static val String REPLACE_KEY_EXISTS_MULTIPLE_TIMES = "REPLACE_KEY_EXISTS_MULTIPLE_TIMES"
	
	@Check(NORMAL)
	def void updateBibTeXCache(BCCBibTeXFile bibTeXFile) {
		BCCBibTeXCache.instance.cacheBibTeXFile(BCCResourceUtil.getIProject(bibTeXFile), bibTeXFile);
	}
	
	@Check(NORMAL)
	def void checkNoDuplicateEntryKeysExist(BCCEntryBody entryBody) {
		var IProject project = BCCResourceUtil.getIProject(entryBody);
		var String entryKey = entryBody.entryKeyObject.entryKey
		
		var List<BCCAbstractBibTeXEntry> foundEntries = BCCBibTeXCache.instance.getEntries(project, entryKey)
		
		if (foundEntries.size() > 1) {
			var String resourcesString = generateResourcesString(foundEntries)
			error("Multiple entries with entry key \"" + entryKey + "\" exist" + resourcesString, entryBody, BCCBibTeXPackage.Literals.BCC_ENTRY_BODY__ENTRY_KEY_OBJECT, ENTRY_KEY_EXISTS_MULTIPLE_TIMES)
		}
	}
	
	@Check(NORMAL)
	def void checkNoDuplicateReplacePatternExist(BCCReplacePatternEntry replacePatternEntry) {
		var IProject project = BCCResourceUtil.getIProject(replacePatternEntry);
		var String replaceKey = replacePatternEntry.replaceKeyObject.replaceKey
		
		var List<BCCReplacePatternEntry> foundEntries = BCCBibTeXCache.instance.getReplacePattern(project, replaceKey)
		
		if (foundEntries.size() > 1) {
			var String resourcesString = generateResourcesString(foundEntries)
			error("Multiple entries with replace key \"" + replaceKey + "\" exist" + resourcesString, BCCBibTeXPackage.Literals.BCC_REPLACE_PATTERN_ENTRY__REPLACE_KEY_OBJECT, REPLACE_KEY_EXISTS_MULTIPLE_TIMES)
		}
	}
	
	protected def generateResourcesString(List<? extends EObject> containingResources) {
		var String containingResourcesString = ""
		var Set<String> distinctResources = new HashSet<String>();
		for (EObject containingResource : containingResources) {
			distinctResources.add(BCCResourceUtil.getIFile(containingResource).name)
		}
		
		if (distinctResources.size > 0) {
			containingResourcesString += " in "
			
			var int i = 0
			for (String containingResource : distinctResources) {
				if (i > 0 && i < distinctResources.size-1) {
					containingResourcesString += ", "
				}
				else if (i > 0 && i == distinctResources.size-1) {
					containingResourcesString += " and "
				}
				containingResourcesString += containingResource
				i++
			}
		}
		
		containingResourcesString += "."
		
		containingResourcesString
	}
	
	@Check(NORMAL)
	def void checkReplacePatternExist(BCCAbstractGenericField genericField) {
		if (BCCBibTeXUtil.usesReplacePattern(genericField)) {
			var IProject project = BCCResourceUtil.getIProject(genericField)
			var String fieldValue = genericField.fieldValueObject.fieldValue
			
			if (BCCBibTeXCache.instance.getReplacePattern(project, fieldValue).size == 0) {
				error("The replace pattern \"" + fieldValue + "\" was never specified.", BCCBibTeXPackage.Literals.BCC_ABSTRACT_GENERIC_FIELD__FIELD_VALUE_OBJECT, REPLACE_PATTERN_DOES_NOT_EXIST)
			}
		}
	}
	
	@Check
	def checkFileIsStoredInBibliographyFolder(BCCBibTeXFile bibTeXFile) {
		var IFile file = BCCResourceUtil.getIFile(bibTeXFile)
		var IProject project = BCCResourceUtil.getIProject(bibTeXFile)
		
		if (!file.fullPath.toPortableString.startsWith(project.fullPath.toPortableString + "/bibliography")) {
			warning("This BibTeX file will never be processed as it is not a sub file of the \"bibliography\" folder.", BCCBibTeXPackage.Literals.BCC_BIB_TE_XFILE__ENTRIES, FILE_NOT_IN_CORRECT_FOLDER)
		}
	}
	
	@Check
	def void checkEachFieldTypeOnlyExistsOnce(BCCEntryBody entryBody) {
		var Set<Class<?>> processedFields = new HashSet<Class<?>>()
		
		var int positionOfProcessedField = 0
		for (BCCAbstractEntryBodyField processedField : entryBody.fields) {
			if (!processedFields.contains(processedField.class)) {
				var boolean foundAdditionalField = false
				for (var i = positionOfProcessedField; i < entryBody.fields.length; i++) {
					var BCCAbstractEntryBodyField entryBodyField = entryBody.fields.get(i)
					
					if (!entryBodyField.equals(processedField)) {
						if (entryBodyField.class.equals(processedField.class)) {
							foundAdditionalField = true
							error("This field has already been specified for this entry. Each field can only exist once per entry.", entryBodyField.eContainingFeature, i, SAME_FIELD_MULTIPLE_TIMES)
						}
					}
				}
				
				if (foundAdditionalField) {
					error("This field has already been specified for this entry. Each field can only exist once per entry.", processedField.eContainingFeature, positionOfProcessedField, SAME_FIELD_MULTIPLE_TIMES)
				}
				
				processedFields.add(processedField.class)
			}
			
			positionOfProcessedField++
		}
	}
	
	@Check
	def void checkLegalMonth(BCCMonthField monthField) {
		if (monthField.month < 1 || monthField.month > 12) {
			error("No legal month. The value has to be between 1 and 12.", BCCBibTeXPackage.Literals.BCC_MONTH_FIELD__MONTH, MONTH_FIELD_GREATER_TWELVE)
		}
	}
	
}
