package com.github.vangj.jbayes.inf.exact.graph.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DomainUtilTest {

  @Test
  public void testCartesian() {
    List<List<String>> domains = Arrays.asList(
        Arrays.asList("a", "b"),
        Arrays.asList("c", "d")
    );
    List<List<String>> product = DomainUtil.cartesian(domains);
    product.forEach(p -> System.out.println(String.join(",", p)));

    assertEquals(4, product.size());
    product.forEach(p -> assertEquals(2, p.size()));
    assertEquals("a", product.get(0).get(0)); assertEquals("c", product.get(0).get(1));
    assertEquals("a", product.get(1).get(0)); assertEquals("d", product.get(1).get(1));
    assertEquals("b", product.get(2).get(0)); assertEquals("c", product.get(2).get(1));
    assertEquals("b", product.get(3).get(0)); assertEquals("d", product.get(3).get(1));
  }
}
