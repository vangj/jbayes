package com.github.vangj.jbayes.inf.exact.graph.pptc;

import com.github.vangj.jbayes.inf.exact.graph.Dag;
import com.github.vangj.jbayes.inf.exact.graph.Node;

import java.util.Arrays;

public class HuangExample {
  protected Dag getDag() {
    return (Dag) (new Dag())
        .addNode(getNode("a"))
        .addNode(getNode("b"))
        .addNode(getNode("c"))
        .addNode(getNode("d"))
        .addNode(getNode("e"))
        .addNode(getNode("f"))
        .addNode(getNode("g"))
        .addNode(getNode("h"))
        .addEdge("a", "b")
        .addEdge("a", "c")
        .addEdge("b", "d")
        .addEdge("c", "e")
        .addEdge("d", "f")
        .addEdge("e", "f")
        .addEdge("c", "g")
        .addEdge("e", "h")
        .addEdge("g", "h");
  }
  protected Node getNode(String id) {
    if("a".equals(id)) {
      return Node.builder()
          .id(id)
          .name(id)
          .value("on")
          .value("off")
          .probs(Arrays.asList(0.5d, 0.5d))
          .build();
    } else if("b".equalsIgnoreCase(id)) {
      return Node.builder()
          .id(id)
          .name(id)
          .value("on")
          .value("off")
          .probs(Arrays.asList(0.5d, 0.5d, 0.4d, 0.6d))
          .build();
    } else if("c".equalsIgnoreCase(id)) {
      return Node.builder()
          .id(id)
          .name(id)
          .value("on")
          .value("off")
          .probs(Arrays.asList(0.7d, 0.3d, 0.2d, 0.8d))
          .build();
    } else if("d".equalsIgnoreCase(id)) {
      return Node.builder()
          .id(id)
          .name(id)
          .value("on")
          .value("off")
          .probs(Arrays.asList(0.9d, 0.1d, 0.5d, 0.5d))
          .build();
    } else if("e".equalsIgnoreCase(id)) {
      return Node.builder()
          .id(id)
          .name(id)
          .value("on")
          .value("off")
          .probs(Arrays.asList(0.3d, 0.7d, 0.6d, 0.4d))
          .build();
    } else if("f".equalsIgnoreCase(id)) {
      return Node.builder()
          .id(id)
          .name(id)
          .value("on")
          .value("off")
          .probs(Arrays.asList(0.01d, 0.99d, 0.01d, 0.99d, 0.01d, 0.99d, 0.99d, 0.01d))
          .build();
    } else if("g".equalsIgnoreCase(id)) {
      return Node.builder()
          .id(id)
          .name(id)
          .value("on")
          .value("off")
          .probs(Arrays.asList(0.8d, 0.2d, 0.1d, 0.9d))
          .build();
    } else if("h".equalsIgnoreCase(id)) {
      return Node.builder()
          .id(id)
          .name(id)
          .value("on")
          .value("off")
          .probs(Arrays.asList(0.05d, 0.95d, 0.95d, 0.05d, 0.95d, 0.05d, 0.95d, 0.05d))
          .build();
    }
    return Node.builder()
        .id(id)
        .name(id)
        .value("on")
        .value("off")
        .build();
  }
}
