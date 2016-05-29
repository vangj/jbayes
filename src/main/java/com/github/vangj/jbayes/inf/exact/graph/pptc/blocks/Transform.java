package com.github.vangj.jbayes.inf.exact.graph.pptc.blocks;

import com.github.vangj.jbayes.inf.exact.graph.pptc.Clique;
import com.github.vangj.jbayes.inf.exact.graph.pptc.Edge;
import com.github.vangj.jbayes.inf.exact.graph.pptc.JoinTree;
import com.github.vangj.jbayes.inf.exact.graph.pptc.SepSet;
import com.github.vangj.jbayes.inf.exact.graph.pptc.traversal.JoinTreeShortestPath;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Step 3. Transforms a list of cliques into a join tree.
 */
public class Transform {
  private Transform() {

  }

  public static JoinTree transform(List<Clique> cliques, JoinTreeShortestPath.Listener listener) {
    JoinTree joinTree = new JoinTree(cliques);
    List<SepSet> sepSets = getSepSets(cliques);
    final int n = cliques.size();
    int total = 0;
    for(SepSet sepSet : sepSets) {
      if(!JoinTreeShortestPath.exists(joinTree, sepSet.left(), sepSet.right(), listener)) {
        joinTree.addEdge(new Edge(sepSet.left(), sepSet));
        joinTree.addEdge(new Edge(sepSet.right(), sepSet));
        total++;
      }

      if(total == (n-1)) {
        break;
      }
    }
    return joinTree;
  }

  private static List<SepSet> getSepSets(List<Clique> cliques) {
    final int size = cliques.size();
    List<SepSet> sepSets = new ArrayList<>();
    for(int i=0; i < size; i++) {
      Clique clique1 = cliques.get(i);
      for(int j=i+1; j < size; j++) {
        Clique clique2 = cliques.get(j);
        SepSet sepSet = clique1.sepSet(clique2);
        sepSets.add(sepSet);
      }
    }
    return sepSets.stream()
        .sorted((o1, o2) -> {
          int result = -1 * Integer.compare(o1.mass(), o2.mass());
          if(0 == result) {
            result = Integer.compare(o1.cost(), o2.cost());
            if(0 == result) {
              result = o1.id().compareTo(o2.id());
            }
          }
          return result;
        })
        .collect(Collectors.toList());
  }
}
