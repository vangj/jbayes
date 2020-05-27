package com.github.vangj.jbayes.inf.exact.graph.pptc;

import static com.github.vangj.jbayes.inf.exact.graph.util.PotentialUtil.marginalizeFor;
import static com.github.vangj.jbayes.inf.exact.graph.util.PotentialUtil.normalize;

import com.github.vangj.jbayes.inf.exact.graph.Node;
import com.github.vangj.jbayes.inf.exact.graph.lpd.Potential;
import com.github.vangj.jbayes.inf.exact.graph.lpd.PotentialEntry;
import com.github.vangj.jbayes.inf.exact.graph.util.NodeUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Join tree.
 */
public class JoinTree {

  private final Map<String, Clique> cliques;
  private final Map<String, Set<Clique>> neighbors;
  private final Set<Edge> edges;
  private final Map<String, Potential> potentials;
  private final Map<String, Map<String, Potential>> evidences;
  private Listener listener;
  public JoinTree() {
    this(new ArrayList<>());
  }

  public JoinTree(List<Clique> cliques) {
    this.cliques = new HashMap<>();
    neighbors = new HashMap<>();
    edges = new LinkedHashSet<>();
    potentials = new LinkedHashMap<>();
    evidences = new HashMap<>();

    for (Clique clique : cliques) {
      addClique(clique);
    }
  }

  /**
   * Sets the listener.
   *
   * @param listener Listener.
   * @return Join tree.
   */
  public JoinTree setListener(Listener listener) {
    this.listener = listener;
    return this;
  }

  /**
   * Gets the evidence associated with the specified node and value. If none exists, will return a
   * potential with likelihood of 1.0.
   *
   * @param node  Node.
   * @param value Value.
   * @return Potential.
   */
  public Potential getEvidencePotential(Node node, String value) {
    Map<String, Potential> nodeEvidences = evidences.get(node.getId());
    if (null == nodeEvidences) {
      nodeEvidences = new HashMap<>();
      evidences.put(node.getId(), nodeEvidences);
    }

    Potential potential = nodeEvidences.get(value);
    if (null == potential) {
      potential = new Potential()
          .addEntry(new PotentialEntry().add(node.getId(), value).setValue(1.0d));
      nodeEvidences.put(value, potential);
    }

    return potential;
  }

  /**
   * Gets the change type.
   *
   * @param evidence Evidence.
   * @return Change type.
   */
  private Evidence.Change getChangeType(Evidence evidence) {
    Node node = evidence.getNode();
    Map<String, Potential> potentials = evidences.get(node.getId());
    Evidence.Change change = evidence.compare(potentials);
    return change;
  }

  /**
   * Gets the change type for the list of evidences. Precendence is retraction, update, then none.
   *
   * @param evidences List of evidence.
   * @return Change type.
   */
  private Evidence.Change getChangeType(List<Evidence> evidences) {
    List<Evidence.Change> changes = evidences.stream()
        .map(evidence -> getChangeType(evidence))
        .collect(Collectors.toList());
    int count = (int) changes.stream()
        .filter(change -> (Evidence.Change.Retraction == change))
        .count();
    if (count > 0) {
      return Evidence.Change.Retraction;
    }

    count = (int) changes.stream()
        .filter(change -> (Evidence.Change.Update == change))
        .count();
    if (count > 0) {
      return Evidence.Change.Update;
    }

    return Evidence.Change.None;
  }

  /**
   * Creates evidence where all likelihoods are set to 1 (unobserved).
   *
   * @param node Node.
   * @return Evidence.
   */
  private Evidence getUnobservedEvidence(Node node) {
    Evidence.Builder builder = Evidence.newBuilder()
        .node(node)
        .type(Evidence.Type.Unobserve);
    node.getValues().forEach(value -> builder.value(value, 1.0d));
    return builder.build();
  }

  /**
   * Unobserves the specified node.
   *
   * @param node Node.
   * @return Join tree.
   */
  public JoinTree unobserve(Node node) {
    updateEvidence(getUnobservedEvidence(node));
    return this;
  }

  /**
   * Unobserves the specified list of nodes.
   *
   * @param nodes List of nodes.
   * @return Join tree.
   */
  public JoinTree unobserve(List<Node> nodes) {
    List<Evidence> evidences = nodes.stream()
        .map(node -> getUnobservedEvidence(node))
        .collect(Collectors.toList());
    updateEvidence(evidences);
    return this;
  }

  /**
   * Unobserves all nodes.
   *
   * @return Join tree.
   */
  public JoinTree unobserveAll() {
    unobserve(new ArrayList<>(nodes()));
    return this;
  }

  /**
   * Update with a single evidence. Will trigger inference.
   *
   * @param evidence Evidence.
   * @return Join tree.
   */
  public JoinTree updateEvidence(Evidence evidence) {
    Evidence.Change change = getChangeType(evidence);
    update(evidence);
    notifiyListener(change);
    return this;
  }

  /**
   * Updates with a list of evidences. Will trigger inference.
   *
   * @param evidences List of evidences.
   * @return Join tree.
   */
  public JoinTree updateEvidence(List<Evidence> evidences) {
    Evidence.Change change = getChangeType(evidences);
    evidences.stream().forEach(evidence -> update(evidence));
    notifiyListener(change);
    return this;
  }

  /**
   * Notifies the listener of evidence change.
   *
   * @param change Change.
   */
  private void notifiyListener(Evidence.Change change) {
    if (null != listener) {
      if (Evidence.Change.Retraction == change) {
        listener.evidenceRetracted(this);
      } else if (Evidence.Change.Update == change) {
        listener.evidenceUpdated(this);
      } else {
        listener.evidenceNoChange(this);
      }
    }
  }

  private void update(Evidence evidence) {
    Node node = evidence.getNode();
    Map<String, Potential> potentials = evidences.get(node.getId());

    evidence.getValues().entrySet().stream().forEach(entry -> {
      Potential potential = potentials.get(entry.getKey());
      potential.entries().get(0).setValue(entry.getValue());
    });
  }

  /**
   * Gets the potential (holding the probabilities) for the specified node.
   *
   * @param node Node.
   * @return Potential.
   */
  public Potential getPotential(Node node) {
    Clique clique = (Clique) node.getMetadata("parent.clique");
    return normalize(marginalizeFor(this, clique, Arrays.asList(node)));
  }

  /**
   * Unmarks all cliques.
   */
  public void unmarkCliques() {
    allCliques().forEach(Clique::unmark);
  }

  /**
   * Gets the potential.
   *
   * @return List of potential.
   */
  public List<Potential> potentials() {
    return potentials.values().stream().collect(Collectors.toList());
  }

  /**
   * Gets all the cliques containing the specified node and its parents.
   *
   * @param node Node.
   * @return List of cliques.
   */
  public List<Clique> cliquesContainingNodeAndParents(Node node) {
    return cliques().stream()
        .filter(clique -> {
          if (!clique.contains(node.getId())) {
            return false;
          }
          List<Node> parents = (List<Node>) node.getMetadata("parents");
          if (parents != null && parents.size() > 0) {
            for (Node parent : parents) {
              if (!clique.contains(parent.getId())) {
                return false;
              }
            }
          }
          return true;
        })
        .collect(Collectors.toList());
  }

  /**
   * Gest all the nodes in this join tree.
   *
   * @return Set of nodes.
   */
  public Set<Node> nodes() {
    Set<Node> nodes = new HashSet<>();
    cliques().forEach(clique -> {
      nodes.addAll(clique.nodes());
    });
    return nodes;
  }

  /**
   * Gets the node associated with the specified id.
   *
   * @param id Id.
   * @return Node.
   */
  public Node node(String id) {
    return nodes().stream()
        .filter(node -> (id.equals(node.getId())))
        .findFirst().get();
  }

  /**
   * Gets the potential associated with the specified clique.
   *
   * @param clique Clique.
   * @return Potential.
   */
  public Potential getPotential(Clique clique) {
    return potentials.get(clique.id());
  }

  /**
   * Adds potential associated with the specified clique.
   *
   * @param clique    Clique.
   * @param potential Potential.
   * @return Join tree.
   */
  public JoinTree addPotential(Clique clique, Potential potential) {
    potentials.put(clique.id(), potential);
    return this;
  }

  /**
   * Gets the neighbors of the specified clique.
   *
   * @param clique Clique.
   * @return Set of neighbors. This includes cliques and separation sets.
   */
  public Set<Clique> neighbors(Clique clique) {
    return neighbors.get(clique.id());
  }

  /**
   * Gets the clique that matches exactly to the specified nodes.
   *
   * @param nodes Nodes.
   * @return Clique.
   */
  public Clique clique(Node... nodes) {
    String id = NodeUtil.id(Arrays.asList(nodes));
    return cliques.get(id);
  }

  /**
   * Gets the separation set that matches exactly to the specifed nodes.
   *
   * @param nodes Nodes.
   * @return Separation set.
   */
  public SepSet sepSet(Node... nodes) {
    String id = NodeUtil.id(Arrays.asList(nodes), "|", "|");
    return (SepSet) cliques.get(id);
  }

  /**
   * Gets the neighbors of the clique that matches exactly with the specified nodes.
   *
   * @param nodes Nodes.
   * @return Set of neighbors. This includes cliques and separation sets.
   */
  public Set<Clique> neighbors(Node... nodes) {
    final String id = NodeUtil.id(Arrays.asList(nodes));
    return neighbors.get(id);
  }

  /**
   * Gets all the cliques (cliques + separation sets).
   *
   * @return Cliques.
   */
  public List<Clique> allCliques() {
    return cliques.values().stream().collect(Collectors.toList());
  }

  /**
   * Gets the cliques (no separation sets).
   *
   * @return Cliques.
   */
  public List<Clique> cliques() {
    return cliques.values().stream()
        .filter(clique -> !(clique instanceof SepSet))
        .collect(Collectors.toList());
  }

  /**
   * Gets the separation sets.
   *
   * @return Separation sets.
   */
  public List<SepSet> sepSets() {
    return cliques.values().stream()
        .filter(clique -> (clique instanceof SepSet))
        .map(clique -> (SepSet) clique)
        .collect(Collectors.toList());
  }

  /**
   * Gets the edges.
   *
   * @return Edges.
   */
  public List<Edge> edges() {
    return edges.stream().collect(Collectors.toList());
  }

  /**
   * Adds a clique to the join tree if it doesn't exist already.
   *
   * @param clique Clique.
   * @return Join tree.
   */
  public JoinTree addClique(Clique clique) {
    final String id = clique.id();
    if (!cliques.containsKey(id)) {
      cliques.put(id, clique);
    }
    return this;
  }

  /**
   * Adds the edge to the join tree if it doesn't exist already.
   *
   * @param edge Edge.
   * @return Join tree.
   */
  public JoinTree addEdge(Edge edge) {
    addClique(edge.left).addClique(edge.right);
    if (!edges.contains(edge)) {
      edges.add(edge);

      final String id1 = edge.left.id();
      final String id2 = edge.right.id();

      Set<Clique> ne1 = neighbors.get(id1);
      Set<Clique> ne2 = neighbors.get(id2);

      if (null == ne1) {
        ne1 = new LinkedHashSet<>();
        neighbors.put(id1, ne1);
      }

      if (null == ne2) {
        ne2 = new LinkedHashSet<>();
        neighbors.put(id2, ne2);
      }

      ne1.add(edge.right);

      ne2.add(edge.left);
    }
    return this;
  }

  @Override
  public String toString() {
    String c = allCliques().stream()
        .map(Clique::toString)
        .collect(Collectors.joining(System.lineSeparator()));
    String e = edges().stream()
        .map(Edge::toString)
        .collect(Collectors.joining(System.lineSeparator()));
    String p = potentials.entrySet().stream()
        .map(entry -> {
          Clique clique = cliques.get(entry.getKey());
          Potential potential = entry.getValue();
          return (new StringBuilder())
              .append(clique.toString())
              .append(" potential")
              .append(System.lineSeparator())
              .append(potential.toString());
        })
        .collect(Collectors.joining(System.lineSeparator()));

    return (new StringBuilder())
        .append(c)
        .append(System.lineSeparator())
        .append(e)
        .append(System.lineSeparator())
        .append(p)
        .toString();
  }

  public interface Listener {

    void evidenceRetracted(JoinTree joinTree);

    void evidenceUpdated(JoinTree joinTree);

    void evidenceNoChange(JoinTree joinTree);
  }
}
