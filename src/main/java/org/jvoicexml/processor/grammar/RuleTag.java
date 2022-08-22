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

/**
 * This class represents the semantic annotations of the grammar. The exact
 * format is not specified, the current library only supports JavaScript like
 * semantic annotations with special restrictions.
 *
 * @author kiefer
 *
 */
public class RuleTag extends RuleComponent {
  private Object tag;

  public RuleTag(Object tag) {
    this.tag = tag;
  }

  public Object getTag() {
    return tag;
  }

  @Override
  void assignName(String myName) {
    name = myName + "_{}";
  }

  @Override
  public String toStringXML() {
    if (tag == null) {
      throw new IllegalArgumentException("null can not be represented in XML");
    }
    final StringBuffer str = new StringBuffer();
    str.append("<tag>");
    str.append(tag);
    str.append("</tag>");

    return str.toString();
  }

  @Override
  public String toStringABNF() {
    if (tag == null)
      return "";

    final StringBuffer str = new StringBuffer();
    str.append("{");
    str.append(tag);
    str.append("}");

    return str.toString();
  }

  @Override
  public boolean equals(Object obj) {
    Boolean b = eq(obj);
    if (b != null)
      return b;
    return tag.equals(((RuleTag) obj).tag);
  }

  @Override
  public int hashCode() {
    return tag.hashCode();
  }

  @Override
  RuleComponent cleanup(Map<RuleToken, RuleToken> terminals,
      Map<RuleComponent, RuleComponent> nonterminals) {
    RuleComponent nonterm = nonterminals.get(this);
    if (nonterm == null) {
      nonterm = this;
      nonterminals.put(nonterm, nonterm);
    }
    return nonterm;
  }

  @Override
  protected Set<RuleComponent> computeLeftCorner(GrammarManager mgr) {
   if (leftCorner != null) return leftCorner;
   leftCorner = new HashSet<>();
   leftCorner.add(this);
   return leftCorner;
  }
}
