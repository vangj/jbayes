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

# Citation

```
@misc{vang_2016, 
title={jbayes}, 
url={https://github.com/vangj/jbayes/}, 
journal={GitHub},
author={Vang, Jee}, 
year={2016}, 
month={Apr}}
```

# Copyright Stuff

```
Copyright 2016 Jee Vang

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
