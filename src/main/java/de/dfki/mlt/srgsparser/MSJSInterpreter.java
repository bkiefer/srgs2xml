/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.mlt.srgsparser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jvoicexml.processor.srgs.ChartGrammarChecker;
import org.jvoicexml.processor.srgs.ChartGrammarChecker.ChartNode;
import org.jvoicexml.processor.srgs.grammar.RuleTag;

/** Interprets the JavaScript tags as the processor for the Microsoft Speech
 *  API does (could not find "official" specification, so do it looking at
 *  examples).
 *
 *  The only exception is the handling of $$NUM patterns for putting the
 *  matched string of the current-NUM token into a slot.
 *
 * @author Christian.Buerckert@DFKI.de, Bernd.Kiefer@dfki.de
 */
public class MSJSInterpreter extends Interpreter {

  private static final Pattern tok = Pattern.compile("\\$\\$[0-9]+");

  protected String massageTag(ChartNode tag) {
    String in = ((RuleTag) tag.getRule()).getTag().toString();
    Matcher m = tok.matcher(in);
    int tagIndex = -1;
    List<ChartNode> seq = null;
    StringBuffer sb = null;
    while (m.find()) {
      if (tagIndex < 0) {
        sb = new StringBuffer();
        // find my position
        tagIndex = 0;
        seq = stack.elementAt(stack.size()-2).getChildren();
        for (ChartNode curr : seq) {
          if (curr == tag)
            break;
          ++tagIndex;
        }
        assert (tagIndex < seq.size());
      }
      String match = m.group();
      int delta = Integer.parseInt(match.substring(2));
      m.appendReplacement(sb, checker.covered(seq.get(tagIndex - delta)));
    }
    if (sb != null) {
      m.appendTail(sb);
      return sb.toString();
    }
    return in;
  }

  public MSJSInterpreter(ChartGrammarChecker c) {
    super(c);
  }

}
