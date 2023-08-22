package de.dfki.mlt.srgsparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.jvoicexml.processor.srgs.abnf.AbnfParserTest.pizzainputs;
import static org.jvoicexml.processor.srgs.abnf.AbnfParserTest.testURI;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.json.JSONObject;
import org.junit.Test;
import org.jvoicexml.processor.AbstractParser;
import org.jvoicexml.processor.ChartNode;
import org.jvoicexml.processor.GrammarManager;
import org.jvoicexml.processor.JVoiceXmlGrammarManager;
import org.jvoicexml.processor.grammar.Grammar;
import org.jvoicexml.processor.grammar.Rule;
import org.jvoicexml.processor.srgs.GrammarException;
import org.jvoicexml.processor.srgs.xml.SrgsRuleGrammarParser;

public class SrgsParserTest {

  @Test
  public void pizzatest() throws GrammarException, IOException, URISyntaxException {
    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final Grammar ruleGrammar = manager.loadGrammar(testURI("pizza.srgs"));

    for (String s : pizzainputs) {
      String[] tokens = s.split(" +");
      final AbstractParser checker = AbstractParser.getParser(manager);
      final ChartNode validRule =
          checker.parse(ruleGrammar, tokens);
      assertTrue(s, validRule != null);
    }
  }

  @Test
  public void pizzatest2() throws GrammarException, IOException, URISyntaxException {
    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final Grammar ruleGrammar = manager.loadGrammar(testURI("pizza.srgs"));

    //for (String s : pizzainputs[pizzainputs.length-1]) {
    {
      String s = pizzainputs[pizzainputs.length-1];
      String[] tokens = s.split(" +");
      final AbstractParser checker = AbstractParser.getParser(manager);
      final ChartNode validRule =
          checker.parse(ruleGrammar, tokens);
      JSONObject o = JSInterpreterTest.interpret(checker, validRule);
      JSONObject order = (JSONObject)o.get("order");
      assertNotNull(order);
      assertEquals("big", order.getString("size"));
      assertEquals("mushrooms", order.getString("topping"));
    }
  }

  @Test
  public void hySocTest() throws GrammarException, IOException, URISyntaxException {
    String[] inputs = {
        "gloria i also need the parcel tape"
    };

    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final Grammar ruleGrammar = manager.loadGrammar(testURI("hysoc.xml"));


    for (String s : inputs) {
      String[] tokens = s.split(" +");
      final AbstractParser checker = AbstractParser.getParser(manager);
      final ChartNode validRule =
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
    final Grammar ruleGrammar = manager.loadGrammar(testURI("repeat.xml"));

    int i = 0;
    for (String s : inputs) {
      String[] tokens = s.split(" +");
      final AbstractParser checker = AbstractParser.getParser(manager);
      final ChartNode validRule =
          checker.parse(ruleGrammar, tokens);
      assertEquals(s, correct[i], (validRule != null));
      ++i;
    }
  }

  @Test
  public void optionalTest() throws GrammarException, IOException, URISyntaxException {
    String[] inputs = {
        "a pizza", //c
        "pizza", //c
    };

    boolean[] correct = { true, true };

    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final Grammar ruleGrammar = manager.loadGrammar(testURI("optional.xml"));

    int i = 0;
    for (String s : inputs) {
      String[] tokens = s.split(" +");
      final AbstractParser checker = AbstractParser.getParser(manager);
      final ChartNode validRule =
          checker.parse(ruleGrammar, tokens);
      assertEquals(s, correct[i], (validRule != null));
      ++i;
    }
  }

  @Test
  public void alternativesTest() throws GrammarException, IOException, URISyntaxException {
    String[] inputs = {
        "one", //c
        "two", //c
        "three", //c
        "four", //w
        "one two", //w
        "fuck fuck yeah yeah yeah" , //c
        "fuck fuck yeah yeah yeah yeah", //c
        "yeah yeah yeah", //w
    };

    boolean[] correct = { true, true, true, false, false, false, false, false };

    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final Grammar ruleGrammar = manager.loadGrammar(testURI("alternatives.xml"));

    int i = 0;
    for (String s : inputs) {
      String[] tokens = s.split(" +");
      final AbstractParser checker = AbstractParser.getParser(manager);
      final ChartNode validRule =
          checker.parse(ruleGrammar, tokens);
      assertEquals(s, correct[i], (validRule != null));
      ++i;
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
    };

    boolean[] correct = { true, true, true, false, false, true, true, false };

    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final Grammar ruleGrammar = manager.loadGrammar(testURI("regex.xml"));

    int i = 0;
    for (String s : inputs) {
      String[] tokens = s.split(" +");
      final AbstractParser checker = AbstractParser.getParser(manager);
      final ChartNode validRule =
          checker.parse(ruleGrammar, tokens);
      assertEquals(s, correct[i], (validRule != null));
      ++i;
    }
  }

  @Test
  public void emptyMatchTest() throws GrammarException, IOException, URISyntaxException {

    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final Grammar ruleGrammar = manager.loadGrammar(testURI("epsstart.gram"));

    String[] tokens = { "pizza" };
    final AbstractParser checker = AbstractParser.getParser(manager);
    final ChartNode validRule =
        checker.parse(ruleGrammar, tokens);
    assertNotNull(validRule);
    // make sure the interpreter does not crash when empty strings are matched
    JSONObject object = JSInterpreterTest.interpret(checker, validRule);
    assertEquals("pizza", object.get("val").toString());
  }

  public void parserTest() throws URISyntaxException, IOException, GrammarException {
    URI grammarReference = testURI("/pizza.srgs");
    final URL url = grammarReference.toURL();
    SrgsRuleGrammarParser p = new SrgsRuleGrammarParser();
    List<Rule> rules = p.load(url.openStream());
    String rootname = (String)p.getAttributes().get("root");
    for (Rule r : rules) {
      System.out.println(r);
    }
    System.out.println(rootname);
  }

  @Test
  public void rulerefTest() throws GrammarException, IOException, URISyntaxException {

    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final Grammar ruleGrammar = manager.loadGrammar(testURI("rootrule.gram"));

    String[] tokens = { "pizza" };
    final AbstractParser checker = AbstractParser.getParser(manager);
    final ChartNode validRule =
        checker.parse(ruleGrammar, tokens);
    assertNotNull(validRule);
    // make sure the interpreter does not crash when empty strings are matched
    JSONObject object = JSInterpreterTest.interpret(checker, validRule);
    assertEquals("pizza", object.get("val").toString());
  }

}
