# BibTeX Consistency Checker

I developed this model-based BibTeX analysis tool based on a small string-based tool that was core of my workflow to ensure consistency of BibTeX entries during my PhD thesis.
The general idea is to have tooling to ensure uniformity and consistency of entries in BibTeX files by defining rules that allow extension of the _BibTeX Consistency Checker_.

The _BibTeX Consistency Checker_ provides the following main features:
- Parsing of BibTeX files
- Definition of replacements of strings in the BibTeX entries
- Definition of custom rules to analyze the BibTeX entries
- Definition of execution models referencing the rules that should be applied to the BibTeX files
- Editor support for all files
- Syntax highlighting
- Linking for referenced BibTeX entry keys and used replacement strings
- Validation with quick fixes to ensure:
  - The correct alphabetic order of BibTeX entries
  - Usage of correct short harvard style BibTeX entry keys
  - That all used replacements exist

## Requirements

The tool was developed using the following languages, environments and frameworks:

- Java JDK 8
- Eclipse IDE for Java and DSL Developers in Version Oxygen (4.7)
- Xtext 2.12.0

## Installation

1. Download and install the _Eclipse IDE for Java and DSL Developers_ package from https://www.eclipse.org/downloads/packages/
2. Checkout all plug-ins starting with prefix `de.david_wille.bibtexconsistencychecker` into the Eclipse workspace
3. Generate the Xtext artifacts:
   1. Right click on file `de.david_wille.bibtexconsistencychecker.bibtex/src/de.david_wille.bibtexconsistencychecker.bibtex/BCCBibTeX.xtext` and execute _Run As_ > _Generate Xtext Artifacts_
   2. Right click on file `de.david_wille.bibtexconsistencychecker.consistencyrule/src/de.david_wille.bibtexconsistencychecker.consistencyrule/BCCConsistencyRule.xtext` and execute _Run As_ > _Generate Xtext Artifacts_
   3. Right click on file `de.david_wille.bibtexconsistencychecker.executionmodel/src/de.david_wille.bibtexconsistencychecker.executionmodel/BCCExecutionModel.xtext` and execute _Run As_ > _Generate Xtext Artifacts_
4. Launch the code as an _Eclipse Application_ by selecting one of the plug-ins and executing _Run As_ > _Eclipse Application_
5. In the newly started Eclipse instance import checkout the `example` plug-in

## Execution

1. Right click on the root of the project or any file or folder and select _Run As_ > _BibTeX Consistency Checker_

## General Structure

- All _BibTeX Consistency Checker_ projects have to use the corresponding nature. There are two ways to realize this:
  1. To enable it, right click on any project and select _Configure_ > _Convert to BibTeX Consistency Checker Project_. This automatically transforms the selected project into a _BibTeX Consistency Checker_ project.
  2. Create a new _BibTeX Consistency Checker_ project using the _New_ Wizard by right clicking into the workspace and selecting _New_ > _Other..._ > _BibTeX Consistency Checker_ > _New BibTeX Consistency Checker Project_
- _BibTeX Consistency Checker_ projects always have to follow the following structure:
  1. __*.bcc file in the project root__: Defining the execution behavior for the analysis. Important: There can only exist one *.bcc file in the project root. Otherwise the execution will not start.
  2. **_rules_ folder in the project root**: This folder should contain all custom rules.
  3. **_bibliography_ folder in the project root**: This folder should contain all analyzed BibTeX files.

## File Structure

The following three general file types are used by the _BibTeX Consistency Checker_:

### BibTeX Consistency Checker Execution Files

These files have the file extension *.bcc and define how the execution is performed.
Below you can find an example for such a file.

The settings block defines a `name` and whether the `alphabetic order` and `short harvard style` for entry keys should be ensured.

The optional blocks `bibtex files` and `consistency rules` allow to select specific sub files and sub folders for the analysis.
If these blocks do not exist, the default folders  `bibliography/` and `rules/` are selected, respectively.

```
settings {
  name "Example"
  ensure {
    alphabetic order
	short harvard style
	}
}

bibtex files {
  bibliography/
}

consistency rules {
  rules/
}
```

### BibTeX Files

These files are later analyzed by the _BibTeX Consistency Checker Rules_.
They basically follow standard BibTeX file syntax with a few problem-specific exceptions that were introduced to ease parsing and support all features that were required for my PhD thesis.

Most important feature is probably the `@string` feature, which allows definition of replacements for strings in BibTeX entries that do not use surrounding braces.
Please also refer to the _Known Limitations_ section.

```TeX
@book{Duc91,
  author = {Duck, Darkwing},
  year = {1991},
  title = {{The Dangerous Live of a Duck}},
  publisher = ABC
}

@string{ABC = "American Broadcasting Company"}
```

### BibTeX Consistency Checker Rules

These rules allow to specify custom analysis rules and to assign warning or error markers to identified inconsistencies.
Below you can find an example for such a file.

Each rule has to define a `name` and an `applies to` section defining, which entry types are analyzed by the rule.
The `*` symbol defines that a rule is applied to all entries.

Optionally, users can exclude certain entry types or specific entries by defining additional `except for` and `excluded entry keys` blocks with corresponding entries.

To assign functionality to a rule, the user has to define corresponding behavior using `if-else` constructs.
Furthermore, it is possible to use `else if` blocks.

Before executing the actual checks, it is important to ensure that the analyzed fields exist.
For example `if (title field exists)` ensures that a analyzed entries contain a `title` field.
Afterwards, it is possible to perform the concrete checks using further `if-else` constructs.

In case errors or warnings have to be assigned to an entry or a specific field it is possible to use `add error` or `add warning` constructs with a corresponding message.
Optionally, users can use a `to` selector to add the warnings and errors to a specific field.

The example project contains a number of sensible examples what checks can be achieved using these files.

```Java
consistency rule {
  name "Ensure Case Sensitivity of Titles"
  applies to *
  if (title field exists) {
    if (!(title starts with "{{" AND title ends with "}}")) {
	  add warning "To ensure case sensitivity of titles you should enclose them in double braces \"{{ ... }}\"" to title
	}
  }
}
```

## Known Limitations

1. All files have to be contained in the workspace. Files that are contained in a folder outside the workspace or linked from such a location cannot be analyzed. Unexpected behavior might occur.
2. Special characters, such as German umlauts or french accents, in the BibTeX entries have to be written using one of the specified in `de.david_wille.bibtexconsistencychecker.util/src/de.david_wille.bibtexconsistencychecker.util/BCCSpecialCharacterHandling.java`
3. The BibTeX grammar expects the following:
   - No quotes are allowed to define the value of fields. For example,`publisher = "Example"` would be an illegal field definition.
   - All fields (e.g., the title field) use either surrounding braces `publisher = {Example}` or non at all `publisher = EXAMPLE`. In case no surrounding braces are used, the _BibTeX Consistency Checker_ assumes that a corresponding string replacement was defined (e.g., `@string{EXAMPLE = "example"}`).
   - All entries have to be defined using lower case (e.g., `@book` for books).
   - Please refer to the grammare `de.david_wille.bibtexconsistencychecker.bibtex/src/de.david_wille.bibtexconsistencychecker.bibtex/BCCBibTeX.xtext` for the full details.