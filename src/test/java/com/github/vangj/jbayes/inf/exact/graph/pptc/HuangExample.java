package com.github.vangj.jbayes.inf.exact.graph.pptc;

import com.github.vangj.jbayes.inf.exact.graph.Dag;
import com.github.vangj.jbayes.inf.exact.graph.Node;

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
    return Node.builder()
        .id(id)
        .name(id)
        .value("on")
        .value("off")
        .build();
  }
}
