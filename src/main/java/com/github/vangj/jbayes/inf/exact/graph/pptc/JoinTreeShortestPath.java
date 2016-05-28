package com.github.vangj.jbayes.inf.exact.graph.pptc;

import java.util.LinkedHashSet;
import java.util.Set;

public class JoinTreeShortestPath {
  public interface Listener {
    void pre(JoinTree joinTree, Clique start, Clique stop);
    void visited(Clique clique);
    void post(JoinTree joinTree, Clique start, Clique stop);
  }

  private JoinTree joinTree;
  private Clique start;
  private Clique stop;
  private Set<Clique> seen;
  private Listener listener;

  private JoinTreeShortestPath(JoinTree joinTree, Clique start, Clique stop, Listener listener) {
    this.joinTree = joinTree;
    this.start = start;
    this.stop = stop;
    this.seen = new LinkedHashSet<>();
    this.listener = listener;
  }

  public static boolean exists(JoinTree joinTree, Clique start, Clique stop, Listener listener) {
    return (new JoinTreeShortestPath(joinTree, start, stop, listener)).search();
  }

  private boolean search() {
    if(null != listener) {
      listener.pre(joinTree, start, stop);
    }

    if(start.equals(stop)) {
      if(null != listener) {
        listener.post(joinTree, start, stop);
      }
      return false;
    }

    boolean result = search(start);

    if(null != listener) {
      listener.post(joinTree, start, stop);
    }

    return result;
  }

  private boolean search(Clique clique) {
    if(null != listener) {
      listener.visited(clique);
    }

    Set<Clique> neighbors = joinTree.neighbors(clique);
    if(null == neighbors || neighbors.size() == 0) {
      return false;
    }

    if(neighbors.contains(stop)) {
      return true;
    } else {
      seen.add(clique);
      for(Clique neighbor : neighbors) {
        if(!seen.contains(neighbor)) {
          if(search(neighbor)) {
            return true;
          }
        }
      }
    }

    return false;
  }
}
