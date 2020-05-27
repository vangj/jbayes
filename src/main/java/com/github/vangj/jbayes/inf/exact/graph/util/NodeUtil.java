package com.github.vangj.jbayes.inf.exact.graph.util;

import static java.util.Arrays.asList;
import static java.util.Optional.of;

import com.github.vangj.jbayes.inf.exact.graph.Node;

import java.util.ArrayList;
import java.util.Collections;
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

  /**
   * Generates the cartesian product of a list of lists.
   * @param lists List.
   * @return List of lists.
   */
  public static List<?> product(List<?>... lists) {
    if (lists.length >= 2) {
      List<?> product = lists[0];
      for (int i = 1; i < lists.length; i++) {
        product = product(product, lists[i]);
      }
      return product;
    }

    return Collections.emptyList();
  }

  private static <A, B> List<?> product(List<A> a, List<B> b) {
    return of(a.stream()
        .map(e1 -> of(b.stream().map(e2 -> asList(e1, e2)).collect(Collectors.toList())).orElse(Collections.emptyList()))
        .flatMap(List::stream)
        .collect(Collectors.toList())).orElse(Collections.emptyList());
  }

  /**
   * Groups a list into sub-lists of size n.
   * @param n Size of each list.
   * @param list List to group.
   * @return List of lists.
   */
  public static List<List<Double>> groupList(final int n, List<Double> list) {
    int nLists = list.size() % n == 0 ? list.size() / n : list.size() / n + 1;

    List<List<Double>> subLists = new ArrayList<>();

    List<Double> subList = new ArrayList<>();
    final int listSize = list.size();
    for (int i = 0; i < listSize; i++) {
      if (i != 0 && i % n == 0) {
        List<Double> temp = new ArrayList<>(subList);
        subLists.add(temp);
        subList.clear();
      }
      subList.add(list.get(i));
    }

    return subLists;
  }

  public static int bisectRight(List<Double> A, double x) {
    return bisect_right(A, x, 0, A.size());
  }

  private static int bisect_right(List<Double> A, double x, int lo, int hi) {
    while (lo < hi) {
      int mid = (lo+hi)/2;
      if (x < A.get(mid)) hi = mid;
      else lo = mid+1;
    }
    return lo;
  }
}
