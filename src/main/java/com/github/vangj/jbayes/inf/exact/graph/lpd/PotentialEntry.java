package com.github.vangj.jbayes.inf.exact.graph.lpd;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PotentialEntry {
  private Map<String, String> entries;
  private Double value;

  public PotentialEntry() {
    entries = new LinkedHashMap<>();
    value = new Double(1.0d);
  }

  public PotentialEntry add(String id, String value) {
    if(!entries.containsKey(id)) {
      entries.put(id, value);
    }
    return this;
  }

  public PotentialEntry value(Double value) {
    this.value = value;
    return this;
  }

  @Override
  public String toString() {
    return entries.entrySet().stream()
        .map(entry -> (new StringBuilder())
                .append(entry.getKey())
                .append('=')
                .append(entry.getValue())
                .toString())
        .collect(Collectors.joining(",", "[" , "]"));
  }
}
