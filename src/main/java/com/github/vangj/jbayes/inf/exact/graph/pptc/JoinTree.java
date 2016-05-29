package com.github.vangj.jbayes.inf.exact.graph.pptc;

import com.github.vangj.jbayes.inf.exact.graph.Node;
import com.github.vangj.jbayes.inf.exact.graph.lpd.Potential;
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
  private Map<String, Clique> cliques;
  private Map<String, Set<Clique>> neighbors;
  private Set<Edge> edges;
  private Map<String, Potential> potentials;

  public JoinTree() {
    this(new ArrayList<>());
  }

  public JoinTree(List<Clique> cliques) {
    this.cliques = new HashMap<>();
    neighbors = new HashMap<>();
    edges = new LinkedHashSet<>();
    potentials = new LinkedHashMap<>();

    for(Clique clique : cliques) {
      addClique(clique);
    }
  }

  /**
   * Gets the potential.
   * @return List of potential.
   */
  public List<Potential> potentials() {
    return potentials.values().stream().collect(Collectors.toList());
  }

  /**
   * Gets all the cliques containing the specified node and its parents.
   * @param node Node.
   * @return List of cliques.
   */
  public List<Clique> cliquesContainingNodeAndParents(Node node) {
    return cliques().stream()
        .filter(clique -> {
          if(!clique.contains(node.getId())) {
            return  false;
          }
          List<Node> parents = (List<Node>)node.getMetadata("parents");
          if(parents != null && parents.size() > 0) {
            for(Node parent : parents) {
              if(!clique.contains(parent.getId())) {
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
   * Gets the potential associated with the specified clique.
   * @param clique Clique.
   * @return Potential.
   */
  public Potential getPotential(Clique clique) {
    return potentials.get(clique.id());
  }

  /**
   * Adds potential associated with the specified clique.
   * @param clique Clique.
   * @param potential Potential.
   * @return Join tree.
   */
  public JoinTree addPotential(Clique clique, Potential potential) {
    potentials.put(clique.id(), potential);
    return this;
  }

  /**
   * Gets the neighbors of the specified clique.
   * @param clique Clique.
   * @return Set of neighbors. This includes cliques and separation sets.
   */
  public Set<Clique> neighbors(Clique clique) {
    return neighbors.get(clique.id());
  }

  /**
   * Gets the clique that matches exactly to the specified nodes.
   * @param nodes Nodes.
   * @return Clique.
   */
  public Clique clique(Node... nodes) {
    String id = NodeUtil.id(Arrays.asList(nodes));
    return cliques.get(id);
  }

  /**
   * Gets the separation set that matches exactly to the specifed nodes.
   * @param nodes Nodes.
   * @return Separation set.
   */
  public SepSet sepSet(Node... nodes) {
    String id = NodeUtil.id(Arrays.asList(nodes), "|", "|");
    return (SepSet)cliques.get(id);
  }

  /**
   * Gets the neighbors of the clique that matches exactly with the
   * specified nodes.
   * @param nodes Nodes.
   * @return Set of neighbors. This includes cliques and separation sets.
   */
  public Set<Clique> neighbors(Node... nodes) {
    final String id = NodeUtil.id(Arrays.asList(nodes));
    return neighbors.get(id);
  }

  /**
   * Gets all the cliques (cliques + separation sets).
   * @return Cliques.
   */
  public List<Clique> allCliques() {
    return cliques.values().stream().collect(Collectors.toList());
  }

  /**
   * Gets the cliques (no separation sets).
   * @return Cliques.
   */
  public List<Clique> cliques() {
    return cliques.values().stream()
        .filter(clique -> !(clique instanceof SepSet))
        .collect(Collectors.toList());
  }

  /**
   * Gets the separation sets.
   * @return Separation sets.
   */
  public List<SepSet> sepSets() {
    return cliques.values().stream()
        .filter(clique -> (clique instanceof SepSet))
        .map(clique -> (SepSet)clique)
        .collect(Collectors.toList());
  }

  /**
   * Gets the edges.
   * @return Edges.
   */
  public List<Edge> edges() {
    return edges.stream().collect(Collectors.toList());
  }

  /**
   * Adds a clique to the join tree if it doesn't exist already.
   * @param clique Clique.
   * @return Join tree.
   */
  public JoinTree addClique(Clique clique) {
    final String id = clique.id();
    if(!cliques.containsKey(id)) {
      cliques.put(id, clique);
    }
    return this;
  }

  /**
   * Adds the edge to the join tree if it doesn't exist already.
   * @param edge Edge.
   * @return Join tree.
   */
  public JoinTree addEdge(Edge edge) {
    addClique(edge.left).addClique(edge.right);
    if(!edges.contains(edge)) {
      edges.add(edge);

      final String id1 = edge.left.id();
      final String id2 = edge.right.id();

      Set<Clique> ne1 = neighbors.get(id1);
      Set<Clique> ne2 = neighbors.get(id2);

      if(null == ne1) {
        ne1 = new LinkedHashSet<>();
        neighbors.put(id1, ne1);
      }

      if(null == ne2) {
        ne2 = new LinkedHashSet<>();
        neighbors.put(id2, ne2);
      }

      if(!ne1.contains(edge.right)) {
        ne1.add(edge.right);
      }

      if(!ne2.contains(edge.left)) {
        ne2.add(edge.left);
      }
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
}
