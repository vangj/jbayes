package com.github.vangj.jbayes.inf.exact.graph.pptc.blocks;

import com.github.vangj.jbayes.inf.exact.graph.Dag;
import com.github.vangj.jbayes.inf.exact.graph.Ug;
import com.github.vangj.jbayes.inf.exact.graph.pptc.Clique;
import com.github.vangj.jbayes.inf.exact.graph.pptc.HuangExample;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TriangulateTest extends HuangExample {
  @Test
  public void triangulate() {
    Dag dag = getDag();
    Ug ug = Moralize.moralize(dag);
    List<Clique> cliques = Triangulate.triangulate(ug);

//    cliques.forEach(cluster -> System.out.println(cluster));

    assertEquals(6, cliques.size());
    assertTrue(cliques.get(0).contains("e"));
    assertTrue(cliques.get(0).contains("g"));
    assertTrue(cliques.get(0).contains("h"));

    assertTrue(cliques.get(1).contains("c"));
    assertTrue(cliques.get(1).contains("e"));
    assertTrue(cliques.get(1).contains("g"));

    assertTrue(cliques.get(2).contains("d"));
    assertTrue(cliques.get(2).contains("e"));
    assertTrue(cliques.get(2).contains("f"));

    assertTrue(cliques.get(3).contains("c"));
    assertTrue(cliques.get(3).contains("d"));
    assertTrue(cliques.get(3).contains("e"));

    assertTrue(cliques.get(4).contains("b"));
    assertTrue(cliques.get(4).contains("c"));
    assertTrue(cliques.get(4).contains("d"));

    assertTrue(cliques.get(5).contains("a"));
    assertTrue(cliques.get(5).contains("b"));
    assertTrue(cliques.get(5).contains("c"));

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
