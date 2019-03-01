package de.david_wille.bibtexconsistencychecker.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class BCCUtil {

	public static void openErrorDialog(String message) {
		openErrorDialog("Error", message);
	}

	public static void openErrorDialog(String title, String message) {
		Shell shell = getWorkbenchWindow().getShell();
		MessageDialog.openError(shell, title, message);
	}

	public static void openWarningDialog(String message) {
		openWarningDialog("Warning", message);
	}

	public static void openWarningDialog(String title, String message) {
		Shell shell = getWorkbenchWindow().getShell();
		MessageDialog.openWarning(shell, title, message);
	}

	public static void openInformationDialog(String message) {
		openInformationDialog("Information", message);
	}

	public static void openInformationDialog(String title, String message) {
		Shell shell = getWorkbenchWindow().getShell();
		MessageDialog.openInformation(shell, title, message);
	}
	
	public static IWorkbenchWindow getWorkbenchWindow() {
		IWorkbench wb = PlatformUI.getWorkbench();
		return wb.getActiveWorkbenchWindow();
	}

}
