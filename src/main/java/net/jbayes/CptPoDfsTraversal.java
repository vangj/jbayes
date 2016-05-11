package net.jbayes;

/**
 * Post-order depth-first-search for CPT.
 */
public class CptPoDfsTraversal {
  /**
   * Listener interface for when a CPT is visited.
   */
  public static interface CptPoDfsTraversalListener {
    public void visited(Cpt cpt);
  }

  private Cpt cpt;
  private CptPoDfsTraversalListener listener;

  /**
   * Constructor.
   * @param cpt CPT.
   * @param listener
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
    for(Cpt children : cpt.getChildren()) {
      postOrder(children);
    }
    if(null != listener) {
      listener.visited(cpt);
    }
  }
}
