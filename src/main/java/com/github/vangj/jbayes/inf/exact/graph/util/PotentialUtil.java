package com.github.vangj.jbayes.inf.exact.graph.util;

import com.github.vangj.jbayes.inf.exact.graph.Node;
import com.github.vangj.jbayes.inf.exact.graph.lpd.Potential;
import com.github.vangj.jbayes.inf.exact.graph.lpd.PotentialEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class PotentialUtil {
  private PotentialUtil() {

  }

  public static String asString(Map<String, String> entries) {
    return entries.entrySet().stream()
        .map(entry -> (new StringBuilder())
            .append(entry.getKey())
            .append('=')
            .append(entry.getValue())
            .toString())
        .collect(Collectors.joining(",", "[" , "]"));
  }

  public static Map<String, String> sortByKeys(Map<String, String> map) {
    Map<String, String> out = new TreeMap<>(map);
    return out;
  }

  public static Potential getPotential(Node node, List<Node> parents) {
    List<List<String>> valueLists = new ArrayList<>();
    parents.forEach(parent -> {
      valueLists.add(new ArrayList<String>(parent.getValues()));
    });
    valueLists.add(new ArrayList<>(node.getValues()));

    List<List<String>> cartesian = cartesian(valueLists);
    final int size = parents.size();
    Potential potential = new Potential();
    cartesian.forEach(values -> {
      PotentialEntry entry = new PotentialEntry();
      for(int i=0; i < size; i++) {
        String value = values.get(i);
        String id = parents.get(i).getId();
        entry.add(id, value);
      }
      entry.add(node.getId(), values.get(size));
      potential.addEntry(entry);
    });
    return potential;
  }

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
