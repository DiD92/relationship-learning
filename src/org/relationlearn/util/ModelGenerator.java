package org.relationlearn.util;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import org.relationlearn.classifiers.SVMClassifier;
import org.relationlearn.filters.FilterGroup;
import org.relationlearn.filters.SequentialFilterGroup;
import org.relationlearn.model.RelationDigraph;
import org.relationlearn.util.io.InputParser;
import org.relationlearn.util.io.XMLFileParser;
import weka.core.Instances;

/**
 * Utility class used to generate trained classifiers and store them in a file.
 * 
 * The call parameters are: &lttraining-file&gt &ltfilter-config-path&gt 
 * &ltoutput-path&gt
 * 
 * @see weka.classifiers.functions.LibSVM
 * 
 */
public class ModelGenerator {
    
    private static InputParser parser;
    private static FilterGroup filters;
    private static InstanceGenerator generator;
    private static SVMClassifier classifier;
    
    private static File configFile;
    
    private static Instances instances;
    
    private static String inputFile;
    private static String baseConfigPath;
    private static String baseOutPath;
    private static String[] svmOptions;
    
    private ModelGenerator() {}
    
    public static void main(String args[]) throws Exception {
        if(args.length < 3) {
            System.err.println("Error in parameters, usage: "
                    + "<input-file.xml> <filter-config-path> <output-path> [<classifier-options>]");
        } else {
            inputFile = args[0];
            baseConfigPath = args[1];
            baseOutPath = args[2];
            if(args.length > 3) {
                svmOptions = Arrays.copyOfRange(args, 3, args.length);
            }
            parser = new XMLFileParser();
            Map<String, RelationDigraph> grahps = parser.parseInput(inputFile);
            String gName;
            for(Entry<String, RelationDigraph> graph : grahps.entrySet()) {
                gName = graph.getKey();
                configFile = new File(baseConfigPath + gName + ".xml");
                if(configFile.exists()) {
                    filters = new SequentialFilterGroup(gName, configFile);
                    filters.addClassAttribute(null);
                    generator = new InstanceGenerator(graph.getValue(), filters);
                    instances = generator.getGraphInstances();
                    classifier = new SVMClassifier();
                    if(args.length > 3) {
                        classifier.setOptions(svmOptions);
                    }
                    classifier.trainClassifier(instances);
                    classifier.storeModel(baseOutPath + gName + ".model");
                } else {
                    System.err.println("Error could not find config file for: "
                            + gName);
                }
                
            }
        }
    }

}
