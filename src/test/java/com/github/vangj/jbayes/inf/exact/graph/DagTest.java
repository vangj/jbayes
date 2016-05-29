package com.github.vangj.jbayes.inf.exact.graph;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DagTest {

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

    Dag graph = new Dag();
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

    //test parents
    assertEquals(0, graph.parents(n0).size());
    assertEquals(1, graph.parents(n1).size());
    assertEquals(1, graph.parents(n2).size());
    assertEquals(1, graph.parents(n3).size());
    assertEquals(0, graph.parents(n4).size());
    assertTrue(graph.parents(n1).contains(n0));
    assertTrue(graph.parents(n2).contains(n1));
    assertTrue(graph.parents(n3).contains(n1));

    //test children
    assertEquals(1, graph.children(n0).size());
    assertEquals(2, graph.children(n1).size());
    assertEquals(0, graph.children(n2).size());
    assertEquals(0, graph.children(n3).size());
    assertEquals(0, graph.children(n4).size());
    assertTrue(graph.children(n0).contains(n1));
    assertTrue(graph.children(n1).contains(n2));
    assertTrue(graph.children(n1).contains(n3));

    //readding nodes and arcs has no effects
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

    //adding cycles do nothing!
    graph.addEdge(n1, n0);

    assertEquals(5, graph.nodes().size());
    assertEquals(3, graph.edges().size());

    //adding cycles do nothing!
    graph.addEdge(n3, n0);

    assertEquals(5, graph.nodes().size());
    assertEquals(3, graph.edges().size());
  }

  @Test
  public void testCoparents() {
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

    Dag graph = new Dag();
    graph.addNode(n0)
        .addNode(n1)
        .addNode(n2)
        .addNode(n3)
        .addNode(n4)
        .addNode(n5)
        .addEdge(n0, n2)
        .addEdge(n1, n2)
        .addEdge(n2, n3)
        .addEdge(n2, n4)
        .addEdge(n5, n4);

    assertEquals(1, graph.coparents(n0).size());
    assertTrue(graph.coparents(n0).contains(n1));

    assertEquals(1, graph.coparents(n1).size());
    assertTrue(graph.coparents(n1).contains(n0));

    assertEquals(1, graph.coparents(n2).size());
    assertTrue(graph.coparents(n2).contains(n5));

    assertEquals(1, graph.coparents(n5).size());
    assertTrue(graph.coparents(n5).contains(n2));

    assertEquals(0, graph.coparents(n3).size());
    assertEquals(0, graph.coparents(n4).size());
  }

  @Test
  public void testMarkovBlanket() {
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

    Dag graph = new Dag();
    graph.addNode(n0)
        .addNode(n1)
        .addNode(n2)
        .addNode(n3)
        .addNode(n4)
        .addNode(n5)
        .addEdge(n0, n2)
        .addEdge(n1, n2)
        .addEdge(n2, n3)
        .addEdge(n2, n4)
        .addEdge(n5, n4);

    assertEquals(5, graph.markovBlanket(n2).size());
    assertTrue(graph.markovBlanket(n2).contains(n0));
    assertTrue(graph.markovBlanket(n2).contains(n1));
    assertTrue(graph.markovBlanket(n2).contains(n3));
    assertTrue(graph.markovBlanket(n2).contains(n4));
    assertTrue(graph.markovBlanket(n2).contains(n5));

    assertEquals(2, graph.markovBlanket(n0).size());
    assertTrue(graph.markovBlanket(n0).contains(n2));
    assertTrue(graph.markovBlanket(n0).contains(n1));

    assertEquals(2, graph.markovBlanket(n1).size());
    assertTrue(graph.markovBlanket(n1).contains(n2));
    assertTrue(graph.markovBlanket(n1).contains(n0));

    assertEquals(1, graph.markovBlanket(n3).size());
    assertTrue(graph.markovBlanket(n3).contains(n2));

    assertEquals(2, graph.markovBlanket(n4).size());
    assertTrue(graph.markovBlanket(n4).contains(n2));
    assertTrue(graph.markovBlanket(n4).contains(n5));

    assertEquals(2, graph.markovBlanket(n5).size());
    assertTrue(graph.markovBlanket(n5).contains(n2));
    assertTrue(graph.markovBlanket(n5).contains(n4));
  }
}
