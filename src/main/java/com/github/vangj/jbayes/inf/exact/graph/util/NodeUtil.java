package com.github.vangj.jbayes.inf.exact.graph.util;

import com.github.vangj.jbayes.inf.exact.graph.Node;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Node util.
 */
public class NodeUtil {
  private NodeUtil() {

  }

  /**
   * Gets the string id composed of the specified nodes.
   * @param nodes List of nodes.
   * @return Id.
   */
  public static String id(List<Node> nodes) {
    return id(nodes, "[", "]");
  }

  /**
   * Gets the string id composed of the specified nodes.
   * @param nodes List of nodes.
   * @param prefix Prefix. e.g. [
   * @param suffix Suffix. e.g. ]
   * @return Id.
   */
  public static String id(List<Node> nodes, String prefix, String suffix) {
    return NodeUtil.sort(nodes).stream()
        .map(Node::getId)
        .collect(Collectors.joining(",", prefix, suffix));
  }

  /**
   * Sorts the nodes according to id.
   * @param nodes List of nodes.
   * @return Sorted list of nodes.
   */
  public static List<Node> sort(List<Node> nodes) {
    return nodes.stream()
        .sorted((o1, o2) -> o1.getId().compareTo(o2.getId()))
        .collect(Collectors.toList());
  }
}
