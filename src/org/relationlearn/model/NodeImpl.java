package org.relationlearn.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ArgumentNode implementation that uses a Map to store its reply
 * ArgumentRelations.
 * 
 * @see ArgumentNode
 */
public class NodeImpl implements ArgumentNode {
    
    private int nodeID;
    private int nodeWeight;
    
    private ArgumentRelation targetRelation;
    private final Map<Integer, ArgumentRelation> repliesTable;
    
    private String nodeText;
    
    private NodeImpl() {
        this.repliesTable = new HashMap<>();
    }
    
    /**
     * Construct a new NodeImpl with id {@code nId}, weight {@code nW} and
     * node text {@code text}.
     * 
     * @param nID identifier of this NodeImpl
     * @param nW weight of this NodeImpl
     * @param text complete text of this NodeImpl
     */
    public NodeImpl(int nID, int nW, String text) {
        this();
        this.nodeID = nID;
        this.nodeWeight = nW;
        this.nodeText = text;
    }

    @Override
    public int getNodeId() {
        return this.nodeID;
    }

    @Override
    public int getNodeWeight() {
        return this.nodeWeight;
    }
    
    @Override
    public void addTargetRelation(ArgumentRelation aRel) {
        this.targetRelation = aRel;
    }

    @Override
    public ArgumentRelation getTargetRelation() {
        return this.targetRelation;
    }

    @Override
    public ArgumentRelation getReplayRelation(int relationId) {
        return this.repliesTable.get(relationId);
    }

    @Override
    public List<ArgumentRelation> getReplyRelations() {
        return new ArrayList<>(this.repliesTable.values());
    }

    @Override
    public void addReplyRelation(ArgumentRelation relation) {
        if(!repliesTable.containsKey(relation.getArgumentRelationId())) {
            this.repliesTable.put(relation.getArgumentRelationId(), relation);
        }
    }
    
    @Override
    public String getArgumentNodeText() {
        return this.nodeText;
    }
    
    /** 
     * Returns a String representation of this NodeImpl and its
     * contents.
     * 
     * @return a String representation of this NodeImpl and its contents
     */
    @Override
    public String toString() {
        String str = "";
        str += "Node id: " + this.getNodeId() + "\n";
        str += "\tWeight: " + this.getNodeWeight() + "\n";
        str += "\tText: " + this.getArgumentNodeText() + "\n";
        if(this.getTargetRelation() == null) {
            str += "\tTarget: NONE\n";
        } else {
            str += "\tTarget: " + this.getTargetRelation().getTarget().getNodeId() + " (" + this.getTargetRelation().getArgumentRelationType() + ")\n";
        }
        str += "\tReplies from: ";
        for(ArgumentRelation ar : repliesTable.values()) {
            str += ar.getArgumentator().getNodeId() + " ";
        }
        str += "\n";
        return str;
    }

}
