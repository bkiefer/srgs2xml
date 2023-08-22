package org.jvoicexml.processor;

import java.util.ArrayList;
import java.util.List;

public class Configuration implements Comparable<Configuration>, TreeStrategy {
  double weight;
  ChartNode node;
  int representative;
  List<Configuration> children;

  public Configuration(ChartNode n) {
    weight = 0;
    node = n;
    representative = -1;
    children = new ArrayList<>(n.children.size());
  }

  public double getWeight() {
    return weight;
  }

  @Override
  public int compareTo(Configuration arg0) {
    return (int)(arg0.weight - weight);
  }

  Configuration nextConf() {
    if (representative == children.size() - 1)
      return null;
    Configuration result = new Configuration(node);
    result.representative = representative + 1;
    return result;
  }

  public void preorder(TreeWalker acceptor) {
    ChartNode current = representative == -1 ? node : node.equivs.get(representative);
    acceptor.enter(current, current.getChildren().isEmpty());
    for (Configuration child : children) {
      child.preorder(acceptor);
    }
    acceptor.leave(current, children.isEmpty());
  }
}