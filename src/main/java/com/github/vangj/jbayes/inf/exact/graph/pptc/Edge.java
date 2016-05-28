package com.github.vangj.jbayes.inf.exact.graph.pptc;

public class Edge {
  protected Clique left;
  protected Clique right;

  public Edge(Clique left, Clique right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public String toString() {
    return (new StringBuilder())
        .append(smaller().id())
        .append(" -- ")
        .append(bigger().id())
        .toString();
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public boolean equals(Object object) {
    if(null == object || !(object instanceof Clique)) {
      return false;
    }
    Clique that = (Clique)object;
    return this.hashCode() == that.hashCode();
  }

  public Clique left() {
    return left;
  }

  public Clique right() {
    return right;
  }

  private Clique smaller() {
    if(left.id().compareTo(right.id()) <= 0) {
      return left;
    }
    return right;
  }

  private Clique bigger() {
    if(left.id().compareTo(right.id()) > 0) {
      return left;
    }
    return right;
  }
}
