package de.david_wille.bibtexconsistencychecker.executionmodel.ui.hyperlink

import de.david_wille.bibtexconsistencychecker.executionmodel.bCCExecutionModel.BCCFilePathEntry
import javax.inject.Inject
import javax.inject.Provider
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IWorkspaceRoot
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.nodemodel.INode
import org.eclipse.xtext.nodemodel.util.NodeModelUtils
import org.eclipse.xtext.resource.EObjectAtOffsetHelper
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.ui.editor.hyperlinking.HyperlinkHelper
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkAcceptor
import org.eclipse.xtext.ui.editor.hyperlinking.XtextHyperlink
import org.eclipse.jface.text.Region
import org.eclipse.emf.common.util.URI

class BCCExecutionModelHyperlinkHelper extends HyperlinkHelper {
	
	@Inject
	private Provider<XtextHyperlink> hyperlinkProvider;
	
	@Inject 
	private EObjectAtOffsetHelper eObjectAtOffsetHelper;
	
	override createHyperlinksByOffset(XtextResource resource, int offset, IHyperlinkAcceptor acceptor) {
		super.createHyperlinksByOffset(resource, offset, acceptor)

		var EObject eObject = eObjectAtOffsetHelper.resolveElementAt(resource, offset)
		
		if (eObject instanceof BCCFilePathEntry) {
			var String path = eObject.path
			
			if (isFile(path)) {
				var INode node = NodeModelUtils.findActualNodeFor(eObject)
			
				var String location = ""
				
				var XtextHyperlink hyperlink = hyperlinkProvider.get()
				hyperlink.setHyperlinkRegion(new Region(node.getOffset(), node.getLength()))
				if (isConsistencyRuleFile(path)) {
					hyperlink.setHyperlinkText("Open referenced consistency rule")
				}
				else if (isBibTexFile(path)) {
					hyperlink.setHyperlinkText("Open referenced BibTeX file")
				}
				
				var IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot()
				var IProject currentProject = root.getProject(eObject.eResource().getURI().segment(1))
				
				if (currentProject.getFile("/" + path).exists()) {
					location = "platform:/resource/"
								+ eObject.eResource().getURI().segment(1) + "/"
								+ path
					
					var URI fileUri = URI.createURI(location)
					hyperlink.setURI(fileUri)
					acceptor.accept(hyperlink)
				}
			}
		}
	}
	
	protected def isFile(String path) {
		isBibTexFile(path) || isConsistencyRuleFile(path)
	}
	
	protected def isBibTexFile(String path) {
		path.endsWith(".bib")
	}
	
	protected def isConsistencyRuleFile(String path) {
		path.endsWith(".bcc_rule")
	}
	
}