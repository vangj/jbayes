package com.github.vangj.jbayes.inf.exact.graph.pptc.blocks;

import com.github.vangj.jbayes.inf.exact.graph.pptc.Clique;
import com.github.vangj.jbayes.inf.exact.graph.pptc.JoinTree;
import com.github.vangj.jbayes.inf.exact.graph.pptc.traversal.CollectEvidence;
import com.github.vangj.jbayes.inf.exact.graph.pptc.traversal.DistributeEvidence;

import static com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.SingleMessagePass.singleMessagePass;

/**
 * Step 5. Global propagation.
 */
public class Propagation {
  private Propagation() {

  }

  /**
   * Performs global propagation on the join tree so that it can become consistent.
   * @param joinTree Join tree.
   */
  public static void propagate(JoinTree joinTree) {
    Clique x = joinTree.cliques().get(0);

    joinTree.unmarkCliques();
    collectEvidence(joinTree, x);

    joinTree.unmarkCliques();
    distributeEvidence(joinTree, x);
  }

  /**
   * Collect evidence phase.
   * @param joinTree Join tree.
   * @param start Clique (the starting clique).
   */
  private static void collectEvidence(JoinTree joinTree, Clique start) {
    CollectEvidence.Listener listener =
        (joinTree1, x, s, y) -> singleMessagePass(joinTree1, x, s, y);

    new CollectEvidence(joinTree, start, listener).start();
  }

  /**
   * Distribute evidence phase.
   * @param joinTree Join tree.
   * @param start Clique (the starting clique).
   */
  private static void distributeEvidence(JoinTree joinTree, Clique start) {
    DistributeEvidence.Listener listener =
        (joinTree1, x, s, y) -> singleMessagePass(joinTree1, x, s, y);

    new DistributeEvidence(joinTree, start, listener).start();
  }
}
