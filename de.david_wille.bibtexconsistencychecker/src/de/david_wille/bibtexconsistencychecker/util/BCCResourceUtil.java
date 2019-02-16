package de.david_wille.bibtexconsistencychecker.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
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
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

import de.david_wille.bibtexconsistencychecker.bibtex.BCCBibTeXStandaloneSetup;

public class BCCResourceUtil {

	@SuppressWarnings("unchecked")
	public static <T> T parseModel(IFile file) {
		Injector injector = new BCCBibTeXStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);

		Resource resource = getResource(file);
		T parsedModel = (T) resource.getContents().get(0);
		
		return parsedModel;
	}
	
	public static boolean resourceIsFolder(IResource resource) {
		return resource.getType() == IResource.FOLDER;
	}
	
	public static boolean resourceIsProject(IResource resource) {
		return resource.getType() == IResource.PROJECT;
	}

	public static boolean resourceIsFile(IResource resource) {
		return resource.getType() == IResource.FILE;
	}

	public static boolean resourceIsContainer(IResource resource) {
		return resourceIsFolder(resource) || resourceIsProject(resource);
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
		
		if (resourceURI.isFile()) {
			String fileString = resourceURI.toFileString();
			File file = new File(fileString);
			return javaIOFileToIFile(file);
		}
		
		if (resourceURI.isPlatformResource()) {
			String platformString = resourceURI.toPlatformString(true);
			return makeIFileRelativeToWorkspace(platformString);
		}
		
		return null;
	}

	private static IFile javaIOFileToIFile(File file) {
		IWorkspaceRoot workspaceRoot = getWorkspaceRoot();
		IPath workspaceLocation = workspaceRoot.getLocation();
		IPath filePath = new Path(file.getPath());
		
		if (!fileIsContainedInWorkspace(file)) {
			return null;
		}
		
		IPath relativeFilePath = filePath.makeRelativeTo(workspaceLocation);
		return workspaceRoot.getFile(relativeFilePath);
	}
	
	public static IWorkspaceRoot getWorkspaceRoot() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		return workspace.getRoot();
	}

	public static boolean fileIsContainedInWorkspace(File file) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		IPath workspaceLocation = workspaceRoot.getLocation();
		
		IPath filePath = new Path(file.getPath());
		
		return workspaceLocation.isPrefixOf(filePath);
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

	public static <T> List<T> parseModels(List<IFile> files) {
		List<T> parsedModels = new ArrayList<>();
		for (IFile file : files) {
			T parsedModel = parseModel(file);
			parsedModels.add(parsedModel);
		}
		return parsedModels;
	}

	public static List<IResource> getChildResources(IResource resource) {
		if (resourceIsFile(resource)) {
			return new ArrayList<>();
		}
		else if (resourceIsContainer(resource)) {
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
	
}
