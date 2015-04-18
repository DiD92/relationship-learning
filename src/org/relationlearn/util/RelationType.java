package org.relationlearn.util;

/**
 * Enumerator used to describe the possible types of relation between two
 * ArgumentNode objects in a RelationDigraph.
 * 
 * @see org.relationlearn.model.ArgumentRelation
 * @see org.relationlearn.model.ArgumentNode
 */
public enum RelationType {
    /**
     * Value representing that the origin ArgumentNode supports the argument
     * stated by the target ArgumentNode.
     */
    SUPPORT, 
    
    /**
     * Value representing that the origin ArgumentNode disapproves the argument
     * stated by the target ArgumentNode.
     */
    ATTACK, 
    
    /**
     * Value representing that the origin ArgumentNode does neither 
     * support the argument stated by the target ArgumentNode nor disapproves 
     * it.
     */
    UNKNOWN;
}
