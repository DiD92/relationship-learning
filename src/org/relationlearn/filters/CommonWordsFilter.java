package org.relationlearn.filters;

import com.aliasi.tokenizer.LowerCaseTokenizerFactory;
import com.aliasi.tokenizer.RegExTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.relationlearn.util.TextUtils;
import weka.core.Attribute;

/**
 * An implementation of TextFilter which returns the ratio of words that
 * appear in both texts compared to the total number of different words found
 * in both texts.
 */
public class CommonWordsFilter implements TextFilter {
    
    private final Attribute FILTER_ATTR;
    
    private final TokenizerFactory TOKENIZER;
    
    /**
     * Constructs a CommonWordsFilter with the Attribute name set
     * to "common-word-ratio".
     * 
     */
    public CommonWordsFilter() {
        this("common-word-ratio");
    }
    
    /**
     * Constructs a CommonWordsFilter with the Attribute name set
     * to {@code name}.
     * 
     * @param name the name this TextFilter Attribute will have
     */
    public CommonWordsFilter(String name) {
        this.FILTER_ATTR = new Attribute(name);
        TokenizerFactory regToken = 
                new RegExTokenizerFactory(TextUtils.WORDS_PATTERN);
        TOKENIZER = new LowerCaseTokenizerFactory(regToken);
    }
    
    @Override
    public Attribute getMappedAttribute() {
        return FILTER_ATTR;
    }
    
    @Override
    public double filter(String r, String h) {
        List<String> rlst, hlst;
        rlst = new ArrayList<>(Arrays.asList(
                TextUtils.getTokensFromTextUsingFactory(r, TOKENIZER)));
        hlst = new ArrayList<>(Arrays.asList(
                TextUtils.getTokensFromTextUsingFactory(h, TOKENIZER)));
        return getCommonWordRatio(rlst, hlst);
    }
    
    private double getCommonWordRatio(List<String> rlst, List<String> hlst) {
        int totalWords = 0, commonWords = 0;
        String word;
        for(int i = 0; i < rlst.size(); i++) {
            word = rlst.get(i);
            if(hlst.contains(word)) {
                i--;
                commonWords++;
                totalWords++;
                while(rlst.remove(word)){}
                while(hlst.remove(word)){}
            }
        }
        totalWords += (rlst.size() + hlst.size());
        if(totalWords > 0) {
            return ((double) commonWords / (double) totalWords);
        } else {
            return 0.0;
        }
    }

}
