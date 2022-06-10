/*
 * File:    $HeadURL$
 * Version: $LastChangedRevision$
 * Date:    $Date$
 * Author:  $LastChangedBy$
 *
 * JVoiceXML - A free VoiceXML implementation.
 *
 * Copyright (C) 2008-2014 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package org.jvoicexml.processor.srgs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jvoicexml.processor.srgs.grammar.Grammar;
import org.jvoicexml.processor.srgs.grammar.GrammarException;
import org.jvoicexml.processor.srgs.grammar.GrammarManager;
import org.jvoicexml.processor.srgs.grammar.Rule;
import org.jvoicexml.processor.srgs.grammar.RuleAlternatives;
import org.jvoicexml.processor.srgs.grammar.RuleComponent;
import org.jvoicexml.processor.srgs.grammar.RuleCount;
import org.jvoicexml.processor.srgs.grammar.RuleParse;
import org.jvoicexml.processor.srgs.grammar.RuleReference;
import org.jvoicexml.processor.srgs.grammar.RuleSequence;
import org.jvoicexml.processor.srgs.grammar.RuleSpecial;
import org.jvoicexml.processor.srgs.grammar.RuleTag;
import org.jvoicexml.processor.srgs.grammar.RuleToken;

/**
 * This class provides a means to perform evaluations on a parsed grammar.
 *
 * @author Bernd Kiefer
 * @version $Revision$
 * @since 0.7
 */
public final class ChartGrammarChecker {

  private static int gen = 0;

  public static interface TreeWalker {

    public void enter(ChartNode node, boolean leaf);

    public void leave(ChartNode node, boolean leaf);
  }

  // A chart node structure, a replacement for the rule walker
  public static class ChartNode {

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
    private ChartNode(int s, int e, RuleComponent r, int d) {
      id = ++gen;
      start = s;
      end = e;
      rule = r;
      dot = d;
      children = new ArrayList<ChartNode>();
    }

    /** Constructor for predicts */
    private ChartNode(int s, RuleComponent r) {
      // if it's an epsilon, it's passive (dot == -1)
      this(s, s, r, (r.equals(RuleSpecial.NULL) ? -1 : 0));
    }

    /** Constructor combining active and passive item */
    private ChartNode(ChartNode active, ChartNode passive) {
      this(active.start, passive.end, active.rule,
          active.rule.nextSlot(active.dot));
      children.addAll(active.children);
      children.add(passive);
    }

    // TODO: would be nicer if we had a "graphical" dot, but for that, we would
    // need functions to print the rule with a dot argument
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

    public void preorder(TreeWalker acceptor) {
      acceptor.enter(this, children.isEmpty());
      for (ChartNode child : children) {
        child.preorder(acceptor);
      }
      acceptor.leave(this, children.isEmpty());
    }

    public boolean equals(ChartNode c) {
      return (start == c.start && end == c.end && rule == c.rule
          && dot == c.dot);
    }

    public boolean isPassive() {
      return dot < 0;
    }

    public List<ChartNode> getChildren() { return children; }

    public int getId() { return id; }
    
    public int getStart() { return start; }
    
    public int getEnd() { return end; }
  }

  private final GrammarManager manager;

  /** Lazy expansion of RuleReference, to avoid tainting the RuleReference with
   * grammar resolution code.
   */
  final private Map<RuleReference, RuleParse> resolved;

  /** The current input */
  private String[] input;

  /** A chart */
  private List<ChartNode>[] chartIn, chartOut;

  /** An agenda */
  private Deque<ChartNode> agenda;

  /** The currently used Grammar */
  private Grammar grammar;

  /** Constructs a new GrammarChecker.
   *
   * @param grammarManager the grammar manager.
   */
  public ChartGrammarChecker(final GrammarManager grammarManager) {
    manager = grammarManager;
    resolved = new HashMap<RuleReference, RuleParse>();
  }

  private RuleComponent getResolved(RuleComponent c) {
    if (c instanceof RuleParse) {
      return ((RuleParse) c).getRuleReference();
    }
    return c;
  }

  private boolean canExpand(ChartNode active, ChartNode passive) {
    return active.rule.looksFor(getResolved(passive.rule), active.dot);
  }

  /**
   * Checks if the given tokens can be represented using the given graph.
   *
   * @param grammar the grammar to check
   * @param input the tokens
   * @return <code>true</code> if the tokens are valid.
   * @throws GrammarException
   */
  public ChartNode parse(final Grammar gram, final String[] in)
      throws GrammarException {
    input = in;
    newChart(input.length);
    agenda = new ArrayDeque<ChartNode>();
    grammar = gram;
    final String root = grammar.getRoot();
    final Rule rule = grammar.getRule(root);
    if (rule == null) {
      throw new GrammarException("Undefined rule referenced: " + root);
    }
    RuleComponent component = rule.getRuleComponent();
    final ChartNode init = new ChartNode(0, component);
    add(init);

    while (!agenda.isEmpty()) {
      ChartNode curr = agenda.pop();
      List<ChartNode> expanded = new ArrayList<ChartNode>();
      if (curr.isPassive()) {
        // complete passive to the left
        for (ChartNode act : chartIn[curr.start]) {
          // all active items that look for this passive item are to be completed
          if (canExpand(act, curr)) {
            expanded.add(new ChartNode(act, curr));
          }
        }
      } else {
        // active item: add predictions
        predict(grammar, curr);
        // Complete active item to the right
        if (null != chartOut[curr.end]) {
          for (ChartNode pass : chartOut[curr.end]) {
            // all passive items this item looks for are to be completed
            if (canExpand(curr, pass)) {
              expanded.add(new ChartNode(curr, pass));
            }
          }
        }
      }
      for (ChartNode c : expanded) {
        add(c);
      }
    }
    if (null == chartOut[0]) {
      return null;
    }
    if (component instanceof RuleReference) {
      component = resolved.get(component);
    }
    for (ChartNode c : chartOut[0]) {
      if (c.end == input.length && c.rule == component) {
        return c;
      }
    }
    return null;
  }
  
  /** Return the input string covered by the chart node n */
  public String covered (ChartNode n) {
    StringBuilder sb = new StringBuilder();
    for(int i = n.start; i < n.end; ++i) {
      sb.append(input[i]).append(' ');
    }
    sb.deleteCharAt(sb.length()-1); // delete trailing space
    return sb.toString();
  }

  private void predict(Grammar grammar, ChartNode current)
      throws GrammarException {
    RuleComponent component = current.rule;

    if (component instanceof RuleSequence) {
      final RuleSequence sequence = (RuleSequence) component;
      predict(grammar, sequence, current);
    } else if (component instanceof RuleAlternatives) {
      final RuleAlternatives alternatives = (RuleAlternatives) component;
      predict(grammar, alternatives, current);
    } else if (component instanceof RuleCount) {
      final RuleCount count = (RuleCount) component;
      predict(grammar, count, current);
    } else if (component instanceof RuleReference) {
      final RuleReference ref = (RuleReference)component;
      predict(grammar, ref, current);
    } else if (component instanceof RuleParse) {
      final RuleParse parse = (RuleParse) component;
      predict(grammar, parse, current);
    } else if (component instanceof RuleToken) {
      final RuleToken token = (RuleToken) component;
      scan(grammar, token, current);
    } else if (component instanceof RuleTag) {
      final RuleTag tag = (RuleTag) component;
      scan(grammar, tag, current);
    }
  }

  @SuppressWarnings("unchecked")
  private void newChart(int tokens) {
    chartIn = new ArrayList[tokens + 1];
    chartOut = new ArrayList[tokens + 1];
  }

  private ChartNode addToChart(ChartNode c) {
    if (c.isPassive()) {
      List<ChartNode> out = chartOut[c.start];
      if (null == out) {
        out = new ArrayList<ChartNode>();
        chartOut[c.start] = out;
      }
      for (ChartNode x : out) {
        if (x.equals(c)) {
          if (null == x.equivs) {
            x.equivs = new ArrayList<ChartNode>();
          }
          x.equivs.add(c);
          return x;
        }
      }
      out.add(c);
    } else {
      List<ChartNode> in = chartIn[c.end];
      if (null == in) {
        in = new ArrayList<ChartNode>();
        chartIn[c.end] = in;
      }
      for (ChartNode x : in) {
        if (x.equals(c)) {
          if (null == x.equivs) {
            x.equivs = new ArrayList<ChartNode>();
          }
          x.equivs.add(c);
          return x;
        }
      }
      in.add(c);
    }
    return c;
  }

  private ChartNode add(ChartNode c) {
    if (addToChart(c) == c) {
      agenda.add(c);
    }
    return c;
  }

  private ChartNode addPrediction(int pos, RuleComponent r)
      throws GrammarException {
    if (r instanceof RuleReference) {
      // resolve r and replace it by the resolved proxy, a RuleParse
      if (resolved.containsKey(r)) {
        r = resolved.get(r);
      } else {
        final RuleReference reference = (RuleReference) r;
        final Rule rule = manager.resolve(reference);
        if (rule == null) {
          throw new GrammarException("Invalid rule reference: " + reference);
        }
        final RuleComponent component = rule.getRuleComponent();
        final RuleParse rp = new RuleParse(reference, component);
        resolved.put(reference, rp);
        r = rp;
      }
    }
    return add(new ChartNode(pos, r));
  }

  private void predict(final Grammar grammar, final RuleParse reference,
      final ChartNode current) throws GrammarException {
    final RuleComponent component = reference.getParse();
    // predict RHS of new nonterminal: new prediction
    addPrediction(current.end, component);
  }

  private void predict(final Grammar grammar,
      final RuleAlternatives alternatives, final ChartNode current)
      throws GrammarException {
    // one new prediction per alternative: an implicit nonterminal
    if (current.dot == 0) {
      // the one with dot == zero is responsible to introduce the rest
      for (int dot = 1; dot < alternatives.size(); ++dot) {
        // add predictions for the other alternatives, and one for the
        // embedded node
        add(new ChartNode(current.end, current.end, alternatives, dot));
      }
    }
    // every alternative predicts its own sub-component
    addPrediction(current.end, alternatives.getAlternative(current.dot));
    /*
    // This seems attractive, but it makes the completion of active items
    // much more complicated. I currently don't have a solution, so i'll stick
    // with what works
    for (RuleAlternative alt : alternatives.getRuleAlternatives()) {
      addPrediction(current.end, alt.getRuleComponent());
    }
    */
  }

  private void predict(final Grammar grammar, final RuleSequence sequence,
      final ChartNode current) throws GrammarException {
    final List<RuleComponent> components = sequence.getRuleComponents();
    addPrediction(current.end, components.get(current.dot));
  }

  private void predict(final Grammar grammar, final RuleCount count,
      final ChartNode current) throws GrammarException {
    final RuleComponent component = count.getRuleComponent();
    final int min = count.getRepeatMin();
    int max = count.getRepeatMax();
    final int repeat = current.dot;
    if (repeat < max) { // predict sub-component
      addPrediction(current.end, component);
    }
    if (repeat >= min) {
      // add passive item: a special case
      ChartNode c = new ChartNode(current.start, current.end, count, -1);
      c.children.addAll(current.children);
      add(c);
    }
  }

  private void predict(final Grammar grammar, final RuleReference ref,
      final ChartNode current) throws GrammarException {
    addPrediction(current.end, ref);
  }

  /**
   * This is rather a scan than predict. It directly creates a passive item,
   * if possible.
   *
   * @param grammar
   * @param token
   * @param current
   * @throws GrammarException
   */
  private void scan(final Grammar grammar, final RuleToken token,
      final ChartNode current) throws GrammarException {
    int pos = current.start;
    Pattern p = token.getPattern();
    if (p == null) {
      final String text = token.getText();
      final String[] tokens = text.split(" ");
      for (String tok : tokens) {
        if (pos >= input.length) {
          return;
        }
        final String currentInput = input[pos];
        if (!tok.equalsIgnoreCase(currentInput)) {
          return;
        }
        ++pos;
      }
    } else {
      if (pos >= input.length) {
        return;
      }
      final String currentInput = input[pos];
      if (! p.matcher(currentInput).matches()) {
        return;
      }
      ++pos;
    }

    // now, for the first time, we add a complete token
    add(new ChartNode(current.start, pos, token, -1));
  }

  /**
   * This is rather a scan than predict. It directly creates a passive item,
   * if possible.
   *
   * @param grammar
   * @param token
   * @param current
   * @throws GrammarException
   */
  private void scan(final Grammar grammar, final RuleTag tag,
      final ChartNode current) throws GrammarException {
    // add complete epsilon item
    add(new ChartNode(current.start, current.end, tag, -1));
  }
  
  /********************** For displaying the chart **********************/
  
  public int chartSize() {
    return chartOut.length;
  }

  public List<ChartNode> getOutEdges(int i) {
    return chartOut[i];
  }
  
}
