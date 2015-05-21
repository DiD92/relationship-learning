package org.relationlearn.test;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.relationlearn.model.ArgumentNode;
import org.relationlearn.model.RelationDigraph;
import org.relationlearn.util.io.DLVFileGenerator;
import org.relationlearn.util.io.DOTFileGenerator;
import org.relationlearn.util.io.InputParser;
import org.relationlearn.util.io.OutputGenerator;
import org.relationlearn.util.io.XMLFileGenerator;
import org.relationlearn.util.io.XMLFileParser;

/**
 *
 * @author Dídac
 */
public class TestFileOutputGeneration {
    
    private static final String PATH = "test_files\\debatepedia\\";
    private static final String DOT_PATH = "test_files\\dot_files\\";
    private static final String DLV_PATH = "test_files\\dlv_files\\";
    private static final String FILE_TRAIN = "debatepedia_train.xml";
    private static final String FILE_TEST = "debatepedia_test.xml";
    private static final String FILE_OUT = "debatepedia_out.xml";
    private static final String BASE_OUT = "output";
    
    public static void main(String args[]) {
        InputParser xfp = new XMLFileParser();
        Map<String, RelationDigraph> relsTable = xfp.parseInput(PATH + FILE_TRAIN);
        /*System.out.println("****************");
        System.out.println(relsTable.size());
        System.out.println("****************");
        for(Entry<String, RelationDigraph> rd : relsTable.entrySet()) {
            System.out.println("Graph: " +rd.getKey());
            Iterator<ArgumentNode> it = rd.getValue().iterator();
            System.out.println(it.hasNext());
            while(it.hasNext()) {
                System.out.println(it.next().toString());
            }
        }*/
        // Generate NoDE XML output from relsTable
        OutputGenerator out = new XMLFileGenerator();
        out.generateOutput(relsTable,PATH + FILE_OUT);
        // Generate DOT files for each graph contained in relsTable
        out = new DOTFileGenerator();
        out.generateOutput(relsTable, DOT_PATH + BASE_OUT);
        // Generate DLV files for each graph contained in relsTable
        out = new DLVFileGenerator();
        out.generateOutput(relsTable, DLV_PATH + BASE_OUT);
    }
}
