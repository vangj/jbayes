package com.github.vangj.jbayes.inf.exact.sampling;

import com.github.vangj.jbayes.inf.exact.graph.Dag;
import com.github.vangj.jbayes.inf.exact.graph.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LogicSampler {

  private final Dag dag;
  private List<String> nodes;
  private Map<String, Table> tables;

  public LogicSampler(Dag dag) {
    this.dag = dag;
    this.initNodes();
    this.initTables();
  }

  private void initNodes() {
    List<SortableNode> nodes = dag.nodes().stream()
        .map(n -> {
          List<String> parents = dag.parents(n).stream()
              .map(pa -> pa.getId())
              .collect(Collectors.toList());
          return new SortableNode(n.getId(), parents);
        })
        .collect(Collectors.toList());
    Collections.sort(nodes, new Comparator<SortableNode>() {
      @Override
      public int compare(SortableNode lhs, SortableNode rhs) {
        String lhsId = lhs.getNodeId();
        String rhsId = rhs.getNodeId();

        if (lhs.hasParent(rhsId)) {
          return 1;
        } else if (rhs.hasParent(lhsId)) {
          return -1;
        } else {
          return lhsId.compareTo(rhsId);
        }
      }
    });

    this.nodes = nodes.stream()
        .map(n -> n.getNodeId())
        .collect(Collectors.toList());
  }

  private void initTables() {
    this.tables = new HashMap<>();
    for (Node node : this.dag.nodes()) {
      Table table = new Table(node, this.dag.parents(node));
      this.tables.put(node.getId(), table);
    }
  }

  public List<Map<String, String>> getSamples(Map<String, String> evidence, final int nSamples,
      final int seed) {
    List<Map<String, String>> samples = new ArrayList<>();
    final Random random = new Random(seed);
    final int n = this.nodes.size();
    while (true) {
      Map<String, String> sample = new HashMap<>(evidence);

      List<Double> probs = IntStream.range(0, n)
          .mapToDouble(i -> random.nextDouble())
          .boxed()
          .collect(Collectors.toList());

      for (int i = 0; i < n; i++) {
        String nodeId = this.nodes.get(i);
        Table table = this.tables.get(nodeId);
        Double p = probs.get(i);
        String v = table.getValue(p, sample);

        if (evidence.containsKey(nodeId) && !v.equals(evidence.get(nodeId))) {
          break;
        }

        sample.put(nodeId, v);
      }

      if (sample.size() == n) {
        samples.add(sample);
      }

      if (samples.size() == nSamples) {
        break;
      }

    }
    return samples;
  }
}
