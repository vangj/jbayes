package com.github.vangj.jbayes.inf.exact.graph;

import com.github.vangj.jbayes.inf.exact.graph.traversal.PdagShortestPath;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A partially directed acyclic graph (PDAG). "A PDAG is a graph where some edges are directed and
 * some are undirected and one cannot trace a cycle by following the direction of directed edges and
 * any direction for undirected edges."
 * <ul>
 *   <li>Quote above taken directly from http://jmlr.csail.mit.edu/papers/volume8/kalisch07a/kalisch07a.pdf</li>
 * </ul>
 */
public class Pdag extends Graph {

  protected Map<String, List<Node>> parents;
  protected Map<String, List<Node>> children;

  public Pdag() {
    parents = new HashMap<>();
    children = new HashMap<>();
  }

  @Override
  protected Graph instance() {
    return new Pdag();
  }

  @Override
  public Graph addEdge(Edge edge) {
    if (Edge.Type.UNDIRECTED == edge.type) {
      super.addEdge(edge);
    } else {
      if (PdagShortestPath.exists(this, edge.right, edge.left, null)) {
        //if right -> -- -> -- left path exists
        //then adding left -> right will form cycle
        //do not add it!
        return this;
      }

      super.addEdge(edge);

      Node n1 = edge.left;
      Node n2 = edge.right;

      List<Node> parents = parents(n2);
      if (!parents.contains(n1)) {
        parents.add(n1);
      }

      List<Node> children = children(n1);
      if (!children.contains(n2)) {
        children.add(n2);
      }
    }

    return this;
  }

  public List<Node> parents(Node node) {
    List<Node> parents = this.parents.get(node.id);
    if (null == parents) {
      parents = new ArrayList<>();
      this.parents.put(node.id, parents);
    }
    return parents;
  }

  public List<Node> children(Node node) {
    List<Node> children = this.children.get(node.id);
    if (null == children) {
      children = new ArrayList<>();
      this.children.put(node.id, children);
    }
    return children;
  }
}
