package LanguageIdentification;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
public class CommonWords {
    Map<String, Integer> langMap;
    public CommonWords() {
        String[] commonwords = {"main","tum","hum","tu","fir","se", "kya", "kyu", "kab"};
        langMap = new HashMap<>();
        for(String s: commonwords) {
            langMap.put(s, 1);
        }
    }
    
    public CommonWords(File langFile) {
        BufferedReader reader = null;
        langMap = new HashMap<>();
        try {
            reader = new BufferedReader(new FileReader(langFile));
            String line;
            while((line = reader.readLine()) != null) {
                String[] words = line.split(" ");
                for(String s: words) {
                    langMap.put(s, 1);
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
    
    public boolean contains(String test) {
        return langMap.containsKey(test);
    }
}
