/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.mlt.srgsparser;

import java.net.URI;

import org.json.JSONObject;
import org.jvoicexml.processor.AbstractParser;
import org.jvoicexml.processor.ChartNode;
import org.jvoicexml.processor.JVoiceXmlGrammarManager;
import org.jvoicexml.processor.SemanticsInterpreter;
import org.jvoicexml.processor.grammar.Grammar;

/**
 *
 * @author Christian.Buerckert@DFKI.de
 */
public class Example {

    public static void main(String[] args) throws Throwable {

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
            final AbstractParser checker = AbstractParser.getParser(manager);
            final ChartNode validRule = checker.parse(ruleGrammar, tokens);

            // System.out.println(validRule);
            if (validRule != null) {
                SemanticsInterpreter walker = new SemanticsInterpreter(checker);
                JSONObject object = SemanticsInterpreter.execute(walker.createProgram(validRule));
                System.out.println("============================================================");
                System.out.println(object.toString());
            } else {
                System.out.println("RULE " + s + " INVALID");
            }

        }
    }

}
