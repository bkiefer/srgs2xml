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

//Comp 2.0.6

public class RuleAlternative extends RuleComponent {
    public static final int MAX_WEIGHT = 0x7fffffff;

    public static final int NORM_WEIGHT = 0x3E8;

    public static final int MIN_WEIGHT = 0x0;

    private RuleComponent component;

    private double weight;

    public RuleAlternative(RuleComponent ruleComponent) {
        this(ruleComponent, NORM_WEIGHT);
    }

    public RuleAlternative(RuleComponent ruleComponent, double weight) {
      this.weight = weight;
      this.component = ruleComponent;
    }

    public RuleAlternative(String token) {
      component = new RuleToken(token);
    }

    public RuleComponent getRuleComponent() {
      return component;
    }

    public double getWeight() {
      return weight;
    }

  public String toString() {
    final StringBuffer str = new StringBuffer();
    str.append("<item");
    if (weight != NORM_WEIGHT) {
      // TODO we should divide by NORM_WEIGHT but this is not
      // supported in CLDC 1.0
      str.append(" weight=\"");
      str.append(Double.toString(weight));
      str.append("\"");
    }
    str.append('>');
    if (component == null) {
      str.append(RuleSpecial.NULL.toString());
    } else {
      str.append(component.toString());
    }
    str.append("</item>");
    return str.toString();
  }

  @Override
  public boolean looksFor(RuleComponent r, int i) {
    // r must be equal to the ith alternative. Because we're using the
    // RuleComponents as immutable objects from the grammar, it should suffice
    // to test for token identity
    return component == r;
  }
}
