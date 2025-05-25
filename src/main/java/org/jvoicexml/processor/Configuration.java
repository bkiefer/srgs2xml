package org.jvoicexml.processor;

import java.util.ArrayList;
import java.util.List;

public class Configuration implements Comparable<Configuration>, Traversable {
  double weight;
  private ChartNode node;
  private int variant;
  List<Configuration> children;

  public Configuration(ChartNode n) {
    weight = 0;
    node = n;
    variant = -1;
    children = new ArrayList<>(n.equivs == null ? 0 :  n.equivs.size());
  }

  private Configuration(Configuration c) {
    weight = c.weight;
    node = c.node;
    variant = c.variant;
    children = new ArrayList<>(c.children);
  }

  public double getWeight() {
    return weight;
  }

  public ChartNode getAlternative() {
    return variant < 0 ? node : node.equivs.get(variant);
  }

  @Override
  public int compareTo(Configuration arg0) {
    return (int)(weight - arg0.weight);
  }

  public boolean isDefault() {
    if (variant != -1) return false;
    for (Configuration child : children) {
      if (! child.isDefault()) {
        return false;
      }
    }
    return true;
  }

  /** Return a new configuration that refers to the next alternative of the
   *  chart node referenced in this configuration
   * @return a new configuration with a different member of the equivalence class
   */
  public Configuration nextConf() {
    if (node.equivs == null || variant == node.equivs.size() - 1)
      return null;
    Configuration result = new Configuration(node);
    result.variant = variant + 1;
    return result;
  }

  /** Create a new copy of this Configuration and append the child configuration
   *  to the children
   * @param child
   * @return a copy of this configuration with the added child
   */
  public Configuration newWithChild(Configuration child) {
    Configuration result = new Configuration(this);
    result.children.add(child);
    return result;
  }

  @Override
  public void preorder(TreeWalker acceptor) {
    ChartNode current = getAlternative();
    acceptor.enter(current, current.getChildren().isEmpty());
    for (Configuration child : children) {
      child.preorder(acceptor);
    }
    acceptor.leave(current, children.isEmpty());
  }

  private static int INDENT = 2;

  private void printRec(StringBuffer sb, int level) {
    for (int i = 0; i < INDENT*level; ++i) sb.append(' ');
    sb.append(node.toString()).append(System.lineSeparator());
    if (children != null) {
      for (Configuration c: children) {
        c.printRec(sb, level + 1);
      }
    }
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    printRec(sb, 0);
    return sb.toString();
  }
}