package com.github.vangj.jbayes.inf.exact.graph.pptc.traversal;

import com.github.vangj.jbayes.inf.exact.graph.Dag;
import com.github.vangj.jbayes.inf.exact.graph.Ug;
import com.github.vangj.jbayes.inf.exact.graph.pptc.Clique;
import com.github.vangj.jbayes.inf.exact.graph.pptc.HuangExample;
import com.github.vangj.jbayes.inf.exact.graph.pptc.JoinTree;
import com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.Initialization;
import com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.Moralize;
import com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.Transform;
import com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.Triangulate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DistributeEvidenceTest extends HuangExample {
  @Test
  public void testTraversal() {
    Dag dag = getDag();
    Ug m = Moralize.moralize(dag);
    List<Clique> cliques = Triangulate.triangulate(m);
    JoinTree joinTree = Transform.transform(cliques, null);
    Initialization.initialization(joinTree);

    final List<Clique> visited = new ArrayList<>();
//    System.out.println(joinTree);
    DistributeEvidence.Listener listener = (joinTree1, x, s, y) -> {
//      System.out.println(x + " -- " + s + " -- " + y);
      visited.add(x);
    };

    new DistributeEvidence(joinTree, joinTree.cliques().get(0), listener).start();

    assertEquals(5, visited.size());

    assertTrue(visited.get(0).contains("e"));
    assertTrue(visited.get(0).contains("g"));
    assertTrue(visited.get(0).contains("h"));

    assertTrue(visited.get(1).contains("c"));
    assertTrue(visited.get(1).contains("e"));
    assertTrue(visited.get(1).contains("g"));

    assertTrue(visited.get(2).contains("c"));
    assertTrue(visited.get(2).contains("d"));
    assertTrue(visited.get(2).contains("e"));

    assertTrue(visited.get(3).contains("b"));
    assertTrue(visited.get(3).contains("c"));
    assertTrue(visited.get(3).contains("d"));

    assertTrue(visited.get(4).contains("c"));
    assertTrue(visited.get(4).contains("d"));
    assertTrue(visited.get(4).contains("e"));
  }
}
