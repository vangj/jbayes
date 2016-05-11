package net.jbayes;

public class NodeUtil {
  private NodeUtil() {

  }

  public static int productOfDomains(Node node) {
    int product = node.numValues();
    for(Node parent : node.getParents()) {
      product *= parent.numValues();
    }
    return product;
  }
}
