package com.github.vangj.jbayes.inf.prob.util;

import com.github.vangj.jbayes.inf.prob.Graph;
import com.github.vangj.jbayes.inf.prob.Node;
import com.github.vangj.jbayes.inf.prob.json.JsonGraph;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test JsonUtil.
 */
public class JsonUtilTest {

  @Test
  public void testSerde() {
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

    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonUtil.serialize(g, baos);
      String json = baos.toString();

      ByteArrayInputStream bais = new ByteArrayInputStream(json.getBytes());
      Graph deGraph = JsonUtil.deserialize(bais);

      assertEquals(3, deGraph.getNodes().size());

      deGraph.sample(10_000);

      double[] probs1 = deGraph.getNode("n1").probs();
      double[] probs2 = deGraph.getNode("n2").probs();
      double[] probs3 = deGraph.getNode("n3").probs();

      assertTrue( (0.5 - probs1[0]) < 0.05 );
      assertTrue( (0.5 - probs1[1]) < 0.05 );

      assertTrue( (0.5 - probs2[0]) < 0.05 );
      assertTrue( (0.5 - probs2[1]) < 0.05 );

      assertTrue( (0.5 - probs3[0]) < 0.05 );
      assertTrue( (0.5 - probs3[1]) < 0.05 );
      System.out.println("done");
    } catch(IOException e) {
      assertTrue(e.getMessage(), false);
    }

  }

  @Test
  public void testInMemorySerde() {
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

    g.setSaveSamples(true);
    g.sample(10_000);

    JsonGraph jsonGraph = new JsonGraph(g);
    String json = JsonUtil.toJson(jsonGraph);

    JsonGraph deJsonGraph = (JsonGraph)JsonUtil.fromJson(json, JsonGraph.class);

    jsonGraph.getNodes().forEach((name, node) -> {
      assertTrue(deJsonGraph.getNodes().containsKey(name));
      Node deNode = deJsonGraph.getNodes().get(name);
      node.getValues().forEach(v -> assertTrue(deNode.getValues().contains(v)));
    });

    jsonGraph.getParents().forEach((name, parents) -> {
      assertTrue(deJsonGraph.getParents().containsKey(name));
      parents.forEach(pa -> assertTrue(deJsonGraph.getParents().get(name).contains(pa)));
    });

    jsonGraph.getCpts().forEach((name, matrix) -> {
      assertTrue(deJsonGraph.getCpts().containsKey(name));
      assertEquals(matrix.length, deJsonGraph.getCpts().get(name).length);
      assertEquals(matrix[0].length, deJsonGraph.getCpts().get(name)[0].length);
    });

    Graph deGraph = deJsonGraph.toGraph();
    assertEquals(3, deGraph.getNodes().size());

    assertEquals(2, deGraph.getNode("n1").numValues());
    assertEquals(2, deGraph.getNode("n2").numValues());
    assertEquals(2, deGraph.getNode("n2").numValues());

    assertEquals(0, deGraph.getNode("n1").numParents());
    assertEquals(1, deGraph.getNode("n2").numParents());
    assertEquals(1, deGraph.getNode("n3").numParents());

    deGraph.sample(10_000);

    double[] probs1 = deGraph.getNode("n1").probs();
    double[] probs2 = deGraph.getNode("n2").probs();
    double[] probs3 = deGraph.getNode("n3").probs();

    assertTrue( (0.5 - probs1[0]) < 0.05 );
    assertTrue( (0.5 - probs1[1]) < 0.05 );

    assertTrue( (0.5 - probs2[0]) < 0.05 );
    assertTrue( (0.5 - probs2[1]) < 0.05 );

    assertTrue( (0.5 - probs3[0]) < 0.05 );
    assertTrue( (0.5 - probs3[1]) < 0.05 );
  }
}
