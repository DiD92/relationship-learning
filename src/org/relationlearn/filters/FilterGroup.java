package org.relationlearn.filters;

import weka.core.FastVector;
import weka.core.Instance;

/**
 *
 * @author Dídac
 */
public interface FilterGroup {
    
    public String getGroupDatasetName();
    public FastVector getGroupAttributes();
    public Instance createInstanceUsingFilters(String r, String h);
    public void addFilter(TextFilter filter);

}
