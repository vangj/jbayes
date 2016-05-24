package com.github.vangj.jbayes.inf.exact.graph.traversal;

import com.github.vangj.jbayes.inf.exact.graph.Graph;
import com.github.vangj.jbayes.inf.exact.graph.Node;
import org.junit.Assert;
import org.junit.Test;

public class GraphShortestPathTest {
  @Test
  public void testShortestPath() {
    Node n0 = Node.builder()
        .id("0")
        .name("n0")
        .value("t")
        .value("f")
        .build();
    Node n1 = Node.builder()
        .id("1")
        .name("n1")
        .value("t")
        .value("f")
        .build();
    Node n2 = Node.builder()
        .id("2")
        .name("n2")
        .value("t")
        .value("f")
        .build();
    Node n3 = Node.builder()
        .id("3")
        .name("n3")
        .value("t")
        .value("f")
        .build();
    Node n4 = Node.builder()
        .id("4")
        .name("n4")
        .value("t")
        .value("f")
        .build();

    Graph graph = new Graph();
    graph.addNode(n0)
        .addNode(n1)
        .addNode(n2)
        .addNode(n3)
        .addNode(n4)
        .addEdge(n0, n1)
        .addEdge(n1, n2)
        .addEdge(n1, n3);

    ShortestPathListener listener = new ShortestPathListener() {
      @Override public void pre(Graph graph, Node start, Node stop) {
        System.out.println("start " + start + " ===> " + stop);
      }

      @Override public void visited(Node node) {
        System.out.println(node);
      }

      @Override public void post(Graph graph, Node start, Node stop) {
        System.out.println("stop");
      }
    };

    boolean result = GraphShortestPath.exists(graph, n0, n1, listener);
    Assert.assertEquals(true, result);

    result = GraphShortestPath.exists(graph, n0, n2, listener);
    Assert.assertEquals(true, result);
    result = GraphShortestPath.exists(graph, n0, n3, listener);
    Assert.assertEquals(true, result);
    result = GraphShortestPath.exists(graph, n0, n4, listener);
    Assert.assertEquals(false, result);
  }
}
