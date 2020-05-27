package com.github.vangj.jbayes.inf.exact.graph.lpd;

import static com.github.vangj.jbayes.inf.exact.graph.util.PotentialUtil.asString;
import static com.github.vangj.jbayes.inf.exact.graph.util.PotentialUtil.sortByKeys;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An entry of a potential. If it helps, think of a potential entry as a table row.
 */
public class PotentialEntry {

  private final Map<String, String> entries;
  private Double value;

  public PotentialEntry() {
    entries = new LinkedHashMap<>();
    value = new Double(1.0d);
  }

  private PotentialEntry(Map<String, String> entries, Double value) {
    this.entries = entries;
    this.value = value;
  }

  public PotentialEntry duplicate() {
    Map<String, String> entries = new LinkedHashMap<>(this.entries);
    Double value = new Double(this.value);
    return new PotentialEntry(entries, value);
  }

  public PotentialEntry add(String id, String value) {
    if (!entries.containsKey(id)) {
      entries.put(id, value);
    }
    return this;
  }

  public Double getValue() {
    return value;
  }

  public PotentialEntry setValue(Double value) {
    this.value = value;
    return this;
  }

  /**
   * Checks if the specified potential entry matches. This potential entry must contain, at the very
   * least, all the same key-value pairs as the specified potential entry passed in.
   *
   * @param that Potential entry.
   * @return Boolean.
   */
  public boolean match(PotentialEntry that) {
    for (Map.Entry<String, String> entry : that.entries.entrySet()) {
      String k = entry.getKey();
      String v = entry.getValue();

      if (!this.entries.containsKey(k)) {
        return false;
      } else {
        if (!this.entries.get(k).equalsIgnoreCase(v)) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    return asString(sortByKeys(entries)).hashCode();
  }

  @Override
  public boolean equals(Object object) {
    if (null == object || !(object instanceof PotentialEntry)) {
      return false;
    }
    PotentialEntry that = (PotentialEntry) object;
    return (this.hashCode() == that.hashCode());
  }

  @Override
  public String toString() {
    return (new StringBuilder())
        .append(asString(entries))
        .append(" ")
        .append(value)
        .toString();
  }
}
