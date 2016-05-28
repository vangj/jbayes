package com.github.vangj.jbayes.inf.exact.graph.pptc;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TransformTest extends HuangExample {
  @Test
  public void testTransform() {
    Clique egh = new Clique(getNode("e"), getNode("g"), getNode("h"));
    Clique ceg = new Clique(getNode("c"), getNode("e"), getNode("g"));
    Clique def = new Clique(getNode("d"), getNode("e"), getNode("f"));
    Clique ace = new Clique(getNode("a"), getNode("c"), getNode("e"));
    Clique abd = new Clique(getNode("a"), getNode("b"), getNode("d"));
    Clique ade = new Clique(getNode("a"), getNode("d"), getNode("e"));

    List<Clique> cliques = Arrays.asList(egh, ceg, def, ace, abd, ade);

    JoinTreeShortestPath.Listener listener = new JoinTreeShortestPath.Listener() {
      @Override public void pre(JoinTree joinTree, Clique start, Clique stop) {
//        System.out.println("starting check for shortest path from " + start + " to " + stop);
      }

      @Override public void visited(Clique clique) {
//        System.out.println("visited " + clique);
      }

      @Override public void post(JoinTree joinTree, Clique start, Clique stop) {
//        System.out.println("finished check for shortest path from " + start + " to " + stop);
      }
    };

    JoinTree joinTree = Transform.transform(cliques, listener);
    System.out.println(joinTree);

    assertEquals(11, joinTree.allCliques().size());

    assertEquals(6, joinTree.cliques().size());
    assertNotNull(joinTree.clique(getNode("a"), getNode("b"), getNode("d")));
    assertNotNull(joinTree.clique(getNode("a"), getNode("d"), getNode("e")));
    assertNotNull(joinTree.clique(getNode("a"), getNode("c"), getNode("e")));
    assertNotNull(joinTree.clique(getNode("c"), getNode("e"), getNode("g")));
    assertNotNull(joinTree.clique(getNode("d"), getNode("e"), getNode("f")));
    assertNotNull(joinTree.clique(getNode("e"), getNode("g"), getNode("h")));

    assertEquals(5, joinTree.sepSets().size());
    assertNotNull(joinTree.sepSet(getNode("a"), getNode("d")));
    assertNotNull(joinTree.sepSet(getNode("a"), getNode("e")));
    assertNotNull(joinTree.sepSet(getNode("c"), getNode("e")));
    assertNotNull(joinTree.sepSet(getNode("d"), getNode("e")));
    assertNotNull(joinTree.sepSet(getNode("e"), getNode("g")));


    List<Edge> edges = joinTree.edges();
    assertEquals(10, edges.size());

    List<Edge> expectedEdges = Arrays.asList(
        new Edge(abd, joinTree.sepSet(getNode("a"), getNode("d"))),
        new Edge(abd, joinTree.sepSet(getNode("a"), getNode("d"))),
        new Edge(ade, joinTree.sepSet(getNode("a"), getNode("d"))),
        new Edge(ade, joinTree.sepSet(getNode("a"), getNode("e"))),
        new Edge(ade, joinTree.sepSet(getNode("d"), getNode("e"))),
        new Edge(def, joinTree.sepSet(getNode("d"), getNode("e"))),
        new Edge(ace, joinTree.sepSet(getNode("a"), getNode("e"))),
        new Edge(ace, joinTree.sepSet(getNode("c"), getNode("e"))),
        new Edge(ceg, joinTree.sepSet(getNode("c"), getNode("e"))),
        new Edge(ceg, joinTree.sepSet(getNode("e"), getNode("g"))),
        new Edge(egh, joinTree.sepSet(getNode("e"), getNode("g")))
    );

    for(Edge expected : expectedEdges) {
      assertTrue(edges.contains(expected));
    }
  }
}
