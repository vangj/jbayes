package com.github.vangj.jbayes.inf.prob;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.vangj.jbayes.inf.prob.util.CptUtil;
import com.github.vangj.jbayes.inf.prob.util.RandomUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Node.
 */
@JsonInclude(content = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Node {

  @JsonIgnore
  private final List<Node> parents;
  private String name;
  private List<String> values;
  @JsonIgnore
  private int value = -1;
  @JsonIgnore
  private boolean observed = false;
  @JsonIgnore
  private boolean wasSampled = false;
  @JsonIgnore
  private List<Double> sampledLw;
  @JsonIgnore
  private Cpt cpt;

  public Node() {
    parents = new ArrayList<>();
    sampledLw = new ArrayList<>();
  }

  private Node(Builder b) {
    this.name = b.name;
    this.values = b.values;
    this.parents = b.parents;
  }

  /**
   * Gets a new Builder instance.
   *
   * @return Builder.
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  public Cpt getCpt() {
    return cpt;
  }

  public void setCpt(Cpt cpt) {
    this.cpt = cpt;
  }

  public void setCpt(double[][] probs) {
    cpt = CptUtil.build(this, probs);
  }

  public List<String> getValues() {
    return values;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public boolean isWasSampled() {
    return wasSampled;
  }

  public void setWasSampled(boolean wasSampled) {
    this.wasSampled = wasSampled;
  }

  public List<Double> getSampledLw() {
    return sampledLw;
  }

  @JsonIgnore
  public String getSampledValue() {
    return values.get(value);
  }

  public void resetSampledLw() {
    if (null == sampledLw || 0 == sampledLw.size()) {
      return;
    }

    final int size = sampledLw.size();
    for (int i = 0; i < size; i++) {
      sampledLw.set(i, 0.0d);
    }
  }

  public void observe(String value) {
    this.value = valueIndex(value);
    this.observed = true;
  }

  public void unobserve() {
    this.value = -1;
    this.observed = false;
  }

  private int valueIndex(String value) {
    int index = -1;
    final int size = values.size();
    for (int i = 0; i < size; i++) {
      String v = values.get(i);
      if (value.equals(v)) {
        index = i;
        break;
      }
    }
    return index;
  }

  public String getName() {
    return name;
  }

  public boolean isObserved() {
    return observed;
  }

  public List<Node> getParents() {
    return parents;
  }

  public int numParents() {
    return parents.size();
  }

  public int numValues() {
    return values.size();
  }

  public boolean hasParents() {
    return !(0 == parents.size());
  }

  public Node getParent(int index) {
    return parents.get(index);
  }

  public Node addParent(Node pa) {
    if (!parents.contains(pa)) {
      parents.add(pa);
    }
    return this;
  }

  /**
   * Converts the sampled values to probabilities.
   *
   * @return Marginal probabilities.
   */
  public double[] probs() {
    double sum = 0.0d;
    for (double lw : sampledLw) {
      sum += lw;
    }

    final int size = sampledLw.size();
    double[] probs = new double[size];
    for (int i = 0; i < size; i++) {
      probs[i] = sampledLw.get(i) / sum;
    }

    return probs;
  }

  public double sampleLw() {
    if (wasSampled) {
      return 1;
    }

    double fa = 1.0d;
    for (Node pa : parents) {
      fa *= pa.sampleLw();
    }

    wasSampled = true;

    final int numParents = parents.size();
    Cpt dh = cpt;
    for (int i = 0; i < numParents; i++) {
      int v = parents.get(i).value;
      dh = dh.get(v);
    }

    if (value != -1) {
      fa *= dh.getValue(value);
    } else {
      double fv = RandomUtil.nextDouble();
      final int dhSize = dh.numOfValues();
      for (int h = 0; h < dhSize; h++) {
        fv -= dh.getValue(h);
        if (fv < 0.0d) {
          value = h;
          break;
        }
      }
    }

    return fa;
  }

  public void saveSampleLw(double f) {
    if (null == sampledLw) {
      sampledLw = new ArrayList<>(values.size());
    }

    if (0 == sampledLw.size()) {
      for (String v : values) {
        sampledLw.add(0.0d);
      }
    }

    double s = sampledLw.get(value) + f;
    sampledLw.set(value, s);
  }

  /**
   * Builder for Node.
   */
  public static final class Builder {

    private final List<String> values = new ArrayList<>();
    private final List<Node> parents = new ArrayList<>();
    private String name;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder value(String value) {
      values.add(value);
      return this;
    }

    public Builder parent(Node parent) {
      parents.add(parent);
      return this;
    }

    public Node build() {
      return new Node(this);
    }
  }
}
