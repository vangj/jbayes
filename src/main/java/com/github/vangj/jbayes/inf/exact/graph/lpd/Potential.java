package com.github.vangj.jbayes.inf.exact.graph.lpd;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A potential. If it helps, think of a potential as a table.
 */
public class Potential {
  private List<PotentialEntry> entries;

  public Potential() {
    entries = new ArrayList<>();
  }

  public List<PotentialEntry> entries() {
    return entries;
  }

  public Potential addEntry(PotentialEntry entry) {
    entries.add(entry);
    return this;
  }

  @Override
  public String toString() {
    return entries.stream()
        .map(PotentialEntry::toString)
        .collect(Collectors.joining(System.lineSeparator()));
  }
}
