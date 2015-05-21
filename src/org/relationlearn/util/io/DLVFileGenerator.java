package org.relationlearn.util.io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import org.relationlearn.model.ArgumentNode;
import org.relationlearn.model.ArgumentRelation;
import org.relationlearn.model.RelationDigraph;
import org.relationlearn.util.RelationType;

/**
 * OutputGenerator implementation which generates a group of files in the
 * <a href="http://www.dbai.tuwien.ac.at/research/project/argumentation/systempage/docu.htm">
 * ASPARTIX</a> AF extension language format, which can be used for argument 
 * reasoning argumnet reasoning using tools such as 
 * <a href="http://www.dlvsystem.com/">DLV</a>.
 * 
 * @see OutputGenerator
 */
public class DLVFileGenerator implements OutputGenerator {
    
    private final String VAF = "vaf.";
    private final String ARG = "arg(";
    private final String VAL = "val(";
    private final String VPR = "valpref(";
    private final String ATT = "att(";
    
    private final Queue<String> ARG_QUEUE;
    private final Queue<String> VAL_QUEUE;
    private final Queue<String> ATT_QUEUE;
    private final PriorityQueue<Integer> WGHT_QUEUE;
    
    public DLVFileGenerator() {
        this.ARG_QUEUE = new ArrayDeque<>();
        this.VAL_QUEUE = new ArrayDeque<>();
        this.ATT_QUEUE = new ArrayDeque<>();
        this.WGHT_QUEUE = new PriorityQueue<>();
    }
    
    /**
     * Generates a group of .dl files taking as base path the
     * parameter {@code basePath}.
     * 
     * <p>
     * <b>Notes on basePath parameter:</b> Take for example the input path:<br/>
     * {@code /home/output}</br/>Then if the parameter {@code graphTable} 
     * contains a graph called {@code graph1} and another one called 
     * {@code graph2} the results after the method call would generate two 
     * files called:<br/> {@code /home/output_graph1.dl} and 
     * {@code /home/output_graph2.dl} <br/> which would contain the 
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
            String file_path = basePath + "_" + de.getKey() + ".dl";
            generateDLVFile(de.getValue(), file_path);
        }
    }
    
    private void generateDLVFile(RelationDigraph digraph, String filename) {
        try (Writer writer = new FileWriter(filename, false)) {
            writer.write(VAF + "\n");
            gatherGraphData(digraph);
            writeGraphData(writer);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Error in .dl generation: " + 
                    ex.getMessage());
        }
    }
    
    private void gatherGraphData(RelationDigraph digraph) {
        String nodeName, targetName;
        Integer nodeWeight;
        ArgumentRelation nodeRelation;
        RelationType nodeRelType;
        ArgumentNode targetNode;
        for(ArgumentNode node : digraph) {
            nodeName = "n" + node.getNodeId();
            nodeWeight = node.getNodeWeight();
            ARG_QUEUE.add(ARG + nodeName + ").");
            VAL_QUEUE.add(VAL + nodeName + ", " + nodeWeight + ").");
            if(!WGHT_QUEUE.contains(nodeWeight)) {
                WGHT_QUEUE.add(nodeWeight);
            }
            if((nodeRelation = node.getTargetRelation()) != null) {
                nodeRelType = nodeRelation.getArgumentRelationType();
                if(RelationType.ATTACK.equals(nodeRelType)) {
                    targetNode = nodeRelation.getTarget();
                    targetName = "n" + targetNode.getNodeId();
                    ATT_QUEUE.add(ATT + nodeName + ", " + targetName + ").");
                }
            }
        }
    }
    
    private void writeGraphData(Writer out) throws IOException {
        Integer f1, f2, i;
        while(ARG_QUEUE.size() > 0) {
            out.write(ARG_QUEUE.poll() + "\n");
        }
        if(WGHT_QUEUE.size() > 1) { 
            while(VAL_QUEUE.size() > 0) {
                out.write(VAL_QUEUE.poll() + "\n");
            }
            while(WGHT_QUEUE.size() > 1) {
                f1 = WGHT_QUEUE.poll();
                f2 = WGHT_QUEUE.peek();
                out.write(VPR + f2 + ", " + f1 + ")." + "\n");
            }
            WGHT_QUEUE.remove();
        }
        while(ATT_QUEUE.size() > 0) {
            out.write(ATT_QUEUE.poll() + "\n");
        }
    }

}
