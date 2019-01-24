/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.mlt.srgsparser;

import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.jvoicexml.processor.srgs.ChartGrammarChecker;
import org.jvoicexml.processor.srgs.ChartGrammarChecker.ChartNode;
import org.jvoicexml.processor.srgs.grammar.*;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/** Interprets the JavaScript tags as the processor for the Microsoft Speech
 *  API does (could not find "official" specification, so do it looking at
 *  examples).
 *
 *  The only exception is the handling of $$NUM patterns for putting the
 *  matched string of the current-NUM token into a slot.
 *
 * @author Christian.Buerckert@DFKI.de, Bernd.Kiefer@dfki.de
 */
public class JSInterpreter implements ChartGrammarChecker.TreeWalker {

  private final ChartGrammarChecker checker;
  private final Stack<ChartNode> stack = new Stack<>();
  private final StringBuilder source = new StringBuilder();

  private static final Pattern tok = Pattern.compile("\\$(\\$|%)[0-9]+");

  private String massageTag(ChartNode tag) {
    String in = ((RuleTag) tag.getRule()).getTag().toString();
    Matcher m = tok.matcher(in);
    int tagIndex = -1;
    List<ChartNode> seq = null;
    StringBuffer sb = null;
    while (m.find()) {
      if (tagIndex < 0) {
        sb = new StringBuffer();
        // find my position
        tagIndex = 0;
        seq = stack.elementAt(stack.size()-2).getChildren();
        for (ChartNode curr : seq) {
          if (curr == tag)
            break;
          ++tagIndex;
        }
        assert (tagIndex < seq.size());
      }
      String match = m.group();
      int delta = Integer.parseInt(match.substring(2));
      ChartNode target = seq.get(tagIndex - delta);
      if (match.charAt(1) == '$') {
        m.appendReplacement(sb, checker.covered(target));
      } else {
        m.appendReplacement(sb, "node_" + target.getId() + "()");
      }
    }
    if (sb != null) {
      m.appendTail(sb);
      return sb.toString();
    }
    return in;
  }

  public JSInterpreter(ChartGrammarChecker c) {
    checker = c;
    //stack.push(null);
    exec("function rule_root(){");
    exec("var out = {}; var rules = {};");
  }

  private void exec(String source) {
    this.source.append(source);
    this.source.append("\n");
  }

  public void enter(ChartNode node, boolean leaf) {
    stack.push(node);
    if (node.getRule() instanceof RuleTag) {
      exec("//user tag start");
      exec(massageTag(node));
      exec("//user tag end");
    } else {
      exec("function node_" + node.getId() + "(){");
      exec(" var out = {}; var rules = {};");
    }
  }

  public void leave(ChartNode node, boolean leaf) {
    ChartNode env = stack.pop(); // == node
    if (node.getRule() instanceof RuleAlternatives) {
      exec("if (out.length == 0) out = node_"
          + node.getChildren().get(0).getId() + "();");
    } else if (node.getRule() instanceof RuleToken) {
      exec("if (out.length == 0) out = \"" + checker.covered(node) + "\";");
    }
    if (!(node.getRule() instanceof RuleTag)) {
      exec("return out;");
      exec("} ");
    }
    if (node.getRule() instanceof RuleParse) {
      // TODO: we need another name generating function for multiple grammars
      String ruleName = ((RuleParse)env.getRule()).getRuleReference().getRuleName();
      exec("rules." + ruleName + "= rule_" + node.getId() + "();");
    }
  }

  public void finish(boolean debug) {
    assert (stack.isEmpty());
    exec("return out;");
    exec("}");
    exec("var rules = {};");
    exec("rules.root = rule_root();");
    exec("var out = rules.root;");
    if (debug) {
      System.out.println(source.toString());
    }
  }

  public JSONObject execute() {
    Context ctx = Context.enter();
    try {
      Scriptable script = ctx.initStandardObjects();
      ctx.evaluateString(script, source.toString(), "<cmd>", 0, null);
      Object result = ctx.evaluateString(script, "JSON.stringify(rules.root);",
          "<cmd>", 0, null);
      return new JSONObject(result.toString());
    } finally {
      Context.exit();
    }
  }

}
