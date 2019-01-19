package org.jvoicexml.processor.srgs;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.jvoicexml.processor.srgs.grammar.*;

public class JVoiceXmlGrammar implements Grammar {
    private final GrammarManager manager;
    private final URI reference;
    private String root;
    private final Map<String, Rule> rules;
    private Map<String, Object> attributes;

    public JVoiceXmlGrammar(final GrammarManager grammarManager, final URI ref,
        List<Rule> rules) {
        manager = grammarManager;
        reference = ref;
        this.rules = new java.util.HashMap<String, Rule>();
        for (Rule r : rules) addRule(r);
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
        final URI uri = ruleReference.getGrammarReference();
        final String name = ruleReference.getRuleName();
        if (uri == null) {
            return new RuleReference(reference, name);
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
