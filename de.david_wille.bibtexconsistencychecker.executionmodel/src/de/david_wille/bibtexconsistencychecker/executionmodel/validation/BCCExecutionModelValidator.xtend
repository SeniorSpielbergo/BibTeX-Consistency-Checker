package de.david_wille.bibtexconsistencychecker.executionmodel.validation

import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCBibTeXFilesEntry
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCConsistencyRulesEntry
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModelPackage
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCFilePathEntry
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil
import java.util.List
import org.eclipse.core.resources.IContainer
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IFolder
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IResource
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.validation.Check

/**
 * This class contains custom validation rules. 
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
class BCCExecutionModelValidator extends AbstractBCCExecutionModelValidator {
	
	public static val RULES_FOLDER = "rules"
	public static val BIBLIOGRAPHY_FOLDER = "bibliography"
	
	public static val String INVALID_FILE_PATH = "INVALID_FILE_PATH"
	public static val String RULES_FOLDER_DOES_NOT_EXIST = "RULES_FOLDER_DOES_NOT_EXIST"
	public static val String BIBLIOGRAPHY_FOLDER_DOES_NOT_EXIST = "BIBLIOGRAPHY_FOLDER_DOES_NOT_EXIST"
	
	@Check
	def checkMandatoryFoldersExist(BCCExecutionModel executionModel) {
		var IProject project = BCCResourceUtil.getIProject(executionModel)
		
		var boolean rulesFolderExists = false
		var boolean bibliographyFolderExists = false
		
		for (IResource resource : BCCResourceUtil.getChildResources(project)) {
			if (resource instanceof IFolder) {
				if (resource.name.equals(RULES_FOLDER)) {
					rulesFolderExists = true;
				}
				else if (resource.name.equals(BIBLIOGRAPHY_FOLDER)) {
					bibliographyFolderExists = true;
				}
			}
		}
		
		if (!rulesFolderExists) {
			error("The mandatory \"rules\" folder does not exist.", BCCExecutionModelPackage.Literals.BCC_EXECUTION_MODEL__SETTINGS_ENTRY, RULES_FOLDER_DOES_NOT_EXIST)
		}
		if (!bibliographyFolderExists) {
			error("The mandatory \"bibliography\" folder does not exist.", BCCExecutionModelPackage.Literals.BCC_EXECUTION_MODEL__SETTINGS_ENTRY, BIBLIOGRAPHY_FOLDER_DOES_NOT_EXIST)
		}
	}

	@Check(NORMAL)
	def checkForInvalidFilePaths(BCCFilePathEntry filePathEntry) {
		if (!pathExists(filePathEntry)) {
			if (isBibTeXFile(filePathEntry)) {
				error("The specified *.bib file does not exist.", BCCExecutionModelPackage.Literals.BCC_FILE_PATH_ENTRY__PATH, INVALID_FILE_PATH)
			}
			else if (isConsistencyRuleFile(filePathEntry)) {
				error("The specified *.bcc_rule file does not exist.", BCCExecutionModelPackage.Literals.BCC_FILE_PATH_ENTRY__PATH, INVALID_FILE_PATH)
			}
			else {
				error("The specified folder does not exist.", BCCExecutionModelPackage.Literals.BCC_FILE_PATH_ENTRY__PATH, INVALID_FILE_PATH)
			}
		}
		else if (isContainer(filePathEntry)) {
			var IFile fileContainingEntry = BCCResourceUtil.getIFile(filePathEntry)
			var IContainer parentContainer = fileContainingEntry.parent
			
			var IResource specifiedContainer = parentContainer.findMember(filePathEntry.path)
			
			var EObject eContainer = filePathEntry.eContainer
			
			if (eContainer instanceof BCCBibTeXFilesEntry) {
				if (!containsFileWithExtension(specifiedContainer, "bib")) {
					warning("The specified folder does not contain *.bib files.", BCCExecutionModelPackage.Literals.BCC_FILE_PATH_ENTRY__PATH, INVALID_FILE_PATH)
				}
			}
			else if (eContainer instanceof BCCConsistencyRulesEntry) {
				if (!containsFileWithExtension(specifiedContainer, "bcc_rule")) {
					warning("The specified folder does not contain *.bcc_rule files.", BCCExecutionModelPackage.Literals.BCC_FILE_PATH_ENTRY__PATH, INVALID_FILE_PATH)
				}
			}
		}
	}
	
	protected def containsFileWithExtension(IResource resource, String fileExtension) {
		var List<IResource> childResources = BCCResourceUtil.getChildResources(resource)
		
		for (IResource childResource : childResources) {
			if (childResource.fileExtension.equals(fileExtension)) {
				return true;
			}
		}
		
		false
	}
	
	protected def pathExists(BCCFilePathEntry filePathEntry) {
		var IFile fileContainingEntry = BCCResourceUtil.getIFile(filePathEntry)
		var IContainer parentContainer = fileContainingEntry.parent
		
		if (resourceExistsInContainer(parentContainer, filePathEntry.path)) {
			return true
		}
		
		false
	}
	
	protected def boolean resourceExistsInContainer(IContainer parentContainer, String searchedPath) {
		parentContainer.findMember(searchedPath) !== null
	}
	
	protected def boolean isConsistencyRuleFile(BCCFilePathEntry filePathEntry) {
		filePathEntry.path.endsWith("bcc_rule")
	}
	
	protected def boolean isBibTeXFile(BCCFilePathEntry filePathEntry) {
		filePathEntry.path.endsWith("bib")
	}
	
	protected def isContainer(BCCFilePathEntry filePathEntry) {
		!isConsistencyRuleFile(filePathEntry) && !isBibTeXFile(filePathEntry)
	}
	
}
