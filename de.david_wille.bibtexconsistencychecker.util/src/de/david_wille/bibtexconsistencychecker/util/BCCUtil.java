package de.david_wille.bibtexconsistencychecker.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class BCCUtil {

	public static void openErrorDialog(String message) {
		MessageDialog.openError(new Shell(), "Error", message);
	}

	public static void openWarningDialog(String message) {
		MessageDialog.openWarning(new Shell(), "Warning", message);
	}

	public static void openInformationDialog(String title, String message) {
		MessageDialog.openInformation(new Shell(), title, message);
	}

}
