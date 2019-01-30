package de.dfki.mlt.srgsparser;

import java.util.Arrays;
import java.util.Stack;

import org.json.JSONObject;
import org.jvoicexml.processor.srgs.ChartGrammarChecker;
import org.jvoicexml.processor.srgs.ChartGrammarChecker.ChartNode;
import org.jvoicexml.processor.srgs.grammar.RuleParse;
import org.jvoicexml.processor.srgs.grammar.RuleTag;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public abstract class AbstractJSInterpreter
implements ChartGrammarChecker.TreeWalker {

  protected final ChartGrammarChecker checker;
  protected final Stack<ChartNode> stack = new Stack<>();
  protected final StringBuilder source = new StringBuilder();
  private int indent = 0;

  private static final char[] INDENT = new char[200];
  static {
    Arrays.fill(INDENT, ' ');
  }

  protected AbstractJSInterpreter(ChartGrammarChecker c) {
    checker = c;
    exec("rules = {};");
    exec("function rule_root()");
    open();
    exec("var out = {};");
  }

  protected abstract String massageTag(ChartNode n);

  private void indent(int k) {
    source.append(INDENT, 0, Math.min(k, INDENT.length));
  }

  protected void open() {
    exec("{");
    indent += 2;
  }

  protected void close() {
    indent -= 2;
    exec("}");
  }


  protected void exec(String source) {
    indent(indent);
    this.source.append(source);
    this.source.append("\n");
  }

  public String JSONCode() {
    return source.toString();
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

  public void enter(ChartNode node, boolean leaf) {
    stack.push(node);
    if (node.getRule() instanceof RuleParse) {
      RuleParse parse = (RuleParse) node.getRule();
      // TODO: we need another name generating function for multiple grammars
      String current = parse.getRuleReference().getRuleName();
      exec("// " + current);
      exec("function rule_" + node.getId() + "()");
      open();
      exec(" var out = {};");
    } else if (node.getRule() instanceof RuleTag) {
      exec("//user tag start");
      exec(massageTag(node).trim());
      exec("//user tag end");
    }
  }

  public void leave(ChartNode node, boolean leaf) {
    ChartNode env = stack.pop(); // == node
    if (node.getRule() instanceof RuleParse) {
      // TODO: we need another name generating function for multiple grammars
      String ruleName = ((RuleParse)env.getRule()).getRuleReference().getRuleName();
      exec("return out;");
      close();
      exec("rules." + ruleName + "= rule_" + node.getId() + "();");
    }
  }

  public void finish(boolean debug) {
    assert (stack.isEmpty());
    exec("return out;");
    close();
    exec("rules.root = rule_root();");
    if (debug) {
      System.out.println(source.toString());
    }
  }
}
