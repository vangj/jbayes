package com.github.vangj.jbayes.inference;

import com.github.vangj.jbayes.inference.util.CptUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Graph.
 */
public class Graph {
  private List<Node> nodes;
  private Map<String, Node> _nodes = new HashMap<>();

  public Graph() {
    nodes = new ArrayList<>();
  }

  public Node getNode(String name) {
    synchronized (_nodes) {
      if(null == _nodes || 0 == _nodes.size()) {
        _nodes = new HashMap<>();
        nodes.forEach(n -> _nodes.put(n.getName(), n));
      }
    }

    return _nodes.get(name);
  }

  public void observe(String name, String value) {
    Node node = getNode(name);
    if(null == node) {
      return;
    }
    node.observe(value);
  }

  public void unobserve(String name) {
    Node node = getNode(name);
    if(null == node) {
      return;
    }
    node.unobserve();
  }

  public void addNode(Node node) {
    nodes.add(node);
  }

  /**
   * Reinitializes the nodes' CPTs.
   */
  public void reinit() {
    for(Node node : nodes) {
      Cpt cpt = CptUtil.build(node);
      node.setCpt(cpt);
    }
  }

  /**
   * Performs the sampling.
   * @param samples Total samples to generate.
   * @return Likelihood-weighted sum.
   */
  public double sample(int samples) {
    nodes.forEach(Node::resetSampledLw);

    double lwSum = 0.0d;

    for(int count=0; count < samples; count++) {
      for(Node node : nodes) {
        if(!node.isObserved()) {
          node.setValue(-1);
        }
        node.setWasSampled(false);
      }

      double fa = 1.0d;
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
