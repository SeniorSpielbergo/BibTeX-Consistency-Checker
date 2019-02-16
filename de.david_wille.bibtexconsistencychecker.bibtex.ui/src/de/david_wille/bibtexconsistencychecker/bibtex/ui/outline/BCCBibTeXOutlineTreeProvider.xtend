package de.david_wille.bibtexconsistencychecker.bibtex.ui.outline

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXFileEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractGenericField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractPersonField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAddressField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCArticleEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAuthorField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBookEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBookletEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBooktitleField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCChapterField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCConferenceEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCDateField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCDoiField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEditionField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEditorField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCHowPublishedField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCInBookEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCInCollectionEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCInProceedingsEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCInstitutionField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCIsbnField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCJournalField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCKeywordsField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCManualEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCMasterthesisEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCMiscEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCMonthField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCNoteField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCNumberField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCOrganizationField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCPagesField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCPhdThesisEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCProceedingsEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCPublisherField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplacePatternEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReportEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCRevisionField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCSchoolField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCSeriesField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCStandardEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCTechReportEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCThesisEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCTitleField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCTypeField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCUnpublishedEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCUrlField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCVersionField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCVolumeField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCYearField
import org.eclipse.swt.graphics.Image
import org.eclipse.xtext.ui.editor.outline.IOutlineNode
import org.eclipse.xtext.ui.editor.outline.impl.DefaultOutlineTreeProvider
import org.eclipse.xtext.ui.editor.outline.impl.DocumentRootNode
import org.eclipse.xtext.ui.editor.outline.impl.EObjectNode

/**
 * Customization of the default outline structure.
 *
 * See https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#outline
 */
class BCCBibTeXOutlineTreeProvider extends DefaultOutlineTreeProvider {
	
	protected def _createNode(DocumentRootNode parentNode, BCCBibTeXFile file) {
		var Object text = textDispatcher.invoke(file);
		if (text === null) {
			text = file.eResource().getURI().trimFileExtension().lastSegment();
		}
		var String stringRepresentation = "BibTeX File \"" + text + "\""
		var Image image = imageDispatcher.invoke(file);
		var IOutlineNode childNode = new EObjectNode(file, parentNode, image, stringRepresentation, false);
		createChildren(childNode, file)
		
		childNode
	}
	
	protected def _createNode(IOutlineNode parentNode, BCCAbstractBibTeXFileEntry fileEntry) {
		var String stringRepresentation = ""
		
		if (fileEntry instanceof BCCInProceedingsEntry) {
			stringRepresentation += "In Proceedings \"" + fileEntry.entryBody.entryKey + "\""
		}
		else if (fileEntry instanceof BCCBookEntry) {
			stringRepresentation += "Book \"" + fileEntry.entryBody.entryKey + "\""
		}
		else if (fileEntry instanceof BCCBookletEntry) {
			stringRepresentation += "Booklet \"" + fileEntry.entryBody.entryKey + "\""
		}
		else if (fileEntry instanceof BCCConferenceEntry) {
			stringRepresentation += "Conference \"" + fileEntry.entryBody.entryKey + "\""
		}
		else if (fileEntry instanceof BCCManualEntry) {
			stringRepresentation += "Manual \"" + fileEntry.entryBody.entryKey + "\""
		}
		else if (fileEntry instanceof BCCMasterthesisEntry) {
			stringRepresentation += "Master's Thesis \"" + fileEntry.entryBody.entryKey + "\""
		}
		else if (fileEntry instanceof BCCMiscEntry) {
			stringRepresentation += "Misc \"" + fileEntry.entryBody.entryKey + "\""
		}
		else if (fileEntry instanceof BCCPhdThesisEntry) {
			stringRepresentation += "PhD Thesis \"" + fileEntry.entryBody.entryKey + "\""
		}
		else if (fileEntry instanceof BCCArticleEntry) {
			stringRepresentation += "Article \"" + fileEntry.entryBody.entryKey + "\""
		}
		else if (fileEntry instanceof BCCStandardEntry) {
			stringRepresentation += "Standard \"" + fileEntry.entryBody.entryKey + "\""
		}
		else if (fileEntry instanceof BCCInBookEntry) {
			stringRepresentation += "In Book \"" + fileEntry.entryBody.entryKey + "\""
		}
		else if (fileEntry instanceof BCCReportEntry) {
			stringRepresentation += "Report \"" + fileEntry.entryBody.entryKey + "\""
		}
		else if (fileEntry instanceof BCCTechReportEntry) {
			stringRepresentation += "Technical Report \"" + fileEntry.entryBody.entryKey + "\""
		}
		else if (fileEntry instanceof BCCUnpublishedEntry) {
			stringRepresentation += "Unpublished \"" + fileEntry.entryBody.entryKey + "\""
		}
		else if (fileEntry instanceof BCCThesisEntry) {
			stringRepresentation += "Thesis \"" + fileEntry.entryBody.entryKey + "\""
		}
		else if (fileEntry instanceof BCCInCollectionEntry) {
			stringRepresentation += "In Collection \"" + fileEntry.entryBody.entryKey + "\""
		}
		else if (fileEntry instanceof BCCProceedingsEntry) {
			stringRepresentation += "Proceedings \"" + fileEntry.entryBody.entryKey + "\""
		}
		else if (fileEntry instanceof BCCReplacePatternEntry) {
			stringRepresentation += "String Replace Pattern \"" + fileEntry.replaceKey + "\""
		}
		
		if (fileEntry instanceof BCCAbstractBibTeXEntry) {
			var Image image = imageDispatcher.invoke(fileEntry);
			var IOutlineNode childNode = new EObjectNode(fileEntry, parentNode, image, stringRepresentation, false);
			createChildren(childNode, fileEntry.entryBody)
			
			childNode
		}
		else {
			var Image image = imageDispatcher.invoke(fileEntry);
			new EObjectNode(fileEntry, parentNode, image, stringRepresentation, true);
		}
	}
	
	protected def _createNode(IOutlineNode parentNode, BCCAbstractPersonField personField) {
		var String stringRepresentation = ""
		
		if (personField instanceof BCCAuthorField) {
			stringRepresentation += "author"
		}
		else if (personField instanceof BCCEditorField) {
			stringRepresentation += "editor"
		}
		
		var Image image = imageDispatcher.invoke(personField);
		new EObjectNode(personField, parentNode, image, stringRepresentation, true);
	}
	
	protected def _createNode(IOutlineNode parentNode, BCCPagesField pagesField) {
		var String stringRepresentation = "pages"
		
		var Image image = imageDispatcher.invoke(pagesField);
		new EObjectNode(pagesField, parentNode, image, stringRepresentation, true);
	}
	
	protected def _createNode(IOutlineNode parentNode, BCCAbstractGenericField genericField) {
		var String stringRepresentation = generateAbstractGenericFieldRepresentation(genericField)
		
		var Image image = imageDispatcher.invoke(genericField);
		new EObjectNode(genericField, parentNode, image, stringRepresentation, true);
	}
	
	protected def String generateAbstractGenericFieldRepresentation(BCCAbstractGenericField genericField) {
		if (genericField instanceof BCCInProceedingsEntry) {
			"title"
		}
		else if (genericField instanceof BCCTitleField) {
			"title"
		}
		else if (genericField instanceof BCCEditionField) {
			"edition"
		}
		else if (genericField instanceof BCCInstitutionField) {
			"institution"
		}
		else if (genericField instanceof BCCOrganizationField) {
			"organization"
		}
		else if (genericField instanceof BCCRevisionField) {
			"revision"
		}
		else if (genericField instanceof BCCUrlField) {
			"url"
		}
		else if (genericField instanceof BCCDoiField) {
			"doi"
		}
		else if (genericField instanceof BCCTypeField) {
			"type"
		}
		else if (genericField instanceof BCCIsbnField) {
			"isbn"
		}
		else if (genericField instanceof BCCAddressField) {
			"address"
		}
		else if (genericField instanceof BCCHowPublishedField) {
			"howpublished"
		}
		else if (genericField instanceof BCCNumberField) {
			"number"
		}
		else if (genericField instanceof BCCVolumeField) {
			"volume"
		}
		else if (genericField instanceof BCCNoteField) {
			"note"
		}
		else if (genericField instanceof BCCSchoolField) {
			"school"
		}
		else if (genericField instanceof BCCKeywordsField) {
			"keywords"
		}
		else if (genericField instanceof BCCBooktitleField) {
			"booktitle"
		}
		else if (genericField instanceof BCCSeriesField) {
			"series"
		}
		else if (genericField instanceof BCCPublisherField) {
			"publisher"
		}
		else if (genericField instanceof BCCJournalField) {
			"journal"
		}
		else if (genericField instanceof BCCChapterField) {
			"chapter"
		}
		else if (genericField instanceof BCCVersionField) {
			"version"
		}
	}
	
	protected def _createNode(IOutlineNode parentNode, BCCYearField yearField) {
		var String stringRepresentation = "year"
		
		var Image image = imageDispatcher.invoke(yearField);
		new EObjectNode(yearField, parentNode, image, stringRepresentation, true);
	}
	
	protected def _createNode(IOutlineNode parentNode, BCCMonthField monthField) {
		var String stringRepresentation = "month"
		
		var Image image = imageDispatcher.invoke(monthField);
		new EObjectNode(monthField, parentNode, image, stringRepresentation, true);
	}
	
	protected def _createNode(IOutlineNode parentNode, BCCDateField dateField) {
		var String stringRepresentation = "date"
		
		var Image image = imageDispatcher.invoke(dateField);
		new EObjectNode(dateField, parentNode, image, stringRepresentation, true);
	}
	
}
