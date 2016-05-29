package com.github.vangj.jbayes.inf.exact.graph.pptc.blocks;

import static com.github.vangj.jbayes.inf.exact.graph.util.PotentialUtil.*;

import com.github.vangj.jbayes.inf.exact.graph.lpd.Potential;
import com.github.vangj.jbayes.inf.exact.graph.pptc.Clique;
import com.github.vangj.jbayes.inf.exact.graph.pptc.JoinTree;
import com.github.vangj.jbayes.inf.exact.graph.pptc.SepSet;

/**
 * Part of global propagation.
 */
public class SingleMessagePass {
  private SingleMessagePass() {

  }

  /**
   * A single message pass between the specified cliques joined by the specified separation set.
   * @param joinTree Join tree.
   * @param x Clique.
   * @param s Separation set.
   * @param y Clique.
   */
  public static void singleMessagePass(JoinTree joinTree, Clique x, SepSet s, Clique y) {
    Potential oldSepSetPotential = joinTree.getPotential(s);
    Potential yPotential = joinTree.getPotential(y);

    //projection
    Potential newSepSetPotential = marginalizeFor(joinTree, x, s.nodes());
    joinTree.addPotential(s, newSepSetPotential);

    //absorption
    multiply(yPotential, divide(newSepSetPotential, oldSepSetPotential));
  }
}
