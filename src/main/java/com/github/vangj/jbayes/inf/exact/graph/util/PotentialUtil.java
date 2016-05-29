package com.github.vangj.jbayes.inf.exact.graph.util;

import com.github.vangj.jbayes.inf.exact.graph.Node;
import com.github.vangj.jbayes.inf.exact.graph.lpd.Potential;
import com.github.vangj.jbayes.inf.exact.graph.lpd.PotentialEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Potential util.
 */
public class PotentialUtil {
  private PotentialUtil() {

  }

  public static void multiply(Potential p1, Potential p2) {

  }

  /**
   * Makes a string from the map.
   * @param entries Map.
   * @return String.
   */
  public static String asString(Map<String, String> entries) {
    return entries.entrySet().stream()
        .map(entry -> (new StringBuilder())
            .append(entry.getKey())
            .append('=')
            .append(entry.getValue())
            .toString())
        .collect(Collectors.joining(",", "[" , "]"));
  }

  /**
   * Creates a new map sorted by the id of the specified map.
   * @param map Map.
   * @return Map sorted by id.
   */
  public static Map<String, String> sortByKeys(Map<String, String> map) {
    Map<String, String> out = new TreeMap<>(map);
    return out;
  }

  private static List<Node> merge(Node node, List<Node> parents) {
    List<Node> list = new ArrayList<>(parents);
    list.add(node);
    return list;
  }

  /**
   * Creates a potential from the node and its parents. If
   * the specified node has any conditional probabilities, the potential
   * entries will be set to them.
   * @param node Node.
   * @param parents Parent nodes.
   * @return Potential.
   */
  public static Potential getPotential(Node node, List<Node> parents) {
    Potential potential = getPotential(merge(node, parents));

    final int total = Math.min(node.probs().size(), potential.entries().size());
    for(int i=0; i < total; i++) {
      Double prob = node.probs().get(i);
      potential.entries().get(i).value(prob);
    }

    return potential;
  }

  /**
   * Creates a potential from the specified list of nodes. All probabilities
   * are initialized to 1.
   * @param nodes List of nodes.
   * @return Potential.
   */
  public static Potential getPotential(List<Node> nodes) {
    List<List<String>> valueLists = new ArrayList<>();
    nodes.forEach(node -> {
      valueLists.add(new ArrayList<>(node.getValues()));
    });

    List<List<String>> cartesian = cartesian(valueLists);
    final int size = nodes.size();
    Potential potential = new Potential();
    cartesian.forEach(values -> {
      PotentialEntry entry = new PotentialEntry();
      for(int i=0; i < size; i++) {
        String value = values.get(i);
        String id = nodes.get(i).getId();
        entry.add(id, value);
      }
      potential.addEntry(entry);
    });

    return potential;
  }

  /**
   * Creates cartesian product between the list of lists.
   * @param lists List of lists.
   * @param <T>
   * @return List of lists representing cartesian product.
   */
  public static <T> List<List<T>> cartesian(List<List<T>> lists) {
    List<List<T>> results = new ArrayList<>();
    if (lists.size() == 0) {
      results.add(new ArrayList<>());
      return results;
    } else {
      List<T> first = lists.get(0);
      List<List<T>> remaining = cartesian(lists.subList(1, lists.size()));
      for (T condition : first) {
        for (List<T> rlist : remaining) {
          ArrayList<T> result = new ArrayList<>();
          result.add(condition);
          result.addAll(rlist);
          results.add(result);
        }
      }
    }
    return results;
  }
}
