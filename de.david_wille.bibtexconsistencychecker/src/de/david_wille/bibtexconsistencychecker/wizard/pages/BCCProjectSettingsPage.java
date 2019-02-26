package de.david_wille.bibtexconsistencychecker.wizard.pages;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil;

public class BCCProjectSettingsPage extends WizardPage {
	
	private static final String PAGE_NAME = "New Project";
	private static final String PAGE_TITLE = "Project";
	private static final String PAGE_DESCRIPTION = "Create a new BibTeX Consistency Checker project.";
	private Composite containerComposite;
	private Text projectNameText;
	private IWorkspaceRoot workspaceRoot;
	private String projectName;

	public BCCProjectSettingsPage() {
		super(PAGE_NAME);
		setTitle(PAGE_TITLE);
		setDescription(PAGE_DESCRIPTION);
		workspaceRoot = BCCResourceUtil.getWorkspaceRoot();
	}

	@Override
	public void createControl(Composite parent) {
		containerComposite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        containerComposite.setLayout(layout);
        layout.numColumns = 2;
        
        Label projectNameLabel = new Label(containerComposite, SWT.NONE);
        projectNameLabel.setText("Project Name");

        projectNameText = new Text(containerComposite, SWT.BORDER | SWT.SINGLE);
        projectNameText.setText("");
        projectNameText.setFocus();
        projectNameText.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                return;
            }

            @Override
            public void keyReleased(KeyEvent e) {
            	String textValue = projectNameText.getText();
            	
                if (!textValue.isEmpty()) {
                	if (!workspaceRoot.getProject(textValue).exists()) {
                		setErrorMessage(null);
                		setPageComplete(true);
                		projectName = textValue;
                		return;
                	}
                	else {
                		setErrorMessage("A project with that name already exists in the workspace.");
                	}
                }
                else {
                	setErrorMessage("The project name cannot be empty.");
                }
                projectName = "";
                setPageComplete(false);
            }
        });
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        projectNameText.setLayoutData(gd);
        
        setControl(containerComposite);
        setPageComplete(false);
	}
	
	public String getProjectName() {
		return projectName;
	}

}
