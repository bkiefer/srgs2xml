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

import static org.jvoicexml.processor.grammar.RuleSpecial.GARBAGE;

import java.util.ArrayList;
import java.util.List;

import org.jvoicexml.processor.grammar.Grammar;
import org.jvoicexml.processor.grammar.RuleAlternatives;
import org.jvoicexml.processor.grammar.RuleComponent;
import org.jvoicexml.processor.grammar.RuleCount;
import org.jvoicexml.processor.grammar.RuleReference;
import org.jvoicexml.processor.grammar.RuleSequence;
import org.jvoicexml.processor.srgs.GrammarException;

/**
 * This class provides a means to perform evaluations on a parsed grammar.
 * It is an implementation of an Earley style parser.
 *
 * @author Bernd Kiefer
 * @version $Revision$
 * @since 0.7
 */
public class ChartGrammarChecker extends AbstractParser {

  private boolean canExpand(ChartNode active, ChartNode passive) {
    return active.rule.looksFor(passive.rule, active.dot);
  }

  /**
   * Checks if the given tokens can be represented using the given graph.
   *
   * @param grammar the grammar to check
   * @param input the tokens
   * @return <code>true</code> if the tokens are valid.
   * @throws GrammarException
   */
  @Override
  public ChartNode parse(final Grammar gram, final String[] in)
      throws GrammarException {
    RuleComponent component = initParse(gram, in);
    addPrediction(0, component);

    addPreterminals();

    while (agendaNotEmpty()) {
      ChartNode curr = agendaPop();
      List<ChartNode> expanded = new ArrayList<ChartNode>();
      if (curr.isPassive()) {
        if (chartIn[curr.start] != null) {
          // complete passive to the left
          for (ChartNode act : chartIn[curr.start]) {
            // all active items that look for this passive item are to be completed
            if (canExpand(act, curr)) {
              expanded.add(new ChartNode(act, curr));
            }
          }
        }
      } else {
        // active item: add predictions
        predict(curr);
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
    return returnFirstResult();
  }

  private void predict(ChartNode current) {
    RuleComponent component = current.rule;

    if (component instanceof RuleSequence) {
      final RuleSequence sequence = (RuleSequence) component;
      predict(sequence, current);
    } else if (component instanceof RuleAlternatives) {
      final RuleAlternatives alternatives = (RuleAlternatives) component;
      predict(alternatives, current);
    } else if (component instanceof RuleCount) {
      final RuleCount count = (RuleCount) component;
      predict(count, current);
    } else if (component instanceof RuleReference) {
      final RuleReference ref = (RuleReference) component;
      //final RuleParse parse = (RuleParse) component;
      predict(ref, current);
    } else if (component == GARBAGE) {
      predictGarbage(current);
    }
  }

  @Override
  protected boolean addToChart(ChartNode c) {
    return c.isPassive()
        ? checkEquiv(getEdges(chartOut, c.start), c)
        : checkEquiv(getEdges(chartIn, c.end), c);
  }


  private void predict(final RuleAlternatives alts, final ChartNode current) {
    // one new prediction per alternative: an implicit nonterminal
    if (current.dot == 0) {
      // the one with dot == zero is responsible to introduce the rest
      for (int dot = 1; dot < alts.size(); ++dot) {
        // add predictions for the other alternatives, and one for the
        // embedded node
        add(new ChartNode(current.end, current.end, alts, dot));
      }
    }
    // every alternative predicts its own sub-component
    addPrediction(current.end, alts.getAlternative(current.dot));
    /*
    // This seems attractive, but it makes the completion of active items
    // much more complicated. I currently don't have a solution, so i'll stick
    // with what works
    for (RuleAlternative alt : alternatives.getRuleAlternatives()) {
      addPrediction(current.end, alt.getRuleComponent());
    }
    */
  }

  private void predict(final RuleSequence sequence, final ChartNode current) {
    final List<RuleComponent> components = sequence.getRuleComponents();
    addPrediction(current.end, components.get(current.dot));
  }

  private void predict(final RuleCount count, final ChartNode current) {
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

  private void predict(final RuleReference ref, final ChartNode current) {
    // predict RHS of new nonterminal
    addPrediction(current.end, ref);
    addPrediction(current.end, ref.getResolved());
  }

  private void predictGarbage(final ChartNode current) {
    final RuleComponent component = GARBAGE.getRuleComponent();
    addPrediction(current.end, component);
    // add passive item: a special case
    ChartNode c = new ChartNode(current.start, current.end, GARBAGE, -1);
    c.children.addAll(current.children);
    add(c);
  }
}
