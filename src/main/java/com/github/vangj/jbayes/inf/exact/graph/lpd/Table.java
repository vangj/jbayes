package com.github.vangj.jbayes.inf.exact.graph.lpd;

import java.util.ArrayList;
import java.util.List;

public class Table {
  private List<TableEntry> tableEntries;

  public Table() {
    tableEntries = new ArrayList<>();
  }

  public Table entry(TableEntry entry) {
    tableEntries.add(entry);
    return this;
  }
}
