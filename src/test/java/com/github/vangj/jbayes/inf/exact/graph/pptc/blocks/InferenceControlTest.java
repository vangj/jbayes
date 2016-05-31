package com.github.vangj.jbayes.inf.exact.graph.pptc.blocks;

import com.github.vangj.jbayes.inf.exact.graph.lpd.Potential;
import com.github.vangj.jbayes.inf.exact.graph.pptc.Evidence;
import com.github.vangj.jbayes.inf.exact.graph.pptc.HuangExample;
import com.github.vangj.jbayes.inf.exact.graph.pptc.JoinTree;
import org.junit.Test;

import static org.junit.Assert.*;
import static com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.InferenceControl.*;

public class InferenceControlTest extends HuangExample {
  @Test
  public void testInference() {
    JoinTree joinTree = apply(getDag());
    joinTree.nodes().forEach(node -> {
      Potential potential = joinTree.getPotential(node);
      System.out.println(node);
      System.out.println(potential);
      System.out.println("---------------");
    });

    joinTree.updateEvidence(Evidence.newBuilder()
        .node(joinTree.node("a"))
        .value("on", 1.0)
        .type(Evidence.Type.Observation)
        .build());

    System.out.println("***************");

    joinTree.nodes().forEach(node -> {
      Potential potential = joinTree.getPotential(node);
      System.out.println(node);
      System.out.println(potential);
      System.out.println("---------------");
    });

    joinTree.updateEvidence(Evidence.newBuilder()
        .node(joinTree.node("a"))
        .value("off", 1.0)
        .type(Evidence.Type.Observation)
        .build());

    System.out.println("***************");

    joinTree.nodes().forEach(node -> {
      Potential potential = joinTree.getPotential(node);
      System.out.println(node);
      System.out.println(potential);
      System.out.println("---------------");
    });
  }
}
