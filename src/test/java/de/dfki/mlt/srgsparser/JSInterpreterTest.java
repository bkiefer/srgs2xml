package de.dfki.mlt.srgsparser;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.jvoicexml.processor.srgs.abnf.AbnfParserTest.pizzainputs;
import static org.jvoicexml.processor.srgs.abnf.AbnfParserTest.testURI;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONObject;
import org.junit.Test;
import org.jvoicexml.processor.AbstractParser;
import org.jvoicexml.processor.GrammarManager;
import org.jvoicexml.processor.JVoiceXmlGrammarManager;
import org.jvoicexml.processor.SemanticsInterpreter;
import org.jvoicexml.processor.grammar.Grammar;
import org.jvoicexml.processor.srgs.GrammarException;

public class JSInterpreterTest {

  public static JSONObject interpret(AbstractParser checker,
      AbstractParser.ChartNode validRule) {
    SemanticsInterpreter walker = new SemanticsInterpreter(checker);
    return SemanticsInterpreter.execute( walker.createProgram(validRule));
  }

  @Test
  public void pizzatest2() throws GrammarException, IOException, URISyntaxException {
    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final Grammar ruleGrammar = manager.loadGrammar(testURI("pizza.srgs"));

    String s = pizzainputs[pizzainputs.length-1];
    String[] tokens = s.split(" +");
    final AbstractParser checker = AbstractParser.getParser(manager);
    final AbstractParser.ChartNode validRule =
        checker.parse(ruleGrammar, tokens);
    JSONObject object = interpret(checker, validRule);
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
    final AbstractParser checker = AbstractParser.getParser(manager);
    final AbstractParser.ChartNode validRule =
        checker.parse(ruleGrammar, tokens);
    JSONObject object = interpret(checker, validRule);
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
    final AbstractParser checker = AbstractParser.getParser(manager);
    AbstractParser.ChartNode validRule =
        checker.parse(ruleGrammar, tokens);
    JSONObject o = interpret(checker, validRule);
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
    final AbstractParser checker = AbstractParser.getParser(manager);
    for (String[] toks : tokens) {
      AbstractParser.ChartNode validRule = checker.parse(ruleGrammar, toks);
      JSONObject o = interpret(checker, validRule);
      assertEquals(toks[2], o.getString("val"));
    }
  }
}
