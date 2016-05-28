package com.github.vangj.jbayes.inf.exact.graph.pptc;

import com.github.vangj.jbayes.inf.exact.graph.Dag;
import com.github.vangj.jbayes.inf.exact.graph.Node;
import com.github.vangj.jbayes.inf.exact.graph.Ug;

import java.util.List;

/**
 * Moralizes a DAG.
 */
public class Moralize {
  private Moralize() {

  }

  /**
   * Creates an undirected graph from a DAG that is
   * moralized.
   * @param dag DAG.
   * @return Moralized undirected graph.
   */
  public static Ug moralize(Dag dag) {
    Ug ug = new Ug();
    dag.nodes().forEach(node -> ug.addNode(node));
    dag.edges().forEach(edge -> ug.addEdge(edge.getLeft(), edge.getRight()));
    dag.nodes().forEach(node -> {
      List<Node> parents = dag.parents(node);
      final int size = parents.size();
      for(int i=0; i < size; i++) {
        Node pa1 = parents.get(i);
        for(int j=i+1; j < size; j++) {
          Node pa2 = parents.get(j);
          ug.addEdge(pa1, pa2);
        }
      }
    });
    return ug;
  }
}
