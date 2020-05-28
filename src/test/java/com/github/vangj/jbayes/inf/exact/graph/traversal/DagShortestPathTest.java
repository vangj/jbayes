package com.github.vangj.jbayes.inf.exact.graph.traversal;

import static org.junit.Assert.assertEquals;

import com.github.vangj.jbayes.inf.exact.graph.Dag;
import com.github.vangj.jbayes.inf.exact.graph.Graph;
import com.github.vangj.jbayes.inf.exact.graph.Node;
import org.junit.Test;

public class DagShortestPathTest {

  @Test
  public void testShortestPath() {
    Node n0 = Node.builder()
        .id("0")
        .name("n0")
        .value("t")
        .value("f")
        .build();
    Node n1 = Node.builder()
        .id("1")
        .name("n1")
        .value("t")
        .value("f")
        .build();
    Node n2 = Node.builder()
        .id("2")
        .name("n2")
        .value("t")
        .value("f")
        .build();
    Node n3 = Node.builder()
        .id("3")
        .name("n3")
        .value("t")
        .value("f")
        .build();
    Node n4 = Node.builder()
        .id("4")
        .name("n4")
        .value("t")
        .value("f")
        .build();

    Dag graph = new Dag();
    graph.addNode(n0)
        .addNode(n1)
        .addNode(n2)
        .addNode(n3)
        .addNode(n4)
        .addEdge(n0, n1)
        .addEdge(n1, n2)
        .addEdge(n1, n3);

    ShortestPathListener listener = new ShortestPathListener() {
      @Override
      public void pre(Graph graph, Node start, Node stop) {
        System.out.println("start " + start + " ===> " + stop);
      }

      @Override
      public void visited(Node node) {
        System.out.println(node);
      }

      @Override
      public void post(Graph graph, Node start, Node stop) {
        System.out.println("stop");
      }
    };

    boolean result = DagShortestPath.exists(graph, n0, n1, listener);
    assertEquals(true, result);

    result = DagShortestPath.exists(graph, n0, n2, listener);
    assertEquals(true, result);
    result = DagShortestPath.exists(graph, n0, n3, listener);
    assertEquals(true, result);
    result = DagShortestPath.exists(graph, n0, n4, listener);
    assertEquals(false, result);

    result = DagShortestPath.exists(graph, n1, n0, listener);
    assertEquals(false, result);
    result = DagShortestPath.exists(graph, n2, n0, listener);
    assertEquals(false, result);
    result = DagShortestPath.exists(graph, n3, n0, listener);
    assertEquals(false, result);
    result = DagShortestPath.exists(graph, n4, n0, listener);
    assertEquals(false, result);

    assertEquals(false, DagShortestPath.exists(graph, n1, n0, null));
    assertEquals(true, DagShortestPath.exists(graph, n1, n2, null));
    assertEquals(true, DagShortestPath.exists(graph, n1, n3, null));
    assertEquals(false, DagShortestPath.exists(graph, n1, n4, null));

    assertEquals(false, DagShortestPath.exists(graph, n2, n0, null));
    assertEquals(false, DagShortestPath.exists(graph, n2, n1, null));
    assertEquals(false, DagShortestPath.exists(graph, n2, n3, null));
    assertEquals(false, DagShortestPath.exists(graph, n2, n4, null));

    assertEquals(false, DagShortestPath.exists(graph, n3, n0, null));
    assertEquals(false, DagShortestPath.exists(graph, n3, n1, null));
    assertEquals(false, DagShortestPath.exists(graph, n3, n2, null));
    assertEquals(false, DagShortestPath.exists(graph, n3, n4, null));

    assertEquals(false, DagShortestPath.exists(graph, n4, n0, null));
    assertEquals(false, DagShortestPath.exists(graph, n4, n1, null));
    assertEquals(false, DagShortestPath.exists(graph, n4, n2, null));
    assertEquals(false, DagShortestPath.exists(graph, n4, n3, null));
  }
}
