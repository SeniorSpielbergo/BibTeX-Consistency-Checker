package de.david_wille.bibtexconsistencychecker.executionmodel.validation

import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCBibTeXPath
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModelPackage
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCRulePath
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil
import org.eclipse.core.resources.IContainer
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IResource
import org.eclipse.xtext.validation.Check

/**
 * This class contains custom validation rules. 
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
class BCCExecutionModelValidator extends AbstractBCCExecutionModelValidator {
	
	public static val String INVALID_CONSISTENCY_RULE_PATH = "INVALID_CONSISTENCY_RULE"
	public static val String INVALID_BIBTEX_FILE_PATH = "INVALID_BIBTEX_FILE_PATH"

	@Check
	def checkForInvalidConsistencyRulePaths(BCCRulePath filePath) {
		if (!pathExists(filePath)) {
			error("The specified *.bcc_rule file does not exist.", BCCExecutionModelPackage.Literals.BCC_RULE_PATH__PATH, INVALID_CONSISTENCY_RULE_PATH)
		}
	}

	@Check
	def checkForInvalidBibTeXFilePaths(BCCBibTeXPath filePath) {
		if (!pathExists(filePath)) {
			error("The specified *.bib file does not exist.", BCCExecutionModelPackage.Literals.BCC_BIB_TE_XPATH__PATH, INVALID_BIBTEX_FILE_PATH)
		}
	}
	
	protected def pathExists(BCCRulePath filePath) {
		var IFile modelFile = BCCResourceUtil.getIFile(filePath)
		var IContainer parentResource = modelFile.parent
		
		for (IResource childResource : parentResource.members) {
			if (BCCResourceUtil.resourceIsFile(childResource)) {
				var IFile childFile = childResource as IFile
				
				if (childFile.name.equals(filePath.path)) {
					return true
				}
			}
		}
		
		false
	}
	
	protected def pathExists(BCCBibTeXPath filePath) {
		var IFile modelFile = BCCResourceUtil.getIFile(filePath)
		var IContainer parentResource = modelFile.parent
		
		for (IResource childResource : parentResource.members) {
			if (BCCResourceUtil.resourceIsFile(childResource)) {
				var IFile childFile = childResource as IFile
				
				if (childFile.name.equals(filePath.path)) {
					return true
				}
			}
		}
		
		false
	}
	
}
