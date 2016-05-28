package com.github.vangj.jbayes.inf.exact.graph.lpd;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TableEntry {
  private Map<String, String> entries;
  private Double value;

  public TableEntry() {
    entries = new LinkedHashMap<>();
    value = new Double(0.0d);
  }

  public TableEntry add(String id, String value) {
    if(!entries.containsKey(id)) {
      entries.put(id, value);
    }
    return this;
  }

  public TableEntry value(Double value) {
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
