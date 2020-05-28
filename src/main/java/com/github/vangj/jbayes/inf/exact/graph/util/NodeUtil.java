package com.github.vangj.jbayes.inf.exact.graph.util;

import com.github.vangj.jbayes.inf.exact.graph.Node;
import java.util.ArrayList;
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
   *
   * @param nodes List of nodes.
   * @return Id.
   */
  public static String id(List<Node> nodes) {
    return id(nodes, "[", "]");
  }

  /**
   * Gets the string id composed of the specified nodes.
   *
   * @param nodes  List of nodes.
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
   *
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
   *
   * @param lists List of lists.
   * @param <T>   Type.
   * @return List of Lists.
   */
  public static <T> List<List<T>> product(List<List<T>> lists) {
    List<List<T>> result = new ArrayList<>();
    int nZeroSizes = lists.stream()
        .filter(list -> list.size() == 0)
        .mapToInt(list -> 1)
        .sum();
    if (nZeroSizes > 0) {
      return result;
    }

    for (T e : lists.get(0)) {
      List<T> l = new ArrayList<T>();
      l.add(e);
      result.add(l);
    }

    for (int i = 1; i < lists.size(); ++i) {
      List<List<T>> temp = new ArrayList<>();
      for (List<T> e : result) {
        for (T f : lists.get(i)) {
          List<T> eTmp = new ArrayList<>(e);
          eTmp.add(f);
          temp.add(eTmp);
        }
      }
      result = temp;
    }
    return result;
  }

  /**
   * Groups a list into sub-lists of size n.
   *
   * @param n    Size of each list.
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

    subLists.add(subList);

    return subLists;
  }

  public static int bisectRight(List<Double> A, double x) {
    return bisect_right(A, x, 0, A.size());
  }

  private static int bisect_right(List<Double> A, double x, int lo, int hi) {
    while (lo < hi) {
      int mid = (lo + hi) / 2;
      if (x < A.get(mid)) {
        hi = mid;
      } else {
        lo = mid + 1;
      }
    }
    return lo;
  }
}
