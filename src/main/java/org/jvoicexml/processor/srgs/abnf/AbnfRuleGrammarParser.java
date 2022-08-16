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

package org.jvoicexml.processor.srgs.abnf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.jvoicexml.processor.grammar.Rule;
import org.jvoicexml.processor.srgs.RuleGrammarParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A parser for SRGS ABNF grammars.
 *
 * @author Bernd Kiefer
 * @version $Revision: 1370 $
 */
public class AbnfRuleGrammarParser implements RuleGrammarParser {

  Logger logger = LoggerFactory.getLogger(AbnfRuleGrammarParser.class);

  private PrintStream err;

  private Map<String, Object> attributes;

  private String description;
  public static boolean DEBUG_GRAMMAR = false;

  private Pattern HEADERPAT =
      Pattern.compile("(?:\\xef\\xbb\\xbf)?#ABNF ([0-9.]+)\\s*( [^;]+)?\\s*;.*");

  public AbnfRuleGrammarParser(String desc, PrintStream ps) {
    String pwd = new File(".").getAbsolutePath();
    pwd = pwd.substring(0, pwd.length() - 1);
    description = desc.startsWith("file:") ? desc.substring(5) : desc;
    description = description.replaceAll("//+", "/");
    if (description.startsWith(pwd)) {
      description = description.substring(pwd.length());
    }
    err = ps;
  }

  public AbnfRuleGrammarParser(String desc) {
    this(desc, System.err);
  }

  public List<Rule> load(final InputStream is) {
    try (BOMInputStream stream = new BOMInputStream(is, ByteOrderMark.UTF_8,
        ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE,
        ByteOrderMark.UTF_32LE)) {
      ByteOrderMark bom = stream.getBOM();
      int c;
      StringBuffer sb = new StringBuffer();
      Reader r = null;
      if (bom != null) {
        r = new InputStreamReader(stream, bom.getCharsetName());
      } else {
        stream.mark(100);
        while ((c = stream.read()) > 0 && (c != '\n') && (c != '\r')) {
          sb.append((char) c);
        }
        String s = sb.toString();
        Matcher m = HEADERPAT.matcher(s);
        if (!m.matches()) {
          err.println(description +":1: error: Wrong ABNF Header: " + s);
          return null;
        }
        stream.reset();
        if (m.groupCount() >= 2 && m.group(2) != null) {
          r = new InputStreamReader(stream, m.group(2).trim());
        } else {
          r = new InputStreamReader(stream);
        }
      }
      return parseGrammar(r);
    } catch (IOException ex) {
      logger.error("{}", ex);
    }
    return null;
  }

  private List<Rule> parseGrammar(Reader r) throws IOException {
    SrgsLexer lexer = new SrgsLexer(r, System.err);
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
