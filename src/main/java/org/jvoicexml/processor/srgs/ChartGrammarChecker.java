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

import org.apache.log4j.Logger;
import org.jvoicexml.processor.srgs.grammar.GrammarException;
import org.jvoicexml.processor.srgs.grammar.GrammarManager;
import org.jvoicexml.processor.srgs.grammar.Rule;
import org.jvoicexml.processor.srgs.grammar.RuleAlternatives;
import org.jvoicexml.processor.srgs.grammar.RuleComponent;
import org.jvoicexml.processor.srgs.grammar.RuleCount;
import org.jvoicexml.processor.srgs.grammar.RuleGrammar;
import org.jvoicexml.processor.srgs.grammar.RuleParse;
import org.jvoicexml.processor.srgs.grammar.RuleReference;
import org.jvoicexml.processor.srgs.grammar.RuleSequence;
import org.jvoicexml.processor.srgs.grammar.RuleTag;
import org.jvoicexml.processor.srgs.grammar.RuleToken;

/**
 * This class provides a means to perform evaluations on a parsed grammar.
 *
 * @author Dirk Schnelle-Walka
 * @author Brian Pendell
 * @version $Revision$
 * @since 0.7
 */
public final class ChartGrammarChecker {
  static int gen = 0;

  // A chart node structure, a replacement for the rule walker
  public class ChartNode {

    int start, end, dot, id;
    RuleComponent rule;
    // multiple parents needed, in case added multiple times
    List<ChartNode> parent;

    /** Constructor
     * @throws GrammarException
     */
    private ChartNode(int s, int e, RuleComponent r, int d)
        throws GrammarException {
      id = ++gen;
      start = s;
      end = e;
      if (r instanceof RuleReference) {
        // resolve r and replace it by the resolved proxy, a RuleParse
        if (resolved.containsKey(r)) {
          r = resolved.get(r);
        } else {
          final RuleReference reference = (RuleReference)r;
          final RuleReference resolvedReference = grammar.resolve(reference);
          final Rule rule = manager.resolve(resolvedReference);
          if (rule == null) {
            throw new GrammarException("Invalid rule reference: " + reference);
          }
          // TODO need to set the new grammar if it changed
          final RuleComponent component = rule.getRuleComponent();
          final RuleParse rp = new RuleParse(reference, component);
          resolved.put(reference, rp);
          r = rp;
        }
      }
      rule = r;
      dot = d;
      parent = new ArrayList<ChartNode>();
    }

    /** Constructor
     * @throws GrammarException
     */
    public ChartNode(int s, int e, RuleComponent r, ChartNode p, int d)
        throws GrammarException {
      this(s, e, r, d);
      parent.add(p);
    }

    /** Constructor
     * @throws GrammarException
     */
    public ChartNode(int s, int e, RuleComponent r, List<ChartNode> pl, int d)
        throws GrammarException {
      this(s, e, r, d);
      parent.addAll(pl);
    }

   /** Constructor for predicts
     * @throws GrammarException
     */
    public ChartNode(int s, RuleComponent r, ChartNode p)
        throws GrammarException {
      this(s, s, r, p, 0);
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append('(').append(Integer.toString(id)).append(':')
      .append(Integer.toString(start)).append(',')
      .append(Integer.toString(end)).append(',').append(Integer.toString(dot))
      .append(" <");
      for (ChartNode c : parent) {
        sb.append(c == null ? "r" : Integer.toString(c.id)).append(' ');
      }
      sb.append("> ").append(rule).append(')');
      return sb.toString();
    }

    public boolean equals(ChartNode c) {
      return (end == c.end && rule == c.rule && dot == c.dot);
    }
  }

  /** Logger instance. */
  private static final Logger LOGGER = Logger.getLogger(ChartGrammarChecker.class);

  private final GrammarManager manager;

  /** Lazy expansion of RuleReference, to avoid tainting the RuleReference with
   *  grammar resolution code.
   */
  final private Map<RuleReference, RuleParse> resolved;

  /** The current input */
  private String[] input;

  /** A chart */
  private List<ChartNode>[] chartIn, chartOut;

  /** An agenda */
  private Deque<ChartNode> agenda;

  /** The currently used RuleGrammar */
  private RuleGrammar grammar;

  /**
   * Constructs a new object.
   *
   * @param grammarManager
   *            the grammar manager.
   * @param grammarGraph
   *            the graph to analyze.
   */
  public ChartGrammarChecker(final GrammarManager grammarManager) {
    manager = grammarManager;
    resolved = new HashMap<RuleReference, RuleParse>();
  }


  public RuleComponent getResolved(RuleComponent c) {
    if (c instanceof RuleParse) return ((RuleParse) c).getRuleReference();
    return c;
  }

  /**
   * Checks if the given tokens can be represented using the given graph.
   *
   * @param grammar
   *            the grammar to check
   * @param input
   *            the tokens
   * @return <code>true</code> if the tokens are valid.
   * @throws GrammarException
   */
  public ChartNode parse(final RuleGrammar gram, final String[] in)
    throws GrammarException {
    input = in;
    newChart(input.length);
    agenda = new ArrayDeque<ChartNode>();
    grammar = gram;
    final String root = grammar.getRoot();
    final Rule rule = grammar.getRule(root);
    final RuleComponent component = rule.getRuleComponent();
    final ChartNode init = new ChartNode(0, component, null);
    addP(init);

    // What's wrong here: parent structure is a failure, or at least
    // a misnomer. Currently, i let it point to the predecessor active
    // item for the "complete"
    while (! agenda.isEmpty()) {
      ChartNode current = agenda.pop();
      List<ChartNode> expanded = new ArrayList<ChartNode>();
      if (current.dot >= 0) {
        // active item: add predictions
        predict(grammar, current);
        // Complete active item to the right
        if (null != chartOut[current.end]) {
          for (ChartNode pass : chartOut[current.end]) {
            // all passive items this item looks for are to be completed
            if (pass.dot < 0
                && current.rule.looksFor(getResolved(pass.rule), current.dot)) {
              expanded.add(new ChartNode(current.start, pass.end, current.rule,
                  current, current.rule.nextSlot(current.dot)));
            }
          }
        }
      } else {
        // complete passive to the left
        for (ChartNode act : chartIn[current.start]) {
          // all active items that look for this passive item are to be completed
          if (act.dot >= 0
              && act.rule.looksFor(getResolved(current.rule), act.dot)) {
            expanded.add(new ChartNode(act.start, current.end, act.rule,
                act, act.rule.nextSlot(act.dot)));
          }
        }
      }
      for (ChartNode c : expanded) {
        addC(c);
      }
    }
    if (null == chartIn[input.length]) return null;
    for (ChartNode c : chartIn[input.length]) {
      if (c.start == 0 && c.rule == component) {
        // a valid root item
        return c;
      }
    }
    return null;
  }

  private void predict(RuleGrammar grammar, ChartNode current)
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
      throw new RuntimeException("WTF?");
    } else if (component instanceof RuleParse) {
      final RuleParse parse = (RuleParse) component;
      predict(grammar, parse, current);
    } else if (component instanceof RuleToken) {
      final RuleToken token = (RuleToken) component;
      predict(grammar, token, current);
    } else if (component instanceof RuleTag) {
      final RuleTag tag = (RuleTag) component;
      predict(grammar, tag, current);
    }
  }


  @SuppressWarnings("unchecked")
  private void newChart(int tokens) {
    chartIn = new ArrayList[tokens + 1];
    chartOut = new ArrayList[tokens + 1];
  }

  private void addToChart(ChartNode c) {
    List<ChartNode> in = chartIn[c.end];
    if (null == in) {
      in = new ArrayList<ChartNode>();
      chartIn[c.end] = in;
    }
    List<ChartNode> out = chartOut[c.start];
    if (null == out) {
      out = new ArrayList<ChartNode>();
      chartOut[c.start] = out;
    }
    in.add(c);
    out.add(c);
  }

  private void add(ChartNode c, String s) {
    if (null != chartOut[c.start])
      for (ChartNode x: chartOut[c.start])
        if (x.equals(c)) {
          x.parent.addAll(c.parent);
          return;
        }

    System.out.println(s+":"+c);

    addToChart(c);
    agenda.add(c);
  }

  private void addP(ChartNode c) { add(c, "P"); }
  private void addC(ChartNode c) { add(c, "C"); }

  private void predict(final RuleGrammar grammar,
      final RuleParse reference,
      final ChartNode current) throws GrammarException {
    // TODO need to set the new grammar if it changed
    final RuleComponent component = reference.getParse();
    // predict RHS of new nonterminal: new prediction
    addP(new ChartNode(current.end, component, current));
  }

  private void predict(final RuleGrammar grammar,
      final RuleAlternatives alternatives,
      final ChartNode current) throws GrammarException {
    final RuleComponent[] components = alternatives.getRuleComponents();
    // one new prediction for all alternatives: an implicit NT
    if (current.dot == 0) {
      for (int dot = 1; dot < components.length; ++dot) {
        // add predictions for the other alternatives, and one for the
        // embedded node
        addP(new ChartNode(current.end, current.end, alternatives,
            current.parent, dot));
      }
    }
    addP(new ChartNode(current.end, components[current.dot], current));
  }

  private void predict(final RuleGrammar grammar,
      final RuleSequence sequence,
      final ChartNode current) throws GrammarException {
    final RuleComponent[] components = sequence.getRuleComponents();
    addP(new ChartNode(current.end, components[current.dot], current));
  }


  private void predict(final RuleGrammar grammar, final RuleCount count,
      final ChartNode current) throws GrammarException {
    final RuleComponent component = count.getRuleComponent();
    final int min = count.getRepeatMin();
    int max = count.getRepeatMax();
    final int pos = current.end;
    if (max > input.length - pos) {
      max = input.length - pos;
    }
    int repeat = current.dot;
    if (repeat < max) {
      // predict sub-component
      addP(new ChartNode(current.end, component, current));
    }
    if (repeat >= min) {
      // add passive item
      addP(new ChartNode(current.start, current.end, count, current.parent, -1));
    }
  }


  /** This is rather a scan than predict. It directly creates a passive item, if
   *  possible.
   * @param grammar
   * @param token
   * @param current
   * @throws GrammarException
   */
  private void predict(final RuleGrammar grammar, final RuleToken token,
      final ChartNode current) throws GrammarException {
    int pos = current.start;
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
    // now, for the first time, we add a complete token
    addP(new ChartNode(current.start, pos, token, current, -1));
  }

  /** This is rather a scan than predict. It directly creates a passive item, if
   *  possible.
   * @param grammar
   * @param token
   * @param current
   * @throws GrammarException
   */
  private void predict(final RuleGrammar grammar, final RuleTag tag,
      final ChartNode current) throws GrammarException {
    // add complete epsilon item
    addP(new ChartNode(current.start, current.end, tag, current, -1));
  }


  /**
   * Retrieves the result of the grammar check process. This may differ from
   * the parsed tokens of the original utterance.
   * <p>
   * A call to this method is only valid after a call to
   * {@link #isValid(String[])} until the next validation check.
   * </p>
   * <p>
   * <b>NOTE:</b> This is just a first attempt to go into the direction of
   * semantic interpretation and may change.
   * </p>
   *
   * @return interpretation result
   *
  public String[] getInterpretation() {
    Collection<String> result = new java.util.ArrayList<String>();
    for (GrammarNode node : matchedTokens) {
      final GrammarNodeType type = node.getType();

      if (type == GrammarNodeType.TOKEN) {
        final TokenGrammarNode tokenNode = (TokenGrammarNode) node;

        Collection<GrammarNode> nextNodes = tokenNode.getNextNodes();
        for (int i = 0; i < nextNodes.size(); i++) {
          GrammarNode nextNode = (GrammarNode) nextNodes.toArray()[i];

          if (nextNode.getType() == GrammarNodeType.TAG) {
            final TagGrammarNode tagNode = (TagGrammarNode) nextNode;
            final String tag = tagNode.getTag();
            result.add(tag);
          }

          if (nextNode.getType() == GrammarNodeType.SEQUENCE_END) {
            nextNode = (GrammarNode) nextNode.getNextNodes()
              .toArray()[0];
            if (nextNode.getType() == GrammarNodeType.TAG) {
              final TagGrammarNode tagNode = (TagGrammarNode) nextNode;
              final String tag = tagNode.getTag();
              result.add(tag);
            }
          }
        }
      }
    }

    String[] finalResult = new String[result.size()];
    for (int i = 0; i < result.size(); i++) {
      finalResult[i] = (String) result.toArray()[i];
    }

    return finalResult;

  }

  /**
   * Prints out information about a node, including the type of node and tag
   * or token information, if available.
   *
   * @param node
   *            The node to be described.
   */
  private void printNode(final GrammarNode node) {
    String typeString = "UNDEFINED";
    String additionalString = "";
    final GrammarNodeType currentType = node.getType();

    if (currentType == GrammarNodeType.START) {
      typeString = "START";
    } else if (currentType == GrammarNodeType.ALTERNATIVE_START) {
      typeString = "ALTERNATIVE_START";
    } else if (currentType == GrammarNodeType.ALTERNATIVE_END) {
      typeString = "ALTERNATIVE_END";
    } else if (currentType == GrammarNodeType.SEQUENCE_START) {
      typeString = "SEQUENCE_START";
    } else if (currentType == GrammarNodeType.SEQUENCE_END) {
      typeString = "SEQUENCE_END";
    } else if (currentType == GrammarNodeType.TOKEN) {
      typeString = "TOKEN";
      TokenGrammarNode tokenNode = (TokenGrammarNode) node;
      additionalString = "token body = '" + tokenNode.getToken() + "'";
    } else if (currentType == GrammarNodeType.TAG) {
      typeString = "TAG";
      TagGrammarNode tagNode = (TagGrammarNode) node;
      additionalString = "tag body = '" + tagNode.getTag() + "'";
    } else if (currentType == GrammarNodeType.GRAPH) {
      typeString = "GRAPH";
    } else if (currentType == GrammarNodeType.RULE) {
      typeString = "RULE";
      RuleNode ruleNode = (RuleNode) node;
      additionalString = " ID = " + ruleNode.getId();
    }

    LOGGER.debug("Node Type:" + typeString + " min repetitions: "
                 + node.getMinRepeat() + ", max repetitions: "
                 + node.getMaxRepeat() + " " + additionalString);
  }
}
