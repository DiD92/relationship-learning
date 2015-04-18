package org.relationlearn.util.io;

import java.util.Map;
import org.relationlearn.model.RelationDigraph;

/**
 * The InputParser interface defines the behaviour of any class that wants
 * to be used as a way of extracting data from a given input format the user
 * needs to.
 * 
 * @see org.relationlearn.model.RelationDigraph
 */
public interface InputParser {
    
    /**
     * Parses data at a given URI and generates a Map containing as many
     * RelationDigraph instances as the implementation is able to find
     * in the URI provided.
     * 
     * @param uri the URI where the data to be parsed should be found
     * @return a Map containing all RelationDigraph instances parsed
     * 
     * <p>
     * <b>Note: </b> Although this method is not expected to throw any 
     * unchecked exceptions, the IllegalArgument exception should be 
     * thrown in case of a fatal error regarding the input URI.
     * </p>
     */
    public Map<String, RelationDigraph> parseInput(String uri);
}
