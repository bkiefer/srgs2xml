# SRGS/XML parser with javascript semantic actions

This project contains a parser for grammars in SRGS XML format, with javascript semantic actions. It attempts to replicate the functionality of, e.g., grammars used in the Microsoft Speech API or the Nuance Recognizer.

The module builds on a modified version of jvoicexml, the SRGS parser contained in this package does not work, at least not at the point when this was implemented.

There is no guarantee that grammars that can be processed with this module will be usable with the beformentioned recognizers, but former usages suggest that this is true for a reasonable subset. The most critical part will be the evaluation of the javascript, because that is not defined in the standard, and there is no documentation how it is handled in the recognizers.

Including subgrammars has not been tested.

There are JUnit tests, and the file Example.java that illustrate how to use the code.