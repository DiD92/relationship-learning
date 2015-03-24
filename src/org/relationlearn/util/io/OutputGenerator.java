package org.relationlearn.util.io;

import java.util.Map;
import org.relationlearn.model.RelationDigraph;

/**
 *
 * @author Dídac
 */
public interface OutputGenerator {
    
    public void generateOutput(Map<String, RelationDigraph> graph_table,
            String output_uri);

}
