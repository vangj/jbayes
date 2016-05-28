package com.github.vangj.jbayes.inf.exact.graph.pptc;

import com.github.vangj.jbayes.inf.exact.graph.Dag;
import com.github.vangj.jbayes.inf.exact.graph.Ug;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TriangulateTest extends HuangExample {
  @Test
  public void triangulate() {
    Dag dag = getDag();
    Ug ug = Moralize.moralize(dag);
    List<Cluster> clusters = Triangulate.triangulate(dag, ug);

//    clusters.forEach(cluster -> System.out.println(cluster));

    assertEquals(8, clusters.size());
    assertTrue(clusters.get(0).contains("e"));
    assertTrue(clusters.get(0).contains("g"));
    assertTrue(clusters.get(0).contains("h"));

    assertTrue(clusters.get(1).contains("c"));
    assertTrue(clusters.get(1).contains("e"));
    assertTrue(clusters.get(1).contains("g"));

    assertTrue(clusters.get(2).contains("d"));
    assertTrue(clusters.get(2).contains("e"));
    assertTrue(clusters.get(2).contains("f"));

    assertTrue(clusters.get(3).contains("c"));
    assertTrue(clusters.get(3).contains("d"));
    assertTrue(clusters.get(3).contains("e"));

    assertTrue(clusters.get(4).contains("b"));
    assertTrue(clusters.get(4).contains("c"));
    assertTrue(clusters.get(4).contains("d"));

    assertTrue(clusters.get(5).contains("a"));
    assertTrue(clusters.get(5).contains("b"));
    assertTrue(clusters.get(5).contains("c"));

    assertTrue(clusters.get(6).contains("a"));
    assertTrue(clusters.get(6).contains("b"));

    assertTrue(clusters.get(7).contains("a"));

//    System.out.println(ug);

    assertEquals(8, ug.nodes().size());
    assertNotNull(ug.node("a"));
    assertNotNull(ug.node("b"));
    assertNotNull(ug.node("c"));
    assertNotNull(ug.node("d"));
    assertNotNull(ug.node("e"));
    assertNotNull(ug.node("f"));
    assertNotNull(ug.node("g"));
    assertNotNull(ug.node("h"));

    assertEquals(13, ug.edges().size());
    assertTrue(ug.edgeExists("a", "b"));
    assertTrue(ug.edgeExists("a", "c"));
    assertTrue(ug.edgeExists("b", "d"));
    assertTrue(ug.edgeExists("c", "e"));
    assertTrue(ug.edgeExists("d", "f"));
    assertTrue(ug.edgeExists("e", "f"));
    assertTrue(ug.edgeExists("c", "g"));
    assertTrue(ug.edgeExists("e", "h"));
    assertTrue(ug.edgeExists("g", "h"));
    assertTrue(ug.edgeExists("g", "e"));
    assertTrue(ug.edgeExists("d", "e"));
    assertTrue(ug.edgeExists("c", "d"));
    assertTrue(ug.edgeExists("b", "c"));
  }
}
