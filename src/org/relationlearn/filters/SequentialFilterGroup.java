package org.relationlearn.filters;

import java.util.ArrayList;
import java.util.List;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Dídac
 */
public class SequentialFilterGroup implements FilterGroup {
    
    private final String GROUP_DATASET;
    private final FastVector FILTER_ATTRS;
    private final List<TextFilter> FILTERS;
    
    private Instances filterDataset;
    
    private boolean changedFilter;
    
    public SequentialFilterGroup() {
        this("test-dataset");
    }
    
    public SequentialFilterGroup(String dataset) {
        this.GROUP_DATASET = dataset;
        this.FILTER_ATTRS = new FastVector();
        this.FILTERS = new ArrayList<>();
        this.filterDataset = new Instances(dataset, FILTER_ATTRS, 0);
        this.changedFilter = true;
    }
    
    @Override
    public String getGroupDatasetName() {
        return GROUP_DATASET;
    }

    @Override
    public FastVector getGroupAttributes() {
        return FILTER_ATTRS;
    }

    @Override
    public Instance createInstanceUsingFilters(String r, String h) {
        buildInstances();
        Instance instance = new Instance(FILTER_ATTRS.size());
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
    
    private void buildInstances() {
        if(changedFilter) {
            filterDataset = new Instances(GROUP_DATASET, FILTER_ATTRS, 0);
            changedFilter = false;
        }
    }

}
