package com.github.vangj.jbayes.inf.exact.sampling;

import com.github.vangj.jbayes.inf.exact.graph.Dag;
import com.github.vangj.jbayes.inf.exact.graph.Node;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class LogicSamplerTest {

  @Test
  public void testSampling() {
    Node a = Node.builder()
        .id("0")
        .name("a")
        .value("on")
        .value("off")
        .probs(Arrays.asList(0.5d, 0.5d))
        .build();
    Node b = Node.builder()
        .id("1")
        .name("b")
        .value("on")
        .value("off")
        .probs(Arrays.asList(0.5d, 0.5d, 0.4d, 0.6d))
        .build();
    Node c = Node.builder()
        .id("2")
        .name("c")
        .value("on")
        .value("off")
        .probs(Arrays.asList(0.7d, 0.3d, 0.2d, 0.8d))
        .build();
    Dag dag = (Dag) new Dag()
        .addNode(a)
        .addNode(b)
        .addNode(c)
        .addEdge("0", "1")
        .addEdge("1", "2");

    LogicSampler sampler = new LogicSampler(dag);
    int seed = 37;
    int nSamples = 10000;
    Map<String, String> evidence = new HashMap<>();

    List<Map<String, String>> samples = sampler.getSamples(evidence, nSamples, seed);

    Assert.assertEquals(nSamples, samples.size());

    int n00 = 0;
    int n01 = 0;
    int n10 = 0;
    int n11 = 0;
    int n20 = 0;
    int n21 = 0;

    for (Map<String, String> sample : samples) {
      if (sample.get("0") == "on") {
        n01++;
      } else {
        n00++;
      }
      if (sample.get("1") == "on") {
        n11++;
      } else {
        n10++;
      }
      if (sample.get("2") == "on") {
        n21++;
      } else {
        n20++;
      }
    }

    int n = samples.size();

    double p00 = n00 / (double) n;
    double p01 = n01 / (double) n;
    double p10 = n10 / (double) n;
    double p11 = n11 / (double) n;
    double p20 = n20 / (double) n;
    double p21 = n21 / (double) n;

    List<Double> observed = Arrays.asList(
        p00, p01,
        p10, p11,
        p20, p21
    );

    List<Double> expected = Arrays.asList(
        0.4985d, 0.5015d,
        0.5502d, 0.4498d,
        0.5721d, 0.4279d
    );

    for (int i = 0; i < observed.size(); i++) {
      double lProb = observed.get(i);
      double rProb = expected.get(i);
      double diff = Math.abs(lProb - rProb);
      Assert.assertTrue(diff < 0.01d);
    }
  }

  @Test
  public void testSamplingWithRejection() {
    Node a = Node.builder()
        .id("0")
        .name("a")
        .value("on")
        .value("off")
        .probs(Arrays.asList(0.5d, 0.5d))
        .build();
    Node b = Node.builder()
        .id("1")
        .name("b")
        .value("on")
        .value("off")
        .probs(Arrays.asList(0.5d, 0.5d, 0.4d, 0.6d))
        .build();
    Node c = Node.builder()
        .id("2")
        .name("c")
        .value("on")
        .value("off")
        .probs(Arrays.asList(0.7d, 0.3d, 0.2d, 0.8d))
        .build();
    Dag dag = (Dag) new Dag()
        .addNode(a)
        .addNode(b)
        .addNode(c)
        .addEdge("0", "1")
        .addEdge("1", "2");

    LogicSampler sampler = new LogicSampler(dag);
    int seed = 37;
    int nSamples = 10000;
    Map<String, String> evidence = new HashMap<String, String>() {{
      put("0", "on");
    }};

    List<Map<String, String>> samples = sampler.getSamples(evidence, nSamples, seed);

    Assert.assertEquals(nSamples, samples.size());

    int n00 = 0;
    int n01 = 0;
    int n10 = 0;
    int n11 = 0;
    int n20 = 0;
    int n21 = 0;

    for (Map<String, String> sample : samples) {
      if (sample.get("0") == "on") {
        n01++;
      } else {
        n00++;
      }
      if (sample.get("1") == "on") {
        n11++;
      } else {
        n10++;
      }
      if (sample.get("2") == "on") {
        n21++;
      } else {
        n20++;
      }
    }

    int n = samples.size();

    double p00 = n00 / (double) n;
    double p01 = n01 / (double) n;
    double p10 = n10 / (double) n;
    double p11 = n11 / (double) n;
    double p20 = n20 / (double) n;
    double p21 = n21 / (double) n;

    List<Double> observed = Arrays.asList(
        p00, p01,
        p10, p11,
        p20, p21
    );

    List<Double> expected = Arrays.asList(
        0.0d, 1.0d,
        0.5006d, 0.4994d,
        0.5521d, 0.4479d
    );

    for (int i = 0; i < observed.size(); i++) {
      double lProb = observed.get(i);
      double rProb = expected.get(i);
      double diff = Math.abs(lProb - rProb);
      Assert.assertTrue(diff < 0.01d);
    }
  }
}
