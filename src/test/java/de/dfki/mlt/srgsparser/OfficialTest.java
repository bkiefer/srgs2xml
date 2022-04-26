package de.dfki.mlt.srgsparser;

import static de.dfki.mlt.srgsparser.AbnfParserTest.RESOURCE_DIR;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.jvoicexml.processor.srgs.ChartGrammarChecker;
import org.jvoicexml.processor.srgs.JVoiceXmlGrammarManager;
import org.jvoicexml.processor.srgs.grammar.Grammar;
import org.jvoicexml.processor.srgs.grammar.GrammarException;
import org.jvoicexml.processor.srgs.grammar.GrammarManager;
import org.jvoicexml.processor.srgs.grammar.Meta;


@RunWith(Parameterized.class)
public class OfficialTest {
  private Path path;
  
  public static void logOff() {
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");
  }
  
  public static void logOn() {
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "error");
  }
  
  @Parameterized.Parameters
  public static Collection<Path> official() throws IOException {
    return Files.walk(Path.of(RESOURCE_DIR + "/official/test/"), 1)
        .filter(p -> p.toFile().isFile()).collect(Collectors.toList()); 
  }
  
  private static Pattern METAPAT = Pattern.compile("in\\.([0-9]+)");
  
  public OfficialTest(Path p) {
    this.path = p;
  }
  
  //AbnfRuleGrammarParser.DEBUG_GRAMMAR = true; // to test parsing of grammar
  
  static String[] grammarsToReject = {
      "wrong-abnf-sih-version.gram",
      "wrong-repeat-abnf-symbols.gram",
      "wrong-tag-delimit-1.gram",
      "wrong-tag-delimit-2.gram",
      "no-abnf-sih-version.gram",
      "duplicated-special-rulenames.gram",
      "rule-no-empty.gram",
      "no-abnf-sih-header.gram",
      "unrecognized-header.gram",
      "dtmf-star-no-quotes.gram",
      "abnf-sih-header-no-newline.gram",
      "wrong-tag-delimit-1.gram",
      "wrong-tag-delimit-2.gram",
      "no-version.gram",
      "conformance-5.gram",
      
      // maybe at some point we will add support for languages, the URIs do not exist
      "lang-ruleref.gram",
      
  };
  
  static HashSet<String> toReject = new HashSet<>();
  
  static String[] grammarsNotToParse = {
      // These use feature we're not supporting
      "tag-delimit-1.gram",         // wrong tag delimitation, we want correct
      "uri-ref-undefined-root-referenced.gram", // undefined root, should not do anything
      "dtmf-pound-star-text.gram", // special symbols not supported
      "root-rule-decl-missing.gram", // see above, undefined root
      
      "conformance-3.gram", // don't support parallel rule activation
      "conformance-4.gram", // don't support parallel rule activation

      // currently leads to heap space overflow
      "tag-repetition.gram",

      // SHOULD WORK
      "example-4-chinese-digits-utf8.gram", // no idea what's going on here
      "conformance-6.gram", // don't know what's going on here

      "special-garbage.gram",  // garbage currently not supported
      "tag-many.gram",  // garbage currently not supported
      
      // SHOULD REJECT, TOO PERMISSIVE
      "language-missing.gram", // missing lang spec
      "no-language-no-mode.gram", // missing lang spec
      "ruleref-ext-private-root.gram", // private external root
      "ruleref-ext-private-rule.gram", // private external rule
  };
  
  static HashSet<String> notToParse = new LinkedHashSet<>();
  static HashSet<String> notToAccept = new LinkedHashSet<>();

  static {
    toReject.addAll(Arrays.asList(grammarsToReject));
    notToParse.addAll(Arrays.asList(grammarsNotToParse));
  }

  @SuppressWarnings("unchecked")
  private static void parseInput(Grammar ruleGrammar, String name,
      GrammarManager manager) {
    List<Meta> metas = (List<Meta>) ruleGrammar.getAttributes().get("meta");
    if (metas != null) {
      for (int i = 0; i < metas.size(); ++i) {
        Meta in = metas.get(i);
        Matcher mat = METAPAT.matcher(in.key);
        if (mat.matches()) { // we have an input string to check (key in.X)
          String[] tokens = in.value.split(" +");
          final ChartGrammarChecker checker = new ChartGrammarChecker(manager);
          ChartGrammarChecker.ChartNode validRule = null;
          try {
            validRule = checker.parse(ruleGrammar, tokens);
          } catch (GrammarException ex) {
            // invalid rule reference
          }
          Meta out = metas.get(++i);
          if (out.value.equals("REJECT")) {
            //if (validRule != null) notToAccept.add(name);
            assertNull(name + " : " + in.value, validRule);
          } else {
            //if (validRule == null) notToParse.add(name);
            assertNotNull(name + " : " + in.value, validRule);
          }
        }
      }
    }
  }
  
  static void parseGrammar(Path p) {
    final GrammarManager manager = new JVoiceXmlGrammarManager();
    String name = p.getFileName().toString();
    //if (toReject.contains(name)) 
    logOff();
    try {
      final Grammar ruleGrammar = manager.loadGrammar(p.toUri());
      assertNotNull(ruleGrammar);
      assertFalse(name, toReject.contains(name));
      if (!notToParse.contains(name) && ruleGrammar.getAttributes() != null) {
        parseInput(ruleGrammar, name, manager);
      }
    } catch (GrammarException|IOException e) {
      //System.out.println(e);
      assertTrue(name, toReject.contains(name));
    }
    logOn();
  }
  
  @Test 
  public void grammarTest() {
    parseGrammar(path);
  }
}
