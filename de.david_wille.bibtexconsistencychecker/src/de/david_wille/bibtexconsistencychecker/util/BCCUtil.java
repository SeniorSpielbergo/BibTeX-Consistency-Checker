package de.david_wille.bibtexconsistencychecker.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXFileEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractEntryBodyField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;

public class BCCUtil {

	public static void openErrorDialog(String message) {
		MessageDialog.openError(new Shell(), "Error", message);
	}

	@SuppressWarnings("unchecked")
	public static <T> T identifyField(BCCAbstractBibTeXEntry bibTeXEntry, Class<T> type) {
		for (BCCAbstractEntryBodyField bodyField : bibTeXEntry.getEntryBody().getFields()) {
			if (type.isInstance(bodyField)) {
				return (T) bodyField;
			}
		}
		return null;
	}

	public static <T> boolean bibTeXEntryContainsField(BCCAbstractBibTeXEntry bibTeXEntry, Class<T> type) {
		for (BCCAbstractEntryBodyField bodyField : bibTeXEntry.getEntryBody().getFields()) {
			if (type.isInstance(bodyField)) {
				return true;
			}
		}
		
		return false;
	}

	public static List<BCCAbstractBibTeXEntry> collectAbstractBibTeXEntries(BCCBibTeXFile bibTeXFile) {
		List<BCCAbstractBibTeXEntry> bibTeXEntries = new ArrayList<>();
		
		for (BCCAbstractBibTeXFileEntry abstractFileEntry : bibTeXFile.getEntries()) {
			if (abstractFileEntry instanceof BCCAbstractBibTeXEntry) {
				BCCAbstractBibTeXEntry bibTeXEntry = (BCCAbstractBibTeXEntry) abstractFileEntry;
				bibTeXEntries.add(bibTeXEntry);
			}
		}
		
		return bibTeXEntries;
	}

}
