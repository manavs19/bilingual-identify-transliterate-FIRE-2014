package LanguageIdentification;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author in dia
 */
public class DictionaryWords {
    Set<String> wordSet;
    public DictionaryWords(String path) {
        wordSet = new HashSet<>(100);
        File langFile = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(langFile));
            String line;
            while((line = reader.readLine()) != null) {
                String[] words = line.split(" ");
                for(String s: words) {
                   wordSet.add(s.toLowerCase());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CommonWords.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(CommonWords.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public boolean contains(String word) {
        return wordSet.contains(word);
    }
}
