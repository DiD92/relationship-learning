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
 * OutputGenerator implementation which generates a group of files in the
 * <a href="http://en.wikipedia.org/wiki/DOT_%28graph_description_language%29">
 * DOT language</a> format for graph representation using tools 
 * such as <a href="http://www.graphviz.org/">GraphViz</a>.
 * 
 * @see OutputGenerator
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
    
    /**
     * Generates a group of .dot files taking as base path the
     * parameter {@code basePath}.
     * 
     * <p>
     * <b>Notes on basePath parameter:</b> Take for example the input path:<br/>
     * {@code /home/output}</br/>Then if the parameter {@code graphTable} 
     * contains a graph called {@code graph1} and another one called 
     * {@code graph2} the results after the method call would generate two 
     * files called:<br/> {@code /home/output_graph1.dot} and 
     * {@code /home/output_graph2.dot} <br/> which would contain the 
     * information found in each RelationDigraph object respectively.
     * 
     * @param graphTable the Map containing the data to be used in the
     * output generation
     * @param basePath the base path were the output files will be
     * generated
     */
    @Override
    public void generateOutput(Map<String, RelationDigraph> graphTable, 
            String basePath) {
        for(Entry<String, RelationDigraph> de : graphTable.entrySet()) {
            String file_path = basePath + "_" + de.getKey() + ".dot";
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
