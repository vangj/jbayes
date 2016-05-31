package com.github.vangj.jbayes.inf.exact.graph.pptc.blocks;

import com.github.vangj.jbayes.inf.exact.graph.Dag;
import com.github.vangj.jbayes.inf.exact.graph.pptc.JoinTree;

import static com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.Initialization.*;
import static com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.Propagation.*;
import static com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.Moralize.*;
import static com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.Triangulate.*;
import static com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.Transform.*;

/**
 * Controls inference based on evidence.
 */
public class InferenceControl implements JoinTree.Listener {

  /**
   * Creates a join tree from a DAG with inference enabled.
   * @param dag DAG.
   * @return Join tree.
   */
  public static JoinTree apply(Dag dag) {
    JoinTree joinTree = propagate(initialization(transform(triangulate(moralize(dag)))))
        .setListener(new InferenceControl());
    return joinTree;
  }

  @Override
  public void evidenceRetracted(JoinTree joinTree) {
    initialization(joinTree);
    propagate(joinTree);
  }

  @Override
  public void evidenceUpdated(JoinTree joinTree) {
    propagate(joinTree);
  }

  @Override
  public void evidenceNoChange(JoinTree joinTree) {
    //do nothing
  }
}
