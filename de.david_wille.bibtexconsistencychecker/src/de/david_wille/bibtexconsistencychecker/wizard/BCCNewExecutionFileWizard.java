package de.david_wille.bibtexconsistencychecker.wizard;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.david_wille.bibtexconsistencychecker.BCCLauncherResourceUtil;

public class BCCNewExecutionFileWizard extends Wizard implements INewWizard {

	private static final String WINDOW_TITLE = "New BibTeX Consistency Checker Execution File";

	public BCCNewExecutionFileWizard() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		ImageDescriptor image = AbstractUIPlugin.imageDescriptorFromPlugin("de.david_wille.bibtexconsistencychecker", "icons/bcc_icon96.png");
		setDefaultPageImageDescriptor(image);
		
		List<IResource> selectedResources = BCCLauncherResourceUtil.identifyAllResourcesFromSelection(selection);
	}

	@Override
	public String getWindowTitle() {
		return WINDOW_TITLE;
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}
