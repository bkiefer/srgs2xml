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

import java.util.regex.Pattern;

//Comp. 2.0.6

public class RuleToken extends RuleComponent {
  private String text;
  private Pattern p;

  /** USE ONLY BY XML GRAMMAR PARSER!!!! */
  public RuleToken(String text) {
    this.text = text;
    if ((text == null) || (text.length() == 0)) {
      throw new IllegalArgumentException(
          "'" + text + "'" + " is not a valid grammar text");
    }
    // BK: extension to match arbitrary regex as token
    if (text.charAt(0) == '"' && text.charAt(text.length() - 1) == '"') {
      this.text = text = text.substring(1, text.length() - 1);
    } else {
      if (! text.startsWith("$$")) {
        this.text = text.trim().replaceAll("  +", " ");
      }
    }
    // BK: extension to match arbitrary regex as token
    if (text.startsWith("$$")) {
      this.p = Pattern.compile(text.substring(2));
    }
  }

  /** USE ONLY BY ABNF GRAMMAR PARSER!!!! */
  public RuleToken(String text, String language) throws IllegalArgumentException {
    if ((text == null) || (text.length() == 0)) {
      throw new IllegalArgumentException(
          "'" + text + "'" + " is not a valid grammar text");
    }
    // BK: extension to match arbitrary regex as token
    if (text.charAt(0) == '"') {
      this.text = text = text.substring(1, text.length() - 1);
    } else {
      if (! text.startsWith("$$")) {
        this.text = text.trim().replaceAll("  +", " ");
      }
    }
    // BK: extension to match arbitrary regex as token
    if (text.startsWith("$$")) {
      this.p = Pattern.compile(text.substring(2));
    }
    this.lang = language;
  }

  public Pattern getPattern() {
    return p;
  }

  public String getText() {
    return text;
  }

  public String getLanguage() {
    return lang;
  }

  void assignName(String myName) {
    name = myName + "_" + toStringABNF();
  }

  public String toStringXML() {
    StringBuffer str = new StringBuffer();
    str.append("<item");
    appendLangXML(str);
    str.append('>');
    if (text.contains(" ") && text.charAt(0) != '"') {
      str.append('"');
      str.append(text);
      str.append('"');
    } else {
      str.append(text);
    }
    str.append("</item>");
    return str.toString();
  }

  public String toStringABNF() {
    StringBuffer str = new StringBuffer();
    if (text.charAt(0) == '"' || !text.contains(" ")) {
      str.append(text);
    } else {
      str.append('"').append(text).append('"');
    }
    appendLangABNF(str);
    return str.toString();
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((text == null) ? 0 : text.hashCode());
    return result;
  }

  public boolean equals(Object obj) {
    Boolean b = eq(obj);
    if (b != null) return b;
    RuleToken other = (RuleToken) obj;
    if (text == null) {
      return (other.text == null);
    }
    return text.equals(other.text);
  }
}
