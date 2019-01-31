/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.mlt.srgsparser;

import java.io.Console;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.log4j.BasicConfigurator;
import org.json.JSONObject;
import org.jvoicexml.processor.srgs.ChartGrammarChecker;
import org.jvoicexml.processor.srgs.ChartGrammarChecker.ChartNode;
import org.jvoicexml.processor.srgs.JVoiceXmlGrammarManager;
import org.jvoicexml.processor.srgs.grammar.Grammar;
import org.jvoicexml.processor.srgs.grammar.GrammarException;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;

/**
 *
 * @author Christian.Buerckert@DFKI.de
 */
public class Main {

  private static void usage() {
    System.out.println("usage: srgsparser grammar.xml examplefile.txt");
  }

  final static JVoiceXmlGrammarManager manager = new JVoiceXmlGrammarManager();

  private static void process(Grammar ruleGrammar, String s) {
    JSInterpreter walker = null;
    try {
      String[] tokens = s.split(" +");
      ChartGrammarChecker checker = new ChartGrammarChecker(manager);
      ChartNode validRule = checker.parse(ruleGrammar, tokens);

      // System.out.println(validRule);
      if (validRule != null) {
        walker = new JSInterpreter(checker);
        JSONObject object = walker.evaluate(validRule);
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
    } catch (EcmaError e) {
      System.out.println("JavaScript error for \"" + walker.JSONCode() + "\"");
      System.out.println(e);
    } catch (EvaluatorException e) {
      System.out.println("JavaScript error for \"" + walker.JSONCode() + "\"");
      System.out.println(e);
    } catch (GrammarException e) {
      System.out.println("Parse error for \"" + s + "\"");
      System.out.println(e);
    }
  }

  public static void main(String[] args) throws Throwable {
    BasicConfigurator.resetConfiguration();

    if (args.length < 1) {
      usage();
      System.exit(1);
    }
    File grammarFile = new File(args[0]);
    Grammar ruleGrammar = manager.loadGrammar(grammarFile.toURI());

    if (args.length > 1) {
      Stream<String> in = Files.lines(Paths.get(args[1]));
      in.forEach((s) -> { process(ruleGrammar, s); });
      in.close();
    } else {
      Console c = System.console();
      if (c == null) System.exit(-1);
      String s;
      while (!(s = c.readLine()).isEmpty()) {
        process(ruleGrammar, s);
      }
    }
  }

}
