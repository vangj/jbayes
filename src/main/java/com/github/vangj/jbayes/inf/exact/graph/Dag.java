package com.github.vangj.jbayes.inf.exact.graph;

import com.github.vangj.jbayes.inf.exact.graph.traversal.DagShortestPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Dag extends Graph {
  protected Map<String, List<Node>> parents;
  protected Map<String, List<Node>> children;

  public Dag() {
    parents = new HashMap<>();
    children = new HashMap<>();
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

  public List<Node> parents(Node node) {
    List<Node> parents = this.parents.get(node.id);
    if(null == parents) {
      parents = new ArrayList<>();
      this.parents.put(node.id, parents);
    }
    return parents;
  }

  public List<Node> children(Node node) {
    List<Node> children = this.children.get(node.id);
    if(null == children) {
      children = new ArrayList<>();
      this.children.put(node.id, children);
    }
    return children;
  }

  public List<Node> coparents(Node node) {
    Set<Node> copas = new HashSet<>();
    copas.addAll(parents(node));
    copas.addAll(children(node));
    children(node).forEach(n -> {
      copas.addAll(parents(n));
    });
    return new ArrayList<>(copas);
  }
}
