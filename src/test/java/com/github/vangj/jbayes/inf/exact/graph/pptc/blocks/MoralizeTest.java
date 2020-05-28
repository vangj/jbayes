package com.github.vangj.jbayes.inf.exact.graph.pptc.blocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.github.vangj.jbayes.inf.exact.graph.Dag;
import com.github.vangj.jbayes.inf.exact.graph.Ug;
import com.github.vangj.jbayes.inf.exact.graph.pptc.HuangExample;
import org.junit.Test;

public class MoralizeTest extends HuangExample {

  @Test
  public void testMoralize() {
    Dag dag = getDag();
    Ug ug = Moralize.moralize(dag);

    assertEquals(8, ug.nodes().size());
    assertNotNull(ug.node("a"));
    assertNotNull(ug.node("b"));
    assertNotNull(ug.node("c"));
    assertNotNull(ug.node("d"));
    assertNotNull(ug.node("e"));
    assertNotNull(ug.node("f"));
    assertNotNull(ug.node("g"));
    assertNotNull(ug.node("h"));

    assertEquals(11, ug.edges().size());
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
  }
}
