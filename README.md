# jbayes
Simple Java Bayesian Belief Network (BBN) inference library using likelihood weight sampling for approximate inference and the junction tree algorithm for exact inference.

Part of this library is a port of [jsbayes](https://github.com/vangj/jsbayes). Another related JavaScript project that provides visualization and interaction with BBN is [jsbayes-viz](https://github.com/vangj/jsbayes-viz).

# How do i use jbayes?

Using Maven, you make a depedency to jbayes.

```
<dependency>
  <groupId>com.github.vangj</groupId>
  <artifactId>jbayes-inference</artifactId>
  <version>0.0.3</version>
</dependency>
```

For construction of a BBN and performing approximate inference, please read [APPROXIMATE-INFERENCE.md](APPROXIMATE-INFERENCE.md).

For construction of a BBN and performing exact inference, please read [EXACT-INFERENCE.md](EXACT-INFERENCE.md).

## Maven Repositories

* [Snapshots](https://oss.sonatype.org/content/repositories/snapshots/com/github/vangj/)
* [Releases](https://mvnrepository.com/artifact/com.github.vangj/jbayes-inference)