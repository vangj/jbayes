package net.jbayes;

import java.util.*;

public class CptBuilder {

  public static Cpt build(Node node, double[][] probs) {
    Cpt cpt = build(node);

    CptPoDfsTraversal.CptPoDfsTraversalListener listener =
        new CptPoDfsTraversal.CptPoDfsTraversalListener() {
          int row = 0;
          @Override public void visited(Cpt cpt) {
            if(cpt.numOfValues() > 0) {
              if(row >= probs.length) {
                //return, not enough cpts were specified
                return;
              }

              if(cpt.numOfValues() != probs[row].length) {
                //throw a fit if the cardinalities do not match
                throw new IllegalArgumentException(
                    String.format(
                        "cardinality mismatch, cpt = %d and probs = %d",
                        cpt.numOfValues(),
                        probs[row].length
                    ));
              }

              for(int col=0; col < probs[row].length; col++) {
                cpt.getValues().set(col, probs[row][col]);
              }

              row++;
            }
          }
        };
    (new CptPoDfsTraversal(cpt, listener)).start();

    return cpt;
  }

  public static Cpt build(Node node) {

    if(!node.hasParents()) {
      return Cpt.newBuilder()
          .values(randomValues(node.numValues()))
          .build();
    }

    Cpt root = new Cpt();
    build(node, root, 0);
    return root;
  }

  private static void build(Node node, Cpt cpt, int paIndex) {
    if(paIndex >= node.numParents()) {
      cpt.setValues(randomValues(node.numValues()));
      return;
    }

    Node parent = node.getParent(paIndex);
    int numValues = parent.numValues();
    for(int i=0; i < numValues; i++) {
      Cpt child = Cpt.newBuilder()
          .index(i)
          .build();
      cpt.addChild(child);
    }

    for(int i=0; i < numValues; i++) {
      Cpt child = cpt.get(i);
      build(node, child, paIndex + 1);
    }
  }

  private static List<Double> randomValues(int total) {
    List<Double> values = new ArrayList<>();
    double sum = 0.0d;
    for(int i=0; i < total; i++) {
      double d = RandomUtil.nextDouble();
      values.add(d);
      sum += d;
    }
    for(int i=0; i < total; i++) {
      double p = values.get(i) / sum;
      values.set(i, p);
    }
    return values;
  }
}
