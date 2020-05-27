package com.github.vangj.jbayes.inf.exact.graph.pptc.traversal;

import com.github.vangj.jbayes.inf.exact.graph.pptc.Clique;
import com.github.vangj.jbayes.inf.exact.graph.pptc.JoinTree;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Shortest path algorithm for join tree.
 */
public class JoinTreeShortestPath {

  private final JoinTree joinTree;
  private final Clique start;
  private final Clique stop;
  private final Set<Clique> seen;
  private final Listener listener;
  /**
   * Ctor.
   *
   * @param joinTree Join tree.
   * @param start    Start clique.
   * @param stop     Stop clique.
   * @param listener Listener.
   */
  private JoinTreeShortestPath(JoinTree joinTree, Clique start, Clique stop, Listener listener) {
    this.joinTree = joinTree;
    this.start = start;
    this.stop = stop;
    this.seen = new LinkedHashSet<>();
    this.listener = listener;
  }

  /**
   * Checks to see if there is a path between the specified start and stop cliques.
   *
   * @param joinTree Join tree.
   * @param start    Start clique.
   * @param stop     Stop clique.
   * @param listener Listener.
   * @return Boolean.
   */
  public static boolean exists(JoinTree joinTree, Clique start, Clique stop, Listener listener) {
    return (new JoinTreeShortestPath(joinTree, start, stop, listener)).search();
  }

  /**
   * Searches for shortest path.
   *
   * @return Boolean indicating if path exists.
   */
  private boolean search() {
    if (null != listener) {
      listener.pre(joinTree, start, stop);
    }

    if (start.equals(stop)) {
      if (null != listener) {
        listener.post(joinTree, start, stop);
      }
      return false;
    }

    boolean result = search(start);

    if (null != listener) {
      listener.post(joinTree, start, stop);
    }

    return result;
  }

  /**
   * Recursive search for path.
   *
   * @param clique Clique.
   * @return Boolean indicating if path exists.
   */
  private boolean search(Clique clique) {
    if (null != listener) {
      listener.visited(clique);
    }

    Set<Clique> neighbors = joinTree.neighbors(clique);
    if (null == neighbors || neighbors.size() == 0) {
      return false;
    }

    if (neighbors.contains(stop)) {
      return true;
    } else {
      seen.add(clique);
      for (Clique neighbor : neighbors) {
        if (!seen.contains(neighbor)) {
          if (search(neighbor)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  /**
   * Listener interface for shortest path algorithm.
   */
  public interface Listener {

    void pre(JoinTree joinTree, Clique start, Clique stop);

    void visited(Clique clique);

    void post(JoinTree joinTree, Clique start, Clique stop);
  }
}
