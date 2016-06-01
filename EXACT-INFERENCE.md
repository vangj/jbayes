#exact inference

The following code snippet shows how to create nodes, define parents of nodes, create the conditional probability tables (CPTs), create a graph (a graph in this case equates to a Bayesian Belief Network), and perform exact inference on the graph.

To create a BBN, you can create a directed acylic graph (DAG) with the associated conditional probabilities as follows. You should also define the edges. This fluent style of creating a BBN should make it easy to create one. Of course, you may create the nodes first then insert them into the DAG and then define the directed edges. Please note that this BBN is taken from C. Huang and A. Darwiche, "Inference in Belief Networks: A Procedural Guide," in International Journal of Approximate Reasoning, vol. 15, pp. 225--263, 1999.    

```
Dag dag = new Dag()
    .addNode(Node.builder()
        .id("a")
        .name("a")
        .value("on")
        .value("off")
        .probs(Arrays.asList(0.5d, 0.5d))
        .build())
    .addNode(Node.builder()
        .id("b")
        .name("b")
        .value("on")
        .value("off")
        .probs(Arrays.asList(0.5d, 0.5d, 0.4d, 0.6d))
        .build())
    .addNode(Node.builder()
        .id("c")
        .name("c")
        .value("on")
        .value("off")
        .probs(Arrays.asList(0.7d, 0.3d, 0.2d, 0.8d))
        .build())
    .addNode(Node.builder()
        .id("d")
        .name("d")
        .value("on")
        .value("off")
        .probs(Arrays.asList(0.9d, 0.1d, 0.5d, 0.5d))
        .build())
    .addNode(Node.builder()
        .id("e")
        .name("e")
        .value("on")
        .value("off")
        .probs(Arrays.asList(0.3d, 0.7d, 0.6d, 0.4d))
        .build())
    .addNode(Node.builder()
        .id("f")
        .name("f")
        .value("on")
        .value("off")
        .probs(Arrays.asList(0.01d, 0.99d, 0.01d, 0.99d, 0.01d, 0.99d, 0.99d, 0.01d))
        .build())
    .addNode(Node.builder()
        .id("g")
        .name("g")
        .value("on")
        .value("off")
        .probs(Arrays.asList(0.8d, 0.2d, 0.1d, 0.9d))
        .build())
     .addNode(Node.builder()
        .id("h")
        .name("h")
        .value("on")
        .value("off")
        .probs(Arrays.asList(0.05d, 0.95d, 0.95d, 0.05d, 0.95d, 0.05d, 0.95d, 0.05d))
        .build())
    .addEdge("a", "b")
    .addEdge("a", "c")
    .addEdge("b", "d")
    .addEdge("c", "e")
    .addEdge("d", "f")
    .addEdge("e", "f")
    .addEdge("c", "g")
    .addEdge("e", "h")
    .addEdge("g", "h");

//assigns the conditional probabilities into potentials
dag.initializePotentials();

//creates a join tree from the BBN
JoinTree joinTree = InferenceControl.apply(dag);

//print out the probabilities for each node
joinTree.nodes().forEach(node -> {
    Potential potential = joinTree.getPotential(node);
    System.out.println(node);
    System.out.println(potential);
    System.out.println("---------------");
});
    
//insert observation
joinTree.updateEvidence(Evidence.newBuilder()
    .node(joinTree.node("a"))
    .value("on", 1.0)
    .type(Evidence.Type.Observation)
    .build());

//insert multiple observations
joinTree.updateEvidence(Arrays.asList(
    Evidence.newBuilder().node(joinTree.node("a")).value("on", 1.0d).build(),
    Evidence.newBuilder().node(joinTree.node("f")).value("on", 1.0d).build()
));

//unobserve an node
joinTree.unobserve(joinTree.node("a"));
```

#evidence
There are 3 types of evidences.

* virtual or soft evidence: each value is in the range [0, 1]
* finding: each values is a 0 or 1 (at least one value is 1)
* observation: only one value is 1 and all others are 0

The most common type of evidence is an observation, where you have a variable set to one value.
