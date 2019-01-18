package org.jvoicexml.processor.srgs;

import java.io.InputStream;
import java.util.Map;

import org.jvoicexml.processor.srgs.grammar.Rule;

public interface RuleGrammarParser {
  public Rule[] load(final InputStream stream) throws Exception;

  public Map<String, String> getAttributes();
}
