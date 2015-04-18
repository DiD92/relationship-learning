package org.relationlearn.util.io;

import java.util.Map;
import org.relationlearn.model.RelationDigraph;

/**
 * The OutputGenerator interface defines the rules any implementationg should
 * follow in order to provide output format for representing the data in a 
 * Map of RelationDigraph objects.
 * 
 * @see org.relationlearn.model.RelationDigraph
 */
public interface OutputGenerator {
    
    /**
     * Using the data provided in {@code graphTable} this method generates an
     * output in the {@code outputUri} parameter.
     * 
     * <p>
     * <b>Note: </b> The specific nature of the output generated is 
     * implementation dependent and should be documented accordingly in 
     * its corresponding javadoc page.
     * </p>
     * 
     * @param graphTable the Map containing the RelationDigraph objects
     * @param outputUri the URI in which the outpur will be generated
     */
    public void generateOutput(Map<String, RelationDigraph> graphTable,
            String outputUri);

}
