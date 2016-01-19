package srgs2xml;

import java.io.File;

import org.apache.log4j.BasicConfigurator;
import org.jvoicexml.processor.srgs.ChartGrammarChecker;
import org.jvoicexml.processor.srgs.JVoiceXmlGrammarManager;
import org.jvoicexml.processor.srgs.grammar.GrammarManager;
import org.jvoicexml.processor.srgs.grammar.RuleGrammar;

public class Srgs2Xml {

  public static void main(String[] args) throws Exception {
    BasicConfigurator.resetConfiguration();
    //Logger.getRootLogger().setLevel(Level.TRACE);
    //Logger.getLogger(GrammarChecker.class).setLevel(Level.TRACE);

    final GrammarManager manager = new JVoiceXmlGrammarManager();
    final RuleGrammar ruleGrammar = (RuleGrammar) manager.loadGrammar( new
        File(args[0]).toURI());
    int correct = 0, wrong = 0;

    String[] inputs = { "fuck fuck fuck yeah yeah" };
    for (String s : inputs) {
      String[] tokens = s.split(" +");
      final ChartGrammarChecker checker = new ChartGrammarChecker(manager);
      final ChartGrammarChecker.ChartNode validRule = checker.parse(ruleGrammar, tokens);

      if (validRule == null) {
        ++wrong;
        for (String ss : tokens) {
          System.out.print(ss + " ");
        }
        System.out.println();
      } else {
        ++correct;
        // System.out.println(validRule);
        validRule.printTree("");
      }
    }
    System.out.println("Correct: "+correct+" Wrong: "+wrong);

  }

}
