package de.david_wille.bibtexconsistencychecker;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import de.david_wille.bibtexconsistencychecker.analysis.BCCAnalysis;
import de.david_wille.bibtexconsistencychecker.bibtex.BCCBibTeXStandaloneSetup;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.consistencyrule.BCCConsistencyRuleStandaloneSetup;
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCConsistencyRule;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCBibTeXFileEntry;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCConsistencyRuleEntry;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel;
import de.david_wille.bibtexconsistencychecker.util.BCCProblemMarkerHandling;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;
import de.david_wille.bibtexconsistencychecker.util.BCCUtil;

public class BCCLauncher {
	
	public void launch(BCCExecutionModel executionModel) {
		try {
			if (!BCCProblemMarkerHandling.resourceHasProblemMarkers(executionModel)) {
				List<BCCBibTeXFile> parsedBibTeXFiles = identifyAndParseAllRelevantBibTeXFiles(executionModel);
				
				if (!BCCProblemMarkerHandling.resourcesHaveProblemMarkers(parsedBibTeXFiles)) {
					List<BCCConsistencyRule> parsedConsistencyRules = identifyAndParseAllRelevantConsistencyRules(executionModel);
					
					if (!BCCProblemMarkerHandling.resourcesHaveProblemMarkers(parsedConsistencyRules)) {
						BCCAnalysis analysis = new BCCAnalysis(executionModel, parsedConsistencyRules, parsedBibTeXFiles);
						analysis.startAnalysis();
					}
					else {
						BCCUtil.openErrorDialog("Cannot execute the consistency checks due to errors in the consistency rules!");
					}
				}
				else {
					BCCUtil.openErrorDialog("Cannot execute the consistency checks due to errors in the BibTeX files!");
				}
			}
			else {
				BCCUtil.openErrorDialog("Cannot execute the consistency checks due to errors in the execution model!");
			}
		}
		catch (CoreException e) {
			BCCUtil.openErrorDialog("An unexpected error occurred while reading the files!");
		}
	}

	private List<BCCBibTeXFile> identifyAndParseAllRelevantBibTeXFiles(BCCExecutionModel executionModel) throws CoreException {
		List<IFile> identifiedBibliographyFiles = identifyRelevantBibliographyFiles(executionModel);
		
		BCCAnalysis.clearAllExistingConsistencyProblemMarkers(identifiedBibliographyFiles);
		
		List<BCCBibTeXFile> parsedBibTeXFiles = BCCResourceUtil.parseModels(new BCCBibTeXStandaloneSetup(), identifiedBibliographyFiles);
		
		return parsedBibTeXFiles;
	}

	private List<BCCConsistencyRule> identifyAndParseAllRelevantConsistencyRules(BCCExecutionModel executionModel) throws CoreException {
		List<IFile> identifiedConsistencyRuleFiles = identifyRelevantConsistencyRuleFiles(executionModel);
		
		BCCAnalysis.clearAllExistingConsistencyProblemMarkers(identifiedConsistencyRuleFiles);
		
		List<BCCConsistencyRule> parsedConsistencyRules = BCCResourceUtil.parseModels(new BCCConsistencyRuleStandaloneSetup(), identifiedConsistencyRuleFiles);
		
		return parsedConsistencyRules;
	}

	private List<IFile> identifyRelevantBibliographyFiles(BCCExecutionModel executionModel) throws CoreException {
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

	private List<IFile> identifyRelevantConsistencyRuleFiles(BCCExecutionModel executionModel) {
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

	private List<IFile> collectSelectedConsistencyRuleFiles(BCCExecutionModel executionModel) {
		List<IFile> relevantConsistencyRuleFiles = new ArrayList<>();
		
		for (BCCConsistencyRuleEntry entry : executionModel.getRulesEntry().getConsistencyRuleEntries()) {
			IFile foundConstistencyRuleFile = identifyConsistencyRuleFile(executionModel, entry);
			relevantConsistencyRuleFiles.add(foundConstistencyRuleFile);
		}
		
		return relevantConsistencyRuleFiles;
	}

	private List<IFile> collectSelectedBibTeXFiles(BCCExecutionModel executionModel) {
		List<IFile> relevantBibliographyFiles = new ArrayList<>();
		
		for (BCCBibTeXFileEntry entry : executionModel.getBibTeXFilesEntry().getBibTeXFileEntries()) {
			IFile foundBibTeXFile = identifyBibTeXFile(executionModel, entry);
			relevantBibliographyFiles.add(foundBibTeXFile);
		}
		
		return relevantBibliographyFiles;
	}

	private IFile identifyConsistencyRuleFile(BCCExecutionModel executionModel, BCCConsistencyRuleEntry entry) {
		String path = entry.getRulePath().getPath();
		
		IFile executionModelFile = BCCResourceUtil.getIFile(executionModel);
		IResource parentResource = executionModelFile.getParent();
		List<IResource> childResourcesOfParentResource = BCCResourceUtil.getChildResources(parentResource);
		
		for (IResource resource : childResourcesOfParentResource) {
			if (BCCResourceUtil.resourceIsFile(resource)) {
				IFile file = (IFile) resource;
				
				if (BCCLaunchShortCut.fileIsConsistencyRule(file)) {
					if (file.getName().equals(path)) {
						return file;
					}
				}
			}
		}
		
		return null;
	}

	private IFile identifyBibTeXFile(BCCExecutionModel executionModel, BCCBibTeXFileEntry entry) {
		String path = entry.getFilePath().getPath();
		
		IFile executionModelFile = BCCResourceUtil.getIFile(executionModel);
		IResource parentResource = executionModelFile.getParent();
		List<IResource> childResourcesOfParentResource = BCCResourceUtil.getChildResources(parentResource);
		
		for (IResource resource : childResourcesOfParentResource) {
			if (BCCResourceUtil.resourceIsFile(resource)) {
				IFile file = (IFile) resource;
				
				if (BCCLaunchShortCut.fileIsBibTeXFile(file)) {
					if (file.getName().equals(path)) {
						return file;
					}
				}
			}
		}
		
		return null;
	}

	private boolean explicitConsistencyRuleSelectionExists(BCCExecutionModel executionModel) {
		return executionModel.getRulesEntry() != null;
	}

	private boolean explicitBibTeXFileSelectionExists(BCCExecutionModel executionModel) {
		return executionModel.getBibTeXFilesEntry() != null;
	}

}
