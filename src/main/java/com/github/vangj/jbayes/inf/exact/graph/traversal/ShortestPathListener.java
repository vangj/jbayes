package com.github.vangj.jbayes.inf.exact.graph.traversal;

import com.github.vangj.jbayes.inf.exact.graph.Graph;
import com.github.vangj.jbayes.inf.exact.graph.Node;

public interface ShortestPathListener {

  void pre(Graph graph, Node start, Node stop);

  void visited(Node node);

  void post(Graph graph, Node start, Node stop);
}
