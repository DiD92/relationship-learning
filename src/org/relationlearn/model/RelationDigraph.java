package org.relationlearn.model;

import org.relationlearn.exception.AlreadyExistingNodeException;

/**
 * The RelationDigraph interface represents a directed weighted graph used to
 * store all arguments in the form of ArgumentNode objects and the relations
 * between those nodes in the form of ArgumentRelation objects.
 * 
 * @see ArgumentNode
 * @see ArgumentRelation
 */
public interface RelationDigraph extends Iterable<ArgumentNode> {

    /**
     * Returns the ArgumentNode with id equal to {@code nodeId}, this
     * method is expected to return null if there's no such ArgumentNode.
     * 
     * @param nodeId the id of the ArgumentNode to return.
     * 
     * @return the ArgumentNode with id {@code nodeId} or null if 
     * the node can't be found.
     */
    public ArgumentNode getArgumentNode(int nodeId);
    
    /**
     * Adds a new ArgumentNode to this RelationDigraph.
     * 
     * <p>
     * <b>Note:</b> Conditions for this method exception throw are
     * implementation dependent and should be specified in 
     * their corresponding javadoc page.
     * </p>
     * 
     * @param node the ArgumentNode to add
     * 
     * @throws AlreadyExistingNodeException
     */
    public void addArgumentNode(ArgumentNode node) 
            throws AlreadyExistingNodeException;
    
    /**
     * Checks if this RelationDigraph has a node stored with the
     * id equal to {@code nodeId}.
     * 
     * @param nodeId the id of the ArgumentNode to look for existence in the
     * RelationDigraph object.
     * 
     * @return true if there's a node with id equal to {@code nodeId}, 
     * false otherwise.
     */
    public boolean containsNode(int nodeId);
}
