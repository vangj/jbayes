package com.github.vangj.jbayes.inf.exact.graph.traversal;

import com.github.vangj.jbayes.inf.exact.graph.Graph;
import com.github.vangj.jbayes.inf.exact.graph.Node;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GraphShortestPath {

  protected Graph graph;
  protected Node start;
  protected Node stop;
  protected Set<Node> seen;
  protected ShortestPathListener listener;

  protected GraphShortestPath(Graph graph, Node start, Node stop, ShortestPathListener listener) {
    this.graph = graph;
    this.start = start;
    this.stop = stop;
    this.seen = new LinkedHashSet<>();
    this.listener = listener;
  }

  public static boolean exists(Graph graph, Node start, Node stop, ShortestPathListener listener) {
    return (new GraphShortestPath(graph, start, stop, listener)).search();
  }

  private boolean search() {
    if (null != listener) {
      listener.pre(graph, start, stop);
    }

    if (start.equals(stop)) {
      if (null != listener) {
        listener.post(graph, start, stop);
      }
      return false;
    }

    boolean result = search(start);

    if (null != listener) {
      listener.post(graph, start, stop);
    }

    return result;
  }

  private boolean search(Node node) {
    if (null != listener) {
      listener.visited(node);
    }

    List<Node> neighbors = graph.neighbors(node);
    if (null == neighbors || neighbors.size() == 0) {
      return false;
    }

    if (neighbors.contains(stop)) {
      return true;
    } else {
      seen.add(node);
      for (Node neighbor : neighbors) {
        if (!seen.contains(neighbor)) {
          if (search(neighbor)) {
            return true;
          }
        }
      }
    }

    return false;
  }
}
