package org.relationlearn.util;

import java.util.regex.Pattern;

/**
 *
 * @author Dídac
 */
public class TextUtils {
    
    private static final Pattern DELIMITERS = Pattern.compile("\\W");
    
    private TextUtils() {}
    
    public static String[] getWordsFromText(String text) {
        return DELIMITERS.split(text);
    }

}
