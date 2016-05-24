package com.github.vangj.jbayes.inf.exact.graph.traversal;

import com.github.vangj.jbayes.inf.exact.graph.Node;
import com.github.vangj.jbayes.inf.exact.graph.Pdag;

import java.util.HashSet;
import java.util.Set;

public class PdagShortestPath extends GraphShortestPath {
  private Pdag pdag;
  protected PdagShortestPath(Pdag pdag, Node start, Node stop, ShortestPathListener listener) {
    super(pdag, start, stop, listener);
    this.pdag = pdag;
  }

  public static boolean exists(Pdag graph, Node start, Node stop, ShortestPathListener listener) {
    return (new PdagShortestPath(graph, start, stop, listener)).search();
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

    Set<Node> outNodes = outNodes(node);
    if(outNodes.contains(stop)) {
      return true;
    } else {
      seen.add(node);
      for(Node outNode : outNodes) {
        if(!seen.contains(outNode)) {
          if(search(outNode)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  private Set<Node> outNodes(Node node) {
    Set<Node> outNodes = new HashSet<>();
    outNodes.addAll(pdag.neighbors(node));
    outNodes.removeAll(pdag.parents(node));
    return outNodes;
  }
}
