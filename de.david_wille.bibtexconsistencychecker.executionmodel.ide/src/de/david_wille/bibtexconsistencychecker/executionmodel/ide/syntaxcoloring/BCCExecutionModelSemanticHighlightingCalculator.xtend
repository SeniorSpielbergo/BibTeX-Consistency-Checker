package de.david_wille.bibtexconsistencychecker.executionmodel.ide.syntaxcoloring

import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCExecutionModelPackage
import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCFilePathEntry
import de.david_wille.bibtexconsistencychecker.executionmodel.services.BCCExecutionModelGrammarAccess
import javax.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.ide.editor.syntaxcoloring.DefaultSemanticHighlightingCalculator
import org.eclipse.xtext.ide.editor.syntaxcoloring.HighlightingStyles
import org.eclipse.xtext.ide.editor.syntaxcoloring.IHighlightedPositionAcceptor
import org.eclipse.xtext.util.CancelIndicator

class BCCExecutionModelSemanticHighlightingCalculator extends DefaultSemanticHighlightingCalculator {
	@Inject package BCCExecutionModelGrammarAccess grammar

	override protected boolean highlightElement(EObject object, IHighlightedPositionAcceptor acceptor,
		CancelIndicator cancelIndicator)
	{
		switch (object) {
			BCCFilePathEntry: {
				highlightFeature(acceptor, object, BCCExecutionModelPackage.eINSTANCE.BCCFilePathEntry_Path, HighlightingStyles.DEFAULT_ID)
				return true
			}
			default: false
		}
	}
	
}
				