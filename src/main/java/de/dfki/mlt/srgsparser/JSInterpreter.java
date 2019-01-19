/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.mlt.srgsparser;

import java.util.Stack;

import org.json.JSONObject;
import org.jvoicexml.processor.srgs.ChartGrammarChecker;
import org.jvoicexml.processor.srgs.ChartGrammarChecker.ChartNode;
import org.jvoicexml.processor.srgs.grammar.RuleParse;
import org.jvoicexml.processor.srgs.grammar.RuleTag;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author Christian.Buerckert@DFKI.de
 */
public class JSInterpreter implements ChartGrammarChecker.TreeWalker {

    private final Stack<ChartNode> stack = new Stack<>();
    private final StringBuilder source = new StringBuilder();

    public JSInterpreter() {
        stack.push(null);
        exec("rules = {};");
        exec("function rule_root(){");
        exec("var out = {};");
    }

    private void exec(String source) {
        this.source.append(source);
        this.source.append("\n");
    }

    public void enter(ChartNode node, boolean leaf) {
        if (node.getRule() instanceof RuleParse) {
            RuleParse parse = (RuleParse) node.getRule();
            String current = parse.getRuleReference().getRuleName();
            stack.push(node);
            exec("function rule_" + current + "(){");
            exec(" var out = {};");
        }
        if (node.getRule() instanceof RuleTag) {
            RuleTag tag = (RuleTag) node.getRule();
            exec("//user tag start");
            exec(tag.getTag().toString());
            exec("//user tag end");
        }
    }

    public void leave(ChartGrammarChecker.ChartNode node, boolean leaf) {
        if (node.getRule() instanceof RuleParse) {
            ChartNode env = stack.pop();
            exec("return out;");
            exec("} ");
            exec("rules." + env + "= rule_" + env + "();");
        }
    }

    public void finish(boolean debug) {
        while (!stack.isEmpty()) {
            ChartNode env = stack.pop();
            exec("return out;");
            exec("}");
            exec("rules." + env + " = rule_" + env + "();");
        }
        if (debug) {
            System.out.println(source.toString());
        }
    }

    public JSONObject execute() {
        Context ctx = Context.enter();
        try {
            Scriptable script = ctx.initStandardObjects();
            ctx.evaluateString(script, source.toString(), "<cmd>", 0, null);
            Object result = ctx.evaluateString(script, "JSON.stringify(rules.root);", "<cmd>", 0, null);
            return new JSONObject(result.toString());
        } finally {
            Context.exit();
        }

    }

}
