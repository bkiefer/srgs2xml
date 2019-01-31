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

public abstract class Interpreter
implements ChartGrammarChecker.TreeWalker {

  protected final ChartGrammarChecker checker;
  protected final Stack<ChartNode> stack = new Stack<>();
  protected StringBuilder source;
  private int indent = 0;

  private static final char[] INDENT = new char[200];
  static {
    Arrays.fill(INDENT, ' ');
  }

  protected Interpreter(ChartGrammarChecker c) {
    checker = c;
  }

  protected abstract String massageTag(ChartNode n);

  private JSONObject execute() {
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

  private void indent(int k) {
    source.append(INDENT, 0, Math.min(k, INDENT.length));
  }

  protected void open(String prefix) {
    indent(indent);
    this.source.append(prefix);
    this.source.append(" {\n");
    indent += 2;
  }

  protected void close() {
    indent -= 2;
    addline("}");
  }

  protected void addline(String source) {
    indent(indent);
    this.source.append(source);
    this.source.append("\n");
  }

  public JSONObject evaluate(ChartNode root) {
    stack.clear();
    source = new StringBuilder();
    addline("rules = {};");
    open("function rule_root()");
    addline("var out = {};");
    root.preorder(this);
    assert (stack.isEmpty());
    addline("return out;");
    close();
    addline("rules.root = rule_root();");
    return execute();
  }

  public String JSONCode() {
    return source.toString();
  }

  public void enter(ChartNode node, boolean leaf) {
    stack.push(node);
    if (node.getRule() instanceof RuleParse) {
      RuleParse parse = (RuleParse) node.getRule();
      // TODO: we need another name generating function for multiple grammars
      String current = parse.getRuleReference().getRuleName();
      addline("// " + current);
      open("function rule_" + node.getId() + "()");
      addline(" var out = {};");
    } else if (node.getRule() instanceof RuleTag) {
      addline("//user tag start");
      addline(massageTag(node).trim());
      addline("//user tag end");
    }
  }

  public void leave(ChartNode node, boolean leaf) {
    ChartNode env = stack.pop(); // == node
    if (node.getRule() instanceof RuleParse) {
      // TODO: we need another name generating function for multiple grammars
      String ruleName = ((RuleParse)env.getRule()).getRuleReference().getRuleName();
      addline("return out;");
      close();
      addline("rules." + ruleName + "= rule_" + node.getId() + "();");
    }
  }
}
