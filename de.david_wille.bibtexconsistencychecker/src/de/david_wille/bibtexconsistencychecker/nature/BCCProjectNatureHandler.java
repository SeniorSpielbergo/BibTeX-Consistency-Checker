package de.david_wille.bibtexconsistencychecker.nature;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.ui.XtextProjectHelper;

import com.google.inject.Injector;

import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel;
import de.david_wille.bibtexconsistencychecker.executionmodel.ui.internal.ExecutionmodelActivator;
import de.david_wille.bibtexconsistencychecker.executionmodel.util.BCCDefaultExecutionModel;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;

public class BCCProjectNatureHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection currentSelection = HandlerUtil.getCurrentSelection(event);

		if (currentSelection instanceof IStructuredSelection) {
			Object firstElement = ((IStructuredSelection) currentSelection).getFirstElement();

			// Get an IResource as an adapter from the current selection
			IAdapterManager adapterManager = Platform.getAdapterManager();
			IResource resourceAdapter = adapterManager.getAdapter(firstElement, IResource.class);

			if (resourceAdapter != null) {
				IResource resource = resourceAdapter;
				IProject project = resource.getProject();
				try {
					IStatus status = applyBibTeXConsistencyCheckerSpecificPreferences(project);
					return status;
				}
				catch (CoreException e) {
					throw new ExecutionException(e.getMessage(), e);
				}
			}
		}

		return Status.OK_STATUS;
	}

	public static IStatus applyBibTeXConsistencyCheckerSpecificPreferences(IProject project) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] natures = description.getNatureIds();
		String[] newNatures = generateAndFillNewNaturesArray(natures);
		
		// add Xtext builder if necessary
		ICommand[] buildSpec = description.getBuildSpec();
		if (!existingBuildSpecContainsXtextBuilder(description.getBuildSpec())) {
			ICommand[] newBuildSpec = generateAndFillNewBuildSpecArray(description, buildSpec);
			description.setBuildSpec(newBuildSpec);
		}
		
		// validate the natures
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IStatus status = workspace.validateNatureSet(newNatures);

		// only apply new nature, if the status is ok
		if (status.getCode() == IStatus.OK) {
			description.setNatureIds(newNatures);
			project.setDescription(description, null);
		}
		
		if (!projectAlreadyContainsExecutionModel(project)) {
			BCCExecutionModel executionModel = BCCDefaultExecutionModel.generate(project, project.getName());
			Injector injector = ExecutionmodelActivator.getInstance().getInjector(ExecutionmodelActivator.DE_DAVID_WILLE_BIBTEXCONSISTENCYCHECKER_EXECUTIONMODEL_BCCEXECUTIONMODEL);
			BCCResourceUtil.storeModel(injector, executionModel, project, project.getName(), "bcc");
			
			IFile executionModelFile = BCCResourceUtil.getIFile(executionModel);
			BCCResourceUtil.openEditor(executionModelFile);
		}
		
		return status;
	}

	private static boolean projectAlreadyContainsExecutionModel(IProject project) {
		for (IResource resource : BCCResourceUtil.getChildResources(project)) {
			if (resource instanceof IFile) {
				IFile file = (IFile) resource;
				if (BCCResourceUtil.fileIsExecutionModel(file)) {
					return true;
				}
			}
		}
		return false;
	}

	private static ICommand[] generateAndFillNewBuildSpecArray(IProjectDescription description, ICommand[] buildSpec) {
		ICommand command = description.newCommand();
		command.setBuilderName(XtextProjectHelper.BUILDER_ID);
		
		ICommand[] newBuildSpec = new ICommand[buildSpec.length+1];
		newBuildSpec[buildSpec.length] = command;
		return newBuildSpec;
	}

	private static String[] generateAndFillNewNaturesArray(String[] natures) {
		int newArrayLength = identifyNewNaturesArrayLength(natures);
		String[] newNatures = new String[newArrayLength];
		System.arraycopy(natures, 0, newNatures, 0, natures.length);
		
		if (existingNaturesContainXtextNature(natures)) {
			newNatures[natures.length] = BCCProjectNature.NATURE_ID;
		}
		else {
			newNatures[natures.length] = BCCProjectNature.NATURE_ID;
			newNatures[natures.length+1] = XtextProjectHelper.NATURE_ID;
		}
		
		return newNatures;
	}

	private static int identifyNewNaturesArrayLength(String[] natures) {
		int newArrayLength = natures.length;
		if (existingNaturesContainXtextNature(natures)) {
			newArrayLength += 1;
		}
		else {
			newArrayLength += 2;
		}
		
		return newArrayLength;
	}

	private static boolean existingBuildSpecContainsXtextBuilder(ICommand[] commands) {
		for (ICommand command : commands) {
			if (command.getBuilderName().equals(XtextProjectHelper.BUILDER_ID)) {
				return true;
			}
		}
		return false;
	}

	private static boolean existingNaturesContainXtextNature(String[] natures) {
		for (String nature : natures) {
			if (nature.equals(XtextProjectHelper.NATURE_ID)) {
				return true;
			}
		}
		return false;
	}

}
