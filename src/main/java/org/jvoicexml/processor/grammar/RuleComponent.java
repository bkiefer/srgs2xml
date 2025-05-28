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

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

//Comp 2.0.6

public abstract class RuleComponent {
  protected static boolean PRINT_COMPACT = true;
  public static boolean SHORTEN_URLS = true;

  protected String lang;

  protected String name;

  public boolean parenthesized = false;

  public RuleReference ruleRoot = null;

  protected Set<RuleComponent> leftCorner;

  private static Pattern valid = Pattern
      .compile("(\\p{IsAlphabetic}|[_])(\\p{IsAlphabetic}|\\d|[-_.])*");

  /**
   * Checks if the given text is a valid grammar text.
   *
   * @param text the text to check.
   */
  protected static void checkValidGrammarText(String text) {
    if (!valid.asMatchPredicate().test(text))
      throw new IllegalArgumentException(
          "Not a valid text for a grammar: " + text);
  }

  public static void printCompact(boolean val) {
    PRINT_COMPACT = val;
  }

  private static int nsNo = 0;
  private static HashMap<String, String> url2ns = new HashMap<>();

  public static String shortUrl(URI grammarRef) {
    String name = grammarRef.toString();
    if (SHORTEN_URLS) {
      if (!url2ns.containsKey(name)) {
        url2ns.put(name, "ref" + (nsNo++));
      }
      return url2ns.get(name);
    }
    return name;
  }

  static boolean isLetter(char ch) {
    return isUpperCase(ch) || isLowerCase(ch) || (ch >= '\u00c0'
        && ch != '\u00d7' && ch != '\u00f7' && ch != '\u0006');
  }

  static boolean isUpperCase(char ch) {
    return (ch >= 'A') && (ch <= 'Z');
  }

  static boolean isLowerCase(char ch) {
    return (ch >= 'a') && (ch <= 'z');
  }

  static boolean isWhitespace(char ch) {
    switch (ch) {
    case ' ':
    case '\t':
      return true;
    default:
      return false;
    }
  }

  abstract void assignName(String myName);

  protected void appendLangXML(StringBuffer str) {
    if (lang != null)
      str.append(" xml:lang=\"").append(lang).append('"');
  }

  protected void appendLangABNF(StringBuffer str) {
    if (lang != null)
      str.append("!").append(lang);
  }

  public abstract String toStringXML();

  public abstract String toStringABNF();

  public static String toStringXML(RuleComponent c) {
    return (c == null) ? RuleSpecial.NULL.toStringXML() : c.toStringXML();
  }

  public static String toStringABNF(RuleComponent c) {
    return (c == null) ? RuleSpecial.NULL.toStringABNF() : c.toStringABNF();
  }

  @Override
  public String toString() {
    return PRINT_COMPACT ? toStringABNF() : toStringXML();
  }

  public String getName() {
    return name;
  }

  /**
   * Test, for every subclass, if the given RuleComponent is the one required in
   * the dot'th "position", which means in the case of RuleAlternative, for
   * example, that it must be equal to the i'th alternative. Because we're using
   * the RuleComponents as immutable objects from the grammar, it should suffice
   * to test for token identity.
   */
  public boolean looksFor(RuleComponent r, int dot) {
    return false;
  }

  /**
   * Test, for every subclass, if the given RuleComponent is the one required in
   * the dot'th "position", which means in the case of RuleAlternative, for
   * example, that it must be equal to the i'th alternative. Because we're using
   * the RuleComponents as immutable objects from the grammar, it should suffice
   * to test for token identity.
   */
  public boolean looksForLC(RuleComponent r, int dot) {
    return looksFor(r, dot);
  }

  /** For counts and alternatives, the dot has a special meaning. To account for
   *  that, we need these special tests for some subclasses
   */
  public Boolean isPassive(int dot) {
    return null;
  }

  /** For counts and alternatives, the dot has a special meaning. To account for
   *  that, we need these special tests for some subclasses
   */
  public Boolean isActive(int dot) {
    return null;
  }

  /**
   * Test if this is the last slot to be filled, i.e., if the dot advances one
   * more position, the item will be passive.
   */
  public int nextSlot(int dot) {
    return -1;
  }

  public void setLanguage(String s) {
    lang = s;
  }

  public String getLanguage() {
    return lang;
  }

  protected boolean equ(RuleComponent r) {
    return this == r; // works because of cleanup: no equals necessary
  }

  protected Boolean eq(Object obj) {
    if (this == obj)
      return true;
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    if (ruleRoot != ((RuleComponent)obj).ruleRoot) {
      return false;
    }
    return null;
  }

  /** Make sure that after grammar loading exactly one instance of every
   * terminal and nonterminal remains in the grammar to avoid artificial
   * spurious ambiguities during parsing.
   *
   * @param terminals
   * @param nonterminals
   * @return
   */
  abstract RuleComponent cleanup(Map<RuleToken, RuleToken> terminals,
      Map<RuleComponent, RuleComponent> nonterminals);

  protected abstract Set<RuleComponent> computeLeftCorner();

  public Set<RuleComponent> getLeftCorner() {
    return leftCorner;
  }

  public Set<RuleComponent> getLeftCorner(int i) {
    return leftCorner;
  }

  public double weight() {
    return 0.00000000001;
  }
}
