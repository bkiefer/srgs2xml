package de.dfki.mlt.srgsparser;


import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jvoicexml.processor.srgs.AbnfRuleGrammarParser;
import org.jvoicexml.processor.srgs.ChartGrammarChecker;
import org.jvoicexml.processor.srgs.JVoiceXmlGrammarManager;
import org.jvoicexml.processor.srgs.SrgsRuleGrammarParser;
import org.jvoicexml.processor.srgs.abnf.SrgsLexer;
import org.jvoicexml.processor.srgs.grammar.GrammarException;
import org.jvoicexml.processor.srgs.grammar.GrammarManager;
import org.jvoicexml.processor.srgs.grammar.Rule;
import org.jvoicexml.processor.srgs.grammar.RuleGrammar;

public class AbnfParserTest {
  @BeforeClass
  public static void init() {
    BasicConfigurator.configure();
  }

  @Test
  public void pizzatest() throws GrammarException, IOException, URISyntaxException {
    String[] inputs = {
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

    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final RuleGrammar ruleGrammar = (RuleGrammar) manager.loadGrammar(
         this.getClass().getResource("/pizza.gram").toURI());

    for (String s : inputs) {
      String[] tokens = s.split(" +");
      final ChartGrammarChecker checker = new ChartGrammarChecker(manager);
      final ChartGrammarChecker.ChartNode validRule =
          checker.parse(ruleGrammar, tokens);
      assertTrue(validRule != null);
    }
  }


  public void lexerTest() throws URISyntaxException, IOException {
    URI grammarReference = this.getClass().getResource("/pizza.gram").toURI();
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
  public void parserTest() throws URISyntaxException, IOException {
    URI grammarReference = this.getClass().getResource("/pizza.gram").toURI();
    URL url = grammarReference.toURL();
    AbnfRuleGrammarParser p = new AbnfRuleGrammarParser(grammarReference.toString());
    Rule[] rules = p.load(url.openStream());
    grammarReference = this.getClass().getResource("/pizza.srgs").toURI();
    url = grammarReference.toURL();
    SrgsRuleGrammarParser s = new SrgsRuleGrammarParser();
    Rule[] xmlrules = s.load(url.openStream());

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
    URI grammarReference = this.getClass().getResource("/mini.gram").toURI();
    URL url = grammarReference.toURL();
    AbnfRuleGrammarParser p = new AbnfRuleGrammarParser(grammarReference.toString());
    Rule[] rules = p.load(url.openStream());
    grammarReference = this.getClass().getResource("/mini.xml").toURI();
    url = grammarReference.toURL();
    SrgsRuleGrammarParser s = new SrgsRuleGrammarParser();
    Rule[] xmlrules = s.load(url.openStream());

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
}
