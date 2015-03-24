package org.relationlearn.model;

import java.util.List;

/**
 *
 * @author Dídac
 */
public interface ArgumentNode {
    
    public int getNodeId();
    public int getNodeWeight();
    public void addTargetRelation(ArgumentRelation aRel);
    public ArgumentRelation getTargetRelation();
    public ArgumentRelation getReplayRelation(int relationId);
    public List<ArgumentRelation> getReplyRelations();
    public void addReplyRelation(ArgumentRelation relation);
    public String getArgumentNodeText(); 
}
