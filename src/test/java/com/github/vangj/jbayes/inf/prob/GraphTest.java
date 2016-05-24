package com.github.vangj.jbayes.inf.prob;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests graph.
 */
public class GraphTest {

  @Test
  public void testSample() {
    Node n1 = Node.newBuilder().name("n1").value("t").value("f").build();
    Node n2 = Node.newBuilder().name("n2").value("t").value("f").build();
    Node n3 = Node.newBuilder().name("n3").value("t").value("f").build();

    n2.addParent(n1);
    n3.addParent(n2);

    n1.setCpt(new double[][] {
        {0.5, 0.5}
    });
    n2.setCpt(new double[][] {
        {0.5, 0.5},
        {0.5, 0.5}
    });
    n3.setCpt(new double[][] {
        {0.5, 0.5},
        {0.5, 0.5}
    });

    Graph g = new Graph();
    g.addNode(n1);
    g.addNode(n2);
    g.addNode(n3);

    g.sample(10000);

    double[] probs1 = n1.probs();
    double[] probs2 = n2.probs();
    double[] probs3 = n3.probs();

    assertTrue( (0.5 - probs1[0]) < 0.05 );
    assertTrue( (0.5 - probs1[1]) < 0.05 );

    assertTrue( (0.5 - probs2[0]) < 0.05 );
    assertTrue( (0.5 - probs2[1]) < 0.05 );

    assertTrue( (0.5 - probs3[0]) < 0.05 );
    assertTrue( (0.5 - probs3[1]) < 0.05 );
  }

  @Test
  public void testSamplingWithObservation() {
    Node n1 = Node.newBuilder().name("n1").value("t").value("f").build();
    Node n2 = Node.newBuilder().name("n2").value("t").value("f").build();
    Node n3 = Node.newBuilder().name("n3").value("t").value("f").build();

    n2.addParent(n1);
    n3.addParent(n2);

    n1.setCpt(new double[][] {
        {0.5, 0.5}
    });
    n2.setCpt(new double[][] {
        {0.5, 0.5},
        {0.5, 0.5}
    });
    n3.setCpt(new double[][] {
        {0.5, 0.5},
        {0.5, 0.5}
    });

    Graph g = new Graph();
    g.addNode(n1);
    g.addNode(n2);
    g.addNode(n3);

    g.observe("n1", "t");

    g.sample(10_000);

    double[] probs1 = n1.probs();
    double[] probs2 = n2.probs();
    double[] probs3 = n3.probs();

    assertTrue( (1.0 - probs1[0]) < 0.001 );
    assertTrue( probs1[1] < 0.05 );

    assertTrue( (0.5 - probs2[0]) < 0.05 );
    assertTrue( (0.5 - probs2[1]) < 0.05 );

    assertTrue( (0.5 - probs3[0]) < 0.05 );
    assertTrue( (0.5 - probs3[1]) < 0.05 );

    g.unobserve("n1");

    g.sample(10_000);

    probs1 = n1.probs();
    probs2 = n2.probs();
    probs3 = n3.probs();

    assertTrue( (0.5 - probs1[0]) < 0.05 );
    assertTrue( (0.5 - probs1[1]) < 0.05 );

    assertTrue( (0.5 - probs2[0]) < 0.05 );
    assertTrue( (0.5 - probs2[1]) < 0.05 );

    assertTrue( (0.5 - probs3[0]) < 0.05 );
    assertTrue( (0.5 - probs3[1]) < 0.05 );
  }

  @Test
  public void testSavingSamples() {
    Node n1 = Node.newBuilder().name("n1").value("t").value("f").build();
    Node n2 = Node.newBuilder().name("n2").value("t").value("f").build();
    Node n3 = Node.newBuilder().name("n3").value("t").value("f").build();

    n2.addParent(n1);
    n3.addParent(n2);

    n1.setCpt(new double[][] {{0.5, 0.5}});
    n2.setCpt(new double[][] {{0.5, 0.5}, {0.5, 0.5}});
    n3.setCpt(new double[][] {{0.5, 0.5}, {0.5, 0.5}});

    Graph g = new Graph();
    g.addNode(n1);
    g.addNode(n2);
    g.addNode(n3);

    g.setSaveSamples(true);
    g.sample(10_000);

    List<String[]> samples = g.getSamples();
    assertEquals(10_000, samples.size());
    samples.forEach(s -> assertEquals(3, s.length));
  }
}
