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

import java.util.ArrayList;
import java.util.List;

import org.jvoicexml.processor.grammar.Grammar;
import org.jvoicexml.processor.grammar.RuleComponent;
import org.jvoicexml.processor.grammar.RuleCount;
import org.jvoicexml.processor.grammar.RuleSpecial;
import org.jvoicexml.processor.grammar.RuleTag;
import org.jvoicexml.processor.srgs.GrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a means to perform evaluations on a parsed grammar.
 * It is an implementation of an Left Corner style parser.
 *
 * @author Bernd Kiefer
 * @version $Revision$
 * @since 0.7
 */
public class LeftCornerParser extends AbstractParser {
  private static final Logger log = LoggerFactory.getLogger(LeftCornerParser.class);

  /** Constructs a new GrammarChecker.
   *
   * @param grammarManager the grammar manager.
   */
  protected LeftCornerParser() {
    log.info("Initializing Left Corner Parser");
  }

  /** Complete for the main loop */
  private void complete(ChartNode act, ChartNode pass, List<ChartNode> expd) {
    //log.debug("   + CO {} ", pass);
    if (act.rule.looksForLC(pass.rule, act.dot)) {
      ChartNode newNode = new ChartNode(act, pass);
      expd.add(newNode);
      //log.debug("succeeds");
    } else {
      //log.debug("fails");
    }
  }

  /** Complete for the left corner predict */
  private void complete(RuleComponent act, ChartNode pass, List<ChartNode> expd) {
    //log.debug("LC {} + {} ", act, pass);
    if (act.looksForLC(pass.rule, 0)) {
      ChartNode newNode = new ChartNode(act, pass);
      expd.add(newNode);
      //log.debug("succeeds");
    } else {
      //log.debug("fails");
    }
  }

  /** Predict all rules where the LHS is in left corner relation with the
   *  next wanted RuleComponent AND the rule can eat the passive item immediately
   *  (i.e. it's the first (non)terminal on the RHS)
   * @param act
   * @param pass
   * @param expd
   * @throws GrammarException
   */
  private void leftCornerPredict(ChartNode act, ChartNode pass,
      List<ChartNode> expd) {
    // We need the left corner relation of act.rule[dot] here
    for (RuleComponent r : act.rule.getLeftCorner(act.dot)) {
      complete(r, pass, expd);
    }
  }

  /** Predict all rules where the LHS is in left corner relation with the
   *  next wanted RuleComponent AND the rule can eat the passive item immediately
   *  (i.e. it's the first (non)terminal on the RHS)
   * @param act
   * @param pass
   * @param expd
   * @throws GrammarException
   */
  private void leftCornerCompleteEmpty(ChartNode act, List<ChartNode> expd) {
    // We need the left corner relation of act.rule[dot] here
    for (RuleComponent r : act.rule.getLeftCorner(act.dot)) {
      // special treatment of all "empty" items: RuleCount that represents an
      // optional item i.e., where repeatMin == 0, RuleTag and NULL
      if ((r instanceof RuleCount && ((RuleCount)r).getRepeatMin() == 0)
          || (r instanceof RuleTag)
          || (r == RuleSpecial.NULL)
          || (r == RuleSpecial.GARBAGE)) {
        //log.debug("EMPTY {} {}", act, r);
        ChartNode newNode = new ChartNode(act.end, r);
        expd.add(newNode);
      }
    }
  }

  private boolean isPassive(ChartNode c) {
    Boolean b = c.rule.isPassive(c.dot);
    return (b != null) ? b : c.isPassive();
  }

  private boolean isActive(ChartNode c) {
    Boolean b = c.rule.isActive(c.dot);
    return (b != null) ? b : ! c.isPassive();
  }

  @Override
  protected boolean addToChart(ChartNode c) {
    boolean toAddP = isPassive(c) && checkEquiv(getEdges(chartOut, c.start), c);
    boolean toAddA = isActive(c) && checkEquiv(getEdges(chartIn, c.end), c);
    return (toAddP || toAddA);
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
      //log.debug("Popped {}", curr);
      List<ChartNode> expanded = new ArrayList<ChartNode>();
      if (isPassive(curr)) {
        if (chartIn[curr.start] != null) {
          // Complete passive item to the left
          for (ChartNode act : chartIn[curr.start]) {
            // complete all active items that look for this passive item
            complete(act, curr, expanded);
            // do all left corner predictions
            leftCornerPredict(act, curr, expanded);
          }
        }
      }
      if (isActive(curr)) {
        //log.debug("Act: {}", curr);
        // Complete active item to the right
        if (null != chartOut[curr.end]) {
          for (ChartNode pass : chartOut[curr.end]) {
            // complete with all passive items this item looks for
            complete(curr, pass, expanded);
            // do all left corner predictions
            leftCornerPredict(curr, pass, expanded);
          }
        }
        leftCornerCompleteEmpty(curr, expanded);
      }
      for (ChartNode c : expanded) {
        add(c);
      }
    }
    return returnFirstResult();
  }
}
