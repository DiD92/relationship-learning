package org.relationlearn.test;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.relationlearn.model.ArgumentNode;
import org.relationlearn.model.RelationDigraph;
import org.relationlearn.util.io.InputParser;
import org.relationlearn.util.io.OutputGenerator;
import org.relationlearn.util.io.XMLFileGenerator;
import org.relationlearn.util.io.XMLFileParser;

/**
 *
 * @author Dídac
 */
public class TestXMLFileParser {
    
    private static final String PATH = "test_files\\debatepedia\\";
    private static final String FILE_TRAIN = "debatepedia_train.xml";
    private static final String FILE_TEST = "debatepedia_test.xml";
    private static final String FILE_OUT = "debatepedia_out.xml";
    public static void main(String args[]) {
        InputParser xfp = new XMLFileParser();
        Map<String, RelationDigraph> relsTable = xfp.parseInput(PATH + FILE_TRAIN);
        System.out.println("****************");
        System.out.println(relsTable.size());
        System.out.println("****************");
        for(Entry<String, RelationDigraph> rd : relsTable.entrySet()) {
            System.out.println("Graph: " +rd.getKey());
            Iterator<ArgumentNode> it = rd.getValue().iterator();
            System.out.println(it.hasNext());
            while(it.hasNext()) {
                System.out.println(it.next().toString());
            }
        }
        OutputGenerator out = new XMLFileGenerator();
        out.generateOutput(relsTable,PATH + FILE_OUT);
    }
}
