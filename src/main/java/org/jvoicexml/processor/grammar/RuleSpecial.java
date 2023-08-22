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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jvoicexml.processor.GrammarManager;

//Comp. 2.0.6

public class RuleSpecial extends RuleComponent {
  public static RuleSpecial GARBAGE = new RuleSpecial("GARBAGE");
  private static RuleToken GARBTOK = new RuleToken("$$.*");
  private static RuleComponent GARBRULE = new RuleCount(GARBTOK, 1);

  public static RuleSpecial NULL = new RuleSpecial("NULL");

  public static RuleSpecial VOID = new RuleSpecial("VOID");

  private String special;

  private RuleSpecial(String special) {
    this.special = special;
    leftCorner = new HashSet<>();
    if (this == GARBAGE) {
      leftCorner.add(GARBRULE);
      leftCorner.add(GARBTOK);
    } else {
      leftCorner.add(this);
    }
  }

  @Override
  public String toStringXML() {
    return "<ruleref special=\"" + special + "\"/>";
  }

  @Override
  public String toStringABNF() {
    return "$" + special;
  }

  @Override
  void assignName(String myName) {
    name = myName + "_" + toStringABNF();
  }

  @Override
  public boolean equals(Object obj) {
    Boolean b = eq(obj);
    if (b != null)
      return b;
    return special.equals(((RuleSpecial) obj).special);
  }

  @Override
  public int hashCode() {
    return special.hashCode() + 23;
  }

  @Override
  RuleComponent cleanup(Map<RuleToken, RuleToken> terminals,
      Map<RuleComponent, RuleComponent> nonterminals) {
    if (this == GARBAGE) {
      if (nonterminals.containsKey(this))
        return nonterminals.get(this);
      if (! terminals.containsKey(GARBTOK)) {
        terminals.put(GARBTOK, GARBTOK);
      } else {
        leftCorner.remove(GARBTOK);
        leftCorner.remove(GARBRULE);
        GARBTOK = terminals.get(GARBTOK);
        GARBRULE = new RuleCount(GARBTOK, 1);
        nonterminals.put(GARBRULE, GARBRULE);
        nonterminals.put(this, GARBRULE);
        leftCorner.add(GARBTOK);
        leftCorner.add(GARBRULE);
      }
      return GARBRULE;
    }
    return this;
  }

  @Override
  protected Set<RuleComponent> computeLeftCorner(GrammarManager mgr){
    return leftCorner;
  }

  public double weight() {
    return (this == GARBAGE) ? 1.0 : super.weight();
  }
}
