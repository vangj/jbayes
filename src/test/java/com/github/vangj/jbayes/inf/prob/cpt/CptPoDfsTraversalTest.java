package com.github.vangj.jbayes.inf.prob.cpt;

import com.github.vangj.jbayes.inf.prob.util.CptUtil;
import com.github.vangj.jbayes.inf.prob.Cpt;
import com.github.vangj.jbayes.inf.prob.Node;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests CptPoDfsTraversal.
 */
public class CptPoDfsTraversalTest {
  @Test
  public void testTraversal() {
    Node n1 = Node.newBuilder()
        .name("n1")
        .value("t")
        .value("f")
        .build();
    Node n2 = Node.newBuilder()
        .name("n2")
        .value("true")
        .value("false")
        .parent(n1)
        .build();
    Node n3 = Node.newBuilder()
        .name("n3")
        .value("yes")
        .value("no")
        .value("maybe")
        .parent(n1)
        .parent(n2)
        .build();

    Cpt cpt = CptUtil.build(n3);

    final List<List<Double>> probs = new ArrayList<>();
    CptPoDfsTraversal.CptPoDfsTraversalListener listener = (c) -> {
      if(c.numOfValues() > 0) {
        probs.add(c.getValues());
      }
    };
    (new CptPoDfsTraversal(cpt, listener)).start();

    assertEquals(4, probs.size());
    for(List<Double> p : probs) {
      assertEquals(3, p.size());
    }

    assertTrue(probs.get(0) == cpt.get(0).get(0).getValues());
    assertTrue(probs.get(1) == cpt.get(0).get(1).getValues());
    assertTrue(probs.get(2) == cpt.get(1).get(0).getValues());
    assertTrue(probs.get(3) == cpt.get(1).get(1).getValues());
  }
}
