package org.relationlearn.util;

import org.relationlearn.filters.FilterGroup;
import org.relationlearn.model.ArgumentNode;
import org.relationlearn.model.ArgumentRelation;
import org.relationlearn.model.RelationDigraph;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Dídac
 */
public class InstanceGenerator {
    
    private final RelationDigraph GRAPH;
    private final FilterGroup FILTER;
    
    public InstanceGenerator(RelationDigraph graph, FilterGroup filter) {
        this.GRAPH = graph;
        this.FILTER = filter;
    }
    
    public Instances getGraphInstances() {
        Instances ins = new Instances(FILTER.getGroupDatasetName(), 
                FILTER.getGroupAttributes(), 0);
        for(ArgumentNode an : GRAPH) {
            ArgumentRelation relation = an.getTargetRelation();
            if(relation != null) {
                Instance in = generateInstace(an.getArgumentNodeText(), 
                        relation.getTarget().getArgumentNodeText());
                ins.add(in);
            }
        }
        return ins;
    }
    
    private Instance generateInstace(String response, String hypothesis) {
        return FILTER.createInstanceUsingFilters(response, hypothesis);
    }

}
