package com.github.vangj.jbayes.inf.exact.graph.pptc;

import com.github.vangj.jbayes.inf.exact.graph.Node;
import com.github.vangj.jbayes.inf.exact.graph.util.NodeUtil;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A separation set (which is a sub-type of a clique).
 */
public class SepSet extends Clique {

  private final Clique left;
  private final Clique right;

  public SepSet(Clique left, Clique right) {
    super();
    this.left = left;
    this.right = right;
    Set<Node> set1 = new LinkedHashSet<>(left.nodes.values());
    Set<Node> set2 = new LinkedHashSet<>(right.nodes.values());
    set1.retainAll(set2);
    set1.forEach(node -> this.nodes.put(node.getId(), node));
  }

  /**
   * Left clique.
   *
   * @return Clique.
   */
  public Clique left() {
    return left;
  }

  /**
   * Right clique.
   *
   * @return Clique.
   */
  public Clique right() {
    return right;
  }

  /**
   * Cost is the weight of the left and right cliques added.
   *
   * @return Cost.
   */
  public int cost() {
    return left.weight() + right.weight();
  }

  /**
   * Mass is the number of nodes in this separation set.
   *
   * @return Mass.
   */
  public int mass() {
    return nodes.size();
  }

  @Override
  public String id() {
    return NodeUtil.id(nodes(), "|", "|");
  }
}
