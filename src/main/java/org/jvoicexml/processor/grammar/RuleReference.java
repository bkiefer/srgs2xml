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

    void assignName(String myName) {
      name = myName + "_r_"
          + (grammarReference == null ? "" : grammarReference + "#")
          + ruleName;
    }

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


    public String toStringABNF() {
        StringBuffer str = new StringBuffer();

        if (grammarReference != null) {
          str.append("$<");
          str.append(shortUrl(grammarReference));
          if (ruleName != null && ! ruleName.equals("___root")) {
            str.append("#").append(ruleName);
          }
          str.append(">");
        } else {
          // rulename can not be null: local reference
          str.append('$').append(ruleName);
        }

        if (mediaType != null) {
            str.append("~<").append(mediaType).append(">");;
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
}
