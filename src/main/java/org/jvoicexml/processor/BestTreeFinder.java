package org.jvoicexml.processor;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

public class BestTreeFinder {
  private final IdentityHashMap<ChartNode, Configuration> minConf;
  private int noConfs;

  private BestTreeFinder() {
    minConf = new IdentityHashMap<>();
    noConfs = 0;
  }

  /** Descend into the children of this node.
   *  Weight of the configuration is determined here.
   *  protected so it can be overridden.
   *
   */
  protected void evaluateChildren(Configuration c) {
    ChartNode node = c.getAlternative();
    c.weight = node.rule.weight() * (node.end - node.start);
    for (ChartNode child : node.children) {
      findBestConf(child);
      c.children.add(minConf.get(child));
      c.weight += minConf.get(child).weight;
    }
  }

  private void findBestConf(ChartNode n) {
    // so the same node is not evaluated twice. Only valid if the weight
    // computation only depends on this node and its children, and not parent
    // features
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

  /** Determine the best tree out of all parsing results */
  public static Configuration findBestTree(Iterable<ChartNode> results) {
    Configuration best = null;
    BestTreeFinder btf = new BestTreeFinder();
    for (ChartNode n : results) {
      btf.findBestConf(n);
      Configuration localBest = btf.minConf.get(n);
      if (best == null || localBest.compareTo(best) < 0) {
        best = localBest;
      }
    }
    return best;
  }

  public int noOfSolutions() {
    return noConfs;
  }

  /** Descend into the children of this node.
   *  Weight of the configuration is determined here.
   *  protected so it can be overridden.
   *
   */
  private static List<Configuration> walkChildren(Configuration c) {
    List<Configuration> l = new ArrayList<>();
    l.add(c);
    List<Configuration> temp = new ArrayList<>();
    for (ChartNode child : c.getAlternative().children) {
      for (Configuration parent : l) {
        for (Configuration childConf : walkAllConfigs(child)) {
          temp.add(parent.newWithChild(childConf));
        }
      }
      l = temp;
      temp = new ArrayList<>();
    }
    return l;
  }

  private static List<Configuration> walkAllConfigs(ChartNode n) {
    // so the same node is not evaluated twice. Only valid if the weight
    // computation only depends on this node and its children, and not parent
    // features
    Configuration c = new Configuration(n);
    List<Configuration> result = new ArrayList<>();
    while (c != null) {
      result.addAll(walkChildren(c));
      c = c.nextConf();
    }
    return result;
  }

  /** Determine the best tree out of all parsing results */
  public static List<Configuration> enumerateTrees(Iterable<ChartNode> results) {
    List<Configuration> result = new ArrayList<>();
    for (ChartNode n : results) {
      result.addAll(walkAllConfigs(n));
    }
    return result;
  }
}
