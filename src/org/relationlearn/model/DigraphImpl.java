package org.relationlearn.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import org.relationlearn.exception.AlreadyExistingNodeException;

/**
 *
 * @author Dídac
 */
public class DigraphImpl implements RelationDigraph {
    
    private Stack<ArgumentNode> treeStack;
    
    private class PostorderTreeIterator implements Iterator<ArgumentNode> {
        
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
            visitStack.push(DigraphImpl.this.getArgumentNode(1)); //HARDCODED
            while(!visitStack.empty()) {
                ArgumentNode node = visitStack.pop();
                treeStack.push(node);
                for(ArgumentRelation rel : node.getReplyRelations()) {
                    visitStack.push(rel.getArgumentator());
                }
            }
        }
        
    }
    
    private final Map<Integer, ArgumentNode> nodeTable;
    
    public DigraphImpl() {
        this.nodeTable = new HashMap<>();
    }

    @Override
    public ArgumentNode getArgumentNode(int nodeId) {
        return this.nodeTable.get(nodeId);
    }

    @Override
    public void addArgumentNode(ArgumentNode node) 
            throws AlreadyExistingNodeException {
        if(nodeTable.containsKey(node.getNodeId())) {
            throw new AlreadyExistingNodeException();
        } else {
            nodeTable.put(node.getNodeId(), node);
        }
    }

    @Override
    public boolean containsNode(int nodeId) {
        return this.nodeTable.containsKey(nodeId);
    }
    
    @Override
    public String toString() {
        String str = "";
        for(ArgumentNode an : nodeTable.values()) {
            str += an.toString() + "\n";
        }
        return str;
    }

    @Override
    public Iterator<ArgumentNode> iterator() {
        return new PostorderTreeIterator();
    }

}
