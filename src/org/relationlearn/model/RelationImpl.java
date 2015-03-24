package org.relationlearn.model;

import org.relationlearn.util.RelationType;

/**
 *
 * @author Dídac
 */
public class RelationImpl implements ArgumentRelation {
    
    private int relationID;
    private ArgumentNode argumentatorNode;
    private ArgumentNode targetNode;
    private RelationType rType;
    
    private RelationImpl() {}
    
    public RelationImpl(int rID, ArgumentNode aNd, ArgumentNode tNd) {
        this(rID, aNd, tNd, RelationType.UNKNOWN);
    }
    
    public RelationImpl(int rID, ArgumentNode aNd, ArgumentNode tNd, 
            RelationType rT) {
        this.relationID = rID;
        this.argumentatorNode = aNd;
        this.targetNode = tNd;
        this.rType = rT;
    }

    @Override
    public int getArgumentRelationId() {
        return this.relationID;
    }

    @Override
    public ArgumentNode getArgumentator() {
        return this.argumentatorNode;
    }

    @Override
    public ArgumentNode getTarget() {
        return this.targetNode;
    }

    @Override
    public RelationType getArgumentRelationType() {
        return this.rType;
    }

    @Override
    public void changeRelationType(RelationType t) {
        this.rType = t;
    }

}
