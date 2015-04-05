package org.relationlearn.filters;

import org.relationlearn.util.TextUtils;
import weka.core.Attribute;

/**
 *
 * @author Dídac
 */
public class WordRatioFilter implements TextFilter {
    
    private final Attribute FILTER_ATTR;
    
    public WordRatioFilter() {
        this("word-ratio");
    }
    
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
