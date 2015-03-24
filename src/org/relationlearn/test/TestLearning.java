package org.relationlearn.test;

import java.util.Arrays;
import org.relationlearn.util.TextUtils;

/**
 *
 * @author Dídac
 */
public class TestLearning {
    
    private static final String TEST_TEXT = "Random alcohol tests are more effective than alternative measures. \""
            + "The federal Justice Department of Canada moved to implement Random Breath Testing (RBT), \""
            + "concluding: \"a system of random checks is more effective "
            + "than a combination of other measures such as a lower "
            + "threshold for blood alcohol level and more frequent RIDE checkpoints\"";
    
    public static void main(String args[]) {
        System.out.println("ORIGINAL: " + TEST_TEXT);
        System.out.println("PARSED: " + Arrays.toString(TextUtils.getWordsFromText(TEST_TEXT)));
    }

}
