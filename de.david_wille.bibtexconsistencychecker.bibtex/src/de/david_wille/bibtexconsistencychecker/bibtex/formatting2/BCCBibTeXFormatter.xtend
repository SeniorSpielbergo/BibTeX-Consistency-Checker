package de.david_wille.bibtexconsistencychecker.bibtex.formatting2

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractBibTeXFileEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractEntryBodyField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAbstractGenericField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAuthorField
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEntryBody
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCPersonFieldBody
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplacePatternEntry
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCYearField
import org.eclipse.xtext.formatting2.AbstractFormatter2
import org.eclipse.xtext.formatting2.IFormattableDocument
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegion

class BCCBibTeXFormatter extends AbstractFormatter2 {

	def dispatch void format(BCCBibTeXFile bCCBibTeXFile, extension IFormattableDocument document) {
		for (BCCAbstractBibTeXFileEntry bCCAbstractBibTeXFileEntry : bCCBibTeXFile.getEntries()) {
			bCCAbstractBibTeXFileEntry.format;
		}
	}
	
	def dispatch void format(BCCAbstractBibTeXEntry bCCAbstractBibTeXEntry, extension IFormattableDocument document) {
		bCCAbstractBibTeXEntry.entryBody.format
	}
	
	def dispatch void format(BCCEntryBody bCCEntryBody, extension IFormattableDocument document) {
		var ISemanticRegion entryBodyStart = bCCEntryBody.regionFor.keyword("{")
		entryBodyStart.prepend[noSpace]
		entryBodyStart.append[noSpace]
		var ISemanticRegion entryBodyEnd = bCCEntryBody.regionFor.keyword("}")
		entryBodyEnd.prepend[newLine]
		entryBodyEnd.append[newLines = 2]
		
		for (ISemanticRegion commaRegion : bCCEntryBody.allRegionsFor.keywords(",")) {
			commaRegion.prepend[noSpace]
		}
		
		interior(entryBodyStart, entryBodyEnd)[indent]
		
		for (BCCAbstractEntryBodyField entryField : bCCEntryBody.fields) {
			entryField.format
		}
	}

	def dispatch void format(BCCAbstractGenericField bCCAbstractGenericField, extension IFormattableDocument document) {
		bCCAbstractGenericField.prepend[newLine]
	}

	def dispatch void format(BCCYearField bCCYearField, extension IFormattableDocument document) {
		bCCYearField.prepend[newLine]
		var ISemanticRegion yearFieldStart = bCCYearField.regionFor.keyword("{")
		yearFieldStart.append[noSpace]
		var ISemanticRegion yearFieldEnd = bCCYearField.regionFor.keyword("}")
		yearFieldEnd.prepend[noSpace]
	}

	def dispatch void format(BCCAuthorField bCCAuthorField, extension IFormattableDocument document) {
		bCCAuthorField.prepend[newLine]
		bCCAuthorField.personFieldBody.format
	}

	def dispatch void format(BCCPersonFieldBody bCCPersonFieldBody, extension IFormattableDocument document) {
		var ISemanticRegion personFieldBodyStart = bCCPersonFieldBody.regionFor.keyword("{")
		personFieldBodyStart.append[noSpace]
		var ISemanticRegion personFieldBodyEnd = bCCPersonFieldBody.regionFor.keyword("}")
		personFieldBodyEnd.prepend[noSpace]
	}

	def dispatch void format(BCCReplacePatternEntry bCCReplacePatternEntry, extension IFormattableDocument document) {
		var ISemanticRegion replacePatternEntryStart = bCCReplacePatternEntry.regionFor.keyword("{")
		replacePatternEntryStart.prepend[noSpace]
		replacePatternEntryStart.append[noSpace]
		var ISemanticRegion replacePatternEntryEnd = bCCReplacePatternEntry.regionFor.keyword("}")
		replacePatternEntryEnd.prepend[noSpace]
	}
}
