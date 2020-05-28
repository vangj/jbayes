package com.github.vangj.jbayes.inf.exact.graph.pptc;

import static org.junit.Assert.assertEquals;

import com.github.vangj.jbayes.inf.exact.graph.Dag;
import com.github.vangj.jbayes.inf.exact.graph.Ug;
import com.github.vangj.jbayes.inf.exact.graph.lpd.Potential;
import com.github.vangj.jbayes.inf.exact.graph.lpd.PotentialEntry;
import com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.Initialization;
import com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.Moralize;
import com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.Propagation;
import com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.Transform;
import com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.Triangulate;
import java.util.List;
import org.junit.Test;

public class JoinTreeTest extends HuangExample {

  @Test
  public void testGetPotential() {
    Dag dag = getDag();
    Ug m = Moralize.moralize(dag);
    List<Clique> cliques = Triangulate.triangulate(m);
    JoinTree joinTree = Transform.transform(cliques, null);
    Initialization.initialization(joinTree);
    Propagation.propagate(joinTree);

    joinTree.nodes().forEach(node -> {
      Potential potential = joinTree.getPotential(node);
//      System.out.println(potential);
      double sum = 0.0d;
      for (PotentialEntry entry : potential.entries()) {
        sum += entry.getValue();
      }
      assertEquals(1.0d, sum, 0.0001);
    });
  }
}
