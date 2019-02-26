package de.david_wille.bibtexconsistencychecker;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

import de.david_wille.bibtexconsistencychecker.executionmodel.BCCExecutionModelStandaloneSetup;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;
import de.david_wille.bibtexconsistencychecker.util.BCCUtil;

public class BCCLaunchShortCut implements ILaunchShortcut {
	
	@Override
	public void launch(ISelection selection, String mode) {
		List<IResource> selectionPaths = BCCLauncherResourceUtil.identifyAllResourcesFromSelection(selection);
		
		if (singleFileSelected(selectionPaths)) {
			IResource resource = selectionPaths.get(0);
			
			if (resource instanceof IFile) {
				IFile selectedFile = (IFile) resource;
				
				executeBibTeXAnalyzer(selectedFile);
			}
			else {
				BCCUtil.openErrorDialog("Please provide a single valid execution model file (*." + BCCResourceUtil.BCC_FILE_EXTENSION + ")!");
			}
		}
		else {
			BCCUtil.openErrorDialog("Please provide a single valid execution model file (*." + BCCResourceUtil.BCC_FILE_EXTENSION + ")!");
		}
	}

	private boolean singleFileSelected(List<IResource> selectionPaths) {
		return selectionPaths.size() == 1;
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		IFile editorFile = getCurrentEditorFile(editor);
		
		executeBibTeXAnalyzer(editorFile);
	}

	private void executeBibTeXAnalyzer(IFile selectedFile) {
		if (BCCResourceUtil.fileIsExecutionModel(selectedFile)) {
			BCCExecutionModel executionModel = BCCResourceUtil.parseModel(new BCCExecutionModelStandaloneSetup(), selectedFile);
			launchExecutionModel(executionModel);
		}
		else if (BCCResourceUtil.fileIsBibTeXFile(selectedFile) || BCCResourceUtil.fileIsConsistencyRule(selectedFile)) {
			BCCExecutionModel executionModel = BCCLauncherResourceUtil.identifyExecutionModel(selectedFile.getProject());
			
			if (executionModel == null) {
				BCCUtil.openErrorDialog("No execution models (*." + BCCResourceUtil.BCC_FILE_EXTENSION + ") were found!");
			}
			else if (!BCCLauncherResourceUtil.identifyWheterExecutionModelAnalyzesFile(executionModel, selectedFile)) {
				BCCUtil.openErrorDialog("This execution model does not analyze the selected file!");
			}
			else {
				launchExecutionModel(executionModel);
			}
		}
	}

	private void launchExecutionModel(BCCExecutionModel executionModel) {
		BCCLauncher launcher = new BCCLauncher();
		launcher.launch(executionModel);
	}

	private IFile getCurrentEditorFile(IEditorPart editor) {
		return ((IFileEditorInput) editor.getEditorInput()).getFile();
	}

}
