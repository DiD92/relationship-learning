package org.relationlearn.filters;

import weka.core.Attribute;

/**
 *
 * @author Dídac
 */
public interface TextFilter {
    
    public Attribute getMappedAttribute();
    public double filter(String r, String h);

}
