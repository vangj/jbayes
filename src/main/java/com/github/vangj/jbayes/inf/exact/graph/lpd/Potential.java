package com.github.vangj.jbayes.inf.exact.graph.lpd;

import java.util.ArrayList;
import java.util.List;

public class Potential {
  private List<PotentialEntry> entries;

  public Potential() {
    entries = new ArrayList<>();
  }

  public Potential entry(PotentialEntry entry) {
    entries.add(entry);
    return this;
  }
}
