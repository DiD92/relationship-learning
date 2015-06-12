package org.relationlearn.util;

import java.io.File;
import java.util.Map;
import org.relationlearn.classifiers.SVMClassifier;
import org.relationlearn.filters.FilterGroup;
import org.relationlearn.filters.SequentialFilterGroup;
import org.relationlearn.model.ArgumentNode;
import org.relationlearn.model.ArgumentRelation;
import org.relationlearn.model.RelationDigraph;
import org.relationlearn.util.io.DLVFileGenerator;
import org.relationlearn.util.io.DOTFileGenerator;
import org.relationlearn.util.io.InputParser;
import org.relationlearn.util.io.OutputGenerator;
import org.relationlearn.util.io.XMLFileGenerator;
import org.relationlearn.util.io.XMLFileParser;
import weka.core.Instances;

/**
 * Utility class used to classify graphs with already trained classifiers.
 * 
 * The call parameters are: &ltinput-file&gt &ltfilter-config-path&gt 
 * &ltmodels-path&gt &ltout-format&gt &ltout-path&gt
 * 
 */
public class GraphClassifier {
    
    private static InputParser parser;
    private static OutputGenerator oGenerator;
    private static FilterGroup filters;
    private static InstanceGenerator generator;
    private static SVMClassifier classifier;
    
    private static File configFile;
    private static File modelFile;
    
    private static Instances instances;
    
    private static String inputFile;
    private static String baseConfigPath;
    private static String modelsPath;
    private static String outFormat;
    private static String baseOutPath;
    
    private static final String attck = RelationType.ATTACK.toString();
    private static final String suppt = RelationType.SUPPORT.toString();
    
    public static void main(String args[]) throws Exception {
        if(args.length != 5) {
            System.err.println("Error in parameters, usage: "
                    + "<input-file.xml> <filter-config-path> <models-path> "
                    + "<-dot|-xml|-dlv> <output-path>");
        } else {
            inputFile = args[0];
            baseConfigPath = args[1];
            modelsPath = args[2];
            outFormat = args[3];
            baseOutPath = args[4];
            parser = new XMLFileParser();
            Map<String, RelationDigraph> graphs = parser.parseInput(inputFile);
            String gName, sResult;
            double result;
            int i;
            ArgumentRelation relation;
            for (Map.Entry<String, RelationDigraph> graph : graphs.entrySet()) {
                gName = graph.getKey();
                configFile = new File(baseConfigPath + gName + ".xml");
                if (configFile.exists()) {
                    filters = new SequentialFilterGroup(gName, configFile);
                    filters.addClassAttribute(null);
                    generator = new InstanceGenerator(
                            graph.getValue(), filters);
                    instances = generator.getGraphInstances();
                    modelFile = new File(modelsPath + gName + ".model");
                    if(modelFile.exists()) {
                        classifier = new SVMClassifier(modelFile);
                        i = 0;
                        for(ArgumentNode node : graph.getValue()) {
                            relation = node.getTargetRelation();
                            if(relation != null) {
                                instances.instance(i).setClassMissing();
                                result = classifier.classifyInstance(
                                        instances.instance(i));
                                sResult = instances.classAttribute().value(
                                        (int)result);
                                if(attck.equals(sResult)) {
                                    relation.changeRelationType(
                                            RelationType.ATTACK);
                                } else if(suppt.equals(sResult)) {
                                    relation.changeRelationType(
                                            RelationType.SUPPORT);
                                } else {
                                    relation.changeRelationType(
                                            RelationType.UNKNOWN);
                                }
                                i++;
                            }
                        }
                    } else {
                        System.err.println("Error could not find " + modelFile 
                                + " file for: " + gName);
                    }
                } else {
                    System.err.println("Error could not find config file for: " +
                            gName);
                }
            }
            if (null != outFormat) {
                switch (outFormat) {
                    case "-dot":
                        oGenerator = new DOTFileGenerator();
                        oGenerator.generateOutput(graphs, baseOutPath);
                        break;
                    case "-xml":
                        oGenerator = new XMLFileGenerator();
                        oGenerator.generateOutput(
                                graphs, baseOutPath + ".xml");
                        break;
                    case "-dlv":
                        oGenerator = new DLVFileGenerator();
                        oGenerator.generateOutput(graphs, baseOutPath);
                        break;
                    default:
                        System.err.println("Error in output format specifier");
                        System.exit(-1);
                }
            }
        }
    }

}
