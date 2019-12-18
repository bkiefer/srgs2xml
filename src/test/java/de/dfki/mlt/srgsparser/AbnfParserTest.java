package de.dfki.mlt.srgsparser;


import static org.junit.Assert.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jvoicexml.processor.srgs.*;
import org.jvoicexml.processor.srgs.abnf.SrgsLexer;
import org.jvoicexml.processor.srgs.grammar.*;

public class AbnfParserTest {
  public static final String RESOURCE_DIR = "src/test/resources/";
  public static URI testURI(String name) {
    return new File(RESOURCE_DIR, name).toURI();
  }

  public static String[] pizzainputs = {
      "small pizza",
      "medium pizza",
      "large pizza",
      "a small pizza",
      "a medium pizza",
      "a large pizza",
      "I want a small pizza",
      "I want a medium pizza",
      "I want a large pizza",
      "I want small pizza",
      "I want medium pizza",
      "I want large pizza",
      "I want a small pizza please",
      "I want a medium pizza please",
      "I want a large pizza please",
      "I want small pizza please",
      "I want medium pizza please",
      "I want large pizza please",
      "pizza with salami",
      "pizza with ham",
      "pizza with mushrooms",
      "a pizza with salami",
      "a pizza with ham",
      "a pizza with mushrooms",
      "I want a pizza with salami",
      "I want a pizza with ham",
      "I want a pizza with mushrooms",
      "I want pizza with salami",
      "I want pizza with ham",
      "I want pizza with mushrooms",
      "I want a pizza with salami please",
      "I want a pizza with ham please",
      "I want a pizza with mushrooms please",
      "I want pizza with salami please",
      "I want pizza with ham please",
      "I want pizza with mushrooms please",
      "small pizza with salami",
      "medium pizza with ham",
      "large pizza with mushrooms",
      "a small pizza with salami",
      "a medium pizza with ham",
      "a large pizza with mushrooms",
      "I want a small pizza with salami",
      "I want a medium pizza with ham",
      "I want a large pizza with mushrooms",
      "I want small pizza with salami",
      "I want medium pizza with ham",
      "I want large pizza with mushrooms",
      "I want a small pizza with salami please",
      "I want a medium pizza with ham please",
      "I want a large pizza with mushrooms please",
      "I want small pizza with salami please",
      "I want medium pizza with ham please",
      "I want large pizza with mushrooms please",
  };

  @BeforeClass
  public static void init() {
    BasicConfigurator.configure();
  }

  @Test
  public void pizzatest() throws GrammarException, IOException, URISyntaxException {

    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final Grammar ruleGrammar = manager.loadGrammar(testURI("pizza.gram"));

    for (String s : pizzainputs) {
      String[] tokens = s.split(" +");
      final ChartGrammarChecker checker = new ChartGrammarChecker(manager);
      final ChartGrammarChecker.ChartNode validRule =
          checker.parse(ruleGrammar, tokens);
      assertTrue(validRule != null);
    }
  }


  public void lexerTest() throws URISyntaxException, IOException {
    URI grammarReference = testURI("pizza.gram");
    final URL url = grammarReference.toURL();
    InputStream in = url.openStream();
    Reader r = new InputStreamReader(in, "UTF-8");
    SrgsLexer lexer = new SrgsLexer(r);
    int clz;
    while ((clz = lexer.yylex()) != SrgsLexer.EOF) {
      System.out.println(lexer.yytext() + " " + clz + " " + lexer.yystate());
    }
  }

  @Test
  public void regexTest() throws GrammarException, IOException, URISyntaxException {
    String[] inputs = {
        "one", //c
        "two", //c
        "three", //c
        "four", //w
        "onetwo", //
        "fuckfuckyeahyeahyeah", //w
        "fuckyeahyeahyeahyeah", //w
        "yeahyeahyeah", //w
        "wow wow", //w
        "wow wow wow", //c
        "wow wow wow wow", //c
    };

    boolean[] correct = {
        true, true, true, false, false, true, true, false, false, true, true
    };

    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final Grammar ruleGrammar = manager.loadGrammar(testURI("regex.gram"));

    final ChartGrammarChecker checker = new ChartGrammarChecker(manager);
    int i = 0;
    for (String s : inputs) {
      String[] tokens = s.split(" +");
      final ChartGrammarChecker.ChartNode validRule =
          checker.parse(ruleGrammar, tokens);
      assertEquals(s, correct[i], (validRule != null));
      ++i;
      if (validRule != null) {
        JSInterpreter walker = new JSInterpreter(checker);
        JSONObject object = walker.evaluate(validRule);
        String out = object.getString("s");
        assertEquals(s, out);
      }
    }
    ChartGrammarChecker.ChartNode validRule;
    String[] tok2 = { "damn" };
    String[] tok3 = { "damn", "goooood"};
    validRule = checker.parse(ruleGrammar, tok2);
    assertNotNull(validRule);
    validRule = checker.parse(ruleGrammar, tok3);
    assertNotNull(validRule);
  }

  @Test
  public void parserTest() throws URISyntaxException, IOException {
    URI grammarReference = testURI("pizza.gram");
    URL url = grammarReference.toURL();
    AbnfRuleGrammarParser p = new AbnfRuleGrammarParser(grammarReference.toString());
    List<Rule> rules = p.load(url.openStream());
    grammarReference = testURI("pizza.srgs");
    url = grammarReference.toURL();
    SrgsRuleGrammarParser s = new SrgsRuleGrammarParser();
    List<Rule> xmlrules = s.load(url.openStream());

    for (Rule r : rules) {
      Rule xmlrule = null;
      for (Rule xr : xmlrules) {
        if (r.getRuleName().equals(xr.getRuleName())) {
          xmlrule = xr;
          break;
        }
      }
      assertNotNull(xmlrule);
      assertEquals(xmlrule.getRuleName(), r.toString(), xmlrule.toString());
    }
  }

  @Test
  public void parserTest2() throws URISyntaxException, IOException {
    URI grammarReference = testURI("mini.gram");
    URL url = grammarReference.toURL();
    AbnfRuleGrammarParser p = new AbnfRuleGrammarParser(grammarReference.toString());
    List<Rule> rules = p.load(url.openStream());
    grammarReference = testURI("mini.xml");
    url = grammarReference.toURL();
    SrgsRuleGrammarParser s = new SrgsRuleGrammarParser();
    List<Rule> xmlrules = s.load(url.openStream());

    for (Rule r : rules) {
      Rule xmlrule = null;
      for (Rule xr : xmlrules) {
        if (r.getRuleName().equals(xr.getRuleName())) {
          xmlrule = xr;
          break;
        }
      }
      assertNotNull(xmlrule);
      assertEquals(xmlrule.getRuleName(), r.toString(), xmlrule.toString());
    }
  }

  @Test
  public void tagsTest() throws URISyntaxException, IOException, GrammarException {
    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final Grammar ruleGrammar = manager.loadGrammar(testURI("tags.gram"));

    String[] tokens = {"1", "is", "2" };
    final ChartGrammarChecker checker = new ChartGrammarChecker(manager);
    ChartGrammarChecker.ChartNode validRule =
        checker.parse(ruleGrammar, tokens);
    JSInterpreter walker = new JSInterpreter(checker);
    JSONObject o = walker.evaluate(validRule);
    assertEquals("1", o.getString("one"));
    assertEquals("2", o.getString("two"));
  }
}
