package org.jvoicexml.processor;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.jvoicexml.processor.grammar.RuleAlternative;
import org.jvoicexml.processor.grammar.RuleAlternatives;
import org.jvoicexml.processor.grammar.RuleComponent;
import org.jvoicexml.processor.grammar.RuleReference;
import org.jvoicexml.processor.grammar.RuleTag;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class SemanticsInterpreter implements TreeWalker<ChartNode> {

  protected final AbstractParser checker;
  protected final Stack<ChartNode> stack = new Stack<>();
  protected StringBuilder source;
  private int indent = 0;

  private static final Pattern tok = Pattern.compile("\\$(\\$|%)[0-9]+");

  private static final char[] INDENT = new char[200];
  static {
    Arrays.fill(INDENT, ' ');
  }

  public SemanticsInterpreter(AbstractParser c) {
    checker = c;
  }

  public static JSONObject execute(String code) {
    Context ctx = Context.enter();
    try {
      Scriptable script = ctx.initStandardObjects();
      ctx.evaluateString(script, code, "<cmd>", 0, null);
      Object result = ctx.evaluateString(script, "JSON.stringify(rules.root);",
          "<cmd>", 0, null);
      return new JSONObject(result.toString());
    } finally {
      Context.exit();
    }
  }

  public static JSONObject interpret(AbstractParser checker,
      Traversable<ChartNode> root) {
    SemanticsInterpreter walker = new SemanticsInterpreter(checker);
    String jscode = walker.createProgram(root);
    return execute(jscode);
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

  public String createProgram(Traversable<ChartNode> root) {
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
    return JSONCode();
  }

  public String JSONCode() {
    return source.toString();
  }

  private RuleReference isRuleRef(ChartNode node) {
    RuleComponent r = node.getRule();
    RuleReference ref = null;
    if (r instanceof RuleReference || r.ruleRoot != null) {
      ref = node.getRule().ruleRoot;
      if (ref == null) {
        ref = (RuleReference) node.getRule();
      }
    }
    return ref;
  }

  /** This does the handling of $$n and $%n tags, which have special meanings
   *  $$n : the matched string n positions before this tag
   *  $%n : the value returned by the rule n positions before this tag
   *  n always starts at one. zero is the tag position (not sensible)
   */
  protected String massageTag(ChartNode tag) {
    String in = ((RuleTag) tag.getRule()).getTag().toString();
    Matcher m = tok.matcher(in);
    int tagIndex = -1;
    List<ChartNode> seq = null;
    StringBuffer sb = null;
    // Replace the $$n and $%n by the right things
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
        ChartNode targ = target;
        while (targ.getRule() instanceof RuleAlternatives
            || targ.getRule() instanceof RuleAlternative) {
          targ = targ.getChildren().get(0);
        }
        if (isRuleRef(targ) != null) {
          m.appendReplacement(sb, "rule_" + targ.getId() + "()");
        } else if (isRuleRef(target) != null) {
          m.appendReplacement(sb, "rule_" + target.getId() + "()");
        }
      }
    }
    if (sb != null) {
      m.appendTail(sb);
      return sb.toString();
    }
    return in;
  }

  @Override
  public void enter(ChartNode node, boolean leaf) {
    stack.push(node);
    RuleReference ref;
    if ((ref = isRuleRef(node)) != null) {
      // TODO: we need another name generating function for multiple grammars
      String current = ref.getRuleName();
      addline("// " + current);
      open("function rule_" + node.getId() + "()");
      addline(" var out = {};");
    } else if (node.getRule() instanceof RuleTag) {
      addline("//user tag start");
      addline(massageTag(node).trim());
      addline("//user tag end");
    }
  }

  @Override
  public void leave(ChartNode node, boolean leaf) {
    @SuppressWarnings("unused")
    ChartNode env = stack.pop(); // == node
    RuleReference ref;
    if ((ref = isRuleRef(node)) != null) {
      // TODO: we need another name generating function for multiple grammars
      String ruleName = ref.getRuleName();
      addline("return out;");
      close();
      addline("rules." + ruleName + "= rule_" + node.getId() + "();");
      // this works in older versions of rhino
      // addline("rules.latest = rule_"+node.getId());
      addline("rules.latest = () => rules."+ruleName+";");
    }
  }
}
