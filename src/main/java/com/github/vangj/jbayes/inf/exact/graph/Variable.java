package com.github.vangj.jbayes.inf.exact.graph;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents a discrete variable.
 */
public class Variable {
  protected String id;
  protected String name;
  protected Set<String> values;

  public Variable() {

  }

  private Variable(Builder builder) {
    id = builder.id;
    name = builder.name;
    values = builder.values;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Set<String> getValues() {
    return values;
  }

  @Override
  public String toString() {
    return (new StringBuilder())
        .append(id)
        .append(" {")
        .append(String.join(",", values))
        .append('}')
        .toString();
  }

  @Override
  public boolean equals(Object object) {
    if(null == object || !(object instanceof Node)) {
      return false;
    }
    Node that = (Node)object;
    return this.id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  public static final class Builder {
    private String id;
    private String name;
    private Set<String> values;

    private Builder() {
      values = new LinkedHashSet<>();
    }

    public Builder id(String val) {
      id = val;
      return this;
    }

    public Builder name(String val) {
      name = val;
      return this;
    }

    public Builder value(String val) {
      values.add(val);
      return this;
    }

    public Builder values(Set<String> val) {
      values = val;
      return this;
    }

    public Variable build() {
      return new Variable(this);
    }
  }
}
