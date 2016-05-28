package com.github.vangj.jbayes.inf.exact.graph.util;

import com.github.vangj.jbayes.inf.exact.graph.Node;

import java.util.List;
import java.util.stream.Collectors;

public class NodeUtil {
  private NodeUtil() {

  }

  public static String id(List<Node> nodes) {
    return id(nodes, "[", "]");
  }

  public static String id(List<Node> nodes, String prefix, String suffix) {
    return NodeUtil.sort(nodes).stream()
        .map(Node::getId)
        .collect(Collectors.joining(",", prefix, suffix));
  }

  public static List<Node> sort(List<Node> nodes) {
    return nodes.stream()
        .sorted((o1, o2) -> o1.getId().compareTo(o2.getId()))
        .collect(Collectors.toList());
  }
}
