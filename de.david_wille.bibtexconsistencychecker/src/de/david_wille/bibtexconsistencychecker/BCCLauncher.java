package de.david_wille.bibtexconsistencychecker;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import de.david_wille.bibtexconsistencychecker.bibtex.BCCBibTeXStandaloneSetup;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.checks.BCCAlphabeticOrder;
import de.david_wille.bibtexconsistencychecker.checks.BCCShortHarvardStyle;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel;
import de.david_wille.bibtexconsistencychecker.util.BCCMarkerHandling;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;
import de.david_wille.bibtexconsistencychecker.util.BCCUtil;

public class BCCLauncher {
	
	private static final String BIB_FILE = "bib";

	public void launch(BCCExecutionModel executionModel) {
		try {
			List<IFile> identifiedBibliographyFiles = identifyRelevantBibliographyFiles(executionModel);
			
			List<BCCBibTeXFile> parsedBibTeXFiles = BCCResourceUtil.parseModels(new BCCBibTeXStandaloneSetup(), identifiedBibliographyFiles);
			
			BCCMarkerHandling.clearAllExistingErrorMarkers(identifiedBibliographyFiles);
			
			if (ensureAlphabeticOrderActivated(executionModel)) {
				BCCAlphabeticOrder.checkAlphabeticOrder(parsedBibTeXFiles);
			}
			
			if (ensureShortHarvardStyleActivated(executionModel)) {
				BCCShortHarvardStyle.checkShortHarvardStyle(parsedBibTeXFiles);
			}
			
			System.out.println(parsedBibTeXFiles);
		}
		catch (CoreException e) {
			BCCUtil.openErrorDialog("An unexpected error occurred while reading the bibliography files!");
		}
	}

	private static List<IFile> identifyRelevantBibliographyFiles(BCCExecutionModel executionModel) throws CoreException {
		List<IFile> relevantBibliographyFiles = new ArrayList<>();
		
		IFile executionModelFile = BCCResourceUtil.getIFile(executionModel);
		IResource parentResource = executionModelFile.getParent();
		List<IResource> childResourcesOfParentResource = BCCResourceUtil.getChildResources(parentResource);
		
		for (IResource resource : childResourcesOfParentResource) {
			if (BCCResourceUtil.resourceIsFile(resource)) {
				IFile file = (IFile) resource;
				
				if (file.getFileExtension().equals(BIB_FILE)) {
					relevantBibliographyFiles.add(file);
				}
			}
		}
		
		return relevantBibliographyFiles;
	}

	private boolean ensureAlphabeticOrderActivated(BCCExecutionModel executionModel) {
		return executionModel.getSettingsEntry().isEnsureAlphbeticOrderActivated();
	}

	private boolean ensureShortHarvardStyleActivated(BCCExecutionModel executionModel) {
		return executionModel.getSettingsEntry().isEnsureShortHarvardStyleActivated();
	}

}
