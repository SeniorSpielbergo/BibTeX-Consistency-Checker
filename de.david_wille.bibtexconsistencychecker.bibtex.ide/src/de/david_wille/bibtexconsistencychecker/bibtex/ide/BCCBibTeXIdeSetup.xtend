/*
 * generated by Xtext 2.12.0
 */
package de.david_wille.bibtexconsistencychecker.bibtex.ide

import com.google.inject.Guice
import de.david_wille.bibtexconsistencychecker.bibtex.BCCBibTeXRuntimeModule
import de.david_wille.bibtexconsistencychecker.bibtex.BCCBibTeXStandaloneSetup
import org.eclipse.xtext.util.Modules2

/**
 * Initialization support for running Xtext languages as language servers.
 */
class BCCBibTeXIdeSetup extends BCCBibTeXStandaloneSetup {

	override createInjector() {
		Guice.createInjector(Modules2.mixin(new BCCBibTeXRuntimeModule, new BCCBibTeXIdeModule))
	}
	
}