package org.relationlearn.filters;

import java.util.regex.Pattern;
import org.relationlearn.util.TextUtils;
import weka.core.Attribute;

/**
 * An implementation of TextFilter which counts the amount of '!' characters
 * found in both input texts.
 */
public class PunctuationCounterFilter implements TextFilter {
    
    private final Attribute FILTER_ATTR;
    
    private final Pattern PATTERN = Pattern.compile("!");
    
    /**
     * Constructs a PuncuationCounterFilter with the Attribute name set
     * to "punct-counter".
     * 
     */
    public PunctuationCounterFilter() {
        this("punct-counter");
    }
    
    /**
     * Constructs a PuncuationCounterFilter with the Attribute name set
     * to {@code name}.
     * 
     * @param name the name this TextFilter Attribute will have
     * 
     */
    public PunctuationCounterFilter(String name) {
        this.FILTER_ATTR = new Attribute(name);
    }
    
    @Override
    public Attribute getMappedAttribute() {
        return FILTER_ATTR;
    }
    
    @Override
    public double filter(String r, String h) {
        int rc = TextUtils.getTokensFromTextUsingRegex(r, PATTERN).length;
        int hc = TextUtils.getTokensFromTextUsingRegex(h, PATTERN).length;
        return (rc + hc);
    }

}
