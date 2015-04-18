package org.relationlearn.util;

import java.util.regex.Pattern;

/**
 * Utility class used to manipulate text in order to aid any TextFilter
 * implementation in its job if needed.
 * 
 * @see org.relationlearn.filters.TextFilter
 */
public class TextUtils {
    
    private static final Pattern DELIMITERS = Pattern.compile("\\W");
    
    private TextUtils() {}
    
    /**
     * Splits a unique String containing a text into and array of Strings
     * that contain words, punctuation marks, and special characters separated.
     * 
     * @param text the original text to split
     * @return the input text splitted in an more easy to manipulate way for
     * TextFilter implementations
     * 
     * @see org.relationlearn.filters.TextFilter
     */
    public static String[] getWordsFromText(String text) {
        return DELIMITERS.split(text);
    }

}
