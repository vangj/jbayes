package com.github.vangj.jbayes.inf.exact.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Graph {
  protected Map<String, List<Node>> neighbors;
  protected Map<String, Node> nodes;
  protected List<Edge> edges;

  public Graph() {
    neighbors = new HashMap<>();
    nodes = new HashMap<>();
    edges = new ArrayList<>();
  }

  public Graph addNode(Node node) {
    if(nodes.containsKey(node.id)) {
      return this;
    }
    nodes.put(node.id, node);
    return this;
  }

  public List<Node> nodes() {
    return new ArrayList<>(nodes.values());
  }

  public List<Edge> edges() {
    return edges;
  }

  public List<Node> neighbors(Node node) {
    return neighbors(node.id);
  }

  public List<Node> neighbors(String id) {
    List<Node> list = neighbors.get(id);
    if(null == list) {
      list = new ArrayList<>();
      neighbors.put(id, list);
    }
    return list;
  }

  public Graph addEdge(Node n1, Node n2) {
    return addEdge(Edge.newBuilder().left(n1).right(n2).build());
  }

  public Graph addEdge(Edge edge) {
    addNode(edge.left).addNode(edge.right);
    if(!edges.contains(edge)) {
      edges.add(edge);

      List neigh1 = neighbors.get(edge.left.id);
      List neigh2 = neighbors.get(edge.right.id);

      if(null == neigh1) {
        neigh1 = new ArrayList<>();
        neighbors.put(edge.left.id, neigh1);
      }

      if(null == neigh2) {
        neigh2 = new ArrayList<>();
        neighbors.put(edge.right.id, neigh2);
      }

      if(!neigh1.contains(edge.right)) {
        neigh1.add(edge.right);
      }

      if(!neigh2.contains(edge.left)) {
        neigh2.add(edge.left);
      }
    }
    return this;
  }

  @Override
  public String toString() {
    return (new StringBuilder())
      .append(String.join(
          System.lineSeparator(),
          nodes().stream().map(Node::toString).collect(Collectors.toList())))
      .append(System.lineSeparator())
      .append(String.join(System.lineSeparator(),
          edges().stream().map(Edge::toString).collect(Collectors.toList())))
      .toString();
  }
}
