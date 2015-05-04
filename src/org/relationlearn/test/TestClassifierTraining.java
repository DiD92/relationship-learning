package org.relationlearn.test;

import java.io.File;
import java.util.Map;
import org.relationlearn.classifiers.SVMClassifier;
import org.relationlearn.filters.FilterGroup;
import org.relationlearn.filters.SequentialFilterGroup;
import org.relationlearn.filters.WordRatioFilter;
import org.relationlearn.model.RelationDigraph;
import org.relationlearn.util.InstanceGenerator;
import org.relationlearn.util.io.InputParser;
import org.relationlearn.util.io.XMLFileParser;
import weka.core.Instances;

/**
 *
 * @author Dídac
 */
public class TestClassifierTraining {
    
    private final static String PATH = "E:\\Bibliotecas\\Documents\\NetBeansProjects\\RelationLearn\\test_files\\debatepedia\\debatepedia_train.xml";
    private final static String PATH2 = "E:\\Bibliotecas\\Documents\\NetBeansProjects\\RelationLearn\\test_files\\debatepedia\\debatepedia_test.xml";
    private final static String MPATH = "E:\\Bibliotecas\\Documents\\NetBeansProjects\\RelationLearn\\";
    
    private final static String GNAME = "Groundzeromosque";
    
    public static void main(String args[]) throws Exception {
        // We create and XML parser and get a Map of digraphs
        InputParser parser = new XMLFileParser();
        Map<String, RelationDigraph> in_digraphs = parser.parseInput(PATH);
        // We get the digraph form which we want to train a classifier
        RelationDigraph digraph = in_digraphs.get(GNAME);
        // We instantiate a FilterGroup and an InstanceGenerator to get the 
        // attributes we want from the digraph relations
        FilterGroup filter =  new SequentialFilterGroup(GNAME);
        // Setting class Attribute to default value
        filter.addClassAttribute(null);
        // Adding filters to the filter group
        filter.addFilter(new WordRatioFilter("word-ratio"));
        // ...
        InstanceGenerator generator = new InstanceGenerator(digraph, filter);
        // We generate the instances and train an SVM classifier with them
        Instances instances = generator.getGraphInstances();
        SVMClassifier svm = new SVMClassifier();
        svm.trainClassifier(instances);
        // Finally we store both the classifier and the dataset in a file
        svm.storeModel(GNAME + ".model");
        
        /** GRAPH 2 **/
        // We create and XML parser and get a Map of digraphs
        parser = new XMLFileParser();
        in_digraphs = parser.parseInput(PATH2);
        // We get the digraph form which we want to train a classifier
        digraph = in_digraphs.get(GNAME);
        // We instantiate a FilterGroup and an InstanceGenerator to get the 
        // attributes we want from the digraph relations
        filter =  new SequentialFilterGroup(GNAME);
        // Setting class Attribute to default value
        filter.addClassAttribute(null);
        // Adding filters to the filter group
        filter.addFilter(new WordRatioFilter("word-ratio"));
        // ...
        generator = new InstanceGenerator(digraph, filter);
        // We generate the instances and train an SVM classifier with them
        instances = generator.getGraphInstances();        
        svm = new SVMClassifier(new File(MPATH + GNAME + ".model"));
        System.out.println(instances.instance(3).value(1));
        instances.instance(3).setClassMissing();
        double res = svm.classifyInstance(instances.instance(3));
        System.out.println(res);
    }

}
