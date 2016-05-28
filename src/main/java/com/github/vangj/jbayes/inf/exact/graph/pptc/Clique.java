package com.github.vangj.jbayes.inf.exact.graph.pptc;

import com.github.vangj.jbayes.inf.exact.graph.Node;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

  public int weight() {
    int weight = 1;
    for(Map.Entry<String, Node> entry : nodes.entrySet()) {
      weight *= entry.getValue().weight();
    }
    return weight;
  }

  public List<Node> nodes() {
    return nodes.values().stream().collect(Collectors.toList());
  }

  public Clique add(Node node) {
    if(!this.nodes.containsKey(node.getId())) {
      this.nodes.put(node.getId(), node);
    }
    return this;
  }

  public boolean contains(String id) {
    return nodes.containsKey(id);
  }

  public SepSet sepSet(Clique that) {
    Set<Node> set1 = new LinkedHashSet<>(this.nodes.values());
    Set<Node> set2 = new LinkedHashSet<>(that.nodes.values());
    set1.retainAll(set2);
    int weight = this.weight() + that.weight();
    return new SepSet(weight, set1);
  }

  @Override
  public String toString() {
    return nodes.entrySet().stream()
        .map(entry -> entry.getKey())
        .collect(Collectors.joining(",","[","]"));
  }
}
