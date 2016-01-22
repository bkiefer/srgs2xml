package de.dfki.mlt.srgsparser;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.jvoicexml.processor.srgs.ChartGrammarChecker;
import org.jvoicexml.processor.srgs.JVoiceXmlGrammarManager;
import org.jvoicexml.processor.srgs.grammar.GrammarException;
import org.jvoicexml.processor.srgs.grammar.GrammarManager;
import org.jvoicexml.processor.srgs.grammar.RuleGrammar;

public class SrgsParserTest {

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
         this.getClass().getResource("/pizza.srgs").toURI());

    for (String s : inputs) {
      String[] tokens = s.split(" +");
      final ChartGrammarChecker checker = new ChartGrammarChecker(manager);
      final ChartGrammarChecker.ChartNode validRule =
          checker.parse(ruleGrammar, tokens);
      assertTrue(validRule != null);
    }
  }

  @Test
  public void hySocTest() throws GrammarException, IOException, URISyntaxException {
    String[] inputs = {
        "gloria i also need the parcel tape"
    };

    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final RuleGrammar ruleGrammar = (RuleGrammar) manager.loadGrammar(
         this.getClass().getResource("/hysoc.xml").toURI());


    for (String s : inputs) {
      String[] tokens = s.split(" +");
      final ChartGrammarChecker checker = new ChartGrammarChecker(manager);
      final ChartGrammarChecker.ChartNode validRule =
          checker.parse(ruleGrammar, tokens);
      assertTrue(validRule != null);
    }
  }

  @Test
  public void repeatTest() throws GrammarException, IOException, URISyntaxException {
    String[] inputs = {
        "fuck yeah", //w
        "fuck yeah yeah", //c
        "fuck fuck yeah", //w
        "fuck fuck yeah yeah", //c
        "fuck yeah yeah yeah", //c
        "fuck fuck yeah yeah yeah", //c
        "fuck fuck yeah yeah yeah yeah", //w
        "yeah yeah yeah", //w
    };

    boolean[] correct = { false, true, false, true, true, true, false, false };

    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final RuleGrammar ruleGrammar = (RuleGrammar) manager.loadGrammar(
        this.getClass().getResource("/repeat.xml").toURI());

    int i = 0;
    for (String s : inputs) {
      String[] tokens = s.split(" +");
      final ChartGrammarChecker checker = new ChartGrammarChecker(manager);
      final ChartGrammarChecker.ChartNode validRule =
          checker.parse(ruleGrammar, tokens);
      assertEquals(s, correct[i], (validRule != null));
      ++i;
    }
  }

}
