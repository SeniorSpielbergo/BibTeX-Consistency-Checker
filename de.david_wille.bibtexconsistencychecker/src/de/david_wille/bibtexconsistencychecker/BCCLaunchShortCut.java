package de.david_wille.bibtexconsistencychecker;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCFilePathEntry;
import de.david_wille.bibtexconsistencychecker.nature.BCCProjectNature;
import de.david_wille.bibtexconsistencychecker.statistics.BCCStatistics;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;
import de.david_wille.bibtexconsistencychecker.util.BCCUtil;

public class BCCLaunchShortCut implements ILaunchShortcut {

	private static final String RULES_FOLDER = "rules";
	private static final String BIBLIOGRAPHY_FOLDER = "bibliography";

	@Override
	public void launch(ISelection selection, String mode) {
		List<IResource> selectionPaths = BCCResourceUtil.identifyAllResourcesFromSelection(selection);

		// the short cut is only enabled when a single resource is selected
		IResource resource = selectionPaths.get(0);

		if (validProjectSetup(resource)) {
			executeBibTeXAnalyzer(resource);
		}
	}

	private boolean validProjectSetup(IResource resource) {
		boolean foundExecutionModelFile = false;
		boolean foundBibliographyFolder = false;
		boolean foundRulesFolder = false;

		IProject project = resource.getProject();

		try {
			if (project.hasNature(BCCProjectNature.NATURE_ID)) {
				for (IResource subResource : BCCResourceUtil.getChildResources(project)) {
					if (subResource instanceof IFile) {
						IFile file = (IFile) subResource;

						if (BCCResourceUtil.fileIsExecutionModel(file)) {
							if (!foundExecutionModelFile) {
								foundExecutionModelFile = true;
							}
							else {
								BCCUtil.openErrorDialog("More than one BibTeX Consistency Checker Execution file found!");
								return false;
							}
						}
					}
					else if (subResource instanceof IFolder) {
						IFolder folder = (IFolder) subResource;

						if (folder.getName().equals(RULES_FOLDER)) {
							foundRulesFolder = true;
						}
						else if (folder.getName().equals(BIBLIOGRAPHY_FOLDER)) {
							foundBibliographyFolder = true;
						}
					}
				}
			}
			else {
				if (resource instanceof IProject) {
					BCCUtil.openErrorDialog("The selected project does not represent a valid BibTeX Consistency Checker project!");
				}
				else if (resource instanceof IFolder) {
					BCCUtil.openErrorDialog("The selected folder is not contained in a valid BibTeX Consistency Checker project!");
				}
				else {
					BCCUtil.openErrorDialog("The selected resource is not contained in a valid BibTeX Consistency Checker project!");
				}
				return false;
			}
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
		
		if (!foundBibliographyFolder) {
			BCCUtil.openErrorDialog("No \"bibliography\" folder found in the project!");
		}
		
		if (!foundRulesFolder) {
			BCCUtil.openErrorDialog("No \"rules\" folder found in the project!");
		}

		return foundExecutionModelFile && foundBibliographyFolder && foundRulesFolder;
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		IFile editorFile = getCurrentEditorFile(editor);

		if (validProjectSetup(editorFile)) {
			executeBibTeXAnalyzer(editorFile);
		}
	}

	private IFile getCurrentEditorFile(IEditorPart editor) {
		return ((IFileEditorInput) editor.getEditorInput()).getFile();
	}

	private void executeBibTeXAnalyzer(IResource selectedResource) {
		if (selectedResource instanceof IFile) {
			IFile selectedFile = (IFile) selectedResource;
			if (BCCResourceUtil.fileIsExecutionModel(selectedFile)) {
				BCCExecutionModel executionModel = BCCResourceUtil.parseModel(selectedFile);
				launchExecutionModel(executionModel);
			}
			else {
				identifyAndExecuteExecutionModel(selectedResource);
			}
		}
		else {
			identifyAndExecuteExecutionModel(selectedResource);
		}
	}

	private void identifyAndExecuteExecutionModel(IResource selectedResource) {
		IProject parentProject = selectedResource.getProject();

		for (IResource resource : BCCResourceUtil.getChildResources(parentProject)) {
			if (resource instanceof IFile) {
				IFile file = (IFile) resource;
				if (BCCResourceUtil.fileIsExecutionModel(file)) {
					BCCExecutionModel executionModel = BCCResourceUtil.parseModel(file);
					launchExecutionModel(executionModel);
					return;
				}
			}
		}

		BCCUtil.openErrorDialog("Could not find a valid execution model file (*.bcc) in the project root!");
	}

	private void launchExecutionModel(BCCExecutionModel executionModel) {
		try {
			BCCStatistics statistics = BCCStatistics.getInstance();
			statistics.reset();
			
			BCCLauncher launcher = new BCCLauncher(executionModel);
			launcher.launch();
			
			printResult(statistics);
		}
		catch (BCCLaunchPreparationException e) {
			BCCUtil.openErrorDialog(e.getMessage());
		}
		catch (CoreException e) {
			BCCUtil.openErrorDialog("An unexpected error occurred while reading the files!");
		}
	}

	private void printResult(BCCStatistics statistics) {
		if (statistics.wasErrorDetected()) {
			int errors = statistics.getErrorCounter();
			int warnings = statistics.getWarningCounter();
			String errorsString = "error";
			String warningsString = "";
			if (errors > 1) {
				errorsString += "s";
			}
			
			if (warnings > 0) {
				warningsString += " and " + warnings + " warning";
				if (warnings > 1) {
					warningsString += "s";
				}
			}
			
			BCCUtil.openErrorDialog("The execution identified " + errors + " " + errorsString + warningsString + "!");
		}
		else if (statistics.wasWarningDetected()) {
			int warnings = statistics.getWarningCounter();
			String warningsString = "warning";
			if (warnings > 1) {
				warningsString += "s";
			}
			
			BCCUtil.openWarningDialog("The execution identified " + warnings + " " + warningsString + "!");
		}
		else {
			BCCUtil.openInformationDialog("No Problems Found", "No problems were detected in the analyzed files!");
		}
	}

	public boolean executionModelAnalyzesFile(BCCExecutionModel executionModel,
			IFile selectedFile)
	{
		if (BCCResourceUtil.fileIsBibTeXFile(selectedFile)) {
			if (executionModel.getBibTeXFilesEntry() == null) {
				return true;
			}
			else {
				List<BCCFilePathEntry> fileEntries = executionModel.getBibTeXFilesEntry().getEntries();
				if (fileIsReferenced(executionModel, fileEntries, selectedFile)) {
					return true;
				}
			}
		}
		else if (BCCResourceUtil.fileIsConsistencyRule(selectedFile)) {
			if (executionModel.getRulesEntry() == null) {
				return true;
			}
			else {
				List<BCCFilePathEntry> fileEntries = executionModel.getRulesEntry().getEntries();
				if (fileIsReferenced(executionModel, fileEntries, selectedFile)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean fileIsReferenced(BCCExecutionModel executionModel, List<BCCFilePathEntry> fileEntries,
			IFile selectedFile)
	{
		for (BCCFilePathEntry fileEntry : fileEntries) {
			IContainer parentContainer = BCCResourceUtil.getIFile(executionModel).getParent();
			IResource resource = parentContainer.findMember(fileEntry.getPath());

			if (resource != null) {
				if (resource instanceof IFile) {
					if (resource.equals(selectedFile)) {
						return true;
					}
				}
				else if (resource instanceof IContainer) {
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

}
