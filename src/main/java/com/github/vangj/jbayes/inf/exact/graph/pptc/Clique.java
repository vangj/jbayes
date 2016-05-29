package com.github.vangj.jbayes.inf.exact.graph.pptc;

import com.github.vangj.jbayes.inf.exact.graph.Node;
import com.github.vangj.jbayes.inf.exact.graph.util.NodeUtil;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A clique; contains a set of nodes. Used as a node in a join tree.
 */
public class Clique {
  protected Map<String, Node> nodes;

  public Clique() {
    nodes = new LinkedHashMap<>();
  }

  public Clique(Node node, List<Node> nodes) {
    this.nodes = new LinkedHashMap<>();
    for(Node n : nodes) {
      this.nodes.put(n.getId(), n);
    }
    this.nodes.put(node.getId(), node);
  }

  public Clique(Node... nodes) {
    this.nodes = new LinkedHashMap<>();
    for(Node n : nodes) {
      this.nodes.put(n.getId(), n);
    }
  }

  public List<Node> nodesMinus(List<Node> nodes) {
    return nodes().stream()
        .filter(node -> {
          if(nodes.contains(node)) {
            return false;
          }
          return true;
        })
        .collect(Collectors.toList());
  }

  /**
   * Checks if this clique is a superset of the
   * specified clique passed in.
   * @param that Clique.
   * @return Boolean.
   */
  public boolean isSuperset(Clique that) {
    Set<Node> set1 = new LinkedHashSet<>(this.nodes.values());
    Set<Node> set2 = new LinkedHashSet<>(that.nodes.values());
    set1.retainAll(set2);
    if(set1.size() == set2.size()) {
      return true;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public boolean equals(Object object) {
    if(null == object || !(object instanceof Clique)) {
      return false;
    }
    Clique that = (Clique)object;
    return this.hashCode() == that.hashCode();
  }

  /**
   * Weight is defined as product of the number of values for each node.
   * @return Weight.
   */
  public int weight() {
    int weight = 1;
    for(Map.Entry<String, Node> entry : nodes.entrySet()) {
      weight *= entry.getValue().weight();
    }
    return weight;
  }

  /**
   * Gets the nodes.
   * @return List of nodes
   */
  public List<Node> nodes() {
    return nodes.values().stream().collect(Collectors.toList());
  }

  /**
   * Checks if this clique contains the node associated
   * with the specified id.
   * @param id Id.
   * @return Boolean.
   */
  public boolean contains(String id) {
    return nodes.containsKey(id);
  }

  /**
   * Creates a separation set from this clique
   * and the clique passed in. The separation set should be
   * the intersection of the nodes between this clique
   * and the one passed in.
   * @param that Clique.
   * @return Separation set.
   */
  public SepSet sepSet(Clique that) {
    return new SepSet(this, that);
  }

  /**
   * Gets the id of this node. Composed of the lexicographically ordered
   * ids of the nodes in this clique.
   * @return Id.
   */
  public String id() {
    return NodeUtil.id(nodes());
  }

  @Override
  public String toString() {
    return id();
  }
}
