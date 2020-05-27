package com.github.vangj.jbayes.inf.prob;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.vangj.jbayes.inf.prob.util.CptUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Graph.
 */
@JsonInclude(content = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Graph {

  private boolean saveSamples = false;
  @JsonIgnore
  private final List<String[]> _samples = new ArrayList<>();
  private final List<Node> nodes;
  @JsonIgnore
  private Map<String, Node> _nodes = new HashMap<>();

  public Graph() {
    nodes = new ArrayList<>();
  }

  public boolean isSaveSamples() {
    return saveSamples;
  }

  public void setSaveSamples(boolean saveSamples) {
    this.saveSamples = saveSamples;
  }

  public List<Node> getNodes() {
    return nodes;
  }

  @JsonIgnore
  public String[] getNodeNames() {
    final int size = nodes.size();
    String[] names = new String[size];
    for (int i = 0; i < size; i++) {
      names[i] = nodes.get(i).getName();
    }
    return names;
  }

  @JsonIgnore
  public List<String[]> getSamples() {
    return _samples;
  }

  public Node getNode(String name) {
    synchronized (_nodes) {
      if (null == _nodes || 0 == _nodes.size()) {
        _nodes = new HashMap<>();
        nodes.forEach(n -> _nodes.put(n.getName(), n));
      }
    }

    return _nodes.get(name);
  }

  public void observe(String name, String value) {
    Node node = getNode(name);
    if (null == node) {
      return;
    }
    node.observe(value);
  }

  public void unobserve(String name) {
    Node node = getNode(name);
    if (null == node) {
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
    for (Node node : nodes) {
      Cpt cpt = CptUtil.build(node);
      node.setCpt(cpt);
    }
  }

  public void clearSamples() {
    _samples.clear();
  }

  /**
   * Performs the sampling.
   *
   * @param samples Total samples to generate.
   * @return Likelihood-weighted sum.
   */
  public double sample(int samples) {
    clearSamples();

    nodes.forEach(Node::resetSampledLw);

    double lwSum = 0.0d;
    final int numNodes = nodes.size();

    for (int count = 0; count < samples; count++) {
      for (Node node : nodes) {
        if (!node.isObserved()) {
          node.setValue(-1);
        }
        node.setWasSampled(false);
      }

      double fa = 1.0d;
      for (Node node : nodes) {
        fa *= node.sampleLw();
      }

      lwSum += fa;

      for (Node node : nodes) {
        node.saveSampleLw(fa);
      }

      if (saveSamples) {
        String[] sampledValues = new String[numNodes];
        for (int i = 0; i < numNodes; i++) {
          sampledValues[i] = nodes.get(i).getSampledValue();
        }
        _samples.add(sampledValues);
      }
    }

    return lwSum;
  }
}
