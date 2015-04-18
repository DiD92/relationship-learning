package org.relationlearn.util;

import org.relationlearn.filters.FilterGroup;
import org.relationlearn.model.ArgumentNode;
import org.relationlearn.model.ArgumentRelation;
import org.relationlearn.model.RelationDigraph;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Utility class that provides the generation of instances from a given
 * RelationDigraph and a combination of filters encapsulated in a FilterGroup.
 * 
 * @see RelationDigraph
 * @see FilterGroup
 */
public class InstanceGenerator {
    
    private final RelationDigraph GRAPH;
    private final FilterGroup FILTER;
    
    /**
     * Constructs a new Instances generator with {@code graph} as the source
     * of data and {@code filter} as the TextFilter objects container.
     * 
     * @param graph the RelationDigraph from which the data will be extracted
     * @param filter the FilterGroup that contains the filter to be applied
     * to the data extracted
     */
    public InstanceGenerator(RelationDigraph graph, FilterGroup filter) {
        this.GRAPH = graph;
        this.FILTER = filter;
    }
    
    /**
     * Generates an Instance object for each ArgumentRelation present in the
     * graph passed to the constructor and packs all those Instance objects in
     * an Instances one.
     * 
     * @return the Instances object containing all instances obtained from
     * the RelationDigraph throught the application of the filters contained
     * in the FilterGroup passed in the constructor
     * 
     * @see weka.core.Instance
     * @see weka.core.Instances
     */
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
