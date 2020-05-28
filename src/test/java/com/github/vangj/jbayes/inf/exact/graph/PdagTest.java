package com.github.vangj.jbayes.inf.exact.graph;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PdagTest {

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
    Node n5 = Node.builder()
        .id("5")
        .name("n5")
        .value("t")
        .value("f")
        .build();

    Pdag graph = new Pdag();
    graph.addNode(n0)
        .addNode(n1)
        .addNode(n2)
        .addNode(n3)
        .addNode(n4)
        .addNode(n5)
        .addEdge(n0, n1)
        .addEdge(n1, n4)
        .addEdge(
            Edge.newBuilder()
                .left(n1)
                .right(n2)
                .type(Edge.Type.DIRECTED)
                .build())
        .addEdge(
            Edge.newBuilder()
                .left(n3)
                .right(n2)
                .type(Edge.Type.DIRECTED)
                .build())
        .addEdge(
            Edge.newBuilder()
                .left(n2)
                .right(n5)
                .type(Edge.Type.DIRECTED)
                .build());

    System.out.println(graph);

    assertEquals(6, graph.nodes().size());
    assertEquals(5, graph.edges().size());

    //readding nodes and arcs does nothing
    graph.addNode(n0)
        .addNode(n1)
        .addNode(n2)
        .addNode(n3)
        .addNode(n4)
        .addNode(n5)
        .addEdge(n0, n1)
        .addEdge(n1, n4)
        .addEdge(
            Edge.newBuilder()
                .left(n1)
                .right(n2)
                .type(Edge.Type.DIRECTED)
                .build())
        .addEdge(
            Edge.newBuilder()
                .left(n3)
                .right(n2)
                .type(Edge.Type.DIRECTED)
                .build())
        .addEdge(
            Edge.newBuilder()
                .left(n2)
                .right(n5)
                .type(Edge.Type.DIRECTED)
                .build());

    assertEquals(6, graph.nodes().size());
    assertEquals(5, graph.edges().size());

    //adding cycle does nothing
    graph.addEdge(
        Edge.newBuilder()
            .left(n2)
            .right(n1)
            .type(Edge.Type.DIRECTED)
            .build());

    assertEquals(6, graph.nodes().size());
    assertEquals(5, graph.edges().size());

    //adding cycle does nothing
    graph.addEdge(
        Edge.newBuilder()
            .left(n5)
            .right(n1)
            .type(Edge.Type.DIRECTED)
            .build());

    assertEquals(6, graph.nodes().size());
    assertEquals(5, graph.edges().size());

    //adding cycle does nothing
    graph.addEdge(
        Edge.newBuilder()
            .left(n4)
            .right(n1)
            .type(Edge.Type.DIRECTED)
            .build());

    assertEquals(6, graph.nodes().size());
    assertEquals(5, graph.edges().size());
  }
}
