package com.github.vangj.jbayes.inf.exact.sampling;

import java.util.List;
import java.util.Set;

/**
 * Sortable node.
 */
public class SortableNode {
  private String nodeId;
  private List<String> parentIds;

  public SortableNode(String nodeId, List<String> parentIds) {
    this.nodeId = nodeId;
    this.parentIds = parentIds;
  }

  public String getNodeId() {
    return nodeId;
  }

  public boolean hasParent(String id) {
    return parentIds.contains(id);
  }
}
