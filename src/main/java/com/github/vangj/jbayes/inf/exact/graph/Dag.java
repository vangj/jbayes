package com.github.vangj.jbayes.inf.exact.graph;

import com.github.vangj.jbayes.inf.exact.graph.traversal.DagShortestPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A directed acylic graph (DAG).
 */
public class Dag extends Graph {
  protected Map<String, List<Node>> parents;
  protected Map<String, List<Node>> children;

  public Dag() {
    parents = new HashMap<>();
    children = new HashMap<>();
  }

  @Override
  protected Graph instance() {
    return new Dag();
  }

  @Override
  public Graph addEdge(Edge edge) {
    edge.type = Edge.Type.DIRECTED;

    if(DagShortestPath.exists(this, edge.right, edge.left, null)) {
      //if right -> -> -> left path alrady exists
      //then adding left -> right will form cycle!
      //do not add it
      return this;
    }

    super.addEdge(edge);
    Node n1 = edge.left;
    Node n2 = edge.right;

    List<Node> parents = parents(n2);
    if(!parents.contains(n1)) {
      parents.add(n1);
    }

    List<Node> children = children(n1);
    if(!children.contains(n2)) {
      children.add(n2);
    }
    return this;
  }

  /**
   * Gets the parent of the specified node.
   * @param node Node.
   * @return List of parents.
   */
  public List<Node> parents(Node node) {
    List<Node> parents = this.parents.get(node.id);
    if(null == parents) {
      parents = new ArrayList<>();
      this.parents.put(node.id, parents);
    }
    return parents;
  }

  /**
   * Gets the children of the specified node.
   * @param node Node.
   * @return List of children.
   */
  public List<Node> children(Node node) {
    List<Node> children = this.children.get(node.id);
    if(null == children) {
      children = new ArrayList<>();
      this.children.put(node.id, children);
    }
    return children;
  }

  /**
   * Gets the coparents of the specified node. Coparents are parents of the
   * specified node's children.
   * @param node Node.
   * @return List of coparents.
   */
  public List<Node> coparents(Node node) {
    Set<Node> copas = new HashSet<>();
    children(node).forEach(n -> {
      copas.addAll(parents(n));
    });
    copas.remove(node);
    return new ArrayList<>(copas);
  }

  /**
   * Gets the Markov blanket for the specified node. The Markov blanket
   * is specified as the union of parents, children, and coparents.
   * @param node Node.
   * @return List of nodes in the Markov blanket.
   */
  public List<Node> markovBlanket(Node node) {
    Set<Node> blanket = new HashSet<>();
    blanket.addAll(parents(node));
    blanket.addAll(children(node));
    blanket.addAll(coparents(node));
    blanket.remove(node);
    return new ArrayList<>(blanket);
  }
}
