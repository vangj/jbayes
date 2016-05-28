package com.github.vangj.jbayes.inf.exact.graph.pptc;

/**
 * Edge in join tree.
 */
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
    if(null == object || !(object instanceof Edge)) {
      return false;
    }
    Edge that = (Edge)object;
    return this.hashCode() == that.hashCode();
  }

  /**
   * Gets the left clique.
   * @return Clique.
   */
  public Clique left() {
    return left;
  }

  /**
   * Gets the right clique.
   * @return Clique.
   */
  public Clique right() {
    return right;
  }

  /**
   * Gets the clique with a smaller id.
   * @return Clique.
   */
  private Clique smaller() {
    if(left.id().compareTo(right.id()) <= 0) {
      return left;
    }
    return right;
  }

  /**
   * Gets the clique with a larger id.
   * @return Clique.
   */
  private Clique bigger() {
    if(left.id().compareTo(right.id()) > 0) {
      return left;
    }
    return right;
  }
}
