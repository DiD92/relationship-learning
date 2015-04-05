package org.relationlearn.filters;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Dídac
 */
public class SequentialFilterGroupTest {
    
    private static class TextFilterDouble implements TextFilter {
        
        private final Attribute attr;
        
        public TextFilterDouble() {
            attr = new Attribute("test");
        }

        @Override
        public Attribute getMappedAttribute() {
            return attr;
        }

        @Override
        public double filter(String r, String h) {
            return 0.0;
        }
        
    }
    
    private static TextFilterDouble filter;
    
    @BeforeClass
    public static void setUp() {
        filter = new TextFilterDouble();
    }

    /**
     * Test of getGroupAttributes method, of class SequentialFilterGroup.
     */
    @Test
    public void testGetGroupAttributesEmpty() {
        SequentialFilterGroup instance = new SequentialFilterGroup();
        FastVector result = instance.getGroupAttributes();
        assertTrue(result.size() == 0);
    }
    
    /**
     * Test of getGroupAttributes method with one element, of class 
     * SequentialFilterGroup.
     */
    @Test
    public void testGetGroupAttributesNotEmpty() {
        SequentialFilterGroup instance = new SequentialFilterGroup();
        instance.addFilter(filter);
        FastVector result = instance.getGroupAttributes();
        assertTrue(result.size() == 1);
    }

    /**
     * Test of createInstanceUsingFilters method, of class SequentialFilterGroup.
     */
    @Test
    public void testCreateInstanceUsingFilters() {
        String str = "";
        SequentialFilterGroup instance = new SequentialFilterGroup();
        instance.addFilter(filter);
        // Generating instance for comparsion
        Attribute test = new Attribute("test");
        FastVector fv = new FastVector();
        fv.addElement(test);
        Instances insts = new Instances("test-dataset", fv, 0);
        Instance ins = new Instance(1);
        ins.setValue(test, 0.0);
        ins.setDataset(insts);
        // Generation instance using filters
        Instance result = instance.createInstanceUsingFilters(str, str);
        assertTrue( ins.attribute(0).equals(result.attribute(0))
                & (ins.value(0) == result.value(0)));         
    } 
}
