package com.github.vangj.jbayes.inf.exact.graph.pptc.blocks;

import static com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.SingleMessagePass.*;

import com.github.vangj.jbayes.inf.exact.graph.pptc.Clique;
import com.github.vangj.jbayes.inf.exact.graph.pptc.JoinTree;
import com.github.vangj.jbayes.inf.exact.graph.pptc.SepSet;
import com.github.vangj.jbayes.inf.exact.graph.pptc.traversal.CollectEvidence;

public class Propagation {
  private Propagation() {

  }

  public static void propagate(JoinTree joinTree) {
    Clique x = joinTree.cliques().get(0);

    joinTree.unmarkCliques();
    collectEvidence(joinTree, x);

    joinTree.unmarkCliques();
    distributeEvidence(joinTree, x);
  }

  private static void collectEvidence(JoinTree joinTree, Clique x) {
    CollectEvidence.Listener listener = new CollectEvidence.Listener() {
      @Override public void cliqueVisited(JoinTree joinTree, Clique x, SepSet s, Clique y) {
        singleMessagePass(joinTree, x, s, y);
      }
    };

    new CollectEvidence(joinTree, x, listener).start();
  }

  private static void distributeEvidence(JoinTree joinTree, Clique x) {

  }
}
