package org.relationlearn.model;

import java.util.List;

/**
 * The ArgumentNode interface represents a text message in a given message 
 * board with additional parameters to help it being identified from other
 * messages in a RelationDigraph instace.
 * 
 * @see RelationDigraph
 * @see ArgumentRelation
 */
public interface ArgumentNode {
    
    /**
     * Returns the ArgumentNode identifier.
     * 
     * @return this ArgumentNode identifier
     */
    public int getNodeId();
    
    /**
     * Returns the weight value of this ArgumentNode.
     * 
     * <p>
     * <b>Note: </b>Negative weight values should be permited by 
     * ArgumentNode implementations.
     * </p>
     * 
     * @return the weight of this ArgumentNode
     */
    public int getNodeWeight();
    
    /**
     * Adds an ArgumentRelation which represents the reply this node
     * made to antoher ArgumentNode.
     * 
     * @param aRel the ArgumentRelation that contains this node as origin and
     * another ArgumentNode as destination.
     */
    public void addTargetRelation(ArgumentRelation aRel);
    
    /**
     * Returns the ArgumentRelation in which this node is the origin.
     * 
     * @return the ArgumentRelation that has its source in this
     * ArgumentNode object.
     * 
     * @see ArgumentRelation
     */
    public ArgumentRelation getTargetRelation();
    
    /**
     * Returns the ArgumentRelation with id {@code relationId} in which
     * this node is the destination.
     * 
     * @param relationId the id of the ArgumentRelation
     * @return the ArgumentRelation in which this node acts as the destination.
     */
    public ArgumentRelation getReplayRelation(int relationId);
    
    /**
     * Returns a List of all the ArgumentRelation objects in which this node
     * acts as the destination.
     * 
     * @return a list containing all the ArgumentRelations in which this node
     * is the destination.
     */
    public List<ArgumentRelation> getReplyRelations();
    
    /**
     * Adds a new ArgumentRelation in which this node acts as the destination.
     * 
     * @param relation the ArgumentRelation to be added
     * 
     * @see ArgumentRelation
     */
    public void addReplyRelation(ArgumentRelation relation);
    
    /**
     * Returns the text that this node contains.
     * 
     * @return the text this node contains
     */
    public String getArgumentNodeText(); 
}
