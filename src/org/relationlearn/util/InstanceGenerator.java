package org.relationlearn.util;

import org.relationlearn.model.RelationDigraph;
import weka.core.Instances;

/**
 *
 * @author Dídac
 */
public class InstanceGenerator {
    
    private final RelationDigraph GRAPH;
    
    public InstanceGenerator(RelationDigraph graph) {
        this.GRAPH = graph;
    }
    
    public Instances getGraphInstances() {
        return null;
    }

}
