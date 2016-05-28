package com.github.vangj.jbayes.inf.exact.graph.pptc;

import com.github.vangj.jbayes.inf.exact.graph.Node;

import java.util.Set;

public class SepSet extends Clique {

  private int cost;

  public SepSet(int cost, Set<Node> nodes) {
    super();
    this.cost = cost;
    nodes.forEach(node -> this.nodes.put(node.getId(), node));
  }

  public int cost() {
    return cost;
  }

  public int mass() {
    return nodes.size();
  }
}
