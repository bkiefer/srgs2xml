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

package org.jvoicexml.processor.srgs.grammar;

import java.util.ArrayList;
import java.util.List;

//Comp 2.0.6

public class RuleAlternatives extends RuleComponent {

  public static final double MAX_WEIGHT = 0x7fffffff;

  public static final double NORM_WEIGHT = 0x3E8;

  public static final double MIN_WEIGHT = 0x0;

  private static class RuleAlternative {
    public RuleComponent component;

    public double weight;

    public RuleAlternative(RuleComponent ruleComponent, double weight) {
      this.weight = weight;
      this.component = ruleComponent;
    }

    public RuleAlternative(String token) { component = new RuleToken(token); }
  }

  private List<RuleAlternative> ruleComponents;

  public RuleAlternatives() {
    ruleComponents = new ArrayList<>();
  }

  public RuleAlternatives(List<RuleComponent> ruleComponents)
      throws IllegalArgumentException {
    if (ruleComponents == null) {
      throw new IllegalArgumentException("Rule components must not be null!");
    }
    this.ruleComponents = new ArrayList<>(ruleComponents.size());
    for (RuleComponent c : ruleComponents) {
      this.ruleComponents.add(new RuleAlternative(c, NORM_WEIGHT));
    }
  }

  public RuleAlternatives(List<RuleComponent> ruleComponents, double[] weights)
      throws IllegalArgumentException {
    if (ruleComponents == null) {
      throw new IllegalArgumentException("Rule components must not be null!");
    }
    this.ruleComponents = new ArrayList<>(ruleComponents.size());
    if (weights == null || ruleComponents.size() != weights.length) {
      for (RuleComponent c : ruleComponents) {
        this.ruleComponents.add(new RuleAlternative(c, NORM_WEIGHT));
      }
    } else {
      int i = 0;
      for (RuleComponent c : ruleComponents) {
        this.ruleComponents.add(new RuleAlternative(c, weights[i++]));
      }
    }
  }

  public RuleAlternatives(String[] tokens) {
    if (tokens != null) {
      ruleComponents = new ArrayList<>();
      for (int i = 0; i < tokens.length; i++) {
        final String token = tokens[i];
        ruleComponents.add(new RuleAlternative(token));
      }
    }
  }

  public void addAlternative(RuleComponent c) {
    addAlternative(c, NORM_WEIGHT);
  }

  public void addAlternative(RuleComponent c, double weight) {
    ruleComponents.add(new RuleAlternative(c, weight));
  }

  public RuleComponent getAlternative(int i) {
    return ruleComponents.get(i).component;
  }

  /** Number of alternatives */
  public int size() {
    return ruleComponents.size();
  }

  public String toString() {
    if ((ruleComponents == null) || (ruleComponents.size() == 0)) {
      return RuleSpecial.VOID.toString();
    }

    final StringBuffer str = new StringBuffer();
    str.append("<one-of>");
    for (RuleAlternative alt : ruleComponents) {
      str.append("<item");
      if (alt.weight != NORM_WEIGHT) {
        // TODO we should divide by NORM_WEIGHT but this is not
        // supported in CLDC 1.0
        str.append(" weight=\"");
        str.append(Double.toString(alt.weight));
        str.append("\"");
      }
      appendLang(str);
      str.append('>');
      RuleComponent component = alt.component;
      if (component == null) {
        str.append(RuleSpecial.NULL.toString());
      } else {
        str.append(component.toString());
      }
      str.append("</item>");
    }
    str.append("</one-of>");

    return str.toString();
  }

  @Override
  public boolean looksFor(RuleComponent r, int i) {
    // r must be equal to the ith alternative. Because we're using the
    // RuleComponents as immutable objects from the grammar, it's sufficient
    // to test for token identity
    return ruleComponents.get(i).component == r;
  }

}
