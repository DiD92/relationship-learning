package org.relationlearn.model;

import org.relationlearn.util.RelationType;

/**
 *
 * @author Dídac
 */
public interface ArgumentRelation {
    
    public int getArgumentRelationId();
    public ArgumentNode getArgumentator();
    public ArgumentNode getTarget();
    public RelationType getArgumentRelationType();
    public void changeRelationType(RelationType t);
}
