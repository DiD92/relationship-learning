# relationship-learning
Java library focused on finding the specific relation between two arguments, 
being it support, attack or neither of those. The library makes use of the 
[Weka](http://www.cs.waikato.ac.nz/ml/weka/) library in order to build the 
datasets and classifiy the arguments with the support of the 
[LibSVM](http://www.csie.ntu.edu.tw/~cjlin/libsvm/) library for the classifier.

# Table of Contents
- [relationship-learning](#relationship-learning)
- [Basic modules](#basic-modules)
  - [I/O](#io)
  - [Model](#model)
  - [Filters](#filters)
  - [Classifiers](#classifiers)
  - [Exception](#exception)
- [Usage examples](#usage-examples)
- [Addtional notes](#additional-notes)

# Basic modules
The library is organized in packages according to the funcionality they provide 
to the library, most of the elements can be subclassed or reimplemented if the 
user has different needs that the original implementations canno satisfy.
## I/O
Package: [org.relationlearn.util.io](../master/src/org/relationlearn/util/io)

The library is divided into multiple packages but the basic elements most users 
may need in order to work with the library are the 
[InputParser](../master/src/org/relationlearn/util/io/InputParser.java) and the 
[OutputGenerator](../master/src/org/relationlearn/util/io/OutputGenerator.java). 
These two interfaces define the input and the output results for the classifier 
to work into, so if you need to define new ways of providing data to your 
classifier (like for example reading input from a database) you just need to 
create a new implementation for the InputParser interface, the same goes of you 
want to create a new output representation for the results in that case you 
would only need to create an implementation for the OutputGenerator interface.
## Model
Package: [org.relationlearn.model](../master/src/org/relationlearn/model)

The classes contained in this module are used in the internal representation of 
the data parsed with the input class the user selected, this data will be then 
used in the training of a classifier or the classification of new instances that 
will be extracted from this internal representation.

Since a group of arguments can be seen as a digraph, the interfaces found in 
this [package](../master/src/org/relationlearn/model) and their implementations, 
define the structure of a digraph with nodes, relationships and the graph itself 
containing the nodes, one can use the already implemented versions of them or 
define new implementations if their need arises.
## Filters
Package: [org.relationlearn.filters](../master/src/org/relationlearn/filters)

This package provides interfaces and classes used to obtain ARFF-type attributes 
from a pair of text arguments represented as a java String type, this attributes 
will then be used to either train a classifier, or use an already trained 
classifier to classifiy new instances obtained from a given input source.
## Classifiers
Package: [org.relationlearn.classifiers](../master/src/org/relationlearn/classifiers)

This package containes the central element for both model training and instance 
classification, for now it contains a single class which should be used as is, 
but you can always use the facilities provided by the Weka library for your 
training classification if you are uncomfortable with this implementation.
## Exception
Package: [org.relationlearn.exception](../master/src/org/relationlearn/exception)

In this package you will find wrapper classes for the Exception class which are 
basically used to provide the user with a more clear reason of failure, in case 
something happens during execution.

# Usage examples
There's basically two major tasks you can accomplish with the already provided 
classes in the library: training a classifier for future use or classifying new 
instances using an already trained classifier.

In this examples the classes shown correspond to the basic implementation 
provided by the library but the logical process would reamin the same in the 
case of user implementations of those interfaces.

* Training new classifier

```Java
// We create and XML parser and get a Map of digraphs
InputParser parser = new XMLFileParser();
Map<String, RelationDigraph> in_digraphs = parser.parse("your_xml_file.xml");
// We get the digraph form which we want to train a classifier
RelationDigraph digraph = in_digraphs.get("digrahp_name");
// We instantiate a FilterGroup and an InstanceGenerator to get the 
// attributes we want from the digraph relations
FilterGroup filter =  new SequentialFilterGroup("digraph_name");
// Adding filters to the filter group
filter.addFilter(new WordRatioFilter("word-ratio"));
// ...
InstanceGenerator generator = new InstanceGenerator(digraph, filter);
// We generate the instances and train an SVM classifier with them
Instances instances = generator.getGraphInstances();
SVMClassifier svm = new SVMClassifier();
svm.trainClassifier(instances);
// Finally we store both the classifier and the dataset in a file
svm.storeModel("digraph_classifier.model");
```

* Classifying new instances

```Java
// We create again and XML parser and get a Map of digraphs
InputParser parser = new XMLFileParser();
Map<String, RelationDigraph> in_digraphs = parser.parse("another_xml_file.xml");
// We get the digraph form which we want to classify its relations
// with an already trained classifier
RelationDigraph digraph = in_digraphs.get("digrahp_name");
// We instantiate a FilterGroup and an InstanceGenerator to get the 
// attributes we want from the digraph relations
FilterGroup filter =  new SequentialFilterGroup("digraph_name");
// Adding filters to the filter group
filter.addFilter(new WordRatioFilter("word-ratio"));
// ...
InstanceGenerator generator = new InstanceGenerator(digraph, filter);
// We generate the instances and initialize a SVM classifier with a
// previously trained model
Instances instances = generator.getGraphInstances();
File model = new File("path_to_your_model.model");
SVMClassifier svm = new SVMClassifier(model);
// We classifiy each instance using the SVM classifier
for(int i = 0; i < instances.numInstances(); i++) {
  double result = svm.classifyInstance(instances.instace(i));
  // Do something with the result value...
}
```
# Additional notes
If you have any doubts about the specifics of an element in the library you can 
always check the provided [Javadoc](../master/dist/javadoc/index.html) for all 
the interfaces and classes found in this repository.
