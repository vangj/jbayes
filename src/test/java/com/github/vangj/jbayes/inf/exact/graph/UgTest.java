package com.github.vangj.jbayes.inf.exact.graph;

import org.junit.Test;

import static org.junit.Assert.*;

public class UgTest {
  @Test
  public void testCreate() {
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

    System.out.println(graph);

    assertEquals(5, graph.nodes().size());
    assertEquals(3, graph.edges().size());

    //readding does nothing
    graph.addNode(n0)
        .addNode(n1)
        .addNode(n2)
        .addNode(n3)
        .addNode(n4)
        .addEdge(n0, n1)
        .addEdge(n1, n2)
        .addEdge(n1, n3);

    assertEquals(5, graph.nodes().size());
    assertEquals(3, graph.edges().size());

    //cycles are ok
    graph.addEdge(n3, n0);

    assertEquals(5, graph.nodes().size());
    assertEquals(4, graph.edges().size());

    //new edge are ok
    graph.addEdge(n4, n1);

    assertEquals(5, graph.nodes().size());
    assertEquals(5, graph.edges().size());
  }
}
