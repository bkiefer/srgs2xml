package de.dfki.mlt.srgsparser;

import static org.junit.Assert.*;
import static org.jvoicexml.processor.srgs.abnf.AbnfParserTest.testURI;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.junit.Test;
import org.jvoicexml.processor.AbstractParser;
import org.jvoicexml.processor.BestTreeFinder;
import org.jvoicexml.processor.ChartNode;
import org.jvoicexml.processor.Configuration;
import org.jvoicexml.processor.GrammarManager;
import org.jvoicexml.processor.JVoiceXmlGrammarManager;
import org.jvoicexml.processor.SemanticsInterpreter;
import org.jvoicexml.processor.grammar.Grammar;
import org.jvoicexml.processor.srgs.GrammarException;

public class TestBestTree {

  @Test
  public void test() throws GrammarException, IOException {
    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final Grammar ruleGrammar = manager.loadGrammar(testURI("schrott.gram"));

    String[] inputs = { "das ist sinnvoll", "schrott das ist sinnvoll",
        "das schrott ist sinnvoll", "schrott das das ist sinnvoll" };
    String[] garbs = { "", "schrott", "schrott", "schrott das" };
    int[] weights = { 0, 1, 1, 2 };
    int i = 0;
    for (String s : inputs) {
      String[] tokens = s.split(" +");
      final AbstractParser checker = AbstractParser.getParser(manager);
      final ChartNode validRule = checker.parse(ruleGrammar, tokens);
      List<ChartNode> all = checker.returnAllResults().collect(Collectors.toList());

      assertFalse(s, all.isEmpty());
      BestTreeFinder btf = new BestTreeFinder();
      Configuration best = btf.findBestTree(all);
      //System.out.println(all.size() + " " +  btf.noConfs);
      assertEquals(weights[i], best.getWeight(), 0.0001);
      SemanticsInterpreter walker = new SemanticsInterpreter(checker);
      JSONObject o = SemanticsInterpreter.execute(walker.createProgram(best));
      assertEquals(garbs[i], o.getString("garb"));
      //System.out.println(o);
      //o = SemanticsInterpreter.execute(walker.createProgram(all.get(0)));
      //System.out.println(o);
      ++i;
    }
  }

}
