package com.github.vangj.jbayes.inf.exact.graph;

/**
 * An undirected graph.
 */
public class Ug extends Graph {

  @Override
  protected Graph instance() {
    return new Ug();
  }

  @Override
  public Graph addEdge(Edge edge) {
    edge.type = Edge.Type.UNDIRECTED;
    return super.addEdge(edge);
  }
}
