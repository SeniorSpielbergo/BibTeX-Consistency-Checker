package de.david_wille.bibtexconsistencychecker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;
import de.david_wille.bibtexconsistencychecker.util.BCCUtil;

public class BCCLaunchShortCut implements ILaunchShortcut {

	private static final String BCC_FILE = "bcc";
	private IWorkspaceRoot workspaceRoot;
	
	public BCCLaunchShortCut() {
		workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
	}

	@Override
	public void launch(ISelection selection, String mode) {
		List<String> selectionPaths = new ArrayList<>();
		try {
			selectionPaths = identifyAllFilesFromSelection(selection);
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
		
		if (selectionPaths.size() == 1) {
			IResource resource = workspaceRoot.findMember(selectionPaths.get(0));
			
			if (resource instanceof IFile) {
				IFile selectedFile = (IFile) resource;
				
				executeBibTeXAnalyzer(selectedFile);
			}
			else {
				BCCUtil.openErrorDialog("Please select a single execution model file (*." + BCC_FILE + ")!");
			}
		}
		else {
			BCCUtil.openErrorDialog("Please select a single execution model file (*." + BCC_FILE + ")!");
		}
	}

	private void executeBibTeXAnalyzer(IFile selectedFile) {
		if (fileIsExecutionModel(selectedFile)) {
			BCCExecutionModel executionModel = BCCResourceUtil.parseModel(selectedFile);
			
			BCCLauncher launcher = new BCCLauncher();
			launcher.launch(executionModel);
		}
		else {
			BCCUtil.openErrorDialog("Please select a valid execution model file (*." + BCC_FILE + ")!");
		}
	}

	private List<String> identifyAllFilesFromSelection(ISelection selection) throws CoreException {
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
		
		// also collect files from folders
		return collectFilesFromSelection(selectionPaths);
	}

	private List<String> collectFilesFromSelection(List<String> selectionPaths) throws CoreException {
		Set<String> finalSelectionSet = new HashSet<>();
		List<String> finalSelectionList = new ArrayList<>();

		// process the found paths
		for (String pathString : selectionPaths) {
			IPath path = new Path(pathString);
			IResource resource = workspaceRoot.findMember(path);
			
			if (BCCResourceUtil.resourceIsFile(resource)) {
				finalSelectionSet.add(pathString);
			}
			else if (BCCResourceUtil.resourceIsContainer(resource)) {
				IContainer container = (IContainer) workspaceRoot.getContainerForLocation(path);
				finalSelectionSet.add(container.getFullPath().toString());
			}
		}

		finalSelectionList.addAll(finalSelectionSet);
		
		return finalSelectionList;
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		IFile editorFile = getCurrentEditorFile(editor);
		
		executeBibTeXAnalyzer(editorFile);
	}

	private boolean fileIsExecutionModel(IFile editorFile) {
		return editorFile.getFileExtension().equals(BCC_FILE);
	}

	private IFile getCurrentEditorFile(IEditorPart editor) {
		return ((IFileEditorInput) editor.getEditorInput()).getFile();
	}

}
