package de.david_wille.bibtexconsistencychecker;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;

import de.david_wille.bibtexconsistencychecker.analysis.BCCAnalysis;
import de.david_wille.bibtexconsistencychecker.bibtex.BCCBibTeXStandaloneSetup;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.consistencyrule.BCCConsistencyRuleStandaloneSetup;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRule;
import de.david_wille.bibtexconsistencychecker.executionmodel.BCCExecutionModelStandaloneSetup;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCBibTeXFilesEntry;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCConsistencyRulesEntry;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCFilePathEntry;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;

public class BCCLauncherResourceUtil {

	private static final String BCC_FILE_EXTENSION = "bcc";
	private static final String BCC_RULE_FILE_EXTENSION = "bcc_rule";
	private static final String BIB_FILE_EXTENSION = "bib";

	public static List<String> identifyAllFilesFromSelection(ISelection selection) {
		StructuredSelection structuredSelection = (StructuredSelection) selection;
		List<String> selectionPaths = new ArrayList<>();
		
		for(Object obj : structuredSelection.toList()) {
			if (obj instanceof IFile) {
				IFile file = (IFile) obj;
				String path = file.getFullPath().toString();
				selectionPaths.add(path);
			}
			else if (obj instanceof IFolder) {
				IFolder folder = (IFolder) obj;
				String path = folder.getFullPath().toString();
				selectionPaths.add(path);
			}
			else if (obj instanceof IProject) {
				IProject project = (IProject) obj;
				String path = project.getFullPath().toString();
				selectionPaths.add(path);
			}
		}
		
		return selectionPaths;
	}

	public static IFile findExecutionModel(IFile selectedFile) {
		IContainer parentContainer = selectedFile.getParent();
		try {
			for (IResource resource : parentContainer.members()) {
				if (resource.getFileExtension().equals(BCCResourceUtil.BCC_FILE_EXTENSION)) {
					return (IFile) resource;
				}
			}
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static List<BCCBibTeXFile> identifyAndParseAllRelevantBibTeXFiles(BCCExecutionModel executionModel) throws CoreException {
		List<BCCFilePathEntry> filePathEntries = new ArrayList<>();
		if (explicitBibTeXFileSelectionExists(executionModel)) {
			filePathEntries = executionModel.getBibTeXFilesEntry().getEntries();
		}
		
		List<IFile> identifiedBibliographyFiles = identifyRelevantFiles(executionModel, filePathEntries, BIB_FILE_EXTENSION);
		
		BCCAnalysis.clearAllExistingConsistencyProblemMarkers(identifiedBibliographyFiles);
		
		List<BCCBibTeXFile> parsedBibTeXFiles = BCCResourceUtil.parseModels(new BCCBibTeXStandaloneSetup(), identifiedBibliographyFiles);
		
		return parsedBibTeXFiles;
	}

	public static List<BCCConsistencyRule> identifyAndParseAllRelevantConsistencyRules(BCCExecutionModel executionModel) throws CoreException {
		List<BCCFilePathEntry> filePathEntries = new ArrayList<>();
		if (explicitConsistencyRuleSelectionExists(executionModel)) {
			filePathEntries = executionModel.getRulesEntry().getEntries();
		}
		
		List<IFile> identifiedConsistencyRuleFiles = identifyRelevantFiles(executionModel, filePathEntries, BCC_RULE_FILE_EXTENSION);
		
		BCCAnalysis.clearAllExistingConsistencyProblemMarkers(identifiedConsistencyRuleFiles);
		
		List<BCCConsistencyRule> parsedConsistencyRules = BCCResourceUtil.parseModels(new BCCConsistencyRuleStandaloneSetup(), identifiedConsistencyRuleFiles);
		
		return parsedConsistencyRules;
	}

	private static List<IFile> identifyRelevantFiles(BCCExecutionModel executionModel,
			List<BCCFilePathEntry> filePathEntries, String fileEnding) throws CoreException
	{
		List<IFile> relevantBibliographyFiles = new ArrayList<>();
		
		if (filePathEntries.size() > 0) {
			relevantBibliographyFiles = collectSelectedFiles(executionModel, filePathEntries);
		}
		else {
			IFile executionModelFile = BCCResourceUtil.getIFile(executionModel);
			IContainer parentContainer = executionModelFile.getParent();
			
			relevantBibliographyFiles = BCCResourceUtil.collectAllFilesInContainer(parentContainer, fileEnding);
		}
		
		return relevantBibliographyFiles;
	}

	private static List<IFile> collectSelectedFiles(BCCExecutionModel executionModel, List<BCCFilePathEntry> entries) {
		List<IFile> relevantFiles = new ArrayList<>();
		
		for (BCCFilePathEntry entry : entries) {
			String path = entry.getPath();
			
			if (isFolder(path)) {
				List<IFile> foundFiles = identifyFiles(executionModel, entry);
				
				for (IFile foundFile : foundFiles) {
					if (!relevantFiles.contains(foundFile)) {
						relevantFiles.add(foundFile);
					}
				}
			}
			else {
				IFile foundFile = identifyFile(executionModel, entry);
				
				if (foundFile != null && !relevantFiles.contains(foundFile)) {
					relevantFiles.add(foundFile);
				}
			}
		}
		
		return relevantFiles;
	}

	private static IFile identifyFile(BCCExecutionModel executionModel, BCCFilePathEntry entry) {
		String path = entry.getPath();
		
		IFile executionModelFile = BCCResourceUtil.getIFile(executionModel);
		IContainer parentContainer = executionModelFile.getParent();
		IResource resource = parentContainer.findMember(path);
		
		if (resource != null) {
			if (BCCResourceUtil.resourceIsFile(resource)) {
				IFile file = (IFile) resource;
				
				if (shouldTheFileBeIncludedInTheAnalysis(file, entry)) {
					return file;
				}
			}
		}
		
		return null;
	}

	private static List<IFile> identifyFiles(BCCExecutionModel executionModel, BCCFilePathEntry entry) {
		List<IFile> files = new ArrayList<>();
		String path = entry.getPath();
		
		IFile executionModelFile = BCCResourceUtil.getIFile(executionModel);
		IContainer parentContainer = executionModelFile.getParent();
		IResource resource = parentContainer.findMember(path);
		
		if (resource != null) {
			if (BCCResourceUtil.resourceIsFolder(resource)) {
				List<IResource> resourceChildren = BCCResourceUtil.getChildResources(resource);
				
				for (IResource resourceChild : resourceChildren) {
					if (BCCResourceUtil.resourceIsFile(resourceChild)) {
						IFile file = (IFile) resourceChild;
						
						if (shouldTheFileBeIncludedInTheAnalysis(file, entry)) {
							files.add(file);
						}
					}
				}
			}
		}
		
		return files;
	}

	private static boolean shouldTheFileBeIncludedInTheAnalysis(IFile file, BCCFilePathEntry entry) {
		if (entry.eContainer() instanceof BCCBibTeXFilesEntry) {
			return BCCResourceUtil.fileIsBibTeXFile(file);
		}
		else if (entry.eContainer() instanceof BCCConsistencyRulesEntry) {
			return BCCResourceUtil.fileIsConsistencyRule(file);
		}
		
		return false;
	}
	
	public static List<IFile> identifyAllExecutionModelFiles(IResource resource) {
		List<IFile> executionModelFiles = new ArrayList<>();
		List<IResource> children = BCCResourceUtil.getChildResources(resource);
		
		for (IResource child : children) {
			if (BCCResourceUtil.resourceIsContainer(child)) {
				List<IFile> subEexecutionModelFiles = identifyAllExecutionModelFiles(child);
				executionModelFiles.addAll(subEexecutionModelFiles);
			}
			else {
				IFile file = (IFile) child;
				if (file.getFileExtension().equals(BCC_FILE_EXTENSION)) {
					executionModelFiles.add(file);
				}
			}
		}
		
		return executionModelFiles;
	}

	public static List<BCCExecutionModel> identifyAllExecutionModelFilesAnalysingFile(List<IFile> possibleExecutionModelFiles,
			IFile selectedFile)
	{
		List<BCCExecutionModel> relevantExecutionModelFiles = new ArrayList<>();
		
		for (IFile executionModelFile : possibleExecutionModelFiles) {
			BCCExecutionModel executionModel = BCCResourceUtil.parseModel(new BCCExecutionModelStandaloneSetup(), executionModelFile);
			if (BCCResourceUtil.fileIsBibTeXFile(selectedFile)) {
				if (executionModel.getBibTeXFilesEntry() == null) {
					relevantExecutionModelFiles.add(executionModel);
				}
				else {
					List<BCCFilePathEntry> fileEntries = executionModel.getBibTeXFilesEntry().getEntries();
					if (fileIsReferenced(executionModelFile, fileEntries, selectedFile)) {
						relevantExecutionModelFiles.add(executionModel);
					}
				}
			}
			else if (BCCResourceUtil.fileIsConsistencyRule(selectedFile)) {
				if (executionModel.getRulesEntry() == null) {
					relevantExecutionModelFiles.add(executionModel);
				}
				else {
					List<BCCFilePathEntry> fileEntries = executionModel.getRulesEntry().getEntries();
					if (fileIsReferenced(executionModelFile, fileEntries, selectedFile)) {
						relevantExecutionModelFiles.add(executionModel);
					}
				}
			}
		}
		
		return relevantExecutionModelFiles;
	}

	private static boolean fileIsReferenced(IFile executionModelFile, List<BCCFilePathEntry> fileEntries,
			IFile selectedFile)
	{
		for (BCCFilePathEntry fileEntry : fileEntries) {
			IContainer parentContainer = executionModelFile.getParent();
			IResource resource = parentContainer.findMember(fileEntry.getPath());
			
			if (resource != null) {
				if (BCCResourceUtil.resourceIsFile(resource)) {
					if (resource.equals(selectedFile)) {
						return true;
					}
				}
				else if (BCCResourceUtil.resourceIsContainer(resource)) {
					List<IResource> childResources = BCCResourceUtil.getChildResources(resource);
					for (IResource childResource : childResources) {
						if (childResource.equals(selectedFile)) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}

	private static boolean explicitConsistencyRuleSelectionExists(BCCExecutionModel executionModel) {
		return executionModel.getRulesEntry() != null;
	}

	private static boolean explicitBibTeXFileSelectionExists(BCCExecutionModel executionModel) {
		return executionModel.getBibTeXFilesEntry() != null;
	}

	private static boolean isFolder(String path) {
		return !path.endsWith(BCC_RULE_FILE_EXTENSION) && !path.endsWith(BIB_FILE_EXTENSION);
	}

}
