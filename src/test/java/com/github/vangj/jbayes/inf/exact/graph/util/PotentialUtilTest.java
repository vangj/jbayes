package com.github.vangj.jbayes.inf.exact.graph.util;

import com.github.vangj.jbayes.inf.exact.graph.Node;
import com.github.vangj.jbayes.inf.exact.graph.lpd.Potential;
import com.github.vangj.jbayes.inf.exact.graph.lpd.PotentialEntry;
import com.github.vangj.jbayes.inf.exact.graph.pptc.HuangExample;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PotentialUtilTest extends HuangExample {

  @Test
  public void testCartesian() {
    List<List<String>> domains = Arrays.asList(
        Arrays.asList("a", "b"),
        Arrays.asList("c", "d")
    );
    List<List<String>> product = PotentialUtil.cartesian(domains);
    product.forEach(p -> System.out.println(String.join(",", p)));

    assertEquals(4, product.size());
    product.forEach(p -> assertEquals(2, p.size()));
    assertEquals("a", product.get(0).get(0)); assertEquals("c", product.get(0).get(1));
    assertEquals("a", product.get(1).get(0)); assertEquals("d", product.get(1).get(1));
    assertEquals("b", product.get(2).get(0)); assertEquals("c", product.get(2).get(1));
    assertEquals("b", product.get(3).get(0)); assertEquals("d", product.get(3).get(1));
  }

  @Test
  public void testGetPotential() {
    Node a = getNode("a");
    Node b = getNode("b");

    Potential potential = PotentialUtil.getPotential(b, Arrays.asList(a));
    assertEquals(4, potential.entries().size());
    assertEquals((new PotentialEntry()).add("a", "on").add("b", "on"), potential.entries().get(0));
    assertEquals((new PotentialEntry()).add("a", "on").add("b", "off"), potential.entries().get(1));
    assertEquals((new PotentialEntry()).add("a", "off").add("b", "on"), potential.entries().get(2));
    assertEquals((new PotentialEntry()).add("a", "off").add("b", "off"), potential.entries().get(3));
  }
}
