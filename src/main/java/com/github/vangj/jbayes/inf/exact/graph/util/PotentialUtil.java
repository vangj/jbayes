package com.github.vangj.jbayes.inf.exact.graph.util;

import java.util.ArrayList;
import java.util.List;

public class PotentialUtil {
  private PotentialUtil() {

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
