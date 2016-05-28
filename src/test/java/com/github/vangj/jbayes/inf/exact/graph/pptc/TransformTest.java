package com.github.vangj.jbayes.inf.exact.graph.pptc;

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
        System.out.println("starting check for shortest path from " + start + " to " + stop);
      }

      @Override public void visited(Clique clique) {
        System.out.println("visited " + clique);
      }

      @Override public void post(JoinTree joinTree, Clique start, Clique stop) {
        System.out.println("finished check for shortest path from " + start + " to " + stop);
      }
    };

    JoinTree joinTree = Transform.transform(cliques, listener);
    System.out.println(joinTree);
  }
}
