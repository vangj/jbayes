package com.github.vangj.jbayes.inference;



import java.util.ArrayList;
import java.util.List;

/**
 * Represents a conditional probability table.
 */
public class Cpt {
  private int index = -1;
  private List<Double> values;
  private List<Cpt> children;

  public Cpt() {
    values = new ArrayList<>();
    children = new ArrayList<>();
  }

  public List<Double> getValues() {
    return values;
  }

  public List<Cpt> getChildren() {
    return children;
  }

  public void setValues(List<Double> values) {
    this.values = values;
  }

  public int numOfChildren() {
    return children.size();
  }

  public int numOfValues() {
    return values.size();
  }

  public void addChild(Cpt child) {
    children.add(child);
  }

  public Double getValue(int index) {
    return values.get(index);
  }

  public Cpt get(int index) {
    return children.get(index);
  }

  private Cpt(Builder builder) {
    index = builder.index;
    values = builder.values;
    children = builder.children;
  }

  /**
   * Gets a new Builder instance.
   * @return Builder.
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Builder class for Cpt.
   */
  public static final class Builder {
    private int index = -1;
    private List<Double> values = new ArrayList<>();
    private List<Cpt> children = new ArrayList<>();

    private Builder() {
    }

    public Builder index(int val) {
      index = val;
      return this;
    }

    public Builder value(Double d) {
      values.add(d);
      return this;
    }

    public Builder values(List<Double> val) {
      values = val;
      return this;
    }

    public Builder children(List<Cpt> val) {
      children = val;
      return this;
    }

    public Builder child(Cpt val) {
      children.add(val);
      return this;
    }

    public Cpt build() {
      return new Cpt(this);
    }
  }
}
