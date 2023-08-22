package org.jvoicexml.processor.srgs.abnf;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;
import org.junit.Test;
import org.jvoicexml.processor.AbstractParser;
import org.jvoicexml.processor.ChartNode;
import org.jvoicexml.processor.GrammarManager;
import org.jvoicexml.processor.JVoiceXmlGrammarManager;
import org.jvoicexml.processor.SemanticsInterpreter;
import org.jvoicexml.processor.grammar.Grammar;
import org.jvoicexml.processor.grammar.JVoiceXmlGrammar;
import org.jvoicexml.processor.grammar.Rule;
import org.jvoicexml.processor.grammar.RuleComponent;
import org.jvoicexml.processor.grammar.RuleToken;
import org.jvoicexml.processor.srgs.GrammarException;
import org.jvoicexml.processor.srgs.xml.SrgsRuleGrammarParser;

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

  @Test
  public void pizzatest() throws GrammarException, IOException, URISyntaxException {

    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final JVoiceXmlGrammar ruleGrammar =
        (JVoiceXmlGrammar)manager.loadGrammar(testURI("pizza.gram"));
    assertEquals(12, ruleGrammar.getTerminals().size());
    assertEquals(41, ruleGrammar.getNonterminals().size());

    for (String s : pizzainputs) {
      String[] tokens = s.split(" +");
      final AbstractParser checker = AbstractParser.getParser(manager);
      final ChartNode validRule =
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
  public void postProcessTest() throws GrammarException, IOException {
    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final JVoiceXmlGrammar ruleGrammar =
        (JVoiceXmlGrammar) manager.loadGrammar(testURI("mini.gram"));
    Set<RuleToken> terms = ruleGrammar.getTerminals();
    Set<RuleComponent> nonterms = ruleGrammar.getNonterminals();
    assertEquals(4, terms.size());
    assertEquals(8, nonterms.size());
  }

  @Test
  public void regexTest()
      throws GrammarException, IOException, URISyntaxException {
    String[] inputs = { "one", //c
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

    final AbstractParser checker = AbstractParser.getParser(manager);
    int i = 0;
    for (String s : inputs) {
      String[] tokens = s.split(" +");
      final ChartNode validRule =
          checker.parse(ruleGrammar, tokens);
      assertEquals(s, correct[i], (validRule != null));
      ++i;
      if (validRule != null) {
        SemanticsInterpreter walker = new SemanticsInterpreter(checker);
        JSONObject object = SemanticsInterpreter.execute(walker.createProgram(validRule));
        String out = object.getString("s");
        assertEquals(s, out);
      }
    }
    ChartNode validRule;
    String[] tok2 = { "damn" };
    String[] tok3 = { "damn", "goooood"};
    validRule = checker.parse(ruleGrammar, tok2);
    assertNotNull(validRule);
    validRule = checker.parse(ruleGrammar, tok3);
    assertNotNull(validRule);
  }

  @Test
  public void parserTest() throws URISyntaxException, IOException, GrammarException {
    URI grammarReference = testURI("pizza.gram");
    URL url = grammarReference.toURL();
    AbnfRuleGrammarParser p = new AbnfRuleGrammarParser(grammarReference.toString());
    List<Rule> rules = p.load(url.openStream());
    grammarReference = testURI("pizza.srgs");
    url = grammarReference.toURL();
    SrgsRuleGrammarParser s = new SrgsRuleGrammarParser();
    List<Rule> xmlrules = s.load(url.openStream());
    RuleComponent.printCompact(false);

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
  public void parserTest2() throws URISyntaxException, IOException, GrammarException {
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
    final AbstractParser checker = AbstractParser.getParser(manager);
    ChartNode validRule =
        checker.parse(ruleGrammar, tokens);
    SemanticsInterpreter walker = new SemanticsInterpreter(checker);
    JSONObject o = SemanticsInterpreter.execute(walker.createProgram(validRule));
    assertEquals("1", o.getString("one"));
    assertEquals("2", o.getString("two"));
  }

  @Test
  public void rootruletest() throws GrammarException, IOException, URISyntaxException {

    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final Grammar ruleGrammar = manager.loadGrammar(testURI("rootrule.gram"));
    String[] inputs = {
        "pizza"
    };
    for (String s : inputs) {
      String[] tokens = s.split(" +");
      final AbstractParser checker = AbstractParser.getParser(manager);
      final ChartNode validRule =
          checker.parse(ruleGrammar, tokens);
      assertTrue(validRule != null);
    }
  }
}
