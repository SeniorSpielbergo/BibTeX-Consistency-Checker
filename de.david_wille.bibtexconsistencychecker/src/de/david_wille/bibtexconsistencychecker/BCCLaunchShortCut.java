package de.david_wille.bibtexconsistencychecker;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

import de.david_wille.bibtexconsistencychecker.executionmodel.BCCExecutionModelStandaloneSetup;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCFilePathEntry;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;
import de.david_wille.bibtexconsistencychecker.util.BCCUtil;

public class BCCLaunchShortCut implements ILaunchShortcut {
	
	@Override
	public void launch(ISelection selection, String mode) {
		List<IResource> selectionPaths = BCCResourceUtil.identifyAllResourcesFromSelection(selection);
		
		// the short cut is only enabled when a single resource is selected
		IResource resource = selectionPaths.get(0);
		
		executeBibTeXAnalyzer(resource);
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		IFile editorFile = getCurrentEditorFile(editor);
		
		executeBibTeXAnalyzer(editorFile);
	}

	private IFile getCurrentEditorFile(IEditorPart editor) {
		return ((IFileEditorInput) editor.getEditorInput()).getFile();
	}

	private void executeBibTeXAnalyzer(IResource selectedResource) {
		if (selectedResource instanceof IFile) {
			IFile selectedFile = (IFile) selectedResource;
			if (BCCResourceUtil.fileIsExecutionModel(selectedFile)) {
				BCCExecutionModel executionModel = BCCResourceUtil.parseModel(new BCCExecutionModelStandaloneSetup(), selectedFile);
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
					BCCExecutionModel executionModel = BCCResourceUtil.parseModel(new BCCExecutionModelStandaloneSetup(), file);
					launchExecutionModel(executionModel);
					return;
				}
			}
		}
		
		BCCUtil.openErrorDialog("Could not find a valid execution model file (*.bcc)!");
	}

	private void launchExecutionModel(BCCExecutionModel executionModel) {
		try {
			BCCLauncher launcher = new BCCLauncher(executionModel);
			launcher.launch();
		}
		catch (BCCLaunchPreparationException e) {
			BCCUtil.openErrorDialog(e.getMessage());
		}
		catch (CoreException e) {
			BCCUtil.openErrorDialog("An unexpected error occurred while reading the files!");
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
