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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jvoicexml.processor.GrammarManager;
import org.jvoicexml.processor.srgs.GrammarException;

//Comp. 2.0.6

public class RuleReference extends RuleComponent {
  private static final String DEFAULT_MEDIA_TYPE = "application/srgs+xml";

  private URI grammarReference;

  private String ruleName;

  private String mediaType;

  public RuleReference(String ruleName) throws IllegalArgumentException {
    checkValidGrammarText(ruleName);

    this.ruleName = ruleName;
  }

  public RuleReference(URI grammarReference) {

    this.grammarReference = grammarReference;
  }

  public RuleReference(URI grammarReference, String ruleName)
      throws IllegalArgumentException {
    checkValidGrammarText(ruleName);

    this.grammarReference = grammarReference;
    this.ruleName = ruleName;
  }

  public URI getGrammarReference() {
    return grammarReference;
  }

  public void setGrammarReference(URI uri) {
    grammarReference = uri;
  }

  public void setMediaType(String t) {
    mediaType = t;
  }

  public String getMediaType() {
    if (mediaType == null) {
      return DEFAULT_MEDIA_TYPE;
    }

    return mediaType;
  }

  public String getRuleName() {
    return ruleName;
  }

  @Override
  void assignName(String myName) {
    name = myName + "_r_"
        + (grammarReference == null ? "" : grammarReference + "#") + ruleName;
  }

  @Override
  public String toStringXML() {
    StringBuffer str = new StringBuffer();
    str.append("<ruleref uri=\"");

    if (grammarReference != null) {
      str.append(grammarReference.toString());
    }
    if (ruleName != null) {
      str.append("#").append(ruleName).append("\"");
    }

    if (mediaType != null) {
      str.append(" type=\"").append(mediaType).append("\"");
    }
    appendLangXML(str); // handle optional language attachment
    str.append("/>");

    return str.toString();
  }

  @Override
  public String toStringABNF() {
    StringBuffer str = new StringBuffer();

    if (grammarReference != null) {
      str.append("$<");
      str.append(shortUrl(grammarReference));
      if (ruleName != null && !ruleName.equals("___root")) {
        str.append("#").append(ruleName);
      }
      str.append(">");
    } else {
      // rulename can not be null: local reference
      str.append('$').append(ruleName);
    }

    if (mediaType != null) {
      str.append("~<").append(mediaType).append(">");
      ;
    }
    appendLangABNF(str); // handle optional language attachment

    return str.toString();
  }

  /** not thread safe! **/
  public String getRepresentation() {
    boolean shorten = SHORTEN_URLS;
    SHORTEN_URLS = false;
    String result = toStringABNF();
    SHORTEN_URLS = shorten;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    Boolean b = eq(obj);
    if (b != null)
      return b;
    RuleReference other = (RuleReference) obj;
    if (grammarReference == null) {
      return (other.grammarReference == null);
    } else if (!grammarReference.equals(other.grammarReference)) {
      return false;
    }
    if (ruleName == null) {
      return (other.ruleName == null);
    } else if (!ruleName.equals(other.ruleName)) {
      return false;
    }
    if (mediaType == null) {
      return (other.mediaType == null);
    }
    return mediaType.equals(other.mediaType);
  }

  @Override
  public int hashCode() {
    return (grammarReference == null ? 0 : grammarReference.hashCode())
        + (ruleName == null ? 0 : ruleName.hashCode())
        + (mediaType == null ? 0 : mediaType.hashCode());
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

  /** The rule reference has to be resolved (which happened before) and the
   *  grammar manager has to be asked for the rule
   * @param mgr
   * @throws GrammarException
   */
    // for the left corner context
  @Override
  public Set<RuleComponent> computeLeftCorner(GrammarManager mgr) {
    if (leftCorner != null) return leftCorner;
    leftCorner = new HashSet<>();
    leftCorner.add(this);
    Rule r = mgr.resolve(this);
    if (r != null) {
      leftCorner.addAll(r.getRuleComponent().computeLeftCorner(mgr));
    }
    return leftCorner;
  }
}
