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
import java.util.regex.Pattern;

import org.jvoicexml.processor.GrammarManager;

//Comp. 2.0.6

public class RuleToken extends RuleComponent {
  private String text;
  private String[] tokens;
  private Pattern p;

  public RuleToken(String text) {
    this(text, null);
  }

  public RuleToken(String text, String language)
      throws IllegalArgumentException {
    if ((text == null) || (text.length() == 0)) {
      throw new IllegalArgumentException(
          "'" + text + "'" + " is not a valid grammar text");
    }
    // BK: extension to match arbitrary regex as token
    if (text.charAt(0) == '"' && text.charAt(text.length() - 1) == '"') {
      text = text.substring(1, text.length() - 1);
    } else {
      if (!text.startsWith("$$")) {
        text = text.trim().replaceAll("  +", " ");
      }
    }
    this.text = text;
    // BK: extension to match arbitrary regex as token
    if (text.startsWith("$$")) {
      this.p = Pattern.compile(text.substring(2));
    } else {
      this.tokens = text.split(" ");
    }
    this.lang = language;
  }

  public Pattern getPattern() {
    return p;
  }

  public String[] getTokens() {
    return tokens;
  }

  public String getText() {
    return text;
  }

  @Override
  public String getLanguage() {
    return lang;
  }

  @Override
  void assignName(String myName) {
    name = myName + "_" + toStringABNF();
  }

  private void addText(StringBuffer str) {
    if (tokens != null && tokens.length > 1) {
      str.append('"').append(text).append('"');
    } else {
      str.append(text);//tokens[0]);
    }
  }

  @Override
  public String toStringXML() {
    StringBuffer str = new StringBuffer();
    str.append("<item");
    appendLangXML(str);
    str.append('>');
    addText(str);
    str.append("</item>");
    return str.toString();
  }

  @Override
  public String toStringABNF() {
    StringBuffer str = new StringBuffer();
    addText(str);
    appendLangABNF(str);
    return str.toString();
  }

  @Override
  public int hashCode() {
    return 31 + ((text == null) ? 0 : text.hashCode());
  }

  @Override
  public boolean equals(Object obj) {
    Boolean b = eq(obj);
    if (b != null)
      return b;
    RuleToken other = (RuleToken) obj;
    if (text == null) {
      return (other.text == null);
    }
    return text.equals(other.text);
  }

  @Override
  RuleComponent cleanup(Map<RuleToken, RuleToken> terminals,
      Map<RuleComponent, RuleComponent> nonterminals) {
    RuleToken term = terminals.get(this);
    if (term == null) {
      term = this;
      terminals.put(term, term);
    }
    return term;
  }

  @Override
  protected Set<RuleComponent> computeLeftCorner(GrammarManager mgr) {
    if (leftCorner == null) {
      leftCorner = new HashSet<>();
      leftCorner.add(this);
    }
    return leftCorner;
  }

  /** Maybe we should reject this as token text and enforce GARBAGE instead */
  @Override
  public double weight() {
    return (getText().equals("$$.*")) ? 1.0 : super.weight();
  }
}
