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

import java.util.Map;

//Comp 2.0.6

public class Rule {
    public static int PUBLIC = 0x1;

    public static int PRIVATE = 0x2;

    private String ruleName;

    private RuleComponent ruleComponent;

    private int scope;

    public Rule(String ruleName, RuleComponent ruleComponent)
        throws IllegalArgumentException {
        this(ruleName, ruleComponent, PRIVATE);
    }

    public Rule(String ruleName, RuleComponent ruleComponent, int scope) {
        RuleComponent.checkValidGrammarText(ruleName);

        if ((scope != PRIVATE) && (scope != PUBLIC)) {
            throw new IllegalArgumentException(
                    "Scope must be either PRIVATE or PUBLIC!");
        }
        if (ruleComponent == null) {
            throw new IllegalArgumentException(
                    "Rule component must not be null!");
        }
        this.ruleName = ruleName;
        this.ruleComponent = ruleComponent;
        this.scope = scope;
        ruleComponent.assignName(ruleName);
    }

    public RuleComponent getRuleComponent() {
        return ruleComponent;
    }

    public String getRuleName() {
        return ruleName;
    }

    public int getScope() {
        return scope;
    }

    public String toStringXML() {
        StringBuffer str = new StringBuffer();

        str.append("<rule id=\"");
        str.append(ruleName);
        str.append("\" scope=\"");
        if (scope == PRIVATE) {
            str.append("private");
        } else {
            str.append("public");
        }
        str.append("\">");
        str.append(RuleComponent.toStringXML(ruleComponent));
        str.append("</rule>");
        return str.toString();
    }


    public String toStringABNF() {
        StringBuffer str = new StringBuffer();

        if (scope == PUBLIC) {
          str.append("public ");
        }

        str.append("$").append(ruleName)
        .append(" = ")
        .append(ruleComponent.toStringABNF())
        .append(";");
        return str.toString();
    }

    @Override
    public String toString() {
      return (RuleComponent.PRINT_COMPACT) ? toStringABNF() : toStringXML();
    }

    /** Compute unique set of terminals and nonterminals */
    public Rule cleanup(Map<RuleToken, RuleToken> terminals,
        Map<RuleComponent, RuleComponent> nonterminals) {
      ruleComponent = ruleComponent.cleanup(terminals, nonterminals);
      return this;
    }
}
