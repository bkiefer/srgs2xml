package org.jvoicexml.processor.srgs;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.jvoicexml.processor.srgs.grammar.Grammar;
import org.jvoicexml.processor.srgs.grammar.GrammarException;
import org.jvoicexml.processor.srgs.grammar.GrammarManager;
import org.jvoicexml.processor.srgs.grammar.Meta;
import org.jvoicexml.processor.srgs.grammar.Rule;
import org.jvoicexml.processor.srgs.grammar.RuleReference;

public class JVoiceXmlGrammar implements Grammar {
    private final GrammarManager manager;
    private final URI reference;
    private URI base;
    private String root;
    private final Map<String, Rule> rules;
    private Map<String, Object> attributes;

    @SuppressWarnings("unchecked")
    private void processAttributes() throws GrammarException {
      final String root = (String)attributes.get("root");
      if (root != null) {
          setRoot(root);
          setActivatable(root, true);
      }
      String uriStr = (String) attributes.get("base");
      if (uriStr == null) {
        if (attributes.containsKey("meta"))
        for (Meta e : (List<Meta>)attributes.get("meta")) {
          if (e.key.equals("base")) {
            uriStr = e.value;
            break;
          }
        }
      }
      if (uriStr != null) {
        try {
          base = new URI(uriStr);
          if (! base.isAbsolute()) {
            base = reference.resolve(base);
          }
        } catch (URISyntaxException ex) {
          throw new GrammarException("Wrong base URI specified: " + uriStr, ex);
        }
      } else {
        base = reference;
      }
    }
    
    public JVoiceXmlGrammar(final GrammarManager grammarManager, final URI ref,
        List<Rule> rules, Map<String, Object> attrs) throws GrammarException {
        manager = grammarManager;
        reference = ref;
        this.rules = new java.util.HashMap<String, Rule>();
        for (Rule r : rules) addRule(r);
        attributes = attrs;
        if (attributes != null) {
          processAttributes();
        }
    }


    public int getActivationMode() {
        return 0;
    }


    public GrammarManager getGrammarManager() {
        return manager;
    }


    public URI getReference() {
        return reference;
    }


    public void setActivationMode(int mode) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    public boolean isActive() {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean isActivatable() {
        // TODO Auto-generated method stub
        return false;
    }


    public void setActivatable(boolean activatable) {
        // TODO Auto-generated method stub

    }


    public boolean isActivatable(String ruleName) {
        // TODO Auto-generated method stub
        return false;
    }


    public void setActivatable(String ruleName, boolean activatable) {
        // TODO Auto-generated method stub

    }

    public Rule getRule(String ruleName) {
        return rules.get(ruleName);
    }


    public void addRule(Rule rule) {
        final String ruleName = rule.getRuleName();
        rules.put(ruleName, rule);
    }

    public void removeRule(String ruleName) throws IllegalArgumentException {
        rules.remove(ruleName);
    }

    public RuleReference resolve(RuleReference ruleReference)
            throws GrammarException {
        URI uri = ruleReference.getGrammarReference();
        // if it has no rule reference, it belongs to this grammar
        if (uri == null) {
          ruleReference.setGrammarReference(reference);
        } else {
          // resolve the URI in case it is relative
          // if the reference is relative the base URI is either
          // 1. a URI declared by the 'base' header element of this grammar
          // 2. default: the base URI of the grammar it is referenced by
          // 3. set by a 'meta' header definition: we don't support that
          if (! uri.isAbsolute()) {
            uri = base.resolve(uri);
            ruleReference.setGrammarReference(uri);
          }
        }
        return ruleReference;
    }

    public void setRoot(String rootName) {
        root = rootName;
    }

    public String getRoot() {
        return root;
    }

    @Override
    public Map<String, Object> getAttributes() {
      return attributes;
    }
}
