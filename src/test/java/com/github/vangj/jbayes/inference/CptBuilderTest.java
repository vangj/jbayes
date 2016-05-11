package com.github.vangj.jbayes.inference;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests CptBuilder.
 */
public class CptBuilderTest {
  @Test
  public void testBuild() {
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

    Cpt cpt1 = CptBuilder.build(n1);
    assertNotNull(cpt1);
    assertEquals(2, cpt1.numOfValues());
    assertEquals(0, cpt1.numOfChildren());

    Cpt cpt2 = CptBuilder.build(n2);
    assertNotNull(cpt2);
    assertEquals(0, cpt2.numOfValues());
    assertEquals(2, cpt2.numOfChildren());
    assertNotNull(cpt2.get(0));
    assertEquals(2, cpt2.get(0).numOfValues());
    assertEquals(0, cpt2.get(0).numOfChildren());
    assertNotNull(cpt2.get(1));
    assertEquals(2, cpt2.get(1).numOfValues());
    assertEquals(0, cpt2.get(1).numOfChildren());

    Cpt cpt3 = CptBuilder.build(n3);
    assertNotNull(cpt3);
    assertEquals(0, cpt3.numOfValues());
    assertEquals(2, cpt3.numOfChildren());

    assertNotNull(cpt3.get(0));
    assertEquals(0, cpt3.get(0).numOfValues());
    assertEquals(2, cpt3.get(0).numOfChildren());
    assertNotNull(cpt3.get(0).get(0));
    assertEquals(3, cpt3.get(0).get(0).numOfValues());
    assertEquals(0, cpt3.get(0).get(0).numOfChildren());
    assertEquals(3, cpt3.get(0).get(1).numOfValues());
    assertEquals(0, cpt3.get(0).get(1).numOfChildren());

    assertNotNull(cpt3.get(1));
    assertEquals(0, cpt3.get(1).numOfValues());
    assertEquals(2, cpt3.get(1).numOfChildren());
    assertNotNull(cpt3.get(1).get(0));
    assertEquals(3, cpt3.get(1).get(0).numOfValues());
    assertEquals(0, cpt3.get(1).get(0).numOfChildren());
    assertEquals(3, cpt3.get(1).get(1).numOfValues());
    assertEquals(0, cpt3.get(1).get(1).numOfChildren());
  }

  @Test
  public void testBuildCptWithProbsAndNoParent() {
    Node n1 = Node.newBuilder()
        .name("n1")
        .value("t")
        .value("f")
        .build();

    Cpt cpt = CptBuilder.build(n1, new double[][] {
        {0.7, 0.3}
    });

    assertEquals(0.7, cpt.getValue(0), 0.001);
    assertEquals(0.3, cpt.getValue(1), 0.001);
  }

  @Test
  public void testBuildCptWithProbsAndOneParent() {
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

    Cpt cpt = CptBuilder.build(n2, new double[][] {
        {0.7, 0.3},
        {0.3, 0.7}
    });

    assertEquals(0.7, cpt.get(0).getValue(0), 0.001);
    assertEquals(0.3, cpt.get(0).getValue(1), 0.001);
    assertEquals(0.3, cpt.get(1).getValue(0), 0.001);
    assertEquals(0.7, cpt.get(1).getValue(1), 0.001);
  }

  @Test
  public void testBuildCptWithProbsAndTwoParents() {
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

    Cpt cpt = CptBuilder.build(n3, new double[][] {
        {0.1, 0.2, 0.7},
        {0.7, 0.2, 0.1},
        {0.2, 0.1, 0.7},
        {0.1, 0.7, 0.2}
    });

    assertEquals(0.1, cpt.get(0).get(0).getValue(0), 0.001);
    assertEquals(0.2, cpt.get(0).get(0).getValue(1), 0.001);
    assertEquals(0.7, cpt.get(0).get(0).getValue(2), 0.001);

    assertEquals(0.7, cpt.get(0).get(1).getValue(0), 0.001);
    assertEquals(0.2, cpt.get(0).get(1).getValue(1), 0.001);
    assertEquals(0.1, cpt.get(0).get(1).getValue(2), 0.001);

    assertEquals(0.2, cpt.get(1).get(0).getValue(0), 0.001);
    assertEquals(0.1, cpt.get(1).get(0).getValue(1), 0.001);
    assertEquals(0.7, cpt.get(1).get(0).getValue(2), 0.001);

    assertEquals(0.1, cpt.get(1).get(1).getValue(0), 0.001);
    assertEquals(0.7, cpt.get(1).get(1).getValue(1), 0.001);
    assertEquals(0.2, cpt.get(1).get(1).getValue(2), 0.001);
  }
}
