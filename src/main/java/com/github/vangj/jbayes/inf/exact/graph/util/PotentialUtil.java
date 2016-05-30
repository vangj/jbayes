package com.github.vangj.jbayes.inf.exact.graph.util;

import com.github.vangj.jbayes.inf.exact.graph.Node;
import com.github.vangj.jbayes.inf.exact.graph.lpd.Potential;
import com.github.vangj.jbayes.inf.exact.graph.lpd.PotentialEntry;
import com.github.vangj.jbayes.inf.exact.graph.pptc.Clique;
import com.github.vangj.jbayes.inf.exact.graph.pptc.JoinTree;

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

  /**
   * Marginalizes the potential (associated with the specified clique) over the specified list of nodes.
   * In words, marginalize out the specified list of nodes.
   * @param joinTree Join tree.
   * @param clique Clique.
   * @param nodes List of nodes.
   * @return Potential.
   */
  public static Potential marginalizeOut(JoinTree joinTree, Clique clique, List<Node> nodes) {
    Potential potential = getPotential(clique.nodesMinus(nodes));
    Potential cliquePotential = joinTree.getPotential(clique);

    potential.entries().forEach(entry -> {
      List<PotentialEntry> matchedEntries = cliquePotential.match(entry);
      double t = 0.0;
      for(PotentialEntry matchedEntry : matchedEntries) {
        t += matchedEntry.getValue();
      }
      entry.setValue(t);
    });

    return potential;
  }

  /**
   * Marginalizes the potential (associated with the specified clique) for the specified list of nodes.
   * In words, marginalize for the specified list of nodes (removing other nodes).
   * @param joinTree Join tree.
   * @param clique Clique.
   * @param nodes List of nodes.
   * @return Potential.
   */
  public static Potential marginalizeFor(JoinTree joinTree, Clique clique, List<Node> nodes) {
    Potential potential = getPotential(nodes);
    Potential cliquePotential = joinTree.getPotential(clique);

    potential.entries().forEach(entry -> {
      List<PotentialEntry> matchedEntries = cliquePotential.match(entry);
      double t = 0.0;
      for(PotentialEntry matchedEntry : matchedEntries) {
        t += matchedEntry.getValue();
      }
      entry.setValue(t);
    });

    return potential;
  }

  /**
   * Normalizes the entry values for the specified potentials so they sum to 1.
   * @param potential Potential.
   * @return Potential.
   */
  public static Potential normalize(Potential potential) {
    double sum = 0.0d;
    for(PotentialEntry entry : potential.entries()) {
      sum += entry.getValue();
    }

    for(PotentialEntry entry : potential.entries()) {
      double d = entry.getValue() / sum;
      entry.setValue(d);
    }

    return potential;
  }

  /**
   * Divides two potentials. The potentials must have identical entries.
   * @param numerator Potential.
   * @param denominator Potential.
   * @return Potential.
   */
  public static Potential divide(Potential numerator, Potential denominator) {
    Potential potential = new Potential();
    numerator.entries().forEach(entry -> {
      List<PotentialEntry> entries = denominator.match(entry);
      if(null != entries && entries.size() > 0) {
        PotentialEntry e = entries.get(0);
        double d = (isZero(entry.getValue()) || isZero(e.getValue())) ? 0.0d : entry.getValue() / e.getValue();
        potential.addEntry(entry.duplicate().setValue(d));
      }
    });
    return potential;
  }

  private static boolean isZero(double d) {
    return (0.0d == d);
  }

  /**
   * Multiplies the two potentials. NOTE: order is important. The first potential
   * must subsume the smaller potential.
   * @param bigger Potential.
   * @param smaller Potential.
   */
  public static void multiply(Potential bigger, Potential smaller) {
    smaller.entries().forEach(entry -> {
      List<PotentialEntry> entries = bigger.match(entry);
      entries.forEach(e -> {
        double d = e.getValue() * entry.getValue();
        e.setValue(d);
      });
    });

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
      potential.entries().get(i).setValue(prob);
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
   * @param <T> Type.
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
