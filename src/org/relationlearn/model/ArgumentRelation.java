package org.relationlearn.model;

import org.relationlearn.util.RelationType;

/**
 * The ArgumentRelation interface represents the link between two ArgumentNode
 * objects and ultimately stores the RelationType value, which describes
 * what kind of relation those nodes have.
 * 
 * @see org.relationlearn.util.RelationType
 * @see ArgumentNode
 */
public interface ArgumentRelation {
    
    /**
     * Returns this ArgumentRelation identifier.
     * 
     * @return the ArgumentRelation identifier
     */
    public int getArgumentRelationId();
    
    /**
     * Returns the ArgumentNode identified as the
     * origin of this ArgumentRelation.
     * 
     * @return the ArgumentNode origin of this relation
     */
    public ArgumentNode getArgumentator();
    
    /**
     * Returns the ArgumentNode identified as the
     * target of this ArgumentRelation.
     * 
     * @return the ArgumentNode destination of this relation
     */
    public ArgumentNode getTarget();
    
    /**
     * Returns a RelationType value that describes the current known
     * state of this ArgumentRelation.
     * 
     * @return the state type of this ArgumentRelation
     */
    public RelationType getArgumentRelationType();
    
    /**
     * Sets the RelationType value of this relation to 
     * the value of the parameter {@code t}.
     * 
     * @param t the new RelationType of this ArgumentRelation
     */
    public void changeRelationType(RelationType t);
}
