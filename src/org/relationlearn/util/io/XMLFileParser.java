package org.relationlearn.util.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.relationlearn.exception.AlreadyExistingNodeException;
import org.relationlearn.model.ArgumentNode;
import org.relationlearn.model.ArgumentRelation;
import org.relationlearn.model.DigraphImpl;
import org.relationlearn.model.NodeImpl;
import org.relationlearn.model.RelationDigraph;
import org.relationlearn.model.RelationImpl;
import org.relationlearn.util.RelationType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * InputParser implementation that parser and XML file written in the
 * <a href="http://www-sop.inria.fr/NoDE/NoDE-xml.html">NoDE</a> format, using
 * a SAX based parser.
 * 
 * @see InputParser
 * @see javax.xml.parsers.SAXParser
 */
public class XMLFileParser implements InputParser {
    
    private static class NodeBuilder {
        
        public int nodeId;
        public int nodeWeight;
        public String nodeText;
        
        public ArgumentNode build() {
            return new NodeImpl(nodeId, nodeWeight, nodeText);
        }
    }
    
    private static class RelationBuilder {
        
        public int relationId;
        public ArgumentNode aNode;
        public ArgumentNode tNode;
        public RelationType relType;
        
        public ArgumentRelation build() {
            return new RelationImpl(relationId, aNode, tNode, relType);
        }
    }
    
    private static class SAXParserHelper extends DefaultHandler {
        
        private final String PAIR_TAG = "pair";
        private final String TARGET_TAG = "h";
        private final String RESPONSE_TAG = "t";
        
        private final String ID_ATTR = "id";
        private final String WEIGHT_ATTR = "weight";
        private final String TOPIC_ATTR = "topic";
        private final String ENTL_ATTR = "entailment";
        
        private final String VALUE_NO = "NO";
        private final String VALUE_YES = "YES";
        
        private final Map<String, RelationDigraph> relGraph;
        
        // Variables used to build the graph nodes while parsing the file
        private String currentGraph;
        
        private RelationBuilder currentRelation;
        
        private boolean buildArgumentator;
        private boolean buildTarget;
        
        private NodeBuilder currentArgumentator;
        private NodeBuilder currentTarget;
        
        private String tempString;
        
        public SAXParserHelper() {
            this.relGraph = new HashMap<>();
        }
        
        @Override
        public void startElement(String uri, String localName, String qName, 
                Attributes attributes) throws SAXException {
            
            if(PAIR_TAG.equals(qName)) {
                currentGraph = attributes.getValue(TOPIC_ATTR);
                if(!relGraph.containsKey(currentGraph)) {
                    relGraph.put(currentGraph, new DigraphImpl());
                }
                parseRelation(attributes);
            }
        
            if(TARGET_TAG.equals(qName)) {
                int nId = Integer.parseInt(attributes.getValue(ID_ATTR));
                if(relGraph.get(currentGraph).containsNode(nId)) {
                    buildTarget = false;
                    currentRelation.tNode = 
                            relGraph.get(currentGraph).getArgumentNode(nId);
                } else {
                    buildTarget = true;
                    currentTarget = new NodeBuilder();
                    parseNode(attributes, currentTarget);
                }
            }
        
            if(RESPONSE_TAG.equals(qName)) {
                int nId = Integer.parseInt(attributes.getValue(ID_ATTR));
                if(relGraph.get(currentGraph).containsNode(nId)) {
                    buildArgumentator = false;
                    currentRelation.aNode = 
                            relGraph.get(currentGraph).getArgumentNode(nId);
                } else {
                    buildArgumentator = true;
                    currentArgumentator = new NodeBuilder();
                    parseNode(attributes, currentArgumentator);
                }
            }
        }
        
        @Override
        public void characters(char[] ac, int i, int j) {
            tempString = new String(ac, i, j);
        }
        
        @Override
        public void endElement(String uri, String localName, String qName) {
            if(PAIR_TAG.equals(qName)) {
                ArgumentRelation ar;
                ArgumentNode argumentator;
                ArgumentNode target;
                RelationDigraph graph = relGraph.get(currentGraph);
                try {
                    if(buildArgumentator) {
                        argumentator = currentArgumentator.build();
                        graph.addArgumentNode(argumentator);
                        currentRelation.aNode = argumentator;
                    } else {
                        argumentator = currentRelation.aNode;
                    }
                    if(buildTarget) {
                        target = currentTarget.build();
                        graph.addArgumentNode(target);
                        currentRelation.tNode = target;
                    } else {
                        target = currentRelation.tNode;
                    }
                    ar = currentRelation.build();
                    argumentator.addTargetRelation(ar);
                    target.addReplyRelation(ar);
                } catch (AlreadyExistingNodeException aenex) {
                    System.err.println("Error already existing node");
                }
                currentRelation = null;
                currentArgumentator = currentTarget = null;
            }
            
            if(TARGET_TAG.equals(qName)) {
                if(buildTarget) {
                    currentTarget.nodeText = tempString;
                }
            }
            
            if(RESPONSE_TAG.equals(qName)) {
                if(buildArgumentator) {
                    currentArgumentator.nodeText = tempString;
                }
            }
        }
        
        public Map<String, RelationDigraph> getParsingResults() {
            return this.relGraph;
        }
        
        private void parseRelation(Attributes attr) {
            currentRelation = new RelationBuilder();
            currentRelation.relationId = 
                    Integer.parseInt(attr.getValue(ID_ATTR));
            String entailment = attr.getValue(ENTL_ATTR);
            if(null != entailment) switch (entailment) {
                case VALUE_YES:
                    currentRelation.relType = RelationType.SUPPORT;
                    break;
                case VALUE_NO:
                    currentRelation.relType = RelationType.ATTACK;
                    break;
                default:
                    currentRelation.relType = RelationType.UNKNOWN;
                    break;
            }
        }
        
        private void parseNode(Attributes attr, NodeBuilder nb) {
            nb.nodeId = Integer.parseInt(attr.getValue(ID_ATTR));
            String weight = attr.getValue(WEIGHT_ATTR);
            if(weight == null) {
                nb.nodeWeight = 1;
            } else {
                nb.nodeWeight = Integer.parseInt(attr.getValue(WEIGHT_ATTR));
            }
        }
    }
    
    /**
     * Parses a given XML file and returns a Map containing all RelationDigraph
     * objects found in it.
     * 
     * @param uri the path of the XML file to parse
     * @return a Map with all RelationDigraph objects parsed from the file
     * 
     * @throws IllegalArgumentException if the path doesn't correspond to a 
     * valid XML file.
     */
    @Override
    public Map<String, RelationDigraph> parseInput(String uri) {
        File input = new File(uri);
        if(input.canRead()) { // ADD FILE EXTENSION CHECK
            return parseFile(input);
        } else {
            throw new IllegalArgumentException("URI doens't "
                    + "correspond to a valid file path");
        }
    }
    
    private Map<String, RelationDigraph> parseFile(File input) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParserHelper helper = new SAXParserHelper();
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(input, helper);
        } catch (ParserConfigurationException pcex) { //ADD CLEARER EXCEPTION 
            System.err.println("Parser config exception: " + pcex.getMessage());
        } catch (SAXException saex) {
            System.err.println("SAX Exception: " + saex.getMessage());
        } catch (IOException ioex) {
            System.err.println("IO Exception: " + ioex.getMessage());
        }
        return helper.getParsingResults();
    }

}
