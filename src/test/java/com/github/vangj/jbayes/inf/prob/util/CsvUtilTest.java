package com.github.vangj.jbayes.inf.prob.util;

import com.github.vangj.jbayes.inf.prob.Graph;
import com.github.vangj.jbayes.inf.prob.Node;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Test CsvUtil.
 */
public class CsvUtilTest {
  @Test
  public void testSaveSample() {
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

    StringWriter writer = new StringWriter();
    try {
      CsvUtil.saveSamples(g, writer);
      String result = writer.toString();
      String[] rows = result.split("\n");
      assertEquals(10_001, rows.length);
      Arrays.stream(rows).forEach(row -> {
        String[] values = row.split(",");
        assertEquals(3, values.length);
      });
    } catch(IOException e) {
      assertTrue(e.getMessage(), false);
    }
  }
}
