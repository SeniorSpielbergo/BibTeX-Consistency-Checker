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
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCBibTeXFileEntry;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCConsistencyRuleEntry;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;

public class BCCLauncherResourceUtil {

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

	public static boolean singleExecutionModelExists(IFile selectedFile) {
		IResource executionModel = null;
		IContainer parentContainer = selectedFile.getParent();
		
		for (IResource resource : BCCResourceUtil.getChildResources(parentContainer)) {
			if (resource.getFileExtension().equals(BCCResourceUtil.BCC_FILE_EXTENSION)) {
				if (executionModel == null) {
					executionModel = resource;
				}
				else {
					return false;
				}
			}
		}
		
		if (executionModel != null) {
			return true;
		}
		
		return false;
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
		List<IFile> identifiedBibliographyFiles = identifyRelevantBibliographyFiles(executionModel);
		
		BCCAnalysis.clearAllExistingConsistencyProblemMarkers(identifiedBibliographyFiles);
		
		List<BCCBibTeXFile> parsedBibTeXFiles = BCCResourceUtil.parseModels(new BCCBibTeXStandaloneSetup(), identifiedBibliographyFiles);
		
		return parsedBibTeXFiles;
	}

	public static List<BCCConsistencyRule> identifyAndParseAllRelevantConsistencyRules(BCCExecutionModel executionModel) throws CoreException {
		List<IFile> identifiedConsistencyRuleFiles = identifyRelevantConsistencyRuleFiles(executionModel);
		
		BCCAnalysis.clearAllExistingConsistencyProblemMarkers(identifiedConsistencyRuleFiles);
		
		List<BCCConsistencyRule> parsedConsistencyRules = BCCResourceUtil.parseModels(new BCCConsistencyRuleStandaloneSetup(), identifiedConsistencyRuleFiles);
		
		return parsedConsistencyRules;
	}

	private static List<IFile> identifyRelevantBibliographyFiles(BCCExecutionModel executionModel) throws CoreException {
		List<IFile> relevantBibliographyFiles = new ArrayList<>();
		
		if (explicitBibTeXFileSelectionExists(executionModel)) {
			relevantBibliographyFiles = collectSelectedBibTeXFiles(executionModel);
		}
		else {
			IFile executionModelFile = BCCResourceUtil.getIFile(executionModel);
			IContainer parentContainer = executionModelFile.getParent();
			
			relevantBibliographyFiles = BCCResourceUtil.collectAllFilesInContainer(parentContainer, "bib");
		}
		
		return relevantBibliographyFiles;
	}

	private static List<IFile> identifyRelevantConsistencyRuleFiles(BCCExecutionModel executionModel) {
		List<IFile> relevantConsistencyRuleFiles = new ArrayList<>();
		
		if (explicitConsistencyRuleSelectionExists(executionModel)) {
			relevantConsistencyRuleFiles = collectSelectedConsistencyRuleFiles(executionModel);
		}
		else {
			IFile executionModelFile = BCCResourceUtil.getIFile(executionModel);
			IContainer parentContainer = executionModelFile.getParent();
			
			relevantConsistencyRuleFiles = BCCResourceUtil.collectAllFilesInContainer(parentContainer, "bcc_rule");
		}
		
		return relevantConsistencyRuleFiles;
	}

	private static List<IFile> collectSelectedConsistencyRuleFiles(BCCExecutionModel executionModel) {
		List<IFile> relevantConsistencyRuleFiles = new ArrayList<>();
		
		for (BCCConsistencyRuleEntry entry : executionModel.getRulesEntry().getConsistencyRuleEntries()) {
			IFile foundConstistencyRuleFile = identifyConsistencyRuleFile(executionModel, entry);
			relevantConsistencyRuleFiles.add(foundConstistencyRuleFile);
		}
		
		return relevantConsistencyRuleFiles;
	}

	private static List<IFile> collectSelectedBibTeXFiles(BCCExecutionModel executionModel) {
		List<IFile> relevantBibliographyFiles = new ArrayList<>();
		
		for (BCCBibTeXFileEntry entry : executionModel.getBibTeXFilesEntry().getBibTeXFileEntries()) {
			IFile foundBibTeXFile = identifyBibTeXFile(executionModel, entry);
			relevantBibliographyFiles.add(foundBibTeXFile);
		}
		
		return relevantBibliographyFiles;
	}

	private static IFile identifyConsistencyRuleFile(BCCExecutionModel executionModel, BCCConsistencyRuleEntry entry) {
		String path = entry.getRulePath().getPath();
		
		IFile executionModelFile = BCCResourceUtil.getIFile(executionModel);
		IResource parentResource = executionModelFile.getParent();
		List<IResource> childResourcesOfParentResource = BCCResourceUtil.getChildResources(parentResource);
		
		for (IResource resource : childResourcesOfParentResource) {
			if (BCCResourceUtil.resourceIsFile(resource)) {
				IFile file = (IFile) resource;
				
				if (BCCResourceUtil.fileIsConsistencyRule(file)) {
					if (file.getName().equals(path)) {
						return file;
					}
				}
			}
		}
		
		return null;
	}

	private static IFile identifyBibTeXFile(BCCExecutionModel executionModel, BCCBibTeXFileEntry entry) {
		String path = entry.getFilePath().getPath();
		
		IFile executionModelFile = BCCResourceUtil.getIFile(executionModel);
		IResource parentResource = executionModelFile.getParent();
		List<IResource> childResourcesOfParentResource = BCCResourceUtil.getChildResources(parentResource);
		
		for (IResource resource : childResourcesOfParentResource) {
			if (BCCResourceUtil.resourceIsFile(resource)) {
				IFile file = (IFile) resource;
				
				if (BCCResourceUtil.fileIsBibTeXFile(file)) {
					if (file.getName().equals(path)) {
						return file;
					}
				}
			}
		}
		
		return null;
	}

	private static boolean explicitConsistencyRuleSelectionExists(BCCExecutionModel executionModel) {
		return executionModel.getRulesEntry() != null;
	}

	private static boolean explicitBibTeXFileSelectionExists(BCCExecutionModel executionModel) {
		return executionModel.getBibTeXFilesEntry() != null;
	}

}
