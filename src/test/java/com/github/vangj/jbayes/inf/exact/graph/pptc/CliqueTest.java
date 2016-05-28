package com.github.vangj.jbayes.inf.exact.graph.pptc;

import static org.junit.Assert.*;

import org.junit.Test;

public class CliqueTest extends HuangExample {
  @Test
  public void testSepSet() {
    Clique clique1 = new Clique()
        .add(getNode("a"))
        .add(getNode("b"))
        .add(getNode("c"));
    Clique clique2 = new Clique()
        .add(getNode("b"))
        .add(getNode("c"))
        .add(getNode("e"));
    SepSet sepSet = clique1.sepSet(clique2);

    assertEquals(2, sepSet.nodes().size());
    assertEquals(16, sepSet.cost());
    assertEquals(2, sepSet.mass());
    assertTrue(sepSet.contains("b"));
    assertTrue(sepSet.contains("c"));
    assertFalse(sepSet.contains("a"));
    assertFalse(sepSet.contains("e"));
  }
}
