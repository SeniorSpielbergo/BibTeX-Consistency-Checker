package de.david_wille.bibtexconsistencychecker;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import de.david_wille.bibtexconsistencychecker.analysis.BCCAnalysis;
import de.david_wille.bibtexconsistencychecker.bibtex.BCCBibTeXStandaloneSetup;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.consistencyrule.BCCConsistencyRuleStandaloneSetup;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRule;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCFilePathEntry;
import de.david_wille.bibtexconsistencychecker.nature.BCCProjectNature;
import de.david_wille.bibtexconsistencychecker.util.BCCProblemMarkerHandling;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;

public class BCCLauncher {
	
	private static final String BCC_RULE_FILE_EXTENSION = "bcc_rule";
	private static final String BIB_FILE_EXTENSION = "bib";
	private BCCExecutionModel executionModel;
	private List<BCCBibTeXFile> parsedBibTeXFiles;
	private List<BCCConsistencyRule> parsedConsistencyRules;
	private IFolder bibliographyFolder;
	private IFolder rulesFolder;
	
	public BCCLauncher(BCCExecutionModel executionModel) throws CoreException, BCCLaunchPreparationException
	{
		bibliographyFolder = identifyChildFolder(BCCResourceUtil.getIProject(executionModel), BCCProjectNature.BIBLIOGRAPHY_FOLDER);
		rulesFolder = identifyChildFolder(BCCResourceUtil.getIProject(executionModel), BCCProjectNature.RULES_FOLDER);
		
		checkAndStoreExecutionModel(executionModel);
		
		parseAndCheckAllRelevantBibTeXFiles(executionModel);
		
		parseAndCheckAllConsistencyRuleFiles(executionModel);
	}

	private void checkAndStoreExecutionModel(BCCExecutionModel executionModel) throws BCCLaunchPreparationException, CoreException {
		if (!BCCProblemMarkerHandling.resourceHasErrorProblemMarkers(executionModel)) {
			this.executionModel = executionModel;
		}
		else {
			throw new BCCLaunchPreparationException("Cannot execute the consistency checks due to errors in the execution model!");
		}
	}

	private void parseAndCheckAllRelevantBibTeXFiles(BCCExecutionModel executionModel) throws CoreException, BCCLaunchPreparationException {
		List<BCCBibTeXFile> parsedBibTeXFiles = identifyAndParseAllRelevantBibTeXFiles(executionModel);
		
		if (!BCCProblemMarkerHandling.resourcesHaveProblemMarkers(parsedBibTeXFiles)) {
			this.parsedBibTeXFiles = parsedBibTeXFiles;
		}
		else {
			throw new BCCLaunchPreparationException("Cannot execute the consistency checks due to errors in the BibTeX files!");
		}
	}

	private void parseAndCheckAllConsistencyRuleFiles(BCCExecutionModel executionModel) throws CoreException, BCCLaunchPreparationException {
		List<BCCConsistencyRule> parsedConsistencyRules = identifyAndParseAllRelevantConsistencyRules(executionModel);
		
		if (!BCCProblemMarkerHandling.resourcesHaveProblemMarkers(parsedConsistencyRules)) {
			this.parsedConsistencyRules = parsedConsistencyRules;
		}
		else {
			throw new BCCLaunchPreparationException("Cannot execute the consistency checks due to errors in the consistency rules!");
		}
	}

	public void launch() {
		BCCAnalysis analysis = new BCCAnalysis(executionModel, parsedConsistencyRules, parsedBibTeXFiles);
		analysis.startAnalysis();
	}

	private List<BCCBibTeXFile> identifyAndParseAllRelevantBibTeXFiles(BCCExecutionModel executionModel) throws CoreException {
		List<BCCFilePathEntry> explicitlySelectedFiles = new ArrayList<>();
		if (explicitBibTeXFileSelectionExists(executionModel)) {
			explicitlySelectedFiles = executionModel.getBibTeXFilesEntry().getEntries();
		}
		
		List<IFile> bibliographyFiles = identifyRelevantFiles(executionModel, explicitlySelectedFiles, bibliographyFolder, BIB_FILE_EXTENSION);
		
		BCCAnalysis.clearAllExistingConsistencyProblemMarkers(bibliographyFiles);
		
		return BCCResourceUtil.parseModels(new BCCBibTeXStandaloneSetup(), bibliographyFiles);
	}

	private List<BCCConsistencyRule> identifyAndParseAllRelevantConsistencyRules(BCCExecutionModel executionModel) throws CoreException {
		List<BCCFilePathEntry> explicitlySelectedFiles = new ArrayList<>();
		if (explicitConsistencyRuleSelectionExists(executionModel)) {
			explicitlySelectedFiles = executionModel.getRulesEntry().getEntries();
		}
		
		List<IFile> consistencyRuleFiles = identifyRelevantFiles(executionModel, explicitlySelectedFiles, rulesFolder, BCC_RULE_FILE_EXTENSION);
		
		BCCAnalysis.clearAllExistingConsistencyProblemMarkers(consistencyRuleFiles);
		
		return BCCResourceUtil.parseModels(new BCCConsistencyRuleStandaloneSetup(), consistencyRuleFiles);
	}

	private List<IFile> identifyRelevantFiles(BCCExecutionModel executionModel,
			List<BCCFilePathEntry> explicitlySelectedFiles, IFolder folder, String fileExtension) throws CoreException
	{
		List<IFile> relevantFiles = new ArrayList<>();
		
		if (explicitlySelectedFiles.size() > 0) {
			relevantFiles = collectExplicitlySelectedFiles(executionModel, folder, fileExtension, explicitlySelectedFiles);
		}
		else {
			relevantFiles = BCCResourceUtil.recursivelyCollectAllFiles(folder, fileExtension);
		}
		
		return relevantFiles;
	}

	private List<IFile> collectExplicitlySelectedFiles(BCCExecutionModel executionModel, IFolder folder, String fileExtension,
			List<BCCFilePathEntry> entries)
	{
		List<IFile> relevantFiles = new ArrayList<>();
		
		for (BCCFilePathEntry entry : entries) {
			IResource referencedResource = identifyResourceForEntry(folder, entry);
			
			if (referencedResource instanceof IFolder) {
				IFolder referencedFolder = (IFolder) referencedResource;
				
				List<IFile> foundFiles = identifyFiles(executionModel, referencedFolder, fileExtension);
				
				for (IFile foundFile : foundFiles) {
					if (!relevantFiles.contains(foundFile)) {
						relevantFiles.add(foundFile);
					}
				}
			}
			else if (referencedResource instanceof IFile){
				IFile referencedFile = (IFile) referencedResource;
				relevantFiles.add(referencedFile);
			}
		}
		
		return relevantFiles;
	}

	private IResource identifyResourceForEntry(IFolder folder, BCCFilePathEntry entry) {
		String searchedPath = entry.getPath().replace(folder.getName() + "/", "");
		
		if (searchedPath.equals("")) {
			return folder;
		}
		else {
			return folder.findMember(searchedPath);
		}
	}

	private List<IFile> identifyFiles(BCCExecutionModel executionModel, IFolder folder, String fileExtension) {
		List<IFile> files = new ArrayList<>();
		
		List<IResource> resourceChildren = BCCResourceUtil.getChildResources(folder);
		
		for (IResource resourceChild : resourceChildren) {
			if (resourceChild instanceof IFile) {
				IFile file = (IFile) resourceChild;
				
				if (file.getFileExtension().equals(fileExtension)) {
					files.add(file);
				}
			}
		}
		
		return files;
	}

	private boolean explicitConsistencyRuleSelectionExists(BCCExecutionModel executionModel) {
		return executionModel.getRulesEntry() != null;
	}

	private boolean explicitBibTeXFileSelectionExists(BCCExecutionModel executionModel) {
		return executionModel.getBibTeXFilesEntry() != null;
	}

	private IFolder identifyChildFolder(IProject project, String folderName) {
		for (IResource projectResource : BCCResourceUtil.getChildResources(project)) {
			if (projectResource instanceof IFolder) {
				if (projectResource.getName().equals(folderName)) {
					return (IFolder) projectResource;
				}
			}
		}
		
		return null;
	}

}
