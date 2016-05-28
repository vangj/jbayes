package com.github.vangj.jbayes.inf.exact.graph.pptc;

import com.github.vangj.jbayes.inf.exact.graph.Dag;
import com.github.vangj.jbayes.inf.exact.graph.Edge;
import com.github.vangj.jbayes.inf.exact.graph.Node;
import com.github.vangj.jbayes.inf.exact.graph.Ug;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Triangulate {
  private static class NodeCluster {
    private Node node;
    private Set<Node> neighbors;
    private int weight;
    private List<Edge> edges;

    public NodeCluster(Node node, List<Node> neighbors, int weight, List<Edge> edges) {
      this.node = node;
      this.neighbors = new LinkedHashSet<>(neighbors);
      this.weight = weight;
      this.edges = edges;
    }

    @Override
    public String toString() {
      List<Node> nodes = new ArrayList<>();
      nodes.add(node);
      nodes.addAll(neighbors);

      String ids = nodes.stream().map(Node::getId).collect(Collectors.joining(",","[","]"));
      return (new StringBuilder())
          .append(ids)
          .append(" ")
          .append(edges.size())
          .append(" ")
          .append(weight)
          .toString();
    }
  }

  private Triangulate() {

  }

  public static List<Cluster> triangulate(Dag dag, Ug m) {
    List<Cluster> clusters = new ArrayList<>();
    Ug mm = (Ug)m.duplicate();
    while(mm.nodes().size() > 0) {
      NodeCluster nodeCluster = selectNode(mm);

      Cluster cluster = new Cluster(nodeCluster.node, mm.neighbors(nodeCluster.node));

      clusters.add(cluster);
      mm.remove(nodeCluster.node);

      connectNeighbors(m, nodeCluster.edges);
      connectNeighbors(mm, nodeCluster.edges);
    }
    return clusters;
  }

  private static void connectNeighbors(Ug m, List<Edge> edges) {
    edges.forEach(edge -> {
      m.addEdge(edge.getLeft(), edge.getRight());
    });
  }

  private static NodeCluster selectNode(Ug m) {
    List<NodeCluster> clusters = m.nodes()
        .stream()
        .map(node -> {
          int weight = weight(node, m);
          List<Edge> edges = edgesToAdd(node, m);
          return new NodeCluster(node, m.neighbors(node), weight, edges);
        })
        .sorted( (c1, c2) -> {
          int result = Integer.compare(c1.edges.size(), c2.edges.size());
          if(0 == result) {
            result = Integer.compare(c1.weight, c2.weight);
            if(0 == result) {
              result = -1 * c1.node.getId().compareTo(c2.node.getId());
            }
          }
          return result;
        })
        .collect(Collectors.toList());
    return clusters.get(0);
  }

  private static int weight(Node n, Ug m) {
    int weight = n.weight();
    for(Node neighbor : m.neighbors(n)) {
      weight *= neighbor.weight();
    }
    return weight;
  }

  private static List<Edge> edgesToAdd(Node n, Ug m) {
    List<Edge> edges = new ArrayList<>();
    List<Node> neighbors = m.neighbors(n);
    final int size = neighbors.size();
    for(int i=0; i < size; i++) {
      Node ne1 = neighbors.get(i);
      for(int j=i+1; j < size; j++) {
        Node ne2 = neighbors.get(j);
        if(!m.edgeExists(ne1.getId(), ne2.getId())) {
          edges.add(Edge.newBuilder().left(ne1).right(ne2).build());
        }
      }
    }
    return edges;
  }
}
