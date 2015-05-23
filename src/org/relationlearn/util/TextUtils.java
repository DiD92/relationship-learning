package org.relationlearn.util;

import com.aliasi.tokenizer.RegExTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import java.util.regex.Pattern;

/**
 * Utility class used to manipulate text in order to aid any TextFilter
 * implementation in its job if needed.
 * 
 * @see org.relationlearn.filters.TextFilter
 */
public class TextUtils {
    
    /**
     * Pattern compiled with the regex "\w+".
     */
    public static final Pattern WORDS_PATTERN = Pattern.compile("\\w+");
    
    private static final TokenizerFactory TOKENIZER;
    
    static {
        TOKENIZER = new RegExTokenizerFactory(WORDS_PATTERN);
    }
    
    private TextUtils() {}
    
    /**
     * Splits a unique String containing a text into and array of Strings
     * that contain words, using the Java regex {@code \w+}.
     * 
     * @param text the original text to split
     * @return the input text splitted in an more easy to manipulate way for
     * TextFilter implementations
     * 
     * @see org.relationlearn.filters.TextFilter
     */
    public static String[] getWordsFromText(String text) {
        return getTokensFromTextUsingFactory(text, TOKENIZER);
    }
    
    /**
     * Splits a unique String into tokens using the Pattern provided by 
     * {@code p}.
     * 
     * @param text the original text to tokenize
     * @param p the Patter to use for tokenization
     * @return the input text splitted in tokens using {@code p}
     * 
     * @see Pattern
     */
    public static String[] getTokensFromTextUsingRegex(String text, Pattern p) {
        TokenizerFactory tf = new RegExTokenizerFactory(p);
        return getTokensFromTextUsingFactory(text, tf);
    }
    
    /**
     * Splits a unique String into tokens using the TokenizerFactory
     * {@code factory}.
     * 
     * @param text the original text to tokenize
     * @param factory the TokenizerFactory to use for tokenization
     * @return the input text tokenized using the TokenizerFactory 
     * {@code factory}
     * 
     * @see com.aliasi.tokenizer.TokenizerFactory
     */
    public static String[] getTokensFromTextUsingFactory(String text,
            TokenizerFactory factory) {
        char[] ch = text.toCharArray();
        return factory.tokenizer(ch, 0, ch.length).tokenize();
    }

}
