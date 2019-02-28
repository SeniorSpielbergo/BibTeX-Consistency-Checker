package de.david_wille.bibtexconsistencychecker.bibtex.util;

import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCAuthorField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFactory;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBibTeXFile;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCBookEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEntryBody;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCEntryKeyObject;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCGenericFieldValueObject;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCPerson;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCPersonFieldBody;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCPublisherField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplaceKeyObject;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCReplacePatternEntry;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCTitleField;
import de.david_wille.bibtexconsistencychecker.bibtex.bCCBibTeX.BCCYearField;

public class BCCDefaultBibTeXFile {
	
	private static final String ENTRY_KEY = "Duc91";
	private static final String AUTHOR_LASTNAME = "Duck";
	private static final String AUTHOR_FIRSTNAME = "Darkwing";
	private static final int YEAR = 1991;
	private static final String TITLE = "{{The Dangerous Live of a Duck}}";
	private static final String PUBLISHER_REPLACEMENT_VALUE = "\"American Broadcasting Company\"";
	private static final String PUBLISHER_REPLACE_KEY = "ABC";

	public static BCCBibTeXFile generate() {
		BCCBibTeXFactory bibTeXFactory = BCCBibTeXFactory.eINSTANCE;
		
		BCCBibTeXFile bibTeXFile = bibTeXFactory.createBCCBibTeXFile();
		
		BCCBookEntry bookEntry = generateBookEntry(bibTeXFactory);
		bibTeXFile.getEntries().add(bookEntry);
		
		BCCReplacePatternEntry replacePatternEntry = generateReplacePatternEntry(bibTeXFactory);
		bibTeXFile.getEntries().add(replacePatternEntry);
		
		return bibTeXFile;
	}

	private static BCCBookEntry generateBookEntry(BCCBibTeXFactory bibTeXFactory) {
		BCCBookEntry bookEntry = bibTeXFactory.createBCCBookEntry();
		
		BCCEntryBody entryBody = generateEntryBody(bibTeXFactory);
		bookEntry.setEntryBody(entryBody);
		
		return bookEntry;
	}

	private static BCCReplacePatternEntry generateReplacePatternEntry(BCCBibTeXFactory bibTeXFactory) {
		BCCReplacePatternEntry replacePatternEntry = bibTeXFactory.createBCCReplacePatternEntry();
		
		BCCReplaceKeyObject replaceKeyObject = generateReplaceKeyObject(bibTeXFactory);
		replacePatternEntry.setReplaceKeyObject(replaceKeyObject);
		
		replacePatternEntry.setReplacementFieldValue(PUBLISHER_REPLACEMENT_VALUE);
		
		return replacePatternEntry;
	}

	private static BCCReplaceKeyObject generateReplaceKeyObject(BCCBibTeXFactory bibTeXFactory) {
		BCCReplaceKeyObject replaceKeyObject = bibTeXFactory.createBCCReplaceKeyObject();
		
		replaceKeyObject.setReplaceKey(PUBLISHER_REPLACE_KEY);
		
		return replaceKeyObject;
	}

	private static BCCEntryBody generateEntryBody(BCCBibTeXFactory bibTeXFactory) {
		BCCEntryBody entryBody = bibTeXFactory.createBCCEntryBody();
		
		BCCEntryKeyObject entryKeyObject = generateEntryKeyObject(bibTeXFactory);
		entryBody.setEntryKeyObject(entryKeyObject);
		
		BCCAuthorField authorField = generateAuthorField(bibTeXFactory);
		entryBody.getFields().add(authorField);
		
		BCCYearField yearField = generateYearField(bibTeXFactory);
		entryBody.getFields().add(yearField);
		
		BCCTitleField titleField = generateTitleField(bibTeXFactory);
		entryBody.getFields().add(titleField);
		
		BCCPublisherField publisherField = generatePublisherField(bibTeXFactory);
		entryBody.getFields().add(publisherField);
		
		return entryBody;
	}

	private static BCCEntryKeyObject generateEntryKeyObject(BCCBibTeXFactory bibTeXFactory) {
		BCCEntryKeyObject entryKeyObject = bibTeXFactory.createBCCEntryKeyObject();
		
		entryKeyObject.setEntryKey(ENTRY_KEY);
		
		return entryKeyObject;
	}

	private static BCCAuthorField generateAuthorField(BCCBibTeXFactory bibTeXFactory) {
		BCCAuthorField authorField = bibTeXFactory.createBCCAuthorField();
		
		BCCPersonFieldBody personFieldBody = generatePersonFieldBody(bibTeXFactory);
		authorField.setPersonFieldBody(personFieldBody);
		
		return authorField;
	}

	private static BCCYearField generateYearField(BCCBibTeXFactory bibTeXFactory) {
		BCCYearField yearField = bibTeXFactory.createBCCYearField();
		
		yearField.setYear(YEAR);
		
		return yearField;
	}

	private static BCCTitleField generateTitleField(BCCBibTeXFactory bibTeXFactory) {
		BCCTitleField titleField = bibTeXFactory.createBCCTitleField();
		
		BCCGenericFieldValueObject fieldValueObject = generateGenericFieldValueObject(bibTeXFactory, TITLE);
		titleField.setFieldValueObject(fieldValueObject);
		
		return titleField;
	}

	private static BCCPublisherField generatePublisherField(BCCBibTeXFactory bibTeXFactory) {
		BCCPublisherField publisherField = bibTeXFactory.createBCCPublisherField();
		
		BCCGenericFieldValueObject fieldValueObject = generateGenericFieldValueObject(bibTeXFactory, PUBLISHER_REPLACE_KEY);
		publisherField.setFieldValueObject(fieldValueObject);
		
		return publisherField;
	}

	private static BCCGenericFieldValueObject generateGenericFieldValueObject(BCCBibTeXFactory bibTeXFactory, String fieldValue) {
		BCCGenericFieldValueObject fieldValueObject = bibTeXFactory.createBCCGenericFieldValueObject();
		
		fieldValueObject.setFieldValue(fieldValue);
		
		return fieldValueObject;
	}

	private static BCCPersonFieldBody generatePersonFieldBody(BCCBibTeXFactory bibTeXFactory) {
		BCCPersonFieldBody personFieldBody = bibTeXFactory.createBCCPersonFieldBody();
		
		BCCPerson person = generatePerson(bibTeXFactory);
		personFieldBody.getPersons().add(person);
		
		return personFieldBody;
	}

	private static BCCPerson generatePerson(BCCBibTeXFactory bibTeXFactory) {
		BCCPerson person = bibTeXFactory.createBCCPerson();
		
		person.setLastName(AUTHOR_LASTNAME);
		person.setFirstName(AUTHOR_FIRSTNAME);
		
		return person;
	}
	
}
