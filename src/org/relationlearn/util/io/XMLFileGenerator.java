package org.relationlearn.util.io;

import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.relationlearn.model.ArgumentNode;
import org.relationlearn.model.ArgumentRelation;
import org.relationlearn.model.RelationDigraph;

/**
 *
 * @author Dídac
 */
public class XMLFileGenerator implements OutputGenerator {
    
    private class XMLStreamGeneratorHelper {
        
        private final Map<String, RelationDigraph> GRAPHS;
        private final String FILE_PATH;
        
        private final String DOCUMENT_ENC = "UTF-8";
        private final String XML_VERSION = "1.0";
        
        private final String ROOT_ELEMENT = "entailment-corpus";
        private final String PAIR_ELEMENT = "pair";
        private final String TEXT_ELEMENT = "t";
        private final String HYPOTHESIS_ELEMENT = "h";
        
        private final String TASK_ATTR = "task";
        private final String ID_ATTR = "id";
        private final String TOPIC_ATTR = "topic";
        private final String ENTAIL_ATTR = "entailment";
        private final String WEIGHT_ATTR = "weight";
        
        private final String TASK_VAL = "ARG";
        
        public XMLStreamGeneratorHelper(Map<String, RelationDigraph> graph,
                String path) {
            this.GRAPHS = graph;
            this.FILE_PATH = path;
        }
        
        public void generateXMLDocument() {
            try {
                
                XMLOutputFactory output = XMLOutputFactory.newInstance();
                XMLStreamWriter writer = 
                        output.createXMLStreamWriter(
                                new FileOutputStream(FILE_PATH), DOCUMENT_ENC);
                writer = new IndentingXMLStreamWriter(writer);
                // document header
                writer.writeStartDocument(DOCUMENT_ENC, XML_VERSION);
                // document root
                writer.writeStartElement(ROOT_ELEMENT);
                
                // writing all pairs for all graphs in map
                for(Entry<String, RelationDigraph> gEntry : GRAPHS.entrySet()) {
                    RelationDigraph graph = gEntry.getValue();
                    for(ArgumentNode node : graph) {
                        ArgumentRelation relation = node.getTargetRelation();
                        if(relation != null) {
                            ArgumentNode target = relation.getTarget();
                            writePair(writer, gEntry.getKey(), 
                                    node, target, relation);
                        }
                    }
                }
                
                //closing root
                writer.writeEndElement();
                
                //writing end and closing
                writer.writeEndDocument();
                writer.flush();
                writer.close();   
                
            } catch (FileNotFoundException fnfex) {
                throw new IllegalArgumentException("Error: " + fnfex.getMessage());
            } catch (XMLStreamException xmlsex) {
                System.err.println("Eror in XML stream: " + xmlsex.getMessage());
            }
        }
        
        private void writePair(XMLStreamWriter writer, String topic, 
                ArgumentNode t, ArgumentNode h, ArgumentRelation r) 
                throws XMLStreamException {
            writer.writeStartElement(PAIR_ELEMENT);
            writer.writeAttribute(TASK_ATTR, TASK_VAL);
            writer.writeAttribute(ID_ATTR, String.valueOf(r.getArgumentRelationId()));
            writer.writeAttribute(TOPIC_ATTR, topic);
            writer.writeAttribute(ENTAIL_ATTR, r.getArgumentRelationType().toString());
            // t element
            writer.writeStartElement(TEXT_ELEMENT);
            writer.writeAttribute(ID_ATTR, String.valueOf(t.getNodeId()));
            writer.writeAttribute(WEIGHT_ATTR, String.valueOf(t.getNodeWeight()));
            writer.writeCharacters(t.getArgumentNodeText());
            writer.writeEndElement();
            // h element
            writer.writeStartElement(HYPOTHESIS_ELEMENT);
            writer.writeAttribute(ID_ATTR, String.valueOf(h.getNodeId()));
            writer.writeAttribute(WEIGHT_ATTR, String.valueOf(h.getNodeWeight()));
            writer.writeCharacters(h.getArgumentNodeText());
            writer.writeEndElement();
            // closing pair
            writer.writeEndElement();
        }
    }
    
    @Override
    public void generateOutput(Map<String, RelationDigraph> table, 
            String path) {
        XMLStreamGeneratorHelper generator = 
                new XMLStreamGeneratorHelper(table, path);
        generator.generateXMLDocument();
    }

}
