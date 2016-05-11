# jbayes
Simple Bayesian Belief Network inference library using likelihood weight sampling for Java.

# How do i use jbayes?

Using Maven, you make a depedency to jbayes.

```
<dependency>
  <groupId>com.github.vangj</groupId>
  <artifactId>jbayes-inference</artifactId>
  <version>0.0.1</version>
</dependency>
```


The following code snippet shows how to create nodes, define parents of nodes, create the conditional probability tables (CPTs), create a graph (a graph in this case equates to a Bayesian Belief Network), and perform inference on the graph.

```
//each node must have its name and values defined
Node n1 = Node.newBuilder().name("n1").value("t").value("f").build();
Node n2 = Node.newBuilder().name("n2").value("t").value("f").build();
Node n3 = Node.newBuilder().name("n3").value("t").value("f").build();

//nodes may have parents
n2.addParent(n1);
n3.addParent(n2);

//define the CPTs for each node
n1.setCpt(new double[][] {
    {0.5, 0.5}
});
n2.setCpt(new double[][] {
    {0.5, 0.5},
    {0.5, 0.5}
});
n3.setCpt(new double[][] {
    {0.5, 0.5},
    {0.5, 0.5}
});

//create a graph from the nodes
Graph g = new Graph();
g.addNode(n1);
g.addNode(n2);
g.addNode(n3);

//samples and computes the marginal probabilities aka the inference
g.sample(10000);

//look at the marginal probabilities
double[] probs1 = n1.probs();
double[] probs2 = n2.probs();
double[] probs3 = n3.probs();
```

# Defining condtional probability tables (CPTs)

Let's say a node, n1, has two values { true, false }. Let's say n1 has two parents with the following values.
* n2 { yes, no, maybe }
* n3 { a, b }

The number of values that a node has is termed the cardinality of the node (or its domain). So, the following are the cardinalities of each node.
* n1 has a cardinality, |n1| = 2
* n2 has a cardinality, |n2| = 3
* n3 has a cardinality, |n3| = 2

The product of the cardinalities of a node and its parents define the number of conditional probabilities that must be defined. In this example, |n1| x |n2| x |n3| = 12. We need a way to store these conditional probabilities. Typically, we store it in a table, called the conditional probability table (CPT). The CPT can be viewed as a 2-dimensional (2D) matrix, where the number of rows is the product of the cardinalities of the parents and the number of columns is the cardinality of the node. In the running example, this is 6 rows and 2 columns (6x2) since |n2| x |n3| = 6 and |n1| = 2.

For a CPT, the columns are easy to each explain; each column in the CPT corresponds to each value of the node. For the rows, each row represents a combination of the cross-product between the parents. When we perform the cross-product between the values of the parents, n2 and n3, we get the following combinations.
* { yes, a }
* { yes, b }
* { no, a }
* { no, b }
* { maybe, a }
* { maybe, b }

Each row correspond to each of these combinations resulting from the cross product of the domains (values) of the parents. 

Typically, we set the the CPT of a node as a 2D matrix; for the running example.

```
n1.setCpt(new double[][] {
 { 0.1, 0.9 }, //P(n1=true|n2=yes,n3=a), P(n1=false|n2=yes,n3=a)
 { 0.8, 0.2 }, //P(n1=true|n2=yes,n3=b), P(n1=false|n2=yes,n3=b)
 { 0.5, 0.5 }, //P(n1=true|n2=no,n3=a), P(n1=false|n2=no,n3=a)
 { 0.4, 0.6 }, //P(n1=true|n2=no,n3=b), P(n1=false|n2=no,n3=b)
 { 0.3, 0.7 }, //P(n1=true|n2=maybe,n3=a), P(n1=false|n2=maybe,n3=a)
 { 0.6, 0.4 }  //P(n1=true|n2=maybe,n3=b), P(n1=false|n2=maybe,n3=b)
});
```

Note that the conditional probabilities per row sum to 1. If you do NOT make the conditional probabilties sum to 1 per row, the inference (or your head) may [asplode](http://www.urbandictionary.com/define.php?term=asplode).

What may seem as a one-off case are nodes without any parents. In these cases, the CPT always has 1 row and as many columns as the cardinality of the node. So if n2 was a node without any parents, its CPT would be defined as follows.

```
n2.setCpt(new double[][] {
 { 0.2, 0.5, 0.3 } //P(n2=yes), P(n2=no), P(n2=maybe)
});
```

