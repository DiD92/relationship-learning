package org.relationlearn.util.io;

import java.util.Map;
import org.relationlearn.model.RelationDigraph;

/**
 *
 * @author Dídac
 */
public interface InputParser {
    
    public Map<String, RelationDigraph> parseInput(String uri);
}
