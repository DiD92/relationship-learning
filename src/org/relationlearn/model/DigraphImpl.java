package org.relationlearn.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import org.relationlearn.exception.AlreadyExistingNodeException;

/**
 * RelationDigraph implementation that stores the ArgumentNode objects in a 
 * Map.
 * 
 * @see RelationDigraph
 * @see java.util.Map
 */
public class DigraphImpl implements RelationDigraph {
    
    private ArgumentNode origin;
    
    private class PostorderTreeIterator implements Iterator<ArgumentNode> {
        
        private final Stack<ArgumentNode> treeStack;
        
        public PostorderTreeIterator() {
            treeStack = new Stack<>();
            if(DigraphImpl.this.nodeTable.size() > 0) {
                fillStack();
            }
        }

        @Override
        public boolean hasNext() {
            return !(treeStack.isEmpty());
        }

        @Override
        public ArgumentNode next() {
            return treeStack.pop();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        private void fillStack() {
            Stack<ArgumentNode> visitStack = new Stack<>();
            ArgumentNode start = getOrigin();
            origin = start;
            graphChanged = false;
            if(start != null) {
                visitStack.push(start);
                while(!visitStack.empty()) {
                    ArgumentNode node = visitStack.pop();
                    treeStack.push(node);
                    for(ArgumentRelation rel : node.getReplyRelations()) {
                        visitStack.push(rel.getArgumentator());
                    }
                }
            }
        }
        
        private ArgumentNode getOrigin() {
            if(graphChanged) {
                for(ArgumentNode node : nodeTable.values()) {
                    if(node.getTargetRelation() == null) {
                        return node;
                    }
                }
                return null;
            } else {
                return origin;
            }
        }
        
    }
    
    private final Map<Integer, ArgumentNode> nodeTable;
    
    private boolean graphChanged;
    
    /**
     * Constucts a new empty DigraphImpl.
     */
    public DigraphImpl() {
        this.nodeTable = new HashMap<>();
    }
    
    /**
     * Returns the ArgumentNode with id equal to {@code nodeId}.
     * 
     * @param nodeId the id of the ArgumentNode to return.
     * 
     * @return the ArgumentNode with id {@code nodeId} or null if 
     * the node can't be found.
     */
    @Override
    public ArgumentNode getArgumentNode(int nodeId) {
        return this.nodeTable.get(nodeId);
    }
    
    /**
     * Adds a new ArgumentNode to this RelationDigraph.
     * 
     * @param node the ArgumentNode to add
     * 
     * @throws AlreadyExistingNodeException if there's already an
     * ArgumentNode with the same id as {@code node}
     */
    @Override
    public void addArgumentNode(ArgumentNode node) 
            throws AlreadyExistingNodeException {
        if(nodeTable.containsKey(node.getNodeId())) {
            throw new AlreadyExistingNodeException();
        } else {
            graphChanged = true;
            nodeTable.put(node.getNodeId(), node);
        }
    }

    @Override
    public boolean containsNode(int nodeId) {
        return this.nodeTable.containsKey(nodeId);
    }
    
    /** Returns a String representation of this DigraphImpl and its 
     * content by recursively calling each ArgumentNode toString method.
     * 
     * @return a String representation of this DigraphImpl and its internal
     * content
     */
    @Override
    public String toString() {
        String str = "";
        for(ArgumentNode an : nodeTable.values()) {
            str += an.toString() + "\n";
        }
        return str;
    }
    
    /**
     * Returns an Iterator that iterates through the graph's
     * ArgumentNode objects in postorder.
     * 
     * <p>
     * <b>Note: </b>This iterator expects the graph to contain an 
     * ArgumentNode with id equal to 1, which is considered the
     * root node (the only node that doesn't have its target relation
     * value set).
     * </p>
     * 
     * @return an Iterator that traverses the graph's nodes in postorder
     * 
     * @see java.util.Iterator
     * @see java.lang.Iterable
     */
    @Override
    public Iterator<ArgumentNode> iterator() {
        return new PostorderTreeIterator();
    }

}
