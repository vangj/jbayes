package com.github.vangj.jbayes.inf.prob.cpt;

import com.github.vangj.jbayes.inf.prob.Cpt;

/**
 * Post-order depth-first-search for CPT.
 */
public class CptPoDfsTraversal {

  private final Cpt cpt;
  private final CptPoDfsTraversalListener listener;
  /**
   * Constructor.
   *
   * @param cpt      CPT.
   * @param listener Listener.
   */
  public CptPoDfsTraversal(Cpt cpt, CptPoDfsTraversalListener listener) {
    this.cpt = cpt;
    this.listener = listener;
  }

  /**
   * Starts the traversal.
   */
  public void start() {
    postOrder(cpt);
  }

  private void postOrder(Cpt cpt) {
    for (Cpt children : cpt.getChildren()) {
      postOrder(children);
    }
    if (null != listener) {
      listener.visited(cpt);
    }
  }

  /**
   * Listener interface for when a CPT is visited.
   */
  public interface CptPoDfsTraversalListener {

    void visited(Cpt cpt);
  }
}
