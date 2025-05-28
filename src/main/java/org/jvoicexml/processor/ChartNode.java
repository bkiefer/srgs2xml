package org.jvoicexml.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jvoicexml.processor.grammar.RuleComponent;
import org.jvoicexml.processor.grammar.RuleSpecial;
import org.jvoicexml.processor.grammar.RuleTag;

// A chart node structure, a replacement for the rule walker
public class ChartNode implements Traversable<ChartNode> {

  int start, end, dot, id;
  RuleComponent rule;
  // multiple parents needed, in case added multiple times
  List<ChartNode> children;
  List<ChartNode> equivs;
  //ChartNode parent;

  public RuleComponent getRule() {
    return rule;
  }

  /** Constructor, for use with other constructors */
  protected ChartNode(int s, int e, RuleComponent r, int d) {
    id = ++AbstractParser.gen;
    start = s;
    end = e;
    rule = r;
    dot = d;
    children = new ArrayList<ChartNode>();
  }

  /** Constructor for predicts */
  protected ChartNode(int s, RuleComponent r) {
    // if it's an epsilon, it's passive (dot == -1)
    this(s, s, r,
        (r.equals(RuleSpecial.NULL) || (r instanceof RuleTag)
            //|| (r instanceof RuleToken && ((RuleToken)r).isEpsilon())
            ? -1 : 0));
  }

  /** Constructor combining active and passive item (only AbstractParser)
   */
  protected ChartNode(ChartNode active, ChartNode passive) {
    this(active.start, passive.end, active.rule,
        active.rule.nextSlot(active.dot));
    children.addAll(active.children);
    children.add(passive);
  }

  /** Constructor for "left corner predicts" that combine a RuleComponent with
   *  dot at position zero and a passive ChartNode
   *  Only used by LeftCornerParser
   */
  protected ChartNode(RuleComponent r, ChartNode passive) {
    // The first item of r has already be filled, and span is the same as the
    // tokNode
    this(passive.getStart(), passive.getEnd(), r, r.nextSlot(0));
    children.add(passive);
  }

  /** Constructor advancing the dot for RuleSequence when the next item is
   *  a RuleTag (only LeftCornerParser)
   */
  protected ChartNode(int pos, RuleTag tag) {
    this(pos, pos, tag, -1);
  }

  // TODO: would be nicer if we had a "graphical" dot, but for that, we would
  // need functions to print the rule with a dot argument
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append('(').append(Integer.toString(id)).append(':')
        .append(Integer.toString(start)).append(',')
        .append(Integer.toString(end)).append(',')
        .append(Integer.toString(dot)).append(" <");
    for (ChartNode c : children) {
      sb.append(c == null ? "r" : Integer.toString(c.id)).append(' ');
    }
    sb.append("> ").append(rule).append(')');
    return sb.toString();
  }

  public void printTree(String indent) {
    System.out.println(indent + this);
    for (ChartNode child : children) {
      child.printTree(indent + "  ");
    }
  }

  @Override
  public void preorder(TreeWalker<ChartNode> acceptor) {
    acceptor.enter(this, children.isEmpty());
    for (ChartNode child : children) {
      child.preorder(acceptor);
    }
    acceptor.leave(this, children.isEmpty());
  }

  public boolean equals(ChartNode c) {
    return (start == c.start && end == c.end && rule.equals(c.rule)
        && dot == c.dot);
  }

  public boolean equalsChildren(ChartNode c) {
    Iterator<ChartNode> it = c.children.iterator();
    for (ChartNode child : children) {
      if (child.id != it.next().id) return false;
    }
    return true;
  }

  public boolean isPassive() {
    return dot < 0;
  }

  public List<ChartNode> getChildren() { return children; }

  public int getId() { return id; }

  public int getDot() { return dot; }

  public int getStart() { return start; }

  public int getEnd() { return end; }
}