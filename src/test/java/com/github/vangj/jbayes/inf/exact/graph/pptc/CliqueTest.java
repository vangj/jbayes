package com.github.vangj.jbayes.inf.exact.graph.pptc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CliqueTest extends HuangExample {

  @Test
  public void testSepSet() {
    Clique clique1 = new Clique(
        getNode("a"),
        getNode("b"),
        getNode("c"));
    Clique clique2 = new Clique(
        getNode("b"),
        getNode("c"),
        getNode("e"));
    SepSet sepSet = clique1.sepSet(clique2);

    assertEquals(2, sepSet.nodes().size());
    assertEquals(16, sepSet.cost());
    assertEquals(2, sepSet.mass());
    assertTrue(sepSet.contains("b"));
    assertTrue(sepSet.contains("c"));
    assertFalse(sepSet.contains("a"));
    assertFalse(sepSet.contains("e"));
  }

  @Test
  public void testIsSuperSet() {
    Clique egh = new Clique(getNode("e"), getNode("g"), getNode("h"));
    Clique ceg = new Clique(getNode("c"), getNode("e"), getNode("g"));
    Clique def = new Clique(getNode("d"), getNode("e"), getNode("f"));
    Clique ace = new Clique(getNode("a"), getNode("c"), getNode("e"));
    Clique abd = new Clique(getNode("a"), getNode("b"), getNode("d"));
    Clique ade = new Clique(getNode("a"), getNode("d"), getNode("e"));
    Clique ae = new Clique(getNode("a"), getNode("e"));
    Clique a = new Clique(getNode("a"));

    assertTrue(ade.isSuperset(ae));
    assertTrue(ace.isSuperset(ae));
    assertTrue(ace.isSuperset(a));
    assertTrue(abd.isSuperset(a));
    assertTrue(ade.isSuperset(a));

    Clique[] cliques = {egh, ceg, def};
    for (Clique clique : cliques) {
      assertFalse(clique.isSuperset(ae));
      assertFalse(clique.isSuperset(a));
    }
  }
}
