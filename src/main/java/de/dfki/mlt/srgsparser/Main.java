/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.mlt.srgsparser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.log4j.BasicConfigurator;
import org.json.JSONObject;
import org.jvoicexml.processor.srgs.ChartGrammarChecker;
import org.jvoicexml.processor.srgs.JVoiceXmlGrammarManager;
import org.jvoicexml.processor.srgs.grammar.GrammarException;
import org.jvoicexml.processor.srgs.grammar.Grammar;

/**
 *
 * @author Christian.Buerckert@DFKI.de
 */
public class Main {

  private static void usage() {
    System.out.println("usage: srgsparser grammar.xml examplefile.txt");
  }

  public static void main(String[] args) throws Throwable {
    BasicConfigurator.resetConfiguration();

    if (args.length < 2) {
      usage();
      System.exit(1);
    }
    File grammarFile = new File(args[0]);
    final JVoiceXmlGrammarManager manager = new JVoiceXmlGrammarManager();
    final Grammar ruleGrammar = (Grammar) manager
        .loadGrammar(grammarFile.toURI());

    Stream<String> in = Files.lines(Paths.get(args[1]));

    in.forEach((s) -> {
      try {
        String[] tokens = s.split(" +");
        final ChartGrammarChecker checker = new ChartGrammarChecker(manager);
        final ChartGrammarChecker.ChartNode validRule = checker
            .parse(ruleGrammar, tokens);

        // System.out.println(validRule);
        if (validRule != null) {
          MSJSInterpreter walker = new MSJSInterpreter(checker);
          validRule.preorder(walker);
          walker.finish(false);
          JSONObject object = walker.execute();
          System.out.println(
              "============================================================");
          System.out.println(s);
          System.out.println(
              "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
          System.out.println(object.toString());
        } else {
          System.out.println(
              "============================================================");
          System.out.println("Parse error for \"" + s + "\"");
        }
      } catch (GrammarException e) {
        System.out.println("Parse error for \"" + s + "\"");
        System.out.println(e);
      }
    });
  }

}
