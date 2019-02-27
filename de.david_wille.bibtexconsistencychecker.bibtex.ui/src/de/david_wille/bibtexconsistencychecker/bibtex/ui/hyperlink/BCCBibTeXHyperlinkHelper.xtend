package de.david_wille.bibtexconsistencychecker.bibtex.ui.hyperlink

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractGenericField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCGenericFieldValueObject
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplaceKeyObject
import de.david_wille.bibtexconsistencychecker.bibtex.cache.BCCBibTeXCache
import de.david_wille.bibtexconsistencychecker.bibtex.util.BCCBibTeXUtil
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil
import javax.inject.Inject
import javax.inject.Provider
import org.eclipse.core.resources.IProject
import org.eclipse.emf.ecore.EObject
import org.eclipse.jface.text.Region
import org.eclipse.xtext.nodemodel.INode
import org.eclipse.xtext.nodemodel.util.NodeModelUtils
import org.eclipse.xtext.resource.EObjectAtOffsetHelper
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.ui.editor.hyperlinking.HyperlinkHelper
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkAcceptor
import org.eclipse.xtext.ui.editor.hyperlinking.XtextHyperlink

class BCCBibTeXHyperlinkHelper extends HyperlinkHelper {
	
	@Inject
	private Provider<XtextHyperlink> hyperlinkProvider;
	
	@Inject 
	private EObjectAtOffsetHelper eObjectAtOffsetHelper;
	
	override createHyperlinksByOffset(XtextResource resource, int offset, IHyperlinkAcceptor acceptor) {
		super.createHyperlinksByOffset(resource, offset, acceptor)

		var EObject eObject = eObjectAtOffsetHelper.resolveElementAt(resource, offset)
		
		if (eObject instanceof BCCGenericFieldValueObject) {
			var BCCAbstractGenericField genericField = eObject.eContainer as BCCAbstractGenericField
			if (BCCBibTeXUtil.usesReplacePattern(genericField)) {
				var String entryKey = genericField.fieldValueObject.fieldValue
				var INode node = NodeModelUtils.findActualNodeFor(genericField.fieldValueObject)
				
				var XtextHyperlink hyperlink = hyperlinkProvider.get()
				hyperlink.setHyperlinkRegion(new Region(node.getOffset(), node.getLength()))
				hyperlink.setHyperlinkText("Open containing *.bib file")
				
				var IProject currentProject = BCCResourceUtil.getIProject(eObject)
				var BCCReplaceKeyObject replaceKeyObject = BCCBibTeXCache.instance.getReplacePattern(currentProject, entryKey).replaceKeyObject
				
				this.createHyperlinksTo(resource, new Region(node.getOffset(), node.getLength()), replaceKeyObject, acceptor);
			}
		}
	}
	
}
