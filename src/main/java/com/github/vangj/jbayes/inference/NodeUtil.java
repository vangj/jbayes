package com.github.vangj.jbayes.inference;

/**
 * Node utility class.
 */
public class NodeUtil {
  private NodeUtil() {

  }

  /**
   * Gets the product of the domains for
   * the specified node and its parents (if any).
   * @param node Node.
   * @return Product of domains.
   */
  public static int productOfDomains(Node node) {
    int product = node.numValues();
    for(Node parent : node.getParents()) {
      product *= parent.numValues();
    }
    return product;
  }
}
