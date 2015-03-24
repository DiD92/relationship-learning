package org.relationlearn.classifiers;

import java.io.File;
import org.relationlearn.exception.ClassifierNotTrainedException;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

/**
 *
 * @author Dídac
 */
public class SVMClassifier {
    
    private final LibSVM SVM;
    private Instances TRAIN_INS;
    
    public SVMClassifier() {
        SVM = new LibSVM();
    }
    
    public SVMClassifier(File model) throws Exception {
        Object objs[] = SerializationHelper.readAll(model.getAbsolutePath());
        SVM = tryCastObject(objs[0], LibSVM.class);
        TRAIN_INS = tryCastObject(objs[1], Instances.class);
    }
    
    public void setOptions(String options[]) throws Exception {
        this.SVM.setOptions(options);
    }
    
    public void trainClassifier(Instances trainData) throws Exception {
        SVM.buildClassifier(trainData);
        TRAIN_INS = trainData;
    }
    
    public void storeModel(String path) throws Exception {
        Object objs[] = new Object[2];
        objs[0] = SVM;
        objs[1] = TRAIN_INS;
        SerializationHelper.writeAll(path, objs);
    }
    
    public double classifyInstance(Instance instance) throws Exception {
        if(TRAIN_INS != null) {
            return SVM.classifyInstance(instance);
        } else {
            throw new ClassifierNotTrainedException();
        }
    }
    
    public double[] testClassifier(Instances test) throws Exception {
        if(TRAIN_INS != null) {
            Evaluation eval = new Evaluation(TRAIN_INS);
            return eval.evaluateModel(SVM, test);
        } else {
            throw new ClassifierNotTrainedException();
        }
    }
    
    private <T> T tryCastObject(Object objs, Class<T> type) 
            throws IllegalArgumentException {
        if(!type.isInstance(objs)) {
            throw new IllegalArgumentException("File is not a correct model");
        } else {
            return type.cast(objs);
        }
        
    }

}
