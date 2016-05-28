package com.github.vangj.jbayes.inf.exact.graph.traversal;

import com.github.vangj.jbayes.inf.exact.graph.Dag;
import com.github.vangj.jbayes.inf.exact.graph.Node;

import java.util.List;

public class DagShortestPath extends GraphShortestPath {

  private Dag dag;

  private DagShortestPath(Dag graph, Node start, Node stop, ShortestPathListener listener) {
    super(graph, start, stop, listener);
    this.dag = graph;
  }

  public static boolean exists(Dag graph, Node start, Node stop, ShortestPathListener listener) {
    return (new DagShortestPath(graph, start, stop, listener)).search();
  }

  private boolean search() {
    if(null != listener) {
      listener.pre(graph, start, stop);
    }

    if(start.equals(stop)) {
      if(null != listener) {
        listener.post(graph, start, stop);
      }
      return false;
    }

    boolean result = search(start);

    if(null != listener) {
      listener.post(graph, start, stop);
    }

    return result;
  }

  private boolean search(Node node) {
    if(null != listener) {
      listener.visited(node);
    }

    List<Node> children = dag.children(node);
    if(null == children || children.size() == 0) {
      return false;
    }

    if(children.contains(stop)) {
      return true;
    } else {
      seen.add(node);
      for(Node child : children) {
        if(!seen.contains(child)) {
          if(search(child)) {
            return true;
          }
        }
      }
    }

    return false;
  }
}
