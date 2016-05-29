package com.github.vangj.jbayes.inf.exact.graph.pptc.blocks;

import com.github.vangj.jbayes.inf.exact.graph.Dag;
import com.github.vangj.jbayes.inf.exact.graph.Ug;
import com.github.vangj.jbayes.inf.exact.graph.lpd.Potential;
import com.github.vangj.jbayes.inf.exact.graph.pptc.Clique;
import com.github.vangj.jbayes.inf.exact.graph.pptc.HuangExample;
import com.github.vangj.jbayes.inf.exact.graph.pptc.JoinTree;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class InitializationTest extends HuangExample {
  @Test
  public void testInitialization() {
    Dag dag = getDag();
    Ug m = Moralize.moralize(dag);
    List<Clique> cliques = Triangulate.triangulate(m);
    JoinTree joinTree = Transform.transform(cliques, null);
    Initialization.initialization(joinTree);

//    System.out.println("-------------------------------");
//    System.out.println(joinTree);

    joinTree.cliques().forEach(clique -> {
      Potential potential = joinTree.getPotential(clique);
      potential.entries().forEach(entry -> {
        double v = entry.getValue();
        assertTrue(v >= 0.0d && v <= 1.0d);
      });
    });

    joinTree.sepSets().forEach(sepset -> {
      Potential potential = joinTree.getPotential(sepset);
      potential.entries().forEach(entry -> {
        double v = entry.getValue();
        assertTrue(v == 1.0d);
      });
    });
  }
}
