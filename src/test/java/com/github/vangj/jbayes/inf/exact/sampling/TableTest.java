package com.github.vangj.jbayes.inf.exact.sampling;

import com.github.vangj.jbayes.inf.exact.graph.Node;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class TableTest {

  @Test
  public void testNoParents() {
    Node a = Node.builder()
        .id("0")
        .name("a")
        .value("on")
        .value("off")
        .probs(Arrays.asList(0.5d, 0.5d))
        .build();
    Table table = new Table(a, new ArrayList<>());

    Assert.assertFalse(table.hasParents());

    String expected = "on";
    String observed = table.getValue(0.4, new HashMap<>());
    Assert.assertEquals(expected, observed);

    expected = "off";
    observed = table.getValue(0.6, new HashMap<>());
    Assert.assertEquals(expected, observed);
  }

  @Test
  public void testOneParent() {
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

    Table table = new Table(b, Arrays.asList(a));

    Assert.assertTrue(table.hasParents());

    Map<String, String> evidence = new HashMap<String, String>() {{
      put("0", "on");
    }};
    String expected = "on";
    String observed = table.getValue(0.4, evidence);
    Assert.assertEquals(expected, observed);

    expected = "off";
    observed = table.getValue(0.7, evidence);
    Assert.assertEquals(expected, observed);

    evidence = new HashMap<String, String>() {{
      put("0", "off");
    }};
    expected = "on";
    observed = table.getValue(0.3, evidence);
    Assert.assertEquals(expected, observed);

    expected = "off";
    observed = table.getValue(0.6, evidence);
    Assert.assertEquals(expected, observed);
  }

  @Test
  public void testMultipleParents() {
    Node g = Node.builder()
        .id("0")
        .name("gender")
        .value("female")
        .value("male")
        .probs(Arrays.asList(0.49d, 0.51d))
        .build();
    Node d = Node.builder()
        .id("1")
        .name("drug")
        .value("false")
        .value("true")
        .probs(Arrays.asList(0.23323615160349853d, 0.7667638483965015d,
            0.7563025210084033d, 0.24369747899159663d))
        .build();
    Node r = Node.builder()
        .id("1")
        .name("recovery")
        .value("false")
        .value("true")
        .probs(Arrays.asList(0.31000000000000005d, 0.69d,
            0.27d, 0.73d,
            0.13d, 0.87d,
            0.06999999999999995d, 0.93d))
        .build();

    Table table = new Table(r, Arrays.asList(d, g));
    Map<String, List<Double>> lhs = table.getProbs();
    Map<String, List<Double>> rhs = new HashMap<String, List<Double>>() {{
      put("0=female,1=false", Arrays.asList(0.31, 1.0));
      put("0=female,1=true", Arrays.asList(0.27, 1.0));
      put("0=male,1=false", Arrays.asList(0.13, 1.0));
      put("0=male,1=true", Arrays.asList(0.07, 1.0));
    }};

    Assert.assertEquals(lhs.size(), rhs.size());

    for (String k : lhs.keySet()) {
      List<Double> lProbs = lhs.get(k);
      List<Double> rProbs = rhs.get(k);

      Assert.assertEquals(lProbs.size(), rProbs.size());
      for (int i = 0; i < lProbs.size(); i++) {
        double lProb = lProbs.get(i);
        double rProb = rProbs.get(i);
        double diff = Math.abs(lProb - rProb);
        Assert.assertTrue(diff < 0.01d);
      }
    }
  }
}
