package com.github.vangj.jbayes.inf.prob.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.vangj.jbayes.inf.prob.Graph;
import com.github.vangj.jbayes.inf.prob.util.CptUtil;
import com.github.vangj.jbayes.inf.prob.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON graph, used for serialization/deserialization (serde).
 */
@JsonInclude(content = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonGraph {


  private Map<String, Node> nodes;
  private Map<String, List<String>> parents;
  private Map<String, double[][]> cpts;

  public JsonGraph() {

  }

  public JsonGraph(Graph g) {
    nodes = new HashMap<>();
    parents = new HashMap<>();
    cpts = new HashMap<>();

    g.getNodes().forEach(n -> nodes.put(n.getName(), n));

    g.getNodes().forEach(n -> {
      List<String> pa = new ArrayList<>();
      n.getParents().forEach(p -> pa.add(p.getName()));
      parents.put(n.getName(), pa);
    });

    g.getNodes().forEach(n -> cpts.put(n.getName(), CptUtil.getMatrix(n.getCpt())));
  }

  public Map<String, double[][]> getCpts() {
    return cpts;
  }

  public Map<String, Node> getNodes() {
    return nodes;
  }

  public Map<String, List<String>> getParents() {
    return parents;
  }

  /**
   * Converts JsonGraph to Graph.
   * @return Graph.
   */
  public Graph toGraph() {
    nodes.forEach((name, node) -> {
      if(parents.containsKey(name)) {
        parents.get(name).forEach(paName -> node.addParent(nodes.get(paName)));
      }
      node.setCpt(cpts.get(name));
    });

    Graph g = new Graph();
    nodes.values().forEach(n -> g.addNode(n));
    return g;
  }
}
