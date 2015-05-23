package org.relationlearn.filters;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.List;
import org.relationlearn.util.TextUtils;
import weka.core.Attribute;

/**
 * An implementation of TextFilter which returns the ratio of words that
 * have been found of the total amount of words specified in a given word
 * list.
 */
public class WordOcurrenceFilter implements TextFilter {
    
    private final Attribute FILTER_ATTR;
    
    private final List<String> WORDS_LIST;
    
    private WordOcurrenceFilter() {
        this.FILTER_ATTR = null;
        this.WORDS_LIST = null;
    }
    
    /**
     * Constructs a WordOcurrenceFilter with the Attribute name set
     * to {@code name} and using the serialized list of words found
     * in {@code path} as word list.
     * 
     * @param name the name this TextFilter Attribute will have
     * @param path the path to the serialized list of words
     */
    public WordOcurrenceFilter(String name, String path) {
        List<String> words = getWordsFromFile(path);
        this.FILTER_ATTR = new Attribute(name);
        this.WORDS_LIST = words;
    }
    
    /**
     * Constructs a WordOcurrenceFilter with the Attribute name set
     * to {@code name} and using the list of words found
     * in {@code words} as word list.
     * 
     * @param name the name this TextFilter Attribute will have
     * @param words the list of words to use
     */
    public WordOcurrenceFilter(String name, List<String> words) {
        this.FILTER_ATTR = new Attribute(name);
        this.WORDS_LIST = words;
    }
    
    @Override
    public Attribute getMappedAttribute() {
        return FILTER_ATTR;
    }
    
    @Override
    public double filter(String r, String h) {
        List<String> rlst = Arrays.asList(TextUtils.getWordsFromText(r));
        List<String> hlst = Arrays.asList(TextUtils.getWordsFromText(h));
        int foundWords = 0, totalWords = WORDS_LIST.size();
        for(String word : WORDS_LIST) {
            if(rlst.contains(word) || hlst.contains(word)) {
                foundWords++;
            }
        }
        return ((double) foundWords / (double) totalWords);
    }
    
    private List<String> getWordsFromFile(String path) {
        List<String> words;
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(new FileInputStream(path));
            words = (List<String>) ois.readObject();
            ois.close();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Error in file: " + 
                    ex.getMessage());
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Error in class cast: " + 
                    ex.getMessage());
        }
        return words;
    }

}
