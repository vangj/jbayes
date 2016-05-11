package com.github.vangj.jbayes.inference;

import java.util.ArrayList;
import java.util.List;

/**
 * Graph.
 */
public class Graph {
  private List<Node> nodes;

  public Graph() {
    nodes = new ArrayList<>();
  }

  public void addNode(Node node) {
    nodes.add(node);
  }

  /**
   * Reinitializes the nodes' CPTs.
   */
  public void reinit() {
    for(Node node : nodes) {
      Cpt cpt = CptBuilder.build(node);
      node.setCpt(cpt);
    }
  }

  /**
   * Performs the sampling.
   * @param samples Total samples to generate.
   * @return Likelihood-weighted sum.
   */
  public double sample(int samples) {
    double lwSum = 0.0d;

    for(int count=0; count < samples; count++) {
      for(Node node : nodes) {
        if(!node.isObserved()) {
          node.setValue(-1);
        }
        node.setWasSampled(false);
      }

      int fa = 1;
      for(Node node : nodes) {
        fa *= node.sampleLw();
      }

      lwSum += fa;

      for(Node node: nodes) {
        node.saveSampleLw(fa);
      }
    }

    return lwSum;
  }
}
