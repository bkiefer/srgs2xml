# SRGS/XML parser with javascript semantic actions

[![mvn build](https://github.com/bkiefer/srgs2xml/actions/workflows/maven.yml/badge.svg)](https://github.com/bkiefer/srgs2xml/actions/workflows/maven.yml)

This project contains a parser for grammars in SRGS XML and ABNF format [https://www.w3.org/TR/speech-grammar/], with javascript semantic actions. It attempts to replicate the functionality of, e.g., grammars used in the Microsoft Speech API or the Nuance Recognizer.

The module builds on a modified version of jvoicexml, whose SRGS parser does not work, at least not at the point when this was implemented.

There is no guarantee that grammars that can be processed with this module will be usable with the aforementioned recognizers, but former experience suggest that this is true for a reasonable subset. The most critical part will be the evaluation of javascript, because that is not defined in the standard, and there is no documentation how it is handled in the recognizers.

The current version has been tested against the W3Cs conformance suite [http://www.w3.org/2002/06/srgs-irp/srgs-test-set-20021017.zip] (see the OfficialTest and UnixLfTest test suites), so it is clear where it is not supporting the standard. There are certainly also corner cases that may not have been caught. Also, the interpretation is not as for the test suite, so tests are reduced to try to parse a sentence and check rejection or acceptance.

The support for the ABNF form of SRGS grammars, i.e, the parser compiling these grammars, was created making use of `bison` and `jflex`. Pre-build java files exist, so installing those tools is only required if the grammar or lexer specification is changed. On Ubuntu, that can be achieved executing the following command:

```
sudo apt install bison jflex
```

The current version used `jflex` version 1.7.0 and `bison` version 3.5.1 to create the java files. If the bison version is bigger than 3.7, check the SrgsAbnf.y file for a necessary modification.

You can also try to run it on a small example which is included (after building the project):

```
java -jar target/srgs-parser-1.0-jar-with-dependencies.jar src/test/resources/hysoc.xml src/test/resources/hyxamples.txt
```

# Unsupported Features / Deviation from the Standard

The ABNF grammar reader assumes the encoding to be UTF-8 if no encoding is specified. The standard does not specify a default encoding.

## Features not planned to be added
- Loading grammars over the internet is not supported
- Status of language markers is not fully clear
- Parallel activation of rules besides the root of a grammar is not supported
  (see e.g. the conformance-4.gram example of the official test suite)

## Features to be possibly added in a future version
- the private attribute to prevent external rule access does not lead to rejection
- weights and probabilities are currently not handled by the parser

# Extensions in String Matching: Regular Expressions

If a token string starts with `$$`, the rest of the token is interpreted as Java regular expression, and input tokens will be matched against this token using Jva's regular expression matching implementation.

# Extensions in the Semantic Interpretation

First and foremost, it is important to note that there is *no semantic return value by default*! That means, if a rule contains no semantic tags, it will return an empty object.

The semantic actions can provide return values not only based on the names of terminal symbols, but also using relative positions of matched strings or tokens:
|       |                                                                        |
|-------|------------------------------------------------------------------------|
| `$%n` | refers to the value returned by the rule `n` positions before this tag |
| `$$n` | refers to the the matched string `n` positions before this tag         |


Note that if `$%n` or `$$n` refers to an alternative, the result of the matched alternative is returned.

`n` always has to be greater than zero, since zero would be at the position where the semantic element is that makes the tag reference, which does not make sense. For determining `n`, you have to count *every* grammar token, including semantic tokens.

As an example, taken from the test cases of the `srgsparser` module, we parse the sentence "I want a medium pizza with Mushrooms please" with the grammar shown below, which will return a JSON object of the form:

`{ "order": { "size": "medium", "topping": "mushrooms" } }`

Since the `$%2` in the `size` rule returned the output of the `$small | $medium | $large` alternative, and the `$$1` returned the string matched in the `medium` rule immediately before the tag. The rest is done with the ordinary JSON semantics present in the standard formalism.

```
#ABNF 1.0 UTF-8;

language en-EN;
root $order;
mode voice;
tag-format "semantics/1.0";

$politeness1 = [I want];
$politeness2 = [please];

$small = small {out = "$$1";};
$medium = medium {out = "$$1";};
$large = large {out = "big";};

$size = (($small | $medium | $large) pizza {out = $%2;})
        | (hot chili) { out="chili"; } ;

$topping = Salami {out = "salami";}
         | Ham {out = "ham";}
         | Mushrooms {out = "mushrooms";} ;

public $order =
  {out = new Object(); out.order = new Object;}
  $politeness1
  (
  [a] $size pizza {out.order.size = rules.size;}
  | [a] [pizza with] $topping {out.order.topping = rules.topping;}
  | [a] $size {out.order.size = rules.size;}
    with $topping {out.order.topping = rules.topping;}
  )
  $politeness2 ;
```
