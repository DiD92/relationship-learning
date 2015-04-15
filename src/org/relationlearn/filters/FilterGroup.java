package org.relationlearn.filters;

import weka.core.FastVector;
import weka.core.Instance;

/**
 * The FilterGroup interface defines a container for an undefined amount of
 * TextFilters allowing the generation of weka 
 * {@link weka.core.Instance Instance} objects with as many
 * attributes as the number of TextFilter objects present in the group.
 * 
 * @see TextFilter
 * @see weka.core.Instance
 */
public interface FilterGroup {
    
    /**
     * Returns the dataset name for this FilterGroup.
     * 
     * @return the dataset name of this FilterGroup
     */
    public String getGroupDatasetName();
    
    /**
     * Returns a FastVector instance containing all the Attribute elements
     * of this FilterGroup, this Attribute elements are the one's each Instace
     * generated with the method createInstanceUsingFilters must have.
     * 
     * @return a FastVector containing all the attributes of the FilterGroup
     * 
     * @see weka.core.Attribute
     * @see weka.core.FastVector
     */
    public FastVector getGroupAttributes();
    
    /**
     * Generates an Instance object by applying all the TextFilter elements
     * contained in the FilterGroup to the input parameters provided.
     * 
     * @param r Response text in the argumentation
     * @param h Hypothesis text in the argumentation
     * @return an Instance with one Attribute for each TextFilter in the
     * FilterGroup
     * 
     * @see weka.core.Instance
     */
    public Instance createInstanceUsingFilters(String r, String h);
    
    /**
     * Adds a new TextFilter to the FilterGroup.
     * 
     * @param filter the TextFilter to be added to this FilterGroup
     * 
     * @see TextFilter
     */
    public void addFilter(TextFilter filter);

}
