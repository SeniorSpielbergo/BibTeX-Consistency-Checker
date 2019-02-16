/*
 * generated by Xtext 2.12.0
 */
package de.david_wille.bibtexconsistencychecker.executionmodel.ide

import com.google.inject.Guice
import de.david_wille.bibtexconsistencychecker.executionmodel.BCCExecutionModelRuntimeModule
import de.david_wille.bibtexconsistencychecker.executionmodel.BCCExecutionModelStandaloneSetup
import org.eclipse.xtext.util.Modules2

/**
 * Initialization support for running Xtext languages as language servers.
 */
class BCCExecutionModelIdeSetup extends BCCExecutionModelStandaloneSetup {

	override createInjector() {
		Guice.createInjector(Modules2.mixin(new BCCExecutionModelRuntimeModule, new BCCExecutionModelIdeModule))
	}
	
}
