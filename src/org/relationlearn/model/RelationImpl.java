package org.relationlearn.model;

import org.relationlearn.util.RelationType;

/**
 * An ArgumentRelation implementation.
 * 
 * @see ArgumentRelation
 */
public class RelationImpl implements ArgumentRelation {
    
    private int relationID;
    private ArgumentNode argumentatorNode;
    private ArgumentNode targetNode;
    private RelationType rType;
    
    private RelationImpl() {}
    
    /**
     * Constructs a new RelationImpl with id {@code rID}, origin node
     * {@code aNd}, destination node {@code tNd} and RelationType set to
     * {@code RelationType.UNKNOWN}.
     * 
     * @param rID identifier of this RelationImpl
     * @param aNd origin node of this RelationImpl
     * @param tNd destination node of this RelationImpl
     */
    public RelationImpl(int rID, ArgumentNode aNd, ArgumentNode tNd) {
        this(rID, aNd, tNd, RelationType.UNKNOWN);
    }
    
    /**
     * Constructs a new RelationImpl with id {@code rID}, origin node
     * {@code aNd}, destination node {@code tNd} and RelationType
     * {@code rT}.
     * 
     * @param rID identifier of this RelationImpl
     * @param aNd origin node of this RelationImpl
     * @param tNd destination node of this RelationImpl
     * @param rT type of this RelationImpl
     * 
     * @see org.relationlearn.util.RelationType
     */
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
