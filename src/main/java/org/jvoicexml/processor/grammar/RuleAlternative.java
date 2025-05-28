package org.jvoicexml.processor.grammar;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RuleAlternative extends RuleComponent {
  RuleComponent component;

  double weight;

  final int nr;

  public RuleAlternative(RuleComponent ruleComponent, double weight, int nr) {
    this.weight = weight;
    this.component = ruleComponent;
    this.nr = nr;
  }

  @Override
  void assignName(String myName) {
    component.assignName(myName);
  }

  @Override
  public String toStringXML() {
    return component.toStringXML();
  }

  @Override
  public String toStringABNF() {
    return component.toStringABNF();
  }

  @Override
  public boolean looksFor(RuleComponent r, int i) {
    // r must be equal to the ith alternative. Because we're using the
    // RuleComponents as immutable objects from the grammar, it's sufficient
    // to test for token identity
    return component.equ(r);
  }

  @Override
  public boolean equals(Object obj) {
    Boolean b = eq(obj);
    if (b != null)
      return b;
    RuleAlternative other = (RuleAlternative) obj;
    return (weight - other.weight < 1e-9 && nr == other.nr
        && component.equals(other.component));
  }

  @Override
  public int hashCode() {
    return ((int) weight * 100) + component.hashCode() + nr;
  }

  @Override
  RuleComponent cleanup(Map<RuleToken, RuleToken> terminals,
      Map<RuleComponent, RuleComponent> nonterminals) {
    RuleAlternative alt = (RuleAlternative) nonterminals.get(this);
    if (alt != null) {
      return alt;
    }
    alt = this;
    component = component.cleanup(terminals, nonterminals);
    nonterminals.put(alt, alt);
    return alt;
  }

  @Override
  protected Set<RuleComponent> computeLeftCorner() {
    if (leftCorner != null) return leftCorner;
    leftCorner = new HashSet<>();
    leftCorner.add(this);
    leftCorner.addAll(component.computeLeftCorner());
    return leftCorner;
  }

  @Override
  public Set<RuleComponent> getLeftCorner(int i) {
    return component.leftCorner;
  }
}