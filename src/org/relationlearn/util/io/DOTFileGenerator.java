package org.relationlearn.util.io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;
import org.jgrapht.Graph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.relationlearn.model.ArgumentNode;
import org.relationlearn.model.ArgumentRelation;
import org.relationlearn.model.RelationDigraph;
import org.relationlearn.util.RelationType;

/**
 *
 * @author Dídac
 */
public class DOTFileGenerator implements OutputGenerator {
    
    private class ArgumentNodeIDProvider 
        implements VertexNameProvider<ArgumentNode> {

        @Override
        public String getVertexName(ArgumentNode vertex) {
            return Integer.toString(vertex.getNodeId());
        }
        
    }
    
    private class ArgumentNodeLabelProvider 
        implements VertexNameProvider<ArgumentNode> {
        
        @Override
        public String getVertexName(ArgumentNode vertex) {
            return "N" + Integer.toString(vertex.getNodeId());
        }
    }
    
    private class RelationTypeProvider 
        implements EdgeNameProvider<RelationTypeEdge> {

        @Override
        public String getEdgeName(RelationTypeEdge edge) {
            return edge.getType().toString();
        }
    }
    
    private class RelationTypeEdge extends DefaultEdge {
        
        private final RelationType REL_TYPE;
        
        public RelationTypeEdge() {
            this(RelationType.UNKNOWN);
        }
        
        public RelationTypeEdge(RelationType type) {
            this.REL_TYPE = type;
        }
        
        public RelationType getType() {
            return REL_TYPE;
        }
    }

    @Override
    public void generateOutput(Map<String, RelationDigraph> graph_table, 
            String base_path) {
        for(Entry<String, RelationDigraph> de : graph_table.entrySet()) {
            String file_path = base_path + "_" + de.getKey() + ".dot";
            generateDOTFile(de.getValue(), file_path);
        }
    }
    
    private void generateDOTFile(RelationDigraph digraph, String filename) {
        try (Writer writer = new FileWriter(filename)) {
            Graph<ArgumentNode, RelationTypeEdge> d_repr;
            d_repr = new DefaultDirectedWeightedGraph<>(RelationTypeEdge.class);
            
            fillGraphWithValues(digraph, d_repr);
            
            DOTExporter dw = new DOTExporter(
                    new ArgumentNodeIDProvider(), 
                    new ArgumentNodeLabelProvider(), 
                    new RelationTypeProvider());   
            dw.export(writer, d_repr);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Error in .dot generation: " + 
                    ex.getMessage());
        }
    }
    
    private void fillGraphWithValues(RelationDigraph digraph, 
            Graph<ArgumentNode, RelationTypeEdge> repr) {
        
        for(ArgumentNode node : digraph) {
            repr.addVertex(node);
            ArgumentRelation relation = node.getTargetRelation();
            if(relation != null) {
                ArgumentNode target = relation.getTarget();
                RelationType rel_type = relation.getArgumentRelationType();
                repr.addVertex(target);
                repr.addEdge(node, target, new RelationTypeEdge(rel_type));
            }
        }
    }

}
