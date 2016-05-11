package net.jbayes;

/**
 * Post-order depth-first-search for Cpt.
 */
public class CptPoDfsTraversal {
  public static interface CptPoDfsTraversalListener {
    public void visited(Cpt cpt);
  }

  private Cpt cpt;
  private CptPoDfsTraversalListener listener;

  public CptPoDfsTraversal(Cpt cpt, CptPoDfsTraversalListener listener) {
    this.cpt = cpt;
    this.listener = listener;
  }

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
