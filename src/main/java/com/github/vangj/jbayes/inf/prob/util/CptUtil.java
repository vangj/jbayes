package com.github.vangj.jbayes.inf.prob.util;

import com.github.vangj.jbayes.inf.prob.Cpt;
import com.github.vangj.jbayes.inf.prob.Node;
import com.github.vangj.jbayes.inf.prob.cpt.CptPoDfsTraversal;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds conditional probability tables (CPTs).
 */
public class CptUtil {

  private CptUtil() {

  }

  /**
   * Converts the CPT into matrix form.
   *
   * @param cpt CPT.
   * @return 2D representation of CPT.
   */
  public static double[][] getMatrix(Cpt cpt) {
    ArrayAccumulatorListener listener = new ArrayAccumulatorListener();
    (new CptPoDfsTraversal(cpt, listener)).start();
    return listener.get();
  }

  /**
   * Builds a CPT for the specified node using the specified conditional probabilities passed in.
   *
   * @param node  Node.
   * @param probs Conditional proabilities.
   * @return Cpt.
   */
  public static Cpt build(Node node, double[][] probs) {
    Cpt cpt = build(node);

    CptPoDfsTraversal.CptPoDfsTraversalListener listener =
        new CptPoDfsTraversal.CptPoDfsTraversalListener() {
          int row = 0;

          @Override
          public void visited(Cpt cpt) {
            if (cpt.numOfValues() > 0) {
              if (row >= probs.length) {
                //return, not enough cpts were specified
                return;
              }

              if (cpt.numOfValues() != probs[row].length) {
                //throw a fit if the cardinalities do not match
                throw new IllegalArgumentException(
                    String.format(
                        "cardinality mismatch, cpt = %d and probs = %d",
                        cpt.numOfValues(),
                        probs[row].length
                    ));
              }

              for (int col = 0; col < probs[row].length; col++) {
                cpt.getValues().set(col, probs[row][col]);
              }

              row++;
            }
          }
        };
    (new CptPoDfsTraversal(cpt, listener)).start();

    return cpt;
  }

  /**
   * Builds a CPT for a node. Random conditional probabilities are assigned.
   *
   * @param node Node.
   * @return Cpt.
   */
  public static Cpt build(Node node) {

    if (!node.hasParents()) {
      return Cpt.newBuilder()
          .values(randomValues(node.numValues()))
          .build();
    }

    Cpt root = new Cpt();
    build(node, root, 0);
    return root;
  }

  private static void build(Node node, Cpt cpt, int paIndex) {
    if (paIndex >= node.numParents()) {
      cpt.setValues(randomValues(node.numValues()));
      return;
    }

    Node parent = node.getParent(paIndex);
    int numValues = parent.numValues();
    for (int i = 0; i < numValues; i++) {
      Cpt child = Cpt.newBuilder()
          .index(i)
          .build();
      cpt.addChild(child);
    }

    for (int i = 0; i < numValues; i++) {
      Cpt child = cpt.get(i);
      build(node, child, paIndex + 1);
    }
  }

  /**
   * Generates a List of Doubles.
   *
   * @param total Total number of Doubles to generate.
   * @return List of Doubles.
   */
  private static List<Double> randomValues(int total) {
    List<Double> values = new ArrayList<>();
    double sum = 0.0d;
    for (int i = 0; i < total; i++) {
      double d = RandomUtil.nextDouble();
      values.add(d);
      sum += d;
    }
    for (int i = 0; i < total; i++) {
      double p = values.get(i) / sum;
      values.set(i, p);
    }
    return values;
  }

  private static class ArrayAccumulatorListener implements
      CptPoDfsTraversal.CptPoDfsTraversalListener {

    private final List<List<Double>> list = new ArrayList<>();

    @Override
    public void visited(Cpt cpt) {
      if (cpt.numOfValues() > 0) {
        list.add(cpt.getValues());
      }
    }

    public double[][] get() {
      final int rows = list.size();
      final int cols = list.get(0).size();

      double[][] cpts = new double[rows][cols];
      for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
          cpts[r][c] = list.get(r).get(c);
        }
      }
      return cpts;
    }
  }
}
