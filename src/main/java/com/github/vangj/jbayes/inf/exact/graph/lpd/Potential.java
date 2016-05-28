package com.github.vangj.jbayes.inf.exact.graph.lpd;

import java.util.ArrayList;
import java.util.List;

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
}
