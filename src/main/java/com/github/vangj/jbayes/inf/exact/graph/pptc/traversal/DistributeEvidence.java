package com.github.vangj.jbayes.inf.exact.graph.pptc.traversal;

import com.github.vangj.jbayes.inf.exact.graph.pptc.Clique;
import com.github.vangj.jbayes.inf.exact.graph.pptc.JoinTree;
import com.github.vangj.jbayes.inf.exact.graph.pptc.SepSet;

public class DistributeEvidence {

  private final JoinTree joinTree;
  private final Clique startClique;
  private final Listener listener;

  public DistributeEvidence(JoinTree joinTree, Clique startClique, Listener listener) {
    this.joinTree = joinTree;
    this.startClique = startClique;
    this.listener = listener;
  }

  public void start() {
    startClique.mark();
    joinTree.neighbors(startClique).forEach(sepSet -> {
      joinTree.neighbors(sepSet)
          .stream()
          .filter(clique -> !clique.isMarked())
          .forEach(y -> {
            if (null != listener) {
              listener.cliqueVisited(joinTree, startClique, (SepSet) sepSet, y);
            }
            walk(startClique, (SepSet) sepSet, y);
          });
    });
  }

  private void walk(Clique x, SepSet s, Clique y) {
    y.mark();
    joinTree.neighbors(y).forEach(sepSet -> {
      joinTree.neighbors(sepSet)
          .stream()
          .filter(clique -> !clique.isMarked())
          .forEach(clique -> {
            if (null != listener) {
              listener.cliqueVisited(joinTree, y, (SepSet) sepSet, clique);
            }
            walk(y, (SepSet) sepSet, clique);
          });
    });
  }

  public interface Listener {

    void cliqueVisited(JoinTree joinTree, Clique x, SepSet s, Clique y);
  }
}
