package org.jvoicexml.processor.grammar;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import org.jvoicexml.processor.GrammarManager;
import org.jvoicexml.processor.srgs.GrammarException;

public class JVoiceXmlGrammar implements Grammar {
  private class TokenMap {

    Map<String, List<RuleToken>> impl;
    List<RuleToken> patterns;

    public TokenMap() {
      impl = new HashMap<>();
      patterns  = new ArrayList<>();
    }

    public void add(RuleToken t) {
      if (t.getPattern() != null) {
        patterns.add(t);
      } else {
        String leftMost = t.getTokens()[0];
        if (! isCaseSensitive) {
          leftMost = leftMost.toLowerCase();
        }
        List<RuleToken> tokens = impl.get(leftMost);
        if (tokens == null) {
          tokens = new ArrayList<>();
          impl.put(leftMost, tokens);
        }
        tokens.add(t);
      }
    }

    public List<RuleToken> get(String leftMost) {
      if (! isCaseSensitive) {
        leftMost = leftMost.toLowerCase();
      }
      List<RuleToken> result = impl.get(leftMost);
      return result == null ? Collections.emptyList() : result;
    }

    public List<RuleToken> get() {
      return patterns;
    }
  }


  private final GrammarManager manager;
  private final URI reference;
  private URI base;
  private String root;

  private final Map<RuleToken, RuleToken> terminals;
  private final Map<RuleComponent, RuleComponent> nonterminals;
  private final Map<String, Rule> rules;

  private Map<String, Object> attributes;

  private boolean isCaseSensitive = false;

  private TokenMap tokenMap = new TokenMap();

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
      tokenMap.add(t);
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

  public void setCaseSensitive(boolean val) {
    isCaseSensitive = val;
  }

  public boolean isCaseSensitive() {
    return isCaseSensitive;
  }

  private int matchTokenSequence(String[] input, int start, String[] tokens) {
    if (start + tokens.length > input.length) return -1;
    int pos = start + 1;
    for (int i = 1; i < tokens.length; ++i, ++pos) {
      if (isCaseSensitive) {
        if (!tokens[i].equals(input[pos])) return -1;
      } else {
        if (!tokens[i].equalsIgnoreCase(input[pos])) return -1;
      }
    }
    return pos;
  }

  public void getPreterminals(String[] input, int start,
      BiConsumer<RuleComponent, Integer> consumer) {
    String leftMostToken = input[start];
    for (RuleToken token : tokenMap.get(leftMostToken)) {
      int pos = matchTokenSequence(input, start, token.getTokens());
      if (pos >= 0) {
        consumer.accept(token, pos);
      }
    }
  }


  public Collection<RuleToken> getPatternTerminals() {
    return tokenMap.get();
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

  public void addSymbols(JVoiceXmlGrammar sub) {
    terminals.putAll(sub.terminals);
    nonterminals.putAll(sub.nonterminals);
  }
}
