package org.relationlearn.test;

import java.io.File;
import org.relationlearn.filters.FilterGroup;
import org.relationlearn.filters.SequentialFilterGroup;
import weka.core.Attribute;

public class TestFilterLoader {
    
    public static String FILE_PATH = "E:\\Bibliotecas\\Documents\\NetBeansProjects\\RelationLearn\\test_files\\filter_config\\test-config.xml";
    
    public static void main(String args[]) {
        File f = new File(FILE_PATH);
        FilterGroup fg = new SequentialFilterGroup("test", f);
        System.out.println(fg.getGroupAttributes().size());
        System.out.println(fg.getGroupDatasetName());
        Attribute a = (Attribute) fg.getGroupAttributes().firstElement();
        System.out.println(a.name());
        Attribute b = (Attribute) fg.getGroupAttributes().elementAt(1);
        System.out.println(b.name());
        Attribute c = (Attribute) fg.getGroupAttributes().elementAt(2);
        System.out.println(c.name());
        Attribute d = (Attribute) fg.getGroupAttributes().elementAt(3);
        System.out.println(d.name());
    }

}
