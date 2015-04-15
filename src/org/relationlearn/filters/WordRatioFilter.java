package org.relationlearn.filters;

import org.relationlearn.util.TextUtils;
import weka.core.Attribute;

/**
 * An implementation of TextFilter which returns the division between
 * the number of words in the hypothesis text and the response text.
 */
public class WordRatioFilter implements TextFilter {
    
    private final Attribute FILTER_ATTR;
    
    /**
     * Constructs a WordRatioFilter with the Attribute name
     * "word-ratio".
     */
    public WordRatioFilter() {
        this("word-ratio");
    }
    
    /**
     * Constructs a WordRatioFilter with the Attribute name
     * {@code name}.
     * 
     * @param name 
     */
    public WordRatioFilter(String name) {
        this.FILTER_ATTR = new Attribute(name);
    }

    @Override
    public Attribute getMappedAttribute() {
        return FILTER_ATTR;
    }

    @Override
    public double filter(String r, String h) {
        double response_words = TextUtils.getWordsFromText(r).length;
        double hypothesis_words = TextUtils.getWordsFromText(h).length;
        return (hypothesis_words / response_words);
    }

}
