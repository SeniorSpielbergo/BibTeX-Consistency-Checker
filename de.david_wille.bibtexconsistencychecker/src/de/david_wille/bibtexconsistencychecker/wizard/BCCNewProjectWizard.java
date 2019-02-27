package de.david_wille.bibtexconsistencychecker.wizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.david_wille.bibtexconsistencychecker.executionmodel.BCCExecutionModelStandaloneSetup;
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModel;
import de.david_wille.bibtexconsistencychecker.executionmodel.util.BCCDefaultExecutionModel;
import de.david_wille.bibtexconsistencychecker.nature.BCCProjectNature;
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;
import de.david_wille.bibtexconsistencychecker.wizard.pages.BCCWizardNewProjectCreationPage;

public class BCCNewProjectWizard extends Wizard implements INewWizard {

	private static final String PROJECT_CREATION_PAGE_NAME = "New Project";
	private static final String PROJECT_CREATION_PAGE_TITLE = "Project";
	private static final String PROJECT_CREATION_PAGE_DESCRIPTION = "Create a new BibTeX Consistency Checker project.";

	private static final String WINDOW_TITLE = "New BibTeX Consistency Checker Project";
	private WizardNewProjectCreationPage newProjectCreationPage;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		ImageDescriptor image = AbstractUIPlugin.imageDescriptorFromPlugin("de.david_wille.bibtexconsistencychecker", "icons/bcc_icon96.png");
		setDefaultPageImageDescriptor(image);
		
		newProjectCreationPage = new BCCWizardNewProjectCreationPage(PROJECT_CREATION_PAGE_NAME, PROJECT_CREATION_PAGE_TITLE, PROJECT_CREATION_PAGE_DESCRIPTION);
	}

	@Override
	public void addPages() {
		addPage(newProjectCreationPage);
	}

	@Override
	public String getWindowTitle() {
		return WINDOW_TITLE;
	}

	@Override
	public boolean performFinish() {
		String projectName = newProjectCreationPage.getProjectName();

		try {
			IProject project = BCCResourceUtil.createNewProject(projectName);
			project.open(null);
			
			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);

			newNatures[natures.length] = BCCProjectNature.NATURE_ID;

			// validate the natures
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IStatus status = workspace.validateNatureSet(newNatures);

			// only apply new nature, if the status is ok
			if (status.getCode() == IStatus.OK) {
				description.setNatureIds(newNatures);
				project.setDescription(description, null);
			}
			
			BCCExecutionModel executionModel = BCCDefaultExecutionModel.generate(projectName);
			BCCResourceUtil.storeModel(new BCCExecutionModelStandaloneSetup(), executionModel, project, projectName, "bcc");
			
			BCCResourceUtil.refreshProject(project);
		}
		catch (CoreException e) {
			e.printStackTrace();
		}

		return true;
	}

}
