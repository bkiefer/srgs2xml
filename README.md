# SRGS/XML parser with javascript semantic actions

This project contains a parser for grammars in SRGS XML format, with javascript semantic actions. It attempts to replicate the functionality of, e.g., grammars used in the Microsoft Speech API or the Nuance Recognizer.

The module builds on a modified version of jvoicexml, the SRGS parser contained in this package does not work, at least not at the point when this was implemented.

There is no guarantee that grammars that can be processed with this module will be usable with the beformentioned recognizers, but former usages suggest that this is true for a reasonable subset. The most critical part will be the evaluation of the javascript, because that is not defined in the standard, and there is no documentation how it is handled in the recognizers.

Including subgrammars has not been tested.

There are JUnit tests, and the file Example.java that illustrate how to use the code.

The new version also contains support for the ABNF form of SRGS grammars, and the parser compiling these grammars makes use of `bison` and `jflex`. Pre-build java files exist, so installing those tools is only required if the grammar or lexer specification is changed. On Ubuntu, that can be achieved executing the following command:

```
sudo apt install bison jflex
```

You can also try to run it on a small example which is included (after building the project):

```
java -jar target/srgs-parser-0.9-jar-with-dependencies.jar src/main/resources/hysoc.xml src/main/resources/hyxamples.txt
```
