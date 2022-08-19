package org.jvoicexml.processor;

import java.io.IOException;
import java.net.URI;

import org.jvoicexml.processor.grammar.Grammar;
import org.jvoicexml.processor.grammar.Rule;
import org.jvoicexml.processor.grammar.RuleReference;
import org.jvoicexml.processor.srgs.GrammarException;

//Comp. 2.0.6

public interface GrammarManager {
  Grammar[] listGrammars();

  Grammar loadGrammar(URI grammarReference)
      throws GrammarException, IOException;

  Grammar getGrammar(URI grammarReference);

  void deleteGrammar(Grammar grammar);

  public Rule resolve(RuleReference reference);
}
