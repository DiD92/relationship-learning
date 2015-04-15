package org.relationlearn.filters;

import weka.core.Attribute;

/**
 * The TextFilter interface represents an object capable of obtaining a 
 * certain information from a pair of text Strings and map them to a 
 * given Attribute to be used when creating Instance objects.
 * 
 * @see weka.core.Attribute
 * @see weka.core.Instance
 * @see FilterGroup
 */
public interface TextFilter {
    
    /**
     * Returns the Attribute that this filter provides a value for.
     * 
     * @return the Attribute this filter gives a value to
     */
    public Attribute getMappedAttribute();
    
    /**
     * Filters the corresponding pair of String values and returns its
     * value in the weka internal floating-point format.
     * <p/>
     * The method return type is able to support all types of 
     * Attribute that exist, with a single method
     * for all possible TextFilter implementations.
     * 
     * @param r Response text in the argumentation
     * @param h Hypothesis text in the argumentation
     * 
     * @return the value of this filter Attribute in weka internal 
     * floating-point format
     * 
     * @see weka.core.Instance#setValue(Attribute,double)
     */
    public double filter(String r, String h);

}
