/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision: 68 $
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy: schnelle $
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007-2014 JVoiceXML group - http://jvoicexml.sourceforge.net
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

package org.jvoicexml.processor.srgs;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jvoicexml.processor.srgs.abnf.SrgsAbnf;
import org.jvoicexml.processor.srgs.abnf.SrgsLexer;
import org.jvoicexml.processor.srgs.grammar.Rule;

/**
 * A parser for SRGS ABNF grammars.
 *
 * @author Bernd Kiefer
 * @version $Revision: 1370 $
 */
public class AbnfRuleGrammarParser implements RuleGrammarParser {

  Logger logger = Logger.getLogger(AbnfRuleGrammarParser.class);

  private Map<String, Object> attributes;

  private String description;
  public static boolean DEBUG_GRAMMAR = false;

  public AbnfRuleGrammarParser(String desc) {
    String pwd = new File(".").getAbsolutePath();
    pwd = pwd.substring(0, pwd.length() - 1);
    description = desc.startsWith("file:") ? desc.substring(5) : desc;
    if (description.startsWith(pwd)) {
      description = description.substring(pwd.length());
    }
  }

  public List<Rule> load(final InputStream stream) {
    try {
      StringBuffer sb = new StringBuffer();
      int c;
      stream.mark(100);
      while ((c = stream.read()) > 0 && (c != '\n')) {
        sb.append((char) c);
      }
      Matcher m = Pattern.compile("(?:\\xef\\xbb\\xbf)?#ABNF ([0-9.]+) ([^;]*);.*")
          .matcher(sb.toString());
      if (!m.matches()) {
        logger.error("Wrong ABNF Header: " + sb.toString());
        return null;
      }
      stream.reset();
      Reader r = null;
      if (m.groupCount() > 2) {
        r = new InputStreamReader(stream, m.group(2));
      } else {
        r = new InputStreamReader(stream);
      }
      return parseGrammar(r);
    } catch (IOException ex) {
      logger.error(ex);
    }
    return null;
  }

  private List<Rule> parseGrammar(Reader r) throws IOException {
    SrgsLexer lexer = new SrgsLexer(r);
    lexer.setOrigin(description);
    SrgsAbnf grammar = new SrgsAbnf(lexer);
    if (DEBUG_GRAMMAR) {
      grammar.setDebugLevel(99);
      grammar.setErrorVerbose(true);
    }
    if (!grammar.parse())
      return null;
    attributes = grammar.getAttributes();
    List<Rule> rules = grammar.getRules();
    return rules;
  }

  public Map<String, Object> getAttributes() {
    return attributes;
  }
}
