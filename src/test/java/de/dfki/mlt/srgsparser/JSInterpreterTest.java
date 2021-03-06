package de.dfki.mlt.srgsparser;


import static de.dfki.mlt.srgsparser.AbnfParserTest.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jvoicexml.processor.srgs.ChartGrammarChecker;
import org.jvoicexml.processor.srgs.JVoiceXmlGrammarManager;
import org.jvoicexml.processor.srgs.grammar.Grammar;
import org.jvoicexml.processor.srgs.grammar.GrammarException;
import org.jvoicexml.processor.srgs.grammar.GrammarManager;

public class JSInterpreterTest {

  @Test
  public void pizzatest2() throws GrammarException, IOException, URISyntaxException {
    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final Grammar ruleGrammar = manager.loadGrammar(testURI("pizza.srgs"));

    String s = pizzainputs[pizzainputs.length-1];
    String[] tokens = s.split(" +");
    final ChartGrammarChecker checker = new ChartGrammarChecker(manager);
    final ChartGrammarChecker.ChartNode validRule =
        checker.parse(ruleGrammar, tokens);
    JSInterpreter walker = new JSInterpreter(checker);
    JSONObject object = walker.evaluate(validRule);
    JSONObject order = object.getJSONObject("order");
    assertNotNull(order);
    assertEquals("big", order.getString("size"));
    assertEquals("mushrooms", order.getString("topping"));
  }

  @Test
  public void pizzatest3() throws GrammarException, IOException, URISyntaxException {
    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final Grammar ruleGrammar = manager.loadGrammar(testURI("pizza2.gram"));

    String s = pizzainputs[pizzainputs.length-1];
    String[] tokens = s.split(" +");
    final ChartGrammarChecker checker = new ChartGrammarChecker(manager);
    final ChartGrammarChecker.ChartNode validRule =
        checker.parse(ruleGrammar, tokens);
    JSInterpreter walker = new JSInterpreter(checker);
    JSONObject object = walker.evaluate(validRule);
    JSONObject order = object.getJSONObject("order");
    assertNotNull(order);
    assertEquals("big", order.getString("size"));
    assertEquals("mushrooms", order.getString("topping"));
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

  @Test
  public void altTest() throws URISyntaxException, IOException, GrammarException {
    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final Grammar ruleGrammar = manager.loadGrammar(testURI("alternatives.gram"));

    String[][] tokens = {
        { "is", "it", "2" },
        { "is", "it", "f" },
        { "is", "it", "?" }
    };
    final ChartGrammarChecker checker = new ChartGrammarChecker(manager);
    for (String[] toks : tokens) {
      ChartGrammarChecker.ChartNode validRule = checker.parse(ruleGrammar, toks);
      JSInterpreter walker = new JSInterpreter(checker);
      JSONObject o = walker.evaluate(validRule);
      assertEquals(toks[2], o.getString("val"));
    }
  }
}
