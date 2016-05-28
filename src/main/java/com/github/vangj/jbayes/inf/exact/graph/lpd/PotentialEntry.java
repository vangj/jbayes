package com.github.vangj.jbayes.inf.exact.graph.lpd;

import static com.github.vangj.jbayes.inf.exact.graph.util.PotentialUtil.*;

import java.util.LinkedHashMap;
import java.util.Map;

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

  public Double value() {
    return value;
  }

  public PotentialEntry value(Double value) {
    this.value = value;
    return this;
  }

  @Override
  public int hashCode() {
    return asString(sortByKeys(entries)).hashCode();
  }

  @Override
  public boolean equals(Object object) {
    if(null == object || !(object instanceof PotentialEntry)) {
      return false;
    }
    PotentialEntry that = (PotentialEntry)object;
    return (this.hashCode() == that.hashCode());
  }

  @Override
  public String toString() {
    return asString(entries);
  }
}
