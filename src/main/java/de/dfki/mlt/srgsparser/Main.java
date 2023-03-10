/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.mlt.srgsparser;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.JSONObject;
import org.jvoicexml.processor.AbstractParser;
import org.jvoicexml.processor.AbstractParser.ChartNode;
import org.jvoicexml.processor.JVoiceXmlGrammarManager;
import org.jvoicexml.processor.SemanticsInterpreter;
import org.jvoicexml.processor.grammar.Grammar;
import org.jvoicexml.processor.srgs.GrammarException;
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

  protected JVoiceXmlGrammarManager manager = new JVoiceXmlGrammarManager();
  protected Grammar ruleGrammar;
  protected AbstractParser checker;

  protected static boolean debug = true;

  public void loadGrammar(URI grammarUri) throws GrammarException, IOException {
    ruleGrammar = manager.loadGrammar(grammarUri);
  }

  protected ChartNode process(String s) {
    SemanticsInterpreter walker = null;
    String jscode = "";
    ChartNode validRule = null;
    try {
      String[] tokens = s.split(" +");
      checker = AbstractParser.getParser(manager);
      validRule = checker.parse(ruleGrammar, tokens);

      // System.out.println(validRule);
      if (validRule != null) {
        walker = new SemanticsInterpreter(checker);
        jscode = walker.createProgram(validRule);
        if (debug) {
          System.out.println(jscode);
        }
        JSONObject object = SemanticsInterpreter.execute(jscode);
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
      System.out.println("JavaScript error for \"" + jscode + "\"");
      System.out.println(e);
    } catch (EvaluatorException e) {
      System.out.println("JavaScript error for \"" + jscode + "\"");
      System.out.println(e);
    } catch (GrammarException e) {
      System.out.println("Parse error for \"" + s + "\"");
      System.out.println(e);
    }
    return validRule;
  }

  public static void main(String[] args) throws Throwable {
    if (args.length < 1) {
      usage();
      System.exit(1);
    }
    File grammarFile = new File(args[0]);
    Main main = new Main();
    main.loadGrammar(grammarFile.toURI());

    if (args.length > 1) {
      Stream<String> in = Files.lines(Paths.get(args[1]));
      in.forEach((s) -> { main.process(s); });
      in.close();
    } else {
      Console c = System.console();
      if (c == null) System.exit(-1);
      String s;
      while (!(s = c.readLine()).isEmpty()) {
        main.process(s);
      }
    }
  }

}
