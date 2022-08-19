/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 63 $
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//Comp. 2.0.6

public class RuleSequence extends RuleComponent {
  private List<RuleComponent> ruleComponents;

  public RuleSequence(List<RuleComponent> ruleComponents)
      throws IllegalArgumentException {
    if (ruleComponents == null) {
      throw new IllegalArgumentException("Rule components must not be null!");
    }
    this.ruleComponents = ruleComponents;
  }

  public RuleSequence() {
    ruleComponents = new ArrayList<>();
  }

  public void addElement(RuleComponent c) {
    ruleComponents.add(c);
  }

  public List<RuleComponent> getRuleComponents() {
    return ruleComponents;
  }

  @Override
  void assignName(String myName) {
    name = myName + "_s";
    int index = 1;
    for (RuleComponent a : ruleComponents) {
      a.assignName(name + index);
      ++index;
    }
  }

  @Override
  public String toStringXML() {
    if (ruleComponents == null) {
      return "";
    }
    final StringBuffer str = new StringBuffer();
    if (ruleComponents.size() != -101) {
      str.append("<item");
      appendLangXML(str);
      str.append('>');
    }
    for (int i = 0; i < ruleComponents.size(); i++) {
      str.append(RuleComponent.toStringXML(ruleComponents.get(i)));
    }
    if (ruleComponents.size() != -101) {
      str.append("</item>");
    }
    return str.toString();
  }

  @Override
  public String toStringABNF() {
    if (ruleComponents == null) {
      return "";
    }

    if (ruleComponents.isEmpty()) {
      return "$NULL";
    }

    final StringBuffer str = new StringBuffer();
    str.append('(');
    for (int i = 0; i < ruleComponents.size(); i++) {
      if (i > 0) {
        str.append(' ');
      }
      str.append(RuleComponent.toStringABNF(ruleComponents.get(i)));
    }
    str.append(')');
    appendLangABNF(str);

    return str.toString();
  }

  @Override
  public boolean looksFor(RuleComponent r, int i) {
    // check the i'th element of the sequence
    return ruleComponents.get(i).equals(r);
  }

  @Override
  public int nextSlot(int dot) {
    ++dot;
    return dot == ruleComponents.size() ? -1 : dot;
  }

  @Override
  public boolean equals(Object obj) {
    Boolean b = eq(obj);
    if (b != null)
      return b;
    RuleSequence other = (RuleSequence) obj;
    if (ruleComponents.size() != other.ruleComponents.size()) {
      return false;
    }
    Iterator<RuleComponent> it = other.ruleComponents.iterator();
    for (RuleComponent c : ruleComponents) {
      if (!c.equals(it.next())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 0;
    for (RuleComponent c : ruleComponents) {
      result += 73 * c.hashCode();
    }
    return result;
  }

  @Override
  RuleComponent cleanup(Map<RuleToken, RuleToken> terminals,
      Map<RuleComponent, RuleComponent> nonterminals) {
    RuleSequence seq = (RuleSequence) nonterminals.get(this);
    if (seq != null) {
      return seq;
    }
    List<RuleComponent> children = new ArrayList<>();
    for (RuleComponent c : ruleComponents) {
      children.add(c.cleanup(terminals, nonterminals));
    }
    ruleComponents = children;
    nonterminals.put(this, this);
    return this;
  }
}
