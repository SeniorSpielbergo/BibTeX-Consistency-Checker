package de.david_wille.bibtexconsistencychecker.consistencyrule.ui.hyperlink

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEntryKeyObject
import de.david_wille.bibtexconsistencychecker.bibtex.cache.BCCBibTeXCache
import de.david_wille.bibtexconsistencychecker.consistencyrule.bCCConsistencyRule.BCCEntryKeyReference
import de.david_wille.bibtexconsistencychecker.util.BCCResourceUtil
import java.util.List
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

class BCCConsistencyRuleHyperlinkHelper extends HyperlinkHelper {
	
	@Inject
	private Provider<XtextHyperlink> hyperlinkProvider;
	
	@Inject 
	private EObjectAtOffsetHelper eObjectAtOffsetHelper;
	
	override createHyperlinksByOffset(XtextResource resource, int offset, IHyperlinkAcceptor acceptor) {
		super.createHyperlinksByOffset(resource, offset, acceptor)

		var EObject eObject = eObjectAtOffsetHelper.resolveElementAt(resource, offset)
		
		if (eObject instanceof BCCEntryKeyReference) {
			var String entryKey = eObject.entryKey
			
			var INode node = NodeModelUtils.findActualNodeFor(eObject)
			
			var XtextHyperlink hyperlink = hyperlinkProvider.get()
			hyperlink.setHyperlinkRegion(new Region(node.getOffset(), node.getLength()))
			hyperlink.setHyperlinkText("Open containing *.bib file")
			
			var IProject currentProject = BCCResourceUtil.getIProject(eObject)
			var List<BCCAbstractBibTeXEntry> cachedBibTeXEntries = BCCBibTeXCache.instance.getEntries(currentProject, entryKey)
			
			if (cachedBibTeXEntries.size() == 1) {
				var BCCEntryKeyObject entryKeyObject = cachedBibTeXEntries.get(0).entryBody.entryKeyObject
				
				this.createHyperlinksTo(resource, new Region(node.getOffset(), node.getLength()), entryKeyObject, acceptor);
			}
		}
	}
	
}
