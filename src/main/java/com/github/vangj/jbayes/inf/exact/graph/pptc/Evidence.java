package com.github.vangj.jbayes.inf.exact.graph.pptc;

import com.github.vangj.jbayes.inf.exact.graph.Node;
import com.github.vangj.jbayes.inf.exact.graph.lpd.Potential;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Evidence.
 */
public class Evidence {

  /**
   * The type of evidence.
   * <ul>
   *   <li>virtual: each value lies in the domain [0, 1]</li>
   *   <li>finding: each value must be 1 or 0; at least one value must be 1</li>
   *   <li>observation: one value is 1 and all others are 0</li>
   *   <li>unobserve: all values are 1</li>
   * </ul>
   */
  public enum Type {
    Virtual, Finding, Observation, Unobserve
  }


  /**
   * The type of change that the evidence can bring about.
   * <ul>
   *   <li>none: nothing changed</li>
   *   <li>update: node was unobseved, now it is observed</li>
   *   <li>retraction: node was unobserved and now observed, or previous observed value is now
   *   not the same as the observed one</li>
   * </ul>
   */
  public enum Change {
    None, Update, Retraction
  }

  private Node node;
  private Map<String, Double> values;
  private Type type;

  private Evidence(Builder builder) {
    node = builder.node;
    values = builder.values;
    type = builder.type;
  }

  /**
   * Gets the node.
   * @return Node.
   */
  public Node getNode() {
    return node;
  }

  /**
   * Gets a map where keys are node's values and the values are likelihoods.
   * @return
   */
  public Map<String, Double> getValues() {
    return values;
  }

  /**
   * Gets the type of evidence.
   * @return Type.
   */
  public Type getType() {
    return type;
  }

  /**
   * Compares this evidence to the current one and determines the change type.
   * @param potentials Map of potentials.
   * @return Change type.
   */
  public Change compare(Map<String, Potential> potentials) {
    Map<String, Double> that = convert(potentials);

    boolean unobservedThat = isUnobserved(that);
    boolean unobservedThis = isUnobserved(values);

    if(unobservedThat && unobservedThis) {
      return Change.None;
    }

    boolean observedThat = isObserved(that);
    boolean observedThis = isObserved(values);

    if(observedThat && observedThis) {
      String s1 = getObservedValue(that);
      String s2 = getObservedValue(values);
      if(s1.equals(s2)) {
        return Change.None;
      } else {
        return Change.Retraction;
      }
    }

    if(unobservedThat && observedThis) {
      //FIXME should signal update, but that doesn't work
      return Change.Retraction;
    }

    if(observedThat && unobservedThis) {
      return Change.Retraction;
    }

    return Change.Retraction;
  }

  /**
   * Converts a map of values to potentials to a map of values to likelihoods.
   * @param map Map.
   * @return Map.
   */
  private static Map<String, Double> convert(Map<String, Potential> map) {
    Map<String, Double> m = new LinkedHashMap<>();
    for(Map.Entry<String, Potential> e : map.entrySet()) {
      m.put(e.getKey(), e.getValue().entries().get(0).getValue());
    }
    return m;
  }

  /**
   * Checks to see if the evidence is unobserved. All likelihoods must be 1.
   * @param values Map of values to likelihoods.
   * @return Boolean.
   */
  private static boolean isUnobserved(Map<String, Double> values) {
    int counts = (int)values.entrySet().stream()
        .filter(entry -> (entry.getValue() == 1.0d))
        .count();
    return (counts == values.size());
  }

  /**
   * Checks to see if the evidence is observed. Exactly one value must be 1 and all others
   * are 0.
   * @param values Map of values to likelihoods.
   * @return Boolean.
   */
  private static boolean isObserved(Map<String, Double> values) {
    long countOne = values.entrySet().stream()
        .filter(entry -> (entry.getValue() == 1.0d))
        .count();
    long countZero = values.entrySet().stream()
        .filter(entry -> (entry.getValue() == 0.0d))
        .count();
    return (1 == countOne && values.size() - 1 == countZero);
  }

  /**
   * Gets the observed value. Should be called only after isObserved returns true.
   * @param values Map of values to likelihoods.
   * @return The value which is observed.
   */
  private static String getObservedValue(Map<String, Double> values) {
    return values.entrySet().stream()
        .filter(entry -> (entry.getValue() == 1.0d))
        .map(Map.Entry::getKey)
        .findFirst().get();
  }

  /**
   * Gest a new builder.
   * @return Builder.
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Builder for evidence.
   */
  public static final class Builder {
    private Node node;
    private Map<String, Double> values;
    private Type type = Type.Observation;

    private Builder() {
      values = new LinkedHashMap<>();
    }

    public Builder node(Node node) {
      this.node = node;
      return this;
    }

    public Builder value(String value, Double likelihood) {
      values.put(value, likelihood);
      return this;
    }

    public Builder type(Type val) {
      type = val;
      return this;
    }

    public Evidence build() {
      if(null == node || null == node.getId() || node.getId().isEmpty()) {
        throw new IllegalArgumentException("node/id cannot be null or empty!");
      }
      validateEvidence();
      return new Evidence(this);
    }

    private void validateEvidence() {
      node.getValues().forEach(value -> {
        if(!values.containsKey(value)) {
          values.put(value, 0.0d);
        }
      });

      if(Type.Virtual == type) {
        //each likelihood must be in [0, 1]
        double sum = 0.0d;
        for(Double value : values.values()) {
          sum += value;
        }
        for(String k : values.keySet()) {
          double d = values.get(k) / sum;
          values.put(k, d);
        }
      } else if(Type.Finding == type) {
        //each likelihood must be either 0 or 1; there should be at least a value with 1
        for(String k : values.keySet()) {
          double d = values.get(k) > 0.0d ? 1.0d : 0.0d;
          values.put(k, d);
        }

        long count = values.entrySet().stream()
            .filter(entry -> entry.getValue() == 1.0d)
            .count();

        //if no value has 1, then make them all 1
        if(0 == count) {
          for(String k : values.keySet()) {
            values.put(k, 1.0d);
          }
        }
      } else if(Type.Observation == type) {
        //only 1 likelihood is 1 and others are 0
        String key = values.entrySet().stream()
            .sorted((o1, o2) -> -1 * o1.getValue().compareTo(o2.getValue()))
            .map(Map.Entry::getKey)
            .findFirst().get();
        values.keySet().forEach(k -> {
          if(key.equals(k)) {
            values.put(k, 1.0d);
          } else {
            values.put(k, 0.0d);
          }
        });
      } else if(Type.Unobserve == type) {
        values.keySet().forEach(k -> {
          values.put(k, 1.0d);
        });
      }
    }
  }
}
