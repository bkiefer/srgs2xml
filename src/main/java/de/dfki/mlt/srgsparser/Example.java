/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.mlt.srgsparser;

import java.net.URI;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONObject;
import org.jvoicexml.processor.srgs.ChartGrammarChecker;
import org.jvoicexml.processor.srgs.JVoiceXmlGrammarManager;
import org.jvoicexml.processor.srgs.grammar.Grammar;

/**
 *
 * @author Christian.Buerckert@DFKI.de
 */
public class Example {

    public static void main(String[] args) throws Throwable {
        BasicConfigurator.resetConfiguration();
        //Logger.getRootLogger().setLevel(Level.TRACE);
        //Logger.getLogger(GrammarChecker.class).setLevel(Level.TRACE);

        URI uri = Example.class.getResource("/hysoc.xml").toURI(); //From Resource Folder
        final JVoiceXmlGrammarManager manager = new JVoiceXmlGrammarManager();
        final Grammar ruleGrammar = manager.loadGrammar(uri);

        String[] inputs = {"aila please start the demo",
            "artemis please start the demo",
            "Gloria I need red ribbon",
            "Yes",
            "Artemis please bring this to Malte",
            "This sentence is invalid"
        };
        for (String s : inputs) {
            String[] tokens = s.split(" +");
            final ChartGrammarChecker checker = new ChartGrammarChecker(manager);
            final ChartGrammarChecker.ChartNode validRule = checker.parse(ruleGrammar, tokens);

            // System.out.println(validRule);
            if (validRule != null) {
                MSJSInterpreter walker = new MSJSInterpreter(checker);
                validRule.preorder(walker);
                walker.finish(false);
                JSONObject object = walker.execute();
                System.out.println("============================================================");
                System.out.println(object.toString());
            } else {
                System.out.println("RULE " + s + " INVALID");
            }

        }
    }

}
