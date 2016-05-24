package com.github.vangj.jbayes.inf.exact.graph;

import static org.junit.Assert.*;

import org.junit.Test;

public class EdgeTest {
  @Test
  public void testHashCode() {
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

    Edge ue1 = Edge.newBuilder()
        .left(n0)
        .right(n1)
        .type(Edge.Type.UNDIRECTED)
        .build();
    Edge ue2 = Edge.newBuilder()
        .left(n1)
        .right(n0)
        .type(Edge.Type.UNDIRECTED)
        .build();
    Edge ue3 = Edge.newBuilder()
        .left(n1)
        .right(n2)
        .type(Edge.Type.UNDIRECTED)
        .build();

    assertEquals(ue1.hashCode(), ue2.hashCode());
    assertNotEquals(ue1.hashCode(), ue3.hashCode());
    assertNotEquals(ue2.hashCode(), ue3.hashCode());

    Edge de1 = Edge.newBuilder()
        .left(n0)
        .right(n1)
        .type(Edge.Type.DIRECTED)
        .build();
    Edge de2 = Edge.newBuilder()
        .left(n1)
        .right(n0)
        .type(Edge.Type.DIRECTED)
        .build();
    Edge de3 = Edge.newBuilder()
        .left(n0)
        .right(n1)
        .type(Edge.Type.DIRECTED)
        .build();

    assertNotEquals(de1.hashCode(), de2.hashCode());
    assertEquals(de1.hashCode(), de3.hashCode());
    assertNotEquals(de2.hashCode(), de3.hashCode());

    assertNotEquals(ue1.hashCode(), de1.hashCode());
    assertNotEquals(ue1.hashCode(), de2.hashCode());
    assertNotEquals(ue1.hashCode(), de3.hashCode());
    assertNotEquals(ue2.hashCode(), de1.hashCode());
    assertNotEquals(ue2.hashCode(), de2.hashCode());
    assertNotEquals(ue2.hashCode(), de3.hashCode());
    assertNotEquals(ue3.hashCode(), de1.hashCode());
    assertNotEquals(ue3.hashCode(), de2.hashCode());
    assertNotEquals(ue3.hashCode(), de3.hashCode());
  }

  @Test
  public void testEquals() {
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

    Edge ue1 = Edge.newBuilder()
        .left(n0)
        .right(n1)
        .type(Edge.Type.UNDIRECTED)
        .build();
    Edge ue2 = Edge.newBuilder()
        .left(n1)
        .right(n0)
        .type(Edge.Type.UNDIRECTED)
        .build();
    Edge ue3 = Edge.newBuilder()
        .left(n1)
        .right(n2)
        .type(Edge.Type.UNDIRECTED)
        .build();

    assertTrue(ue1.equals(ue2));
    assertFalse(ue1.equals(ue3));
    assertTrue(ue2.equals(ue1));
    assertFalse(ue2.equals(ue3));
    assertFalse(ue3.equals(ue1));
    assertFalse(ue3.equals(ue2));

    Edge de1 = Edge.newBuilder()
        .left(n0)
        .right(n1)
        .type(Edge.Type.DIRECTED)
        .build();
    Edge de2 = Edge.newBuilder()
        .left(n1)
        .right(n0)
        .type(Edge.Type.DIRECTED)
        .build();
    Edge de3 = Edge.newBuilder()
        .left(n0)
        .right(n1)
        .type(Edge.Type.DIRECTED)
        .build();

    assertFalse(de1.equals(de2));
    assertTrue(de1.equals(de3));
    assertFalse(de2.equals(de1));
    assertFalse(de2.equals(de3));
    assertTrue(de3.equals(de1));
    assertFalse(de3.equals(de2));

    assertFalse(ue1.equals(de1));
    assertFalse(ue1.equals(de2));
    assertFalse(ue1.equals(de2));
    assertFalse(ue2.equals(de1));
    assertFalse(ue2.equals(de2));
    assertFalse(ue2.equals(de2));
    assertFalse(ue3.equals(de1));
    assertFalse(ue3.equals(de2));
    assertFalse(ue3.equals(de2));
  }

  @Test
  public void testToString() {
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

    Edge ue1 = Edge.newBuilder()
        .left(n0)
        .right(n1)
        .type(Edge.Type.UNDIRECTED)
        .build();
    Edge ue2 = Edge.newBuilder()
        .left(n1)
        .right(n0)
        .type(Edge.Type.UNDIRECTED)
        .build();

    assertEquals("0 -- 1", ue1.toString());
    assertEquals("0 -- 1", ue2.toString());

    Edge de1 = Edge.newBuilder()
        .left(n0)
        .right(n1)
        .type(Edge.Type.DIRECTED)
        .build();
    Edge de2 = Edge.newBuilder()
        .left(n1)
        .right(n0)
        .type(Edge.Type.DIRECTED)
        .build();

    assertEquals("0 -> 1", de1.toString());
    assertEquals("1 -> 0", de2.toString());
  }
}
