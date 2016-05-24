package com.github.vangj.jbayes.inf.exact.graph;

public class Edge {
  public enum Type {
    DIRECTED, UNDIRECTED
  }

  protected Node left;
  protected Node right;
  protected Type type = Type.UNDIRECTED;

  public Edge() {

  }

  public Edge(Node left, Node right) {
    this(left, right, Type.UNDIRECTED);
  }

  public Edge(Node left, Node right, Type type) {
    this.left = left;
    this.right = right;
    this.type = type;
  }

  private Node smaller() {
    if(left.id.compareTo(right.id) <= 0) {
      return left;
    }
    return right;
  }

  private Node bigger() {
    if(left.id.compareTo(right.id) > 0) {
      return left;
    }
    return right;
  }

  @Override
  public String toString() {
    return (new StringBuilder())
        .append((type == Type.UNDIRECTED) ? smaller().id : left.id)
        .append((type == Type.UNDIRECTED) ? " -- " : " -> ")
        .append((type == Type.UNDIRECTED) ? bigger().id : right.id)
        .toString();
  }

  @Override
  public int hashCode() {
    return 37 + toString().hashCode();
  }

  @Override
  public boolean equals(Object object) {
    if(null == object || !(object instanceof Edge)) {
      return false;
    }

    Edge that = (Edge)object;
    if(this.type != that.type) {
      return false;
    }

    if(this.type == Type.UNDIRECTED) {
      return this.smaller().id.equals(that.smaller().id) &&
          this.bigger().id.equals(that.bigger().id);
    }

    return (this.left.id.equals(that.left.id) && this.right.id.equals(that.right.id));
  }

  private Edge(Builder builder) {
    left = builder.left;
    right = builder.right;
    type = builder.type;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public Node getLeft() {
    return left;
  }

  public Node getRight() {
    return right;
  }


  public static final class Builder {
    private Node left;
    private Node right;
    private Type type = Type.UNDIRECTED;

    private Builder() {
    }

    public Builder left(Node val) {
      left = val;
      return this;
    }

    public Builder right(Node val) {
      right = val;
      return this;
    }

    public Builder type(Type t) {
      type = t;
      return this;
    }

    public Edge build() {
      return new Edge(this);
    }
  }
}
