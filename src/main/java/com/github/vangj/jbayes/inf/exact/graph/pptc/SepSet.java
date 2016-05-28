package com.github.vangj.jbayes.inf.exact.graph.pptc;

import com.github.vangj.jbayes.inf.exact.graph.Node;

import java.util.LinkedHashSet;
import java.util.Set;

public class SepSet extends Clique {

  private Clique left;
  private Clique right;

  public SepSet(Clique left, Clique right) {
    super();
    this.left = left;
    this.right = right;
    Set<Node> set1 = new LinkedHashSet<>(left.nodes.values());
    Set<Node> set2 = new LinkedHashSet<>(right.nodes.values());
    set1.retainAll(set2);
    set1.forEach(node -> this.nodes.put(node.getId(), node));
  }

  public Clique left() {
    return left;
  }

  public Clique right() {
    return right;
  }

  public int cost() {
    return left.weight() + right.weight();
  }

  public int mass() {
    return nodes.size();
  }
}
