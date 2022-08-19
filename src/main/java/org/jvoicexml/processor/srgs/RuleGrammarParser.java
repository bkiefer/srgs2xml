package org.jvoicexml.processor.srgs;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jvoicexml.processor.grammar.Rule;

public abstract class RuleGrammarParser {

  protected Map<String, Object> attributes;

  public abstract List<Rule> load(final InputStream stream) throws Exception;

  protected RuleGrammarParser() {
    attributes = new HashMap<>();
  }

  public Map<String, Object> getAttributes() {
    return attributes;
  }
}
