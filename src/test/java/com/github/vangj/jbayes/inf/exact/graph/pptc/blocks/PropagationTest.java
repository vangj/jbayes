package com.github.vangj.jbayes.inf.exact.graph.pptc.blocks;

import com.github.vangj.jbayes.inf.exact.graph.Dag;
import com.github.vangj.jbayes.inf.exact.graph.Ug;
import com.github.vangj.jbayes.inf.exact.graph.lpd.PotentialEntry;
import com.github.vangj.jbayes.inf.exact.graph.pptc.Clique;
import com.github.vangj.jbayes.inf.exact.graph.pptc.HuangExample;
import com.github.vangj.jbayes.inf.exact.graph.pptc.JoinTree;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PropagationTest extends HuangExample {
  @Test
  public void testPropagation() {
    Dag dag = getDag();
    Ug m = Moralize.moralize(dag);
    List<Clique> cliques = Triangulate.triangulate(m);
    JoinTree joinTree = Transform.transform(cliques, null);
    Initialization.initialization(joinTree);
    Propagation.propagate(joinTree);

    joinTree.potentials().forEach(potential -> {
      double sum = 0.0d;
      for(PotentialEntry entry : potential.entries()) {
        sum += entry.getValue();
      }

      assertEquals(1.0d, sum, 0.001d);
    });
  }
}
