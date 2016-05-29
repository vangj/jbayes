package com.github.vangj.jbayes.inf.exact.graph.util;

import com.github.vangj.jbayes.inf.exact.graph.Dag;
import com.github.vangj.jbayes.inf.exact.graph.Node;
import com.github.vangj.jbayes.inf.exact.graph.Ug;
import com.github.vangj.jbayes.inf.exact.graph.lpd.Potential;
import com.github.vangj.jbayes.inf.exact.graph.lpd.PotentialEntry;
import com.github.vangj.jbayes.inf.exact.graph.pptc.Clique;
import com.github.vangj.jbayes.inf.exact.graph.pptc.HuangExample;
import com.github.vangj.jbayes.inf.exact.graph.pptc.JoinTree;
import com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.Initialization;
import com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.Moralize;
import com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.Transform;
import com.github.vangj.jbayes.inf.exact.graph.pptc.blocks.Triangulate;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.vangj.jbayes.inf.exact.graph.util.PotentialUtil.*;
import static org.junit.Assert.*;

public class PotentialUtilTest extends HuangExample {

  @Test
  public void testMarginalizeOut() {
    Dag dag = getDag();
    Ug m = Moralize.moralize(dag);
    List<Clique> cliques = Triangulate.triangulate(m);
    JoinTree joinTree = Transform.transform(cliques, null);
    Initialization.initialization(joinTree);

//    System.out.println(joinTree);

    Clique clique = joinTree.clique(getNode("e"), getNode("g"), getNode("h"));
    Potential potential = PotentialUtil.marginalizeOut(joinTree, clique, Arrays.asList(getNode("g"), getNode("h")));
//    System.out.println("--------------");
//    System.out.println(potential);
    assertEquals(2, potential.entries().size());
    assertEquals(2.0d, potential.entries().get(0).getValue(), 0.01d);
    assertEquals(2.0d, potential.entries().get(1).getValue(), 0.01d);

    potential = PotentialUtil.marginalizeOut(joinTree, clique, Arrays.asList(getNode("e"), getNode("h")));
//    System.out.println("--------------");
//    System.out.println(potential);
    assertEquals(2, potential.entries().size());
    assertEquals(2.0d, potential.entries().get(0).getValue(), 0.01d);
    assertEquals(2.0d, potential.entries().get(1).getValue(), 0.01d);

    potential = PotentialUtil.marginalizeOut(joinTree, clique, Arrays.asList(getNode("e"), getNode("g")));
//    System.out.println("--------------");
//    System.out.println(potential);
    assertEquals(2, potential.entries().size());
    assertEquals(2.9d, potential.entries().get(0).getValue(), 0.01d);
    assertEquals(1.1d, potential.entries().get(1).getValue(), 0.01d);

    potential = PotentialUtil.marginalizeOut(joinTree, clique, Arrays.asList(getNode("e")));
//    System.out.println("--------------");
//    System.out.println(potential);
    assertEquals(4, potential.entries().size());
    assertEquals(1.0d, potential.entries().get(0).getValue(), 0.01d);
    assertEquals(1.0d, potential.entries().get(1).getValue(), 0.01d);
    assertEquals(1.9d, potential.entries().get(2).getValue(), 0.01d);
    assertEquals(0.1d, potential.entries().get(3).getValue(), 0.01d);
  }

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

    assertEquals(0.5d, potential.entries().get(0).getValue(), 0.001d);
    assertEquals(0.5d, potential.entries().get(1).getValue(), 0.001d);
    assertEquals(0.4d, potential.entries().get(2).getValue(), 0.001d);
    assertEquals(0.6d, potential.entries().get(3).getValue(), 0.001d);
  }

  @Test
  public void testMultiply() {
    Dag dag = getDag();
    Potential c = dag.node("c").getPotential();
    Potential e = dag.node("e").getPotential();
    Potential ace = PotentialUtil.getPotential(Arrays.asList(dag.node("a"), dag.node("c"), dag.node("e")));

    multiply(ace, c);
    multiply(ace, e);

    double[] expected = { 0.21d, 0.49d, 0.18d, 0.12d, 0.06d, 0.14d, 0.48d, 0.32d };
    assertEquals(expected.length, ace.entries().size());
    for(int i=0; i < expected.length; i++) {
      assertEquals(expected[i], ace.entries().get(i).getValue(), 0.001d);
    }
  }

  @Test
  public void testDivide() {
    Potential potential1 = new Potential()
        .addEntry(new PotentialEntry().add("a", "on").add("b", "on").setValue(2.0d))
        .addEntry(new PotentialEntry().add("a", "on").add("b", "off").setValue(3.0d))
        .addEntry(new PotentialEntry().add("a", "off").add("b", "on").setValue(10.0d))
        .addEntry(new PotentialEntry().add("a", "off").add("b", "off").setValue(20.0d));

    Potential potential2 = new Potential()
        .addEntry(new PotentialEntry().add("a", "on").add("b", "on").setValue(2.0d))
        .addEntry(new PotentialEntry().add("a", "on").add("b", "off").setValue(2.0d))
        .addEntry(new PotentialEntry().add("a", "off").add("b", "on").setValue(5.0d))
        .addEntry(new PotentialEntry().add("a", "off").add("b", "off").setValue(2.0d));

    Potential result = divide(potential1, potential2);
//    System.out.println(result);
    assertEquals(4, result.entries().size());
    assertEquals(1.0d, result.entries().get(0).getValue(), 0.01d);
    assertEquals(1.5d, result.entries().get(1).getValue(), 0.01d);
    assertEquals(2.0d, result.entries().get(2).getValue(), 0.01d);
    assertEquals(10.0d, result.entries().get(3).getValue(), 0.01d);
  }
}
