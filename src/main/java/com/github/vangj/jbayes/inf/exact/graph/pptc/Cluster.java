package com.github.vangj.jbayes.inf.exact.graph.pptc;

import com.github.vangj.jbayes.inf.exact.graph.Node;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Cluster {
  private Map<String, Node> nodes;

  public Cluster() {
    nodes = new LinkedHashMap<>();
  }

  public Cluster(Node node, List<Node> nodes) {
    this.nodes = new LinkedHashMap<>();
    for(Node n : nodes) {
      this.nodes.put(n.getId(), n);
    }
    this.nodes.put(node.getId(), node);
  }

  public boolean contains(String id) {
    return nodes.containsKey(id);
  }

  @Override
  public String toString() {
    return nodes.entrySet().stream()
        .map(entry -> entry.getKey())
        .collect(Collectors.joining(",","[","]"));
  }
}
