package de.david_wille.bibtexconsistencychecker;

import java.util.ArrayList;
import java.util.List;

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
import de.david_wille.bibtexconsistencychecker.util.BCCMarkerHandling;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;
import de.david_wille.bibtexconsistencychecker.util.BCCUtil;

public class BCCLauncher {
	
	private static final String BIB_FILE = "bib";
	private static final String BCC_RULE_FILE = "bcc_rule";

	public void launch(BCCExecutionModel executionModel) {
		try {
			List<BCCBibTeXFile> parsedBibTeXFiles = identifyAndParseAllRelevantBibTeXFiles(executionModel);
			List<BCCConsistencyRule> parsedConsistencyRules = identifyAndParseAllRelevantConsistencyRules(executionModel);
			
			BCCAnalysis analysis = new BCCAnalysis(executionModel, parsedConsistencyRules, parsedBibTeXFiles);
			analysis.startAnalysis();
		}
		catch (CoreException e) {
			BCCUtil.openErrorDialog("An unexpected error occurred while reading the bibliography files!");
		}
	}

	private List<BCCBibTeXFile> identifyAndParseAllRelevantBibTeXFiles(BCCExecutionModel executionModel) throws CoreException {
		List<IFile> identifiedBibliographyFiles = identifyRelevantBibliographyFiles(executionModel);
		
		BCCMarkerHandling.clearAllExistingErrorMarkers(identifiedBibliographyFiles);
		
		List<BCCBibTeXFile> parsedBibTeXFiles = BCCResourceUtil.parseModels(new BCCBibTeXStandaloneSetup(), identifiedBibliographyFiles);
		
		return parsedBibTeXFiles;
	}

	private List<BCCConsistencyRule> identifyAndParseAllRelevantConsistencyRules(BCCExecutionModel executionModel) throws CoreException {
		List<IFile> identifiedConsistencyRuleFiles = identifyRelevantConsistencyRuleFiles(executionModel);
		
		List<BCCConsistencyRule> parsedConsistencyRules = BCCResourceUtil.parseModels(new BCCConsistencyRuleStandaloneSetup(), identifiedConsistencyRuleFiles);
		
		return parsedConsistencyRules;
	}

	private List<IFile> identifyRelevantBibliographyFiles(BCCExecutionModel executionModel) throws CoreException {
		List<IFile> relevantBibliographyFiles = new ArrayList<>();
		
		if (explicitBibTeXFileSelectionExists(executionModel)) {
			relevantBibliographyFiles = collectSelectedBibTeXFiles(executionModel);
		}
		else {
			relevantBibliographyFiles = collectAllBibTeXFiles(executionModel);
		}
		
		return relevantBibliographyFiles;
	}

	private List<IFile> identifyRelevantConsistencyRuleFiles(BCCExecutionModel executionModel) {
		List<IFile> relevantConsistencyRuleFiles = new ArrayList<>();
		
		if (explicitConsistencyRuleSelectionExists(executionModel)) {
			relevantConsistencyRuleFiles = collectSelectedConsistencyRuleFiles(executionModel);
		}
		else {
			relevantConsistencyRuleFiles = collectAllConsistencyRuleFiles(executionModel);
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
			IFile foundConstistencyRuleFile = identifyBibTeXFile(executionModel, entry);
			relevantBibliographyFiles.add(foundConstistencyRuleFile);
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
				
				if (file.getFileExtension().equals(BCC_RULE_FILE)) {
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
				
				if (file.getFileExtension().equals(BIB_FILE)) {
					if (file.getName().equals(path)) {
						return file;
					}
				}
			}
		}
		
		return null;
	}

	private List<IFile> collectAllConsistencyRuleFiles(BCCExecutionModel executionModel) {
		List<IFile> relevantConsistencyRuleFiles = new ArrayList<>();
		
		IFile executionModelFile = BCCResourceUtil.getIFile(executionModel);
		IResource parentResource = executionModelFile.getParent();
		List<IResource> childResourcesOfParentResource = BCCResourceUtil.getChildResources(parentResource);
		
		for (IResource resource : childResourcesOfParentResource) {
			if (BCCResourceUtil.resourceIsFile(resource)) {
				IFile file = (IFile) resource;
				
				if (file.getFileExtension().equals(BCC_RULE_FILE)) {
					relevantConsistencyRuleFiles.add(file);
				}
			}
		}
		
		return relevantConsistencyRuleFiles;
	}

	private List<IFile> collectAllBibTeXFiles(BCCExecutionModel executionModel) {
		List<IFile> relevantBibTeXFiles = new ArrayList<>();
		
		IFile executionModelFile = BCCResourceUtil.getIFile(executionModel);
		IResource parentResource = executionModelFile.getParent();
		List<IResource> childResourcesOfParentResource = BCCResourceUtil.getChildResources(parentResource);
		
		for (IResource resource : childResourcesOfParentResource) {
			if (BCCResourceUtil.resourceIsFile(resource)) {
				IFile file = (IFile) resource;
				
				if (file.getFileExtension().equals(BIB_FILE)) {
					relevantBibTeXFiles.add(file);
				}
			}
		}
		
		return relevantBibTeXFiles;
	}

	private boolean explicitConsistencyRuleSelectionExists(BCCExecutionModel executionModel) {
		return executionModel.getRulesEntry() != null;
	}

	private boolean explicitBibTeXFileSelectionExists(BCCExecutionModel executionModel) {
		return executionModel.getBibTeXFilesEntry() != null;
	}

}
