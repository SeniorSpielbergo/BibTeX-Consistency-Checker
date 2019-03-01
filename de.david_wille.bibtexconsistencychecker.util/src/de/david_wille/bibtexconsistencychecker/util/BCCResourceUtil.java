package de.david_wille.bibtexconsistencychecker.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

public class BCCResourceUtil {

	public static final String BIB_FILE_EXTENSION = "bib";
	public static final String BCC_FILE_EXTENSION = "bcc";
	public static final String BCC_RULE_FILE_EXTENSION = "bcc_rule";
	
	public static List<IResource> identifyAllResourcesFromSelection(ISelection selection) {
		StructuredSelection structuredSelection = (StructuredSelection) selection;
		List<IResource> selectionPaths = new ArrayList<>();
		
		for(Object obj : structuredSelection.toList()) {
			if (obj instanceof IFile) {
				IFile file = (IFile) obj;
				selectionPaths.add(file);
			}
			else if (obj instanceof IFolder) {
				IFolder folder = (IFolder) obj;
				selectionPaths.add(folder);
			}
			else if (obj instanceof IProject) {
				IProject project = (IProject) obj;
				selectionPaths.add(project);
			}
		}
		
		return selectionPaths;
	}
	
	public static void openEditor(IFile file) {
		IWorkbenchWindow win = BCCUtil.getWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		IEditorDescriptor desc = PlatformUI.getWorkbench().
				getEditorRegistry().getDefaultEditor(file.getName());
		try {
			page.openEditor(new FileEditorInput(file), desc.getId());
		}
		catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends EObject> T parseModel(IFile file) {
		Resource resource = getResource(file);
		
		if (resource.getContents().size() > 0) {
			T parsedModel = (T) resource.getContents().get(0);
			return parsedModel;
		}
		
		return null;
	}

	public static <T extends EObject> List<T> parseModels(List<IFile> files) {
		List<T> parsedModels = new ArrayList<>();
		for (IFile file : files) {
			T parsedModel = parseModel(file);
			if (parsedModel != null) {
				parsedModels.add(parsedModel);
			}
		}
		return parsedModels;
	}
	
	public static <T extends EObject> void storeModel(Injector injector, T model, IContainer target, String modelName, String fileExtension) {
		ResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		
		String location = "platform:/resource/"
				+ target.getFullPath().toPortableString() + "/"
				+ modelName + "." + fileExtension;
	
		URI fileUri = URI.createURI(location);
		
		Resource resource = resourceSet.createResource(fileUri);
		resource.getContents().add(model);
		try {
			resource.save(null);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static IProject identifyParentProject(IResource resource) {
		return resource.getProject();
	}

	public static IProject getIProject(EObject eObject) {
		return getIFile(eObject.eResource()).getProject();
	}
	
	public static IFile getIFile(EObject eObject) {
		return getIFile(eObject.eResource());
	}
	
	public static IFile getIFile(Resource resource) {
		URI uri = resource.getURI();
		return getIFile(uri);
	}
	
	public static IFile getIFile(URI resourceURI) {
		if (resourceURI == null) {
			return null;
		}
		
		if (resourceURI.isPlatformResource()) {
			String platformString = resourceURI.toPlatformString(true);
			return makeIFileRelativeToWorkspace(platformString);
		}
		
		return null;
	}

	public static IWorkspaceRoot getWorkspaceRoot() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		return workspace.getRoot();
	}

	private static IFile makeIFileRelativeToWorkspace(String rawFilePath) {
		IPath filePath = new Path(rawFilePath);
		return makePathRelativeToWorkspace(filePath);
	}
	
	public static IFile makePathRelativeToWorkspace(IPath filePath) {
		IWorkspaceRoot workspaceRoot = getWorkspaceRoot();
		IPath workspacePath = workspaceRoot.getLocation();
		
		IPath relativeFilePath = filePath.makeRelativeTo(workspacePath);
		
		return workspaceRoot.getFile(relativeFilePath);
	}
	
	public static Resource getResource(IFile file) {
		URI uri = URI.createURI(file.getLocationURI().toString());
		return getResource(uri);
	}

	public static Resource getResource(URI uri) {
		final ResourceSet resourceSet = new ResourceSetImpl();
		final Resource resource = resourceSet.getResource(uri, true);
		return resource;
	}

	public static List<IResource> getChildResources(IResource resource) {
		if (resource instanceof IFile) {
			return new ArrayList<>();
		}
		else if (resource instanceof IContainer) {
			IContainer container = (IContainer) resource;
			try {
				return new ArrayList<>(Arrays.asList(container.members()));
			}
			catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

	public static List<IFile> recursivelyCollectAllFiles(IContainer container, String fileExtension) {
		List<IFile> relevantFiles = new ArrayList<>();
		
		List<IResource> childResourcesOfParentResource = BCCResourceUtil.getChildResources(container);
		
		for (IResource resource : childResourcesOfParentResource) {
			if (resource instanceof IFile) {
				IFile file = (IFile) resource;
				
				if (file.getFileExtension().equals(fileExtension)) {
					relevantFiles.add(file);
				}
			}
			else if (resource instanceof IFolder) {
				IFolder subFolder = (IFolder) resource;
				List<IFile> relevantSubFiles = recursivelyCollectAllFiles(subFolder, fileExtension);
				relevantFiles.addAll(relevantSubFiles);
			}
		}
		
		return relevantFiles;
	}

	public static boolean fileIsExecutionModel(IFile file) {
		return file.getFileExtension().equals(BCC_FILE_EXTENSION);
	}

	public static boolean fileIsBibTeXFile(IFile file) {
		return file.getFileExtension().equals(BIB_FILE_EXTENSION);
	}

	public static boolean fileIsConsistencyRule(IFile file) {
		return file.getFileExtension().equals(BCC_RULE_FILE_EXTENSION);
	}

	public static void createFolder(IProject project, String folderName) throws CoreException
	{
		IFolder folder = project.getFolder(folderName);
		folder.create(IResource.NONE, true, null);
	}

	public static IProject createNewProject(String projectName) throws CoreException
	{
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject project = workspace.getRoot().getProject(projectName);
		
		if (!project.exists()) {
			project.create(null);
		}
		
		return project;
	}

	public static void refreshProject(IResource resource) throws CoreException
	{
		resource.refreshLocal(IResource.DEPTH_INFINITE, null);
	}

}
