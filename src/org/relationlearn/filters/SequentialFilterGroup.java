package org.relationlearn.filters;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.relationlearn.util.RelationClass;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Implementation of FilterGroup which applies the TextFilter objects 
 * to the input in the same order they were added to group and in a 
 * sequential manner.
 * 
 * <p>
 * <h3>Configuration file options</h3>
 * Apart adding filters through code you can also supply an XML file
 * which will contain your desired filter configuration to be used
 * wihtout adding them manually by code.<br/> The code
 * below is an example of all the possible input formats:
 * </p>
 * <pre>
 * {@code
 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
 * <filters>
 *     <filter id="1" local="yes" relative="no" 
 *         classpath="file:/C:/your/absolute/path/here">
 *     class.fully.qualified.name
 *     </filter>
 *     <filter id="4" local="yes" relative="yes" 
 *         classpath="your/relative/path/here">
 *      class.fully.qualified.name
 *      <filter_params>
 *          <param>your_param_value_here</param>
 *      <filter_params>
 *     </filter>
 *     <filter id="3" local="no" classpath="your_url_here">
 *      class.fully.qualified.name
 *     </filter>
 * </filters>
 * }
 * </pre>
 * <p>
 * You can basically have two types of filter configurations: local filters and
 * remote filters. Local filters can then be found using a relative path or
 * an absolute path, both Windows and Linux path separators work directly with 
 * this system, so only  you need to input the path the same way the 
 * system represents it.
 * <p/>
 * <p>
 * For remote paths your path is the URL addres where the base directory for
 * the sources or the .jar file are located.
 * </p>
 * <p> 
 * The id attribute only defines the priority of the filter being
 * {@link java.lang.Integer#MIN_VALUE} the one with the hightest priority and
 * {@link java.lang.Integer#MAX_VALUE} the value with less priority. Although
 * its recommended that you begin identifying your filters with the value 1 
 * for the first, 2 for the second and so on.
 * </p>
 * <p>
 * Additionally if the TextFilter you want to load accepts additional parameters
 * , you can supply them inside the {@code filter_params} tag as shown in the
 * example with {@code id=4}.
 * </p>
 * <p>
 * <b>Important Note:</b> Keep in mind that the only parameters you can 
 * supply to the TextFilter using a config file will be of type 
 * {@link java.lang.String} so build your TextFilter with that in mind. 
 * For example you could provide a constructor that accepts a 
 * {@link java.util.List} and the implement another
 * one which accepts as paramter the path to that same list serialized in a 
 * file for its use with this configuration file.
 * </p>
 * 
 * @see java.net.URLClassLoader
 * @see FilterGroup
 * @see TextFilter
 */
public class SequentialFilterGroup implements FilterGroup {
    
    private static class FilterItem {
        
        public final TextFilter FILTER;
        public final Integer ID;
        
        private FilterItem() {
            this(null, -1);
        }
        
        public FilterItem(TextFilter filter, Integer id) {
            this.FILTER = filter;
            this.ID = id;
        }
    }
    
    private static class ItemComparator implements Comparator<FilterItem> {

        @Override
        public int compare(FilterItem item1, FilterItem item2) {
            return item1.ID.compareTo(item2.ID);
        }
        
    }
    
    private class SAXParserHelper extends DefaultHandler {
        
        private final String FILTER_TAG = "filter";
        private final String FLTPR_TAG = "filter_params";
        private final String PARAM_TAG = "param";
        
        private final String ID_ATTR = "id";
        private final String TYPE_ATTR = "type";
        private final String NAME_ATTR = "name";
        private final String LOCAL_ATTR = "local";
        private final String RELATIVE_ATTR = "relative";
        private final String CLASSPATH_ATTR = "classpath";
        
        private final String VALUE_NO = "no";
        private final String VALUE_YES = "yes";
        
        private final PriorityQueue<FilterItem> ITEM_QUEUE;
        private final List<TextFilter> PARSED_FILTERS;
        
        private int filterOrder;
        private String classPath;
        
        private String currentClass;
        private String currentValue;
        
        private final List<String> filterParams;
        
        public SAXParserHelper() {
            this.ITEM_QUEUE = new PriorityQueue<>(11, new ItemComparator());
            this.PARSED_FILTERS = new LinkedList<>();
            this.filterParams = new ArrayList<>();
        }
        
        @Override
        public void startElement(String uri, String localName, String qName, 
                Attributes attributes) throws SAXException {
            
            switch (qName) {
                case FILTER_TAG:
                    parseFilterAttributes(attributes);
                    break;
                case FLTPR_TAG:
                    currentClass = currentValue;
                    filterParams.clear();
                    break;
                default:
                    break;
            }
        }
        
        @Override
        public void characters(char[] ac, int i, int j) {
            currentValue = new String(ac, i, j).trim();
        }
        
        @Override
        public void endElement(String uri, String localName, String qName) 
                throws SAXException {
            switch (qName) {
                case FILTER_TAG:
                    TextFilter filter = tryLoadFilter(classPath, filterParams);
                    FilterItem item = new FilterItem(filter, filterOrder);
                    ITEM_QUEUE.add(item);
                    break;
                case PARAM_TAG:
                    filterParams.add(currentValue);
                    break;
                default:
                    break;
            }
        }
        
        public List<TextFilter> getParsingResults() {
            if(ITEM_QUEUE.size() > 0) {
                while(ITEM_QUEUE.size() > 0) {
                    PARSED_FILTERS.add(ITEM_QUEUE.poll().FILTER);
                }
            }
            return this.PARSED_FILTERS;
        }
        
        private void parseFilterAttributes(Attributes attr) 
                throws SAXException {
            String id, name, local, relative, classpath;
            id = attr.getValue(ID_ATTR);
            name = attr.getValue(NAME_ATTR);
            local = attr.getValue(LOCAL_ATTR);
            relative = attr.getValue(RELATIVE_ATTR);
            classpath = attr.getValue(CLASSPATH_ATTR);
            if((id != null) && (local != null) && (classpath != null)) {
                filterOrder = Integer.valueOf(id);
                switch(local) {
                    case VALUE_YES:
                        if(null != relative) {
                            switch (relative) {
                                case VALUE_YES:
                                    classPath = convertRelativePath(classpath);
                                    break;
                                case VALUE_NO:
                                    classPath = classpath;
                                    break;
                                default:
                                    throw new SAXException("Error: relative "
                                        + "attribute must be set when local "
                                        + "attribute value is set to {yes} !");
                            }
                        } else {
                            throw new SAXException("Error: relative attribute "
                                    + "value must be {yes} or {no} !"); 
                        }
                        break;
                    case VALUE_NO:
                        classPath = classpath;
                        break;
                    default:
                        throw new SAXException("Error: local attribute values "
                                + "must be {yes} or {no}");
                }
            } else {
                throw new SAXException("Error: filter tag is missing "
                        + "one or more mandatory attributes!");
            }
        }
        
        private String convertRelativePath(String relPath) throws SAXException {
            String absPath;
            File f = new File(relPath);
            absPath = f.toURI().normalize().toString();
            return absPath;
        }
        
        private TextFilter tryLoadFilter(String path, List<String> params) 
                throws SAXException {
            TextFilter tf = null;
            Class[] paramTypes;
            String[] paramValues;
            try {
                URL[] urls = { new URL(path) };
                URLClassLoader ucl = new URLClassLoader(urls);
                Class<TextFilter> c;
                c = (Class<TextFilter>) ucl.loadClass(currentClass)
                        .asSubclass(TextFilter.class);
                Constructor<TextFilter> con;
                if(params.size() > 0) {
                    paramTypes = new Class[params.size()];
                    Arrays.fill(paramTypes, String.class);
                    paramValues = new String[params.size()];
                    paramValues = params.toArray(paramValues);
                    con = c.getConstructor(paramTypes);
                    tf = con.newInstance((Object[]) paramValues); 
                } else {
                    con = c.getConstructor();
                    tf = con.newInstance();
                }
                ucl.close();
            } catch (MalformedURLException muex) {
                throw new SAXException("Error: path doesn't correspond to a "
                        + "valid system path/URL");
            } catch (ClassNotFoundException | NoSuchMethodException | 
                    SecurityException | InstantiationException | 
                    IllegalAccessException | IllegalArgumentException | 
                    InvocationTargetException | IOException ex) {
                throw new SAXException(ex);
            }
            return tf;
        }
    }
    
    private final String GROUP_DATASET;
    private final FastVector FILTER_ATTRS;
    private final List<TextFilter> FILTERS;
    
    private Attribute classAttr;
    
    private Instances filterDataset;
    
    private boolean changedFilter;
    
    /** 
     * Constructs a SequentialFilterGroup with the 
     * dataset name "test-dataset" without any filter
     * added.
     */
    public SequentialFilterGroup() {
        this("test-dataset");
    }
    
    /**
     * Constructs a SequentialFilterGroup with the dataset name
     * {@code dataset} without any filter added.
     * 
     * @param dataset the name of this SequentialFilterGroup dataset
     */
    public SequentialFilterGroup(String dataset) {
        this.GROUP_DATASET = dataset;
        this.FILTER_ATTRS = new FastVector();
        this.FILTERS = new ArrayList<>();
        this.filterDataset = new Instances(dataset, FILTER_ATTRS, 0);
        this.changedFilter = true;
    }
    
    /**
     * Constructs a SequentialFilterGroup with the dataset name
     * {@code dataset}, and, using the information found in
     * {@code configFile}, loads all specified TextFilter objects and
     * adds them to this SequentialFilterGroup.
     * 
     * @param dataset the name of this SequentialFilterGroup dataset
     * @param configFile the XML file whioh contains the filter configuration
     * to load onto this SequentialFilterGroup
     */
    public SequentialFilterGroup(String dataset, File configFile) {
        this(dataset);
        loadFiltersFromFile(configFile);
    }
    
    @Override
    public String getGroupDatasetName() {
        return GROUP_DATASET;
    }
    
    @Override
    public Instances getGroupDataset() {
        buildInstances();
        return new Instances(filterDataset); 
    }

    @Override
    public FastVector getGroupAttributes() {
        return FILTER_ATTRS;
    }

    @Override
    public Instance createInstanceUsingFilters(String r, String h) {
        buildInstances();
        Instance instance = new Instance(filterDataset.numAttributes());
        for(TextFilter filter : FILTERS) {
            instance.setValue(filter.getMappedAttribute(), 
                    filter.filter(r, h));
        }
        instance.setDataset(filterDataset);
        return instance;
    }

    @Override
    public void addFilter(TextFilter filter) {
        changedFilter = true;
        FILTER_ATTRS.addElement(filter.getMappedAttribute());
        FILTERS.add(filter);
    }
    
    /**
     * Adds the Attribute {@code attr} as the Instances class attribute
     * or sets the Instances class value to 
     * {@link org.relationlearn.util.RelationClass#DEFAULT_CLASS}.
     * 
     * @param attr the Attribute to be used as class attribute or null
     */
    @Override
    public void addClassAttribute(Attribute attr) {
        if(attr == null) {
            this.classAttr = RelationClass.DEFAULT_CLASS;
        } else {
            this.classAttr = attr;
        }
    }
    
    private void buildInstances() {
        if(changedFilter) {
            FastVector completeAttr = new FastVector(FILTER_ATTRS.size() + 1);
            completeAttr.appendElements(FILTER_ATTRS);
            completeAttr.addElement(classAttr);
            filterDataset = new Instances(GROUP_DATASET, completeAttr, 0);
            filterDataset.setClassIndex(completeAttr.size() - 1);
            changedFilter = false;
        }
    }
    
    private void loadFiltersFromFile(File configFile) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParserHelper helper = new SAXParserHelper();
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(configFile, helper);
        } catch (ParserConfigurationException | SAXException | 
                IOException mex) {
            throw new IllegalArgumentException(mex);
        }
        
        for(TextFilter filter : helper.getParsingResults()) {
            this.addFilter(filter);
        }
    }

}
