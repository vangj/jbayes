package com.github.vangj.jbayes.inf.exact.graph;

import com.github.vangj.jbayes.inf.exact.graph.lpd.Potential;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A node in a graph.
 */
public class Node extends Variable {
  private List<Double> probs;
  private Potential potential;
  private Map<String, Object> metadata;

  public Node() {
    probs = new ArrayList<>();
  }

  public Node(Variable v) {
    probs = new ArrayList<>();
    metadata = new HashMap<>();
    this.id = v.id;
    this.name = v.name;
    this.values = v.values;
  }

  private Node(NodeBuilder builder) {
    id = builder.id;
    name = builder.name;
    values = builder.values;
    probs = builder.probs;
  }

  /**
   * Adds any metadata.
   * @param k Key.
   * @param v Value.
   */
  public void addMetadata(String k, Object v) {
    metadata.put(k, v);
  }

  /**
   * Gets the metadata associated with the specified key.
   * @param k Key.
   * @return Value.
   */
  public Object getMetadata(String k) {
    return metadata.get(k);
  }

  public void setPotential(Potential potential) {
    this.potential = potential;
  }

  public Potential getPotential() {
    return potential;
  }

  public Node addProb(Double prob) {
    probs.add(prob);
    return this;
  }

  public int weight() {
    return values.size();
  }

  public List<Double> probs() {
    return probs;
  }

  public static NodeBuilder builder() {
    return new NodeBuilder();
  }


  public static final class NodeBuilder {
    private String id;
    private String name;
    private Set<String> values;
    private List<Double> probs;

    private NodeBuilder() {
      values = new LinkedHashSet<>();
      probs = new ArrayList<>();
    }

    public NodeBuilder from(Variable v) {
      this.id = v.id;
      this.name = v.name;
      this.values = v.values;
      return this;
    }

    public NodeBuilder id(String val) {
      id = val;
      return this;
    }

    public NodeBuilder name(String val) {
      name = val;
      return this;
    }

    public NodeBuilder value(String val) {
      if(!values.contains(val)) {
        values.add(val);
      }
      return this;
    }

    public NodeBuilder values(Set<String> val) {
      values = val;
      return this;
    }

    public NodeBuilder probs(List<Double> probs) {
      this.probs = probs;
      return this;
    }

    public NodeBuilder prob(Double prob) {
      this.probs.add(prob);
      return this;
    }

    public Node build() {
      return new Node(this);
    }
  }
}
