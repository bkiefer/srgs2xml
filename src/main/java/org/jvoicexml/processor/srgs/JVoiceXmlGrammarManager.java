package org.jvoicexml.processor.srgs;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.jvoicexml.processor.srgs.abnf.AbnfRuleGrammarParser;
import org.jvoicexml.processor.srgs.grammar.Grammar;
import org.jvoicexml.processor.srgs.grammar.GrammarException;
import org.jvoicexml.processor.srgs.grammar.GrammarManager;
import org.jvoicexml.processor.srgs.grammar.Rule;
import org.jvoicexml.processor.srgs.grammar.RuleAlternatives;
import org.jvoicexml.processor.srgs.grammar.RuleComponent;
import org.jvoicexml.processor.srgs.grammar.RuleCount;
import org.jvoicexml.processor.srgs.grammar.RuleReference;
import org.jvoicexml.processor.srgs.grammar.RuleSequence;

public class JVoiceXmlGrammarManager implements GrammarManager {

    private final Stack<Grammar> grammarStack;

    private final Map<URI, Grammar> grammars;

    public JVoiceXmlGrammarManager() {
        grammars = new java.util.HashMap<URI, Grammar>();
        grammarStack = new Stack<>();
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
            throw new GrammarException(e.getMessage(), e);
        } catch (Exception e) {
            throw new GrammarException(e.getMessage(), e);
        }
        if (rules == null || rules.isEmpty()) {
          throw new GrammarException("Failure in parsing '" + grammarReference
                    + "'");
        }
        // Initialize rule grammar
        final JVoiceXmlGrammar grammar = new JVoiceXmlGrammar(this,
            grammarReference, rules, parser.getAttributes());

        // Register grammar
        grammars.put(grammar.getReference(), grammar);

        grammarStack.push(grammar);
        loadExternalGrammars(rules, grammar);
        grammarStack.pop();

        return grammar;
    }

    public void deleteGrammar(Grammar grammar) {
        grammars.remove(grammar.getReference());
    }

    public Rule resolve(RuleReference reference) {
        final URI ref = reference.getGrammarReference();
        final Grammar grammar = grammars.get(ref);
        if (ref == null || grammar == null) {
            return null;
        }
        String name = reference.getRuleName();
        if (name == null || name.equals("___root")) {
            name = grammar.getRoot();
        }
        return grammar.getRule(name);
    }

    /** Recursively walk the elements of this grammar to look for external
     *  references.
     *
     * @param component
     * @throws IOException
     * @throws GrammarException
     */
    private void walkSubcomponents(RuleComponent component) throws GrammarException, IOException {
      if (component instanceof RuleSequence) {
        final RuleSequence sequence = (RuleSequence) component;
        for (RuleComponent c : sequence.getRuleComponents()) {
          walkSubcomponents(c);
        }
      } else if (component instanceof RuleAlternatives) {
        final RuleAlternatives alternatives = (RuleAlternatives) component;
        for (int i = 0; i < alternatives.size(); ++i) {
          walkSubcomponents(alternatives.getAlternative(i));
        }
      } else if (component instanceof RuleCount) {
        final RuleCount count = (RuleCount) component;
        walkSubcomponents(count.getRuleComponent());
      } else if (component instanceof RuleReference) {
        final RuleReference ref =
            grammarStack.peek().resolve((RuleReference)component);
        // check if this is an unknown external reference
        if (! grammars.containsKey(ref.getGrammarReference())) {
            loadGrammar(ref.getGrammarReference());
        }
        // now it must be possible to resolve the reference!
        if (resolve(ref) == null) {
          throw new GrammarException("Unresolvable rule reference: "
              + ref.getRepresentation());
        }
      }
    }

    /** Check all right hand sides for external references and load the
     *  referenced grammars
     * @param rules a list of Rules
     * @throws IOException
     * @throws GrammarException
     */
    private void loadExternalGrammars(List<Rule> rules, JVoiceXmlGrammar grammar) throws GrammarException, IOException {
      for (Rule r : rules) {
        walkSubcomponents(r.getRuleComponent());
      }
    }
}
