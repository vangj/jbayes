package com.github.vangj.jbayes.inf.exact.graph.pptc;

import com.github.vangj.jbayes.inf.exact.graph.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Evidence {

  public enum Type {
    Virtual, Finding, Observation
  }

  private Node node;
  private Map<String, Double> values;
  private Type type;

  private Evidence(Builder builder) {
    node = builder.node;
    values = builder.values;
    type = builder.type;
  }

  public Node getNode() {
    return node;
  }

  public Map<String, Double> getValues() {
    return values;
  }

  public Type getType() {
    return type;
  }

  public static Builder newBuilder() {
    return new Builder();
  }


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
      }
    }
  }
}
