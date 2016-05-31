package com.github.vangj.jbayes.inf.exact.graph.pptc;

import com.github.vangj.jbayes.inf.exact.graph.Node;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EvidenceTest extends HuangExample {

  @Test
  public void testVirtual() {
    Node node = getNode("a");

    Evidence evidence = Evidence.newBuilder()
        .node(node)
        .type(Evidence.Type.Virtual)
        .value("on", 1.0d)
        .build();

    assertEquals(2, evidence.getValues().size());
    assertEquals(1.0d, evidence.getValues().get("on"), 0.01d);
    assertEquals(0.0d, evidence.getValues().get("off"), 0.01d);

    evidence = Evidence.newBuilder()
        .node(node)
        .type(Evidence.Type.Virtual)
        .value("on", 1.0d)
        .value("off", 1.0d)
        .build();

    assertEquals(2, evidence.getValues().size());
    assertEquals(0.5d, evidence.getValues().get("on"), 0.01d);
    assertEquals(0.5d, evidence.getValues().get("off"), 0.01d);

    evidence = Evidence.newBuilder()
        .node(node)
        .type(Evidence.Type.Virtual)
        .value("on", 2.0d)
        .build();

    assertEquals(2, evidence.getValues().size());
    assertEquals(1.0d, evidence.getValues().get("on"), 0.01d);
    assertEquals(0.0d, evidence.getValues().get("off"), 0.01d);

    evidence = Evidence.newBuilder()
        .node(node)
        .type(Evidence.Type.Virtual)
        .value("on", 2.0d)
        .value("off", 1.0d)
        .build();

    assertEquals(2, evidence.getValues().size());
    assertEquals(0.66d, evidence.getValues().get("on"), 0.01d);
    assertEquals(0.33d, evidence.getValues().get("off"), 0.01d);
  }

  @Test
  public void testFinding() {
    Node node = getNode("a");

    Evidence evidence = Evidence.newBuilder()
        .node(node)
        .type(Evidence.Type.Finding)
        .value("on", 1.0d)
        .build();

    assertEquals(2, evidence.getValues().size());
    assertEquals(1.0d, evidence.getValues().get("on"), 0.01d);
    assertEquals(0.0d, evidence.getValues().get("off"), 0.01d);

    evidence = Evidence.newBuilder()
        .node(node)
        .type(Evidence.Type.Finding)
        .value("on", 0.5d)
        .value("off", 0.5d)
        .build();

    assertEquals(2, evidence.getValues().size());
    assertEquals(1.0d, evidence.getValues().get("on"), 0.01d);
    assertEquals(1.0d, evidence.getValues().get("off"), 0.01d);

    evidence = Evidence.newBuilder()
        .node(node)
        .type(Evidence.Type.Finding)
        .value("on", 0.0d)
        .value("off", 0.0d)
        .build();

    assertEquals(2, evidence.getValues().size());
    assertEquals(1.0d, evidence.getValues().get("on"), 0.01d);
    assertEquals(1.0d, evidence.getValues().get("off"), 0.01d);
  }

  @Test
  public void testObservation() {
    Node node = getNode("a");

    Evidence evidence = Evidence.newBuilder()
        .node(node)
        .type(Evidence.Type.Observation)
        .value("on", 1.0d)
        .build();

    assertEquals(2, evidence.getValues().size());
    assertEquals(1.0d, evidence.getValues().get("on"), 0.01d);
    assertEquals(0.0d, evidence.getValues().get("off"), 0.01d);

    evidence = Evidence.newBuilder()
        .node(node)
        .type(Evidence.Type.Observation)
        .value("on", 1.0d)
        .value("off", 1.5d)
        .build();

    assertEquals(2, evidence.getValues().size());
    assertEquals(0.0d, evidence.getValues().get("on"), 0.01d);
    assertEquals(1.0d, evidence.getValues().get("off"), 0.01d);
  }
}
