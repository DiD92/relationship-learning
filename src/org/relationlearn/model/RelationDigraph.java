package org.relationlearn.model;

import org.relationlearn.exception.AlreadyExistingNodeException;

/**
 *
 * @author Dídac
 */
public interface RelationDigraph extends Iterable<ArgumentNode> {

    public ArgumentNode getArgumentNode(int nodeId);
    public void addArgumentNode(ArgumentNode node) throws AlreadyExistingNodeException;
    public boolean containsNode(int nodeId);
}
