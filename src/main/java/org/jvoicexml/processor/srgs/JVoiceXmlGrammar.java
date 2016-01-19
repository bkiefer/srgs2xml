package org.jvoicexml.processor.srgs;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.jvoicexml.processor.srgs.grammar.GrammarException;
import org.jvoicexml.processor.srgs.grammar.GrammarManager;
import org.jvoicexml.processor.srgs.grammar.Rule;
import org.jvoicexml.processor.srgs.grammar.RuleGrammar;
import org.jvoicexml.processor.srgs.grammar.RuleParse;
import org.jvoicexml.processor.srgs.grammar.RuleReference;

public class JVoiceXmlGrammar implements RuleGrammar {
    private final GrammarManager manager;
    private final URI reference;
    private String root;
    private final Map<String, Rule> rules;

    public JVoiceXmlGrammar(final GrammarManager grammarManager, final URI ref) {
        manager = grammarManager;
        reference = ref;
        rules = new java.util.HashMap<String, Rule>();
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

    public void addElement(String element) throws GrammarException {
        // TODO Auto-generated method stub

    }


    public void removeElement(String element) {
        // TODO Auto-generated method stub

    }


    public Rule getRule(String ruleName) {
        return rules.get(ruleName);
    }


    public void addRule(Rule rule) {
        final String ruleName = rule.getRuleName();
        rules.put(ruleName, rule);
    }


    public void addRule(String ruleText) throws GrammarException {
        // TODO Auto-generated method stub

    }


    public void addRules(Rule[] rules) {
        for (Rule rule : rules) {
            addRule(rule);
        }
    }


    public void removeRule(String ruleName) throws IllegalArgumentException {
        rules.remove(ruleName);
    }


    public String[] listRuleNames() {
        final Set<String> keys = rules.keySet();
        final String[] names = new String[keys.size()];
        return keys.toArray(names);
    }


    public void setAttribute(String attribute, String value)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }


    public String getAttribute(String attribute)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }


    public String[] getElements() {
        // TODO Auto-generated method stub
        return null;
    }


    public RuleParse parse(String[] tokens, String ruleName)
            throws IllegalArgumentException, GrammarException {
        // TODO Auto-generated method stub
        return null;
    }


    public RuleParse parse(String text, String ruleName)
            throws IllegalArgumentException, GrammarException {
        // TODO Auto-generated method stub
        return null;
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

}
