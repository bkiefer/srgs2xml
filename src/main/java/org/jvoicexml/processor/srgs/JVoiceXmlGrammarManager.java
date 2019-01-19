package org.jvoicexml.processor.srgs;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.jvoicexml.processor.srgs.grammar.*;

public class JVoiceXmlGrammarManager implements GrammarManager {

    private final Map<URI, Grammar> grammars;

    public JVoiceXmlGrammarManager() {
        grammars = new java.util.HashMap<URI, Grammar>();
    }

    public Grammar[] listGrammars() {
        final Grammar[] listed = new Grammar[grammars.size()];
        return grammars.values().toArray(listed);
    }

    public Grammar getGrammar(URI grammarReference) {
        return grammars.get(grammarReference);
    }

    public Grammar loadGrammar(URI grammarReference)
            throws GrammarException, IOException {
        final URL url = grammarReference.toURL();
        InputStream in = url.openStream();
        int c;
        while ((c = in.read()) != '<' && c >= 0 && c != 'A') {}
        in.close();
        in = url.openStream();
        final RuleGrammarParser parser = c == '<'
            ? new SrgsRuleGrammarParser()
                : new AbnfRuleGrammarParser(grammarReference.toString());

        List<Rule> rules = null;
        try {
            rules = parser.load(in);
        } catch (URISyntaxException e) {
            throw new IOException(e.getMessage(), e);
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        if (rules == null) {
            throw new IOException("Unable to load grammar '" + grammarReference
                    + "'");
        }
        // Initialize rule grammar
        final JVoiceXmlGrammar grammar = new JVoiceXmlGrammar(this,
            grammarReference, rules);
        final Map<String, Object> attributes = parser.getAttributes();
        final String root = (String)attributes.get("root");
        if (root != null) {
            grammar.setRoot(root);
            grammar.setActivatable(root, true);
        }

        // Register grammar
        grammars.put(grammarReference, grammar);

        return grammar;
    }

    public void deleteGrammar(Grammar grammar) {
        grammars.remove(grammar);
    }

    public Rule resolve(RuleReference reference) {
        final URI ref = reference.getGrammarReference();
        final Grammar grammar = (Grammar) grammars.get(ref);
        if (ref == null) {
            return null;
        }
        String name = reference.getRuleName();
        if (name == null) {
            name = grammar.getRoot();
        }
        return grammar.getRule(name);
    }
}
