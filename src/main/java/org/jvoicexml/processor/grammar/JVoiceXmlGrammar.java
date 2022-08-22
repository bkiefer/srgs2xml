package org.jvoicexml.processor.grammar;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jvoicexml.processor.GrammarManager;
import org.jvoicexml.processor.srgs.GrammarException;

public class JVoiceXmlGrammar implements Grammar {
  private final GrammarManager manager;
  private final URI reference;
  private URI base;
  private String root;

  private final Map<RuleToken, RuleToken> terminals;
  private final Map<RuleComponent, RuleComponent> nonterminals;
  private final Map<String, Rule> rules;

  private Map<String, Object> attributes;

  /**
   * Create a list of unique tokens (terminals) and non-terminals, as well as
   * left-corner information. In all rules, terminals and non-terminals may be
   * replaced by their unique representatives
   *
   * @param parsedRules
   */
  public void postProcess() {
    for (RuleToken t : getTerminals()) {
      t.computeLeftCorner(manager);
    }
    for (RuleComponent c : getNonterminals()) {
      c.computeLeftCorner(manager);
    }
  }

  @SuppressWarnings("unchecked")
  private void processAttributes() throws GrammarException {
    final String root = (String) attributes.get("root");
    if (root != null) {
      setRoot(root);
      setActivatable(root, true);
    }
    String uriStr = (String) attributes.get("base");
    if (uriStr == null) {
      if (attributes.containsKey("meta"))
        for (Meta e : (List<Meta>) attributes.get("meta")) {
          if (e.key.equals("base")) {
            uriStr = e.value;
            break;
          }
        }
    }
    if (uriStr != null) {
      try {
        base = new URI(uriStr);
        if (!base.isAbsolute()) {
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
      List<Rule> parsedRules, Map<String, Object> attrs)
      throws GrammarException {
    manager = grammarManager;
    reference = ref;

    rules = new HashMap<>();
    terminals = new HashMap<>();
    nonterminals = new HashMap<>();
    for (Rule r : parsedRules) {
      addRule(r.cleanup(terminals, nonterminals));
    }

    attributes = attrs;
    if (attributes != null) {
      processAttributes();
    }
  }

  @Override
  public int getActivationMode() {
    return 0;
  }

  public GrammarManager getGrammarManager() {
    return manager;
  }

  @Override
  public URI getReference() {
    return reference;
  }

  @Override
  public void setActivationMode(int mode) throws IllegalArgumentException {
    // TODO Auto-generated method stub
  }

  @Override
  public boolean isActive() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isActivatable() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
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

  @Override
  public Rule getRule(String ruleName) {
    return rules.get(ruleName);
  }

  @Override
  public void addRule(Rule rule) {
    final String ruleName = rule.getRuleName();
    rules.put(ruleName, rule);
  }

  public void removeRule(String ruleName) throws IllegalArgumentException {
    rules.remove(ruleName);
  }

  @Override
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
      if (!uri.isAbsolute()) {
        uri = base.resolve(uri);
        ruleReference.setGrammarReference(uri);
      }
    }
    return ruleReference;
  }

  @Override
  public void setRoot(String rootName) {
    root = rootName;
  }

  @Override
  public String getRoot() {
    return root;
  }

  public Set<RuleToken> getTerminals() {
    return terminals.keySet();
  }

  public Set<RuleComponent> getNonterminals() {
    return nonterminals.keySet();
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }
}
