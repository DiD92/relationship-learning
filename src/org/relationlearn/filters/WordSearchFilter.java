package org.relationlearn.filters;

import weka.core.Attribute;
import weka.core.FastVector;

/**
 * An implementation of TextFilter which checks if the text given contains a
 * certain word or not.
 */
public class WordSearchFilter implements TextFilter {
    
    private enum AttributeValues { NO, YES; }
    
    private final FastVector ATTR_VEC;
    private final Attribute FILTER_ATTR;
    
    private final String WORD_TO_SEARCH;
    
    private WordSearchFilter() {
        this("word-search", "null");
    }
    
    /**
     * Constructs a CommonWordsFilter with the Attribute name set
     * to "word-search" and the word to search set to {@code word}.
     * 
     * @param word the word to be searched in the texts passed to the
     * filter
     * 
     */
    public WordSearchFilter(String word) {
        this("word-search", word);
    }
    
    /**
     * Constructs a CommonWordsFilter with the Attribute name set
     * to {@code name} and the word to search set to {@code word}.
     * 
     * @param name the name tihs TextFilter Attribute will have
     * @param word the word to be searched in the texts passed to the
     * filter
     * 
     */
    public WordSearchFilter(String name, String word) {
        ATTR_VEC = new FastVector(2);
        fillVector(ATTR_VEC);
        this.FILTER_ATTR = new Attribute(name, ATTR_VEC);
        this.WORD_TO_SEARCH = word;
    }
    
    @Override
    public Attribute getMappedAttribute() {
        return FILTER_ATTR;
    }
    
    @Override
    public double filter(String r, String h) {
        if(r != null && h != null) {
            if(r.contains(WORD_TO_SEARCH) || h.contains(WORD_TO_SEARCH)) {
                return ATTR_VEC.indexOf(AttributeValues.YES.toString());
            } else {
                return ATTR_VEC.indexOf(AttributeValues.NO.toString());
            }
        } else {
            return ATTR_VEC.indexOf(AttributeValues.NO.toString());
        }
    }
    
    private void fillVector(FastVector fv) {
        for(AttributeValues val : AttributeValues.values()) {
            fv.addElement(val.toString());
        }
    }

}
