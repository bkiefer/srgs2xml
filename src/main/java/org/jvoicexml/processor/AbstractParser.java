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
package org.jvoicexml.processor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.jvoicexml.processor.grammar.Grammar;
import org.jvoicexml.processor.grammar.JVoiceXmlGrammar;
import org.jvoicexml.processor.grammar.Rule;
import org.jvoicexml.processor.grammar.RuleComponent;
import org.jvoicexml.processor.grammar.RuleToken;
import org.jvoicexml.processor.srgs.GrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a means to perform evaluations on a parsed grammar.
 *
 * @author Bernd Kiefer
 * @version $Revision$
 * @since 0.7
 */
public abstract class AbstractParser {
  private static final Logger log = LoggerFactory.getLogger(AbstractParser.class);

  protected static int gen = 0;

  public static boolean useLeftCorner = true;

  public static AbstractParser getParser() {
    return useLeftCorner ? new LeftCornerParser() : new ChartGrammarChecker();
  }

  /** An agenda */
  private final Deque<ChartNode> agenda;

  /** The current input */
  protected String[] input;

  /** A chart */
  protected List<ChartNode>[] chartIn, chartOut;

  /** The currently used Grammar */
  protected Grammar grammar;

  /** Constructs a new GrammarChecker.
   *
   * @param grammarManager the grammar manager.
   */
  protected AbstractParser() {
    agenda = new ArrayDeque<ChartNode>();
  }

  public Stream<ChartNode> returnAllResults() {
    List<ChartNode> fromZero = getOutEdges(0);
    if (null == fromZero) {
      return Stream.empty();
    }
    final Rule rule = grammar.getRule(grammar.getRoot());
    RuleComponent compo = rule.getRuleComponent();
    final RuleComponent component = compo;
    return fromZero
        .stream()
        .filter(c -> c.end == input.length && c.rule == component);
  }

  public ChartNode returnFirstResult() {
    return returnAllResults().findFirst().orElse(null);
  }

  protected final boolean agendaNotEmpty() {
    return ! agenda.isEmpty();
  }

  protected final void addToAgenda(ChartNode c) {
    agenda.add(c);
  }

  protected final ChartNode agendaPop() {
    return agenda.pop();
  }


  @SuppressWarnings("unchecked")
  protected final RuleComponent initParse(Grammar gram, String[] in)
      throws GrammarException {
    agenda.clear();
    input = in;
    chartIn = new ArrayList[in.length + 1];
    chartOut = new ArrayList[in.length + 1];
    grammar = gram;
    final String root = grammar.getRoot();
    final Rule rule = grammar.getRule(root);
    if (rule == null) {
      // TODO: SHOULD NEVER HAPPEN, CAUGHT WHEN READING GRAMMAR!
      throw new GrammarException("Undefined rule referenced: " + root);
    }
    return rule.getRuleComponent();
  }

  /** Check (possibly parser specific) conditions for adding a chart node to
   *  the agenda
   *
   * @param c the chart node in question
   * @return true if the chart node should be added to the agenda, false
   * otherwise
   */
  protected abstract boolean addToChart(ChartNode c);

  /** Add a chart node, checking specific preconditions beforehand */
  protected void add(ChartNode c) {
    if (addToChart(c)) {
      addToAgenda(c);
    }
  };

  /**
   * Checks if the given tokens can be represented using the given graph.
   *
   * @param grammar the grammar to check
   * @param input the tokens
   * @return <code>true</code> if the tokens are valid.
   * @throws GrammarException
   */
  public abstract ChartNode parse(final Grammar gram, final String[] in)
      throws GrammarException;

  /** Return the input string covered by the chart node n */
  public String covered (ChartNode n) {
    StringBuilder sb = new StringBuilder();
    for(int i = n.start; i < n.end; ++i) {
      sb.append(input[i]).append(' ');
    }
    if (n.start != n.end)
      sb.deleteCharAt(sb.length()-1); // delete trailing space
    return sb.toString();
  }

  /** Access with lazy initialization of chart heads */
  protected final List<ChartNode> getEdges(List<ChartNode>[] heads, int pos) {
    List<ChartNode> out = heads[pos];
    if (null == out) {
      out = new ArrayList<ChartNode>();
      heads[pos] = out;
    }
    return out;
  }

  /** Check if there exists an equivalent chart node already and return true
   *  if it's not been already in the chart, otherwise add it to the equivs
   *  field of the representative edge
   * @param edges the edge list to add the chart edge to (potentially)
   * @param c the new chart edge
   * @return true if this is a truely new edge, false otherwise
   */
  protected final boolean checkEquiv(List<ChartNode> edges, ChartNode c) {
    for (ChartNode x : edges) {
      if (x.equals(c)) {
        if (x.equalsChildren(c)) {
          log.debug("Identical chart node produced: {}", c);
        } else {
          if (null == x.equivs) {
            x.equivs = new ArrayList<ChartNode>();
          }
          x.equivs.add(c);
        }
        return false;
      }
    }
    edges.add(c);
    return true;
  }


  protected final void addPrediction(int pos, RuleComponent r) {
    add(new ChartNode(pos, r));
  }


  /**
   * This adds all rule tokens that are compatible with the input at some input
   * position.
   * It immediately creates a passive item, if possible.
   *
   * @param start the start position of the input token
   */
  private final void addPreterminals(int start) {
    JVoiceXmlGrammar g = ((JVoiceXmlGrammar)grammar);
    if (start >= input.length) {
      return;
    }
    g.getPreterminals(input, start,
        (RuleComponent r, Integer end) -> add(new ChartNode(start, end, r, -1)));

    for (RuleToken token : g.getPatternTerminals()) {
      Pattern p = token.getPattern();
      final String currentInput = input[start];
      if (p.matcher(currentInput).matches()) {
        // now we add a complete token
        add(new ChartNode(start, start + 1, token, -1));
      }
    }
  }


  /** Apply all rules that work on input tokens
   *
   *  TODO: Possible target for adding "lexicon" functionality
   */
  protected void addPreterminals() {
    // Add all token nodes for that are applicable to the input tokens
    for (int start = 0; start < input.length; ++ start) {
      addPreterminals(start);
    }
  }

  /********************** For displaying the chart **********************/

  public int chartSize() {
    return chartOut.length;
  }

  public List<ChartNode> getOutEdges(int i) {
    return chartOut[i];
  }

  public List<ChartNode> getInEdges(int i) {
    return chartIn[i];
  }

  public String tokenAt(int i) {
    return i < input.length ? input[i] : null;
  }
}
