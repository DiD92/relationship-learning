package org.relationlearn.util;

import weka.core.Attribute;
import weka.core.FastVector;

/**
 * Utility class used to represent the default class Attribute to be used in
 * classifier training or instance classificaction.
 */
public class RelationClass {
    
    /**
     * Numeric type attribute with values equal to the ones found in
     * {@link org.relationlearn.util.RelationType}.
     * 
     */
    public final static Attribute DEFAULT_CLASS;
    
    static {
        FastVector values = new FastVector(3);
        values.addElement(RelationType.ATTACK.toString());
        values.addElement(RelationType.SUPPORT.toString());
        values.addElement(RelationType.UNKNOWN.toString());
        DEFAULT_CLASS = new Attribute("default-class", values);
    }
    
    private RelationClass() {}

}
