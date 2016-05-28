package com.github.vangj.jbayes.inf.exact.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A generic graph.
 */
public class Graph {
  protected Map<String, List<Node>> neighbors;
  protected Map<String, Node> nodes;
  protected List<Edge> edges;

  public Graph() {
    neighbors = new HashMap<>();
    nodes = new HashMap<>();
    edges = new ArrayList<>();
  }

  public void remove(Node node) {
    nodes.remove(node.id);
    neighbors.remove(node.id);
    neighbors.forEach((k, v) -> {
      v.remove(node);
    });
    for(Iterator<Edge> it = edges.iterator(); it.hasNext(); ) {
      Edge edge = it.next();
      if(node.id.equalsIgnoreCase(edge.left.id) || node.id.equalsIgnoreCase(edge.right.id)) {
        it.remove();
      }
    }
  }

  public Graph duplicate() {
    Graph g = instance();
    nodes().forEach(node -> g.addNode(node));
    edges().forEach(edge -> g.addEdge(edge));
    return g;
  }

  protected Graph instance() {
    return new Graph();
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

  public Node node(String id) {
    return nodes.get(id);
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

  public boolean edgeExists(String id1, String id2) {
    return edgeExists(id1, id2, Edge.Type.UNDIRECTED);
  }

  public boolean edgeExists(String id1, String id2, Edge.Type type) {
    return edges().stream().filter(edge -> {
      if(!type.equals(edge.type)) {
        return false;
      }

      String left = edge.left.id;
      String right = edge.right.id;

      if(Edge.Type.UNDIRECTED.equals(type)) {
        if((left.equalsIgnoreCase(id1) && right.equalsIgnoreCase(id2)) ||
            (left.equalsIgnoreCase(id2) && right.equalsIgnoreCase(id1))) {
          return true;
        }
      } else {
        if(left.equalsIgnoreCase(id1) && right.equalsIgnoreCase(id2)) {
          return true;
        }
      }


      return false;
    }).count() > 0;
  }

  public Graph addEdge(String id1, String id2) {
    return addEdge(node(id1), node(id2));
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
