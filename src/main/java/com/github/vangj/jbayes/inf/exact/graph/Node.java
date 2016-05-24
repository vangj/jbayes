package com.github.vangj.jbayes.inf.exact.graph;

import java.util.LinkedHashSet;
import java.util.Set;

public class Node extends Variable {
  public Node() {

  }

  public Node(Variable v) {
    this.id = v.id;
    this.name = v.name;
    this.values = v.values;
  }

  private Node(NodeBuilder builder) {
    id = builder.id;
    name = builder.name;
    values = builder.values;
  }

  public static NodeBuilder builder() {
    return new NodeBuilder();
  }


  public static final class NodeBuilder {
    private String id;
    private String name;
    private Set<String> values;

    private NodeBuilder() {
      values = new LinkedHashSet<>();
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

    public Node build() {
      return new Node(this);
    }
  }
}