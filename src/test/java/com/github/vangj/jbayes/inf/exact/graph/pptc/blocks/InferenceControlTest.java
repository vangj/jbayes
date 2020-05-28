package com.github.vangj.jbayes.inf.exact.graph.pptc.blocks;

import static com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.InferenceControl.apply;

import com.github.vangj.jbayes.inf.exact.graph.lpd.Potential;
import com.github.vangj.jbayes.inf.exact.graph.pptc.Evidence;
import com.github.vangj.jbayes.inf.exact.graph.pptc.HuangExample;
import com.github.vangj.jbayes.inf.exact.graph.pptc.JoinTree;
import java.util.Arrays;
import org.junit.Test;

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

  @Test
  public void testMultipleObservations() {
    JoinTree joinTree = apply(getDag());
    joinTree.updateEvidence(Arrays.asList(
        Evidence.newBuilder().node(joinTree.node("a")).value("on", 1.0d).build(),
        Evidence.newBuilder().node(joinTree.node("f")).value("on", 1.0d).build()
    ));

    joinTree.nodes().forEach(node -> {
      Potential potential = joinTree.getPotential(node);
      System.out.println(node);
      System.out.println(potential);
      System.out.println("---------------");
    });
  }

  @Test
  public void testObserveThenUnobserveSingle() {
    JoinTree joinTree = apply(getDag());

    joinTree.updateEvidence(Evidence.newBuilder()
        .node(joinTree.node("a"))
        .value("on", 1.0)
        .type(Evidence.Type.Observation)
        .build());

    System.out.println(joinTree.getPotential(joinTree.node("a")));

    joinTree.unobserve(joinTree.node("a"));

    System.out.println(joinTree.getPotential(joinTree.node("a")));
  }

  @Test
  public void testObserveMultipleThenUnobserveAll() {
    JoinTree joinTree = apply(getDag());
    joinTree.updateEvidence(Arrays.asList(
        Evidence.newBuilder().node(joinTree.node("a")).value("on", 1.0d).build(),
        Evidence.newBuilder().node(joinTree.node("f")).value("on", 1.0d).build()
    ));

    joinTree.nodes().forEach(node -> {
      Potential potential = joinTree.getPotential(node);
      System.out.println(node);
      System.out.println(potential);
      System.out.println("---------------");
    });

    System.out.println("***************");

    joinTree.unobserveAll();
    joinTree.nodes().forEach(node -> {
      Potential potential = joinTree.getPotential(node);
      System.out.println(node);
      System.out.println(potential);
      System.out.println("---------------");
    });
  }
}
