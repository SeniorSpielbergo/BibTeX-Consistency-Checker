package de.david_wille.bibtexconsistencychecker.bibtex.util;

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFactory;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;

public class BCCDefaultBibTeXFile {
	
	public static BCCBibTeXFile generate(String name) {
		BCCBibTeXFactory bibTeXFactory = BCCBibTeXFactory.eINSTANCE;
		
		BCCBibTeXFile bibTeXFile = bibTeXFactory.createBCCBibTeXFile();
		
		return bibTeXFile;
	}
	
}
