package de.david_wille.bibtexconsistencychecker.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class BCCUtil {

	public static void openErrorDialog(String message) {
		MessageDialog.openError(new Shell(), "Error", message);
	}

}
