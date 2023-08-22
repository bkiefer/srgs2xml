package org.jvoicexml.processor;
import java.util.IdentityHashMap;

public class BestTreeFinder {
  IdentityHashMap<ChartNode, Configuration> minConf;
  public int noConfs = 0;

  /** Descend into the children of this node */
  private void evaluateChildren(Configuration c) {
    c.weight = c.node.rule.weight() * (c.node.end - c.node.start);
    for (ChartNode child : c.node.children) {
      findBestConf(child);
      c.children.add(minConf.get(child));
      c.weight += minConf.get(child).weight;
    }
  }

  private void findBestConf(ChartNode n) {
    if (minConf.containsKey(n))
      return;
    Configuration c = new Configuration(n);
    Configuration localMin = c;
    while (c != null) {
      evaluateChildren(c);
      if (c.compareTo(localMin) < 0) {
        localMin = c;
      }
      c = c.nextConf();
      ++noConfs;
    }
    minConf.put(n, localMin);
  }

  public Configuration findBestTree(Iterable<ChartNode> results) {
    minConf = new IdentityHashMap<>();
    noConfs = 0;
    Configuration best = null;
    for (ChartNode n : results) {
      findBestConf(n);
      Configuration localBest = minConf.get(n);
      if (best == null || localBest.compareTo(best) < 0) {
        best = localBest;
      }
    }
    return best;
  }
}
