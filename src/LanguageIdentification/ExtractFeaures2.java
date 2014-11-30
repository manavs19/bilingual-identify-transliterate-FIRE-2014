package LanguageIdentification;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author in dia
 */
public class ExtractFeaures2 {
    Pipe pipe;
    public ExtractFeaures2(Pipe pipe) {
        this.pipe = pipe;
    }
    
    public List<Instance> createFeatureVectors (File inputFile) {
        List<Instance> features = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String line;
            int wordIndex = 0;
            while((line = reader.readLine()) != null)
            {
               //System.out.println(line);
               int lineWordIndex = 0;
               Pattern p = Pattern.compile("([a-zA-Z0-9]+)\\\\([EH])");
               Matcher m = p.matcher(line);
               while(m.find()) {
                   List<String> feat = new ArrayList<>();
                   String word = m.group(1);
                   String label = m.group(2);
                   int ng = 1;
                   while(ng <= 5 && ng <= word.length()){
                       feat.addAll(CharNGramExtractor.getGrams(word, ng));
                       ng++;
                   }
                   if(word.length() > 5){
                       feat.add(word);
                   }
                   
                   StringBuilder featur = new StringBuilder();
                   for(String f: feat) {
                       featur.append(f);
                       featur.append(" ");
                   }
                   String feature = featur.toString();
                   //System.out.println(feature);
                   features.add(new Instance(feature, label, word, null));
               }
               
            }
            return features;
        } catch (IOException ex) {
            Logger.getLogger(ExtractFeaures.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<String> Features(String word) {
        
        List<String> feat = new ArrayList<>();
        int ng = 1;
        while(ng <= 5 && ng <= word.length()){
            feat.addAll(CharNGramExtractor.getGrams(word, ng));
            ng++;
        }
        if(word.length() > 5){
            feat.add(word);
        }
        return feat;
    }
    
    public List<Instance> createTestFeatureVectors(File inputFile) {
        List<Instance> features = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String line;
            while((line = reader.readLine()) != null)
            {
               //System.out.println(line);
               
               Pattern p = Pattern.compile("([a-zA-Z0-9]+)");
               Matcher m = p.matcher(line);
               while(m.find()) {
                   List<String> feat = new ArrayList<>();
                   String word = m.group(1);
                   int ng = 1;
                   while(ng <= 5 && ng <= word.length()){
                       feat.addAll(CharNGramExtractor.getGrams(word, ng));
                       ng++;
                   }
                   if(word.length() > 5){
                       feat.add(word);
                   }
                   
                   StringBuilder featur = new StringBuilder();
                   for(String f: feat) {
                       featur.append(f);
                       featur.append(" ");
                   }
                   String feature = featur.toString();
                   features.add(new Instance(feature, null, word, null));
               }
               
            }
            return features;
        } catch (IOException ex) {
            Logger.getLogger(ExtractFeaures.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Map<String, Map<String, Integer> > labeledFeatureCounts(File inputFile) {
        BufferedReader reader = null;
        try {
            Map<String, Integer> lab1 = new HashMap<>();
            Map<String, Integer> lab2 = new HashMap<>();
            reader = new BufferedReader(new FileReader(inputFile));
            String line;
            while((line = reader.readLine()) != null) {
                Pattern p = Pattern.compile("([a-zA-Z0-9]+)\\\\([EH])");
                Matcher m = p.matcher(line);
                while(m.find()) {
                    String word = m.group(1);
                    String label = m.group(2);
                    
                    List<String> feat = Features(word);
                    for(String s: feat) {
                        if(label.startsWith("E")){
                            if(lab1.containsKey(s)) {
                                lab1.put(s, lab1.get(s)+1);
                            }
                            else{
                                lab1.put(s, 1);
                            }
                        }
                        else {
                            if(lab2.containsKey(s)) {
                                lab2.put(s, lab2.get(s)+1);
                            }
                            else{
                                lab2.put(s, 1);
                            }
                        }
                    }
                    
                }
            }
            Map<String, Map<String, Integer> > labeledFeaturedCounts = new HashMap<>();
            labeledFeaturedCounts.put("E", lab1);
            labeledFeaturedCounts.put("H", lab2);
            return labeledFeaturedCounts;
        } catch (IOException ex) {
            Logger.getLogger(ExtractFeaures.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(ExtractFeaures.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    public List<Instance> createTestFeatureVectors(String testString) {
        List<Instance> features = new ArrayList<>();
        
        Pattern p = Pattern.compile("([a-zA-Z0-9]+)");
        Matcher m = p.matcher(testString);
        while(m.find()) {
            List<String> feat = new ArrayList<>();
            String word = m.group(1);
            int ng = 1;
            while(ng <= 5 && ng <= word.length()){
                feat.addAll(CharNGramExtractor.getGrams(word, ng));
                ng++;
            }
            if(word.length() > 5){
                feat.add(word);
            }

            StringBuilder featur = new StringBuilder();
            for(String f: feat) {
                featur.append(f);
                featur.append(" ");
            }
            String feature = featur.toString();
            //System.out.println(feature);
            features.add(new Instance(feature, "", word, null));
        }
        return features;        
    }
    
    public InstanceList getInstanceList(String fileName, boolean testTrain) {
        File inputFile = new File(fileName);
        InstanceList il = new InstanceList(pipe);
        List<Instance> fv;
        if(testTrain) {
            fv = createFeatureVectors(inputFile);
        }
        else {
            fv = createTestFeatureVectors(inputFile);
        }
        for (Instance i: fv) {
            il.addThruPipe(i);
        }
        return il;
    }
    
    public InstanceList getInstanceList(String testString) {
        
        InstanceList il = new InstanceList(pipe);
        List<Instance> fv = createTestFeatureVectors(testString);
        for (Iterator<Instance> it = fv.iterator(); it.hasNext();) {
            Instance i = it.next();
            il.addThruPipe(i);
        }
        return il;
    }
}
