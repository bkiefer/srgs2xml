/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 68 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007 JVoiceXML group - http://jvoicexml.sourceforge.net
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

package org.jvoicexml.processor.grammar;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jvoicexml.processor.GrammarManager;

//Comp 2.0.6

public class RuleCount extends RuleComponent {
  public static final int MAX_PROBABILITY = 0x7fffffff;

  public static final int REPEAT_INDEFINITELY = 0x7fffffff;

  private RuleComponent ruleComponent;

  private int repeatMin;

  private int repeatMax;

  private double repeatProbability;

  public RuleCount(RuleComponent ruleComponent, int repeatMin)
      throws IllegalArgumentException {
    if (repeatMin < 0) {
      throw new IllegalArgumentException(
          "Repeat minimum must be greater or equal to 0!");
    }
    this.ruleComponent = ruleComponent;
    this.repeatMax = REPEAT_INDEFINITELY;
    this.repeatMin = repeatMin;
    this.repeatProbability = -1;
  }

  public RuleCount(RuleComponent ruleComponent, int repeatMin, int repeatMax)
      throws IllegalArgumentException {
    if (repeatMin < 0 || (repeatMin > repeatMax)) {
      throw new IllegalArgumentException(
          "Repeat minimum must be greater or equal to 0 and smaller "
              + "than or equal to repeat maximum!");
    }
    this.ruleComponent = ruleComponent;
    this.repeatMin = repeatMin;
    this.repeatMax = repeatMax;
    this.repeatProbability = -1;
  }

  public RuleCount(RuleComponent ruleComponent, int repeatMin, int repeatMax,
      double repeatProbability) throws IllegalArgumentException {
    if (repeatMin < 0 || (repeatMin > repeatMax)) {
      throw new IllegalArgumentException(
          "Repeat minimum must be greater or equal to 0 and smaller "
              + "than or equal to repeat maximum!");
    }

    if (repeatProbability < 0) {
      throw new IllegalArgumentException(
          "Repeat propability must be greater or equal to 0!");
    }
    this.ruleComponent = ruleComponent;
    this.repeatMin = repeatMin;
    this.repeatMax = repeatMax;
    this.repeatProbability = repeatProbability;
  }

  public int getRepeatMax() {
    return repeatMax;
  }

  public int getRepeatMin() {
    return repeatMin;
  }

  public double getRepeatProbability() {
    if (repeatProbability < 0) {
      return REPEAT_INDEFINITELY;
    }

    return repeatProbability;
  }

  public RuleComponent getRuleComponent() {
    return ruleComponent;
  }

  @Override
  void assignName(String myName) {
    name = myName + "_c";
    ruleComponent.assignName(name + "_");
  }

  @Override
  public String toStringXML() {
    StringBuffer str = new StringBuffer();

    str.append("<item repeat=\"");
    str.append(repeatMin);
    if (repeatMin != repeatMax) {
      str.append("-");

      if (repeatMax != REPEAT_INDEFINITELY) {
        str.append(repeatMax);
      }
    }
    str.append("\"");

    if (repeatProbability >= 0) {
      // TODO we should divide by MAX_PROBABILTY but this is not
      // supported in CLDC 1.0
      str.append(" repeat-prob=\"");
      str.append(repeatProbability);
      str.append("\"");
    }

    appendLangXML(str);
    str.append(">");

    // TODO: What to do with null rule components?
    str.append(RuleComponent.toStringXML(ruleComponent));

    str.append("</item>");

    return str.toString();
  }

  @Override
  public String toStringABNF() {
    StringBuffer str = new StringBuffer();

    // nicer if <0-1>/1.0/ is [ ... ]
    if (repeatMin == 0 && repeatMax == 1 && repeatProbability < 0) {
      str.append('[');
      str.append(RuleComponent.toStringABNF(ruleComponent));
      str.append(']');
    } else {
      str.append(RuleComponent.toStringABNF(ruleComponent));

      str.append("<");
      str.append(repeatMin);
      if (repeatMin != repeatMax) {
        str.append("-");
        if (repeatMax != REPEAT_INDEFINITELY) {
          str.append(repeatMax);
        }
      }

      if (repeatProbability >= 0) {
        // TODO we should divide by MAX_PROBABILTY but this is not
        // supported in CLDC 1.0
        str.append("/");
        str.append(repeatProbability);
        str.append("/");
      }
      str.append(">");
    }

    appendLangABNF(str);

    return str.toString();
  }

  @Override
  public boolean looksFor(RuleComponent r, int dot) {
    // dot is the number of repetitions already covered
    return dot < repeatMax && ruleComponent.equals(r);
  }

  /** For counts and alternatives, the dot has a special meaning. To account for
   *  that, we need these special tests for some subclasses
   */
  public Boolean isPassive(int dot) {
    return dot >= repeatMin && dot <= repeatMax;
  }

  /** For counts and alternatives, the dot has a special meaning. To account for
   *  that, we need these special tests for some subclasses
   */
  public Boolean isActive(int dot) {
    return dot < repeatMax;
  }

  /**
   * dot is the number of repetitions already covered
   */
  @Override
  public int nextSlot(int dot) {
    return ++dot;
  }

  @Override
  public boolean equals(Object obj) {
    Boolean b = eq(obj);
    if (b != null)
      return b;
    RuleCount other = (RuleCount) obj;
    return repeatMax == other.repeatMax && repeatMin == other.repeatMin
        && (repeatProbability - other.repeatProbability < 1e-9)
        && ruleComponent.equals(other.ruleComponent);
  }

  @Override
  public int hashCode() {
    return repeatMax + repeatMin + ruleComponent.hashCode();
  }

  @Override
  RuleComponent cleanup(Map<RuleToken, RuleToken> terminals,
      Map<RuleComponent, RuleComponent> nonterminals) {
    RuleCount rc = (RuleCount) nonterminals.get(this);
    if (rc != null) {
      return rc;
    }
    rc = this;
    nonterminals.put(rc, rc);
    rc.ruleComponent = rc.ruleComponent.cleanup(terminals, nonterminals);
    return rc;
  }

  @Override
  protected Set<RuleComponent> computeLeftCorner(GrammarManager mgr) {
    if (leftCorner != null) return leftCorner;
    // TODO WHAT ABOUT EPSILON? (RULEMIN = 0)
    leftCorner = new HashSet<>();
    leftCorner.add(this);
    leftCorner.addAll(ruleComponent.computeLeftCorner(mgr));
    return leftCorner;
  }

  public Set<RuleComponent> getLeftCorner(int i) {
    return ruleComponent.leftCorner;
  }
}
