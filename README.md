# jpack

This Java package aims at making it easy to quickly write readable and efficient
code for general scientific computing. It is a toolbox that tries to be coherent,
concise, and easy to integrate.

So far, **jpack** provides

* Linear algebra primitives
* An implementation of [CMA-ES](https://en.wikipedia.org/wiki/CMA-ES), a 
black-box, derivative-free optimization algorithm . It's a state-of-the-art, 
a strong benchmark champion, it's used in the industry, and it has strong 
theoritical foundations. A few variations are provided, so that one can exploits
hypothesis on the optimization problem.
* A very bare-bone implementation of feed-forward neural network.

I (the original author) made **jpack** to help a friend. A few years later, I
have been encouraged to release this code. I plan to add tools in **jpack** as 
time goes. 

## Compilation and installation

An [Ant](https://ant.apache.org/) build file is provided to automate **jpack** 
build. From the main directory, do

```
ant
```

**jpack** is going to be compiled and packaged as a single *.jar* file in the
*dist* directory, as *jpack.jar*.

You can delete all the build files and distribution files by doing

```
ant clean
```

## Unit testing

Unit testing is done with [JUnit 4.12](https://junit.org/junit4/), and is managed
with *Ant*. 

To build and run the unit tests, do

```
ant test
```

The *JUnit* *.jar* files are not included in the source code, you will have
to download them and copy them in a directory named *vendor*, at the root
of the source tree.

The following commands show how you can do this

```
mkdir vendor
cd vendor
wget http://search.maven.org/remotecontent?filepath=junit/junit/4.12/junit-4.12.jar
wget http://search.maven.org/remotecontent?filepath=org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar
cd ..
``` 

## Authors

* **Alexandre Devert** - *Initial work* - [marmakoide](https://github.com/marmakoide)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

