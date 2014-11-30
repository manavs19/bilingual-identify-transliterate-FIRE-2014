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
public class ExtractFeaures {
    Pipe pipe;
    DictionaryWords english;
    DictionaryWords hindi;
    
    final String hprep="__HPRE";
    final String eprep="__EPRE";
    final String punc="__PUNC";
    final String digt="__DIGT";
    final String capt="__CAPT";
    final String edict="__EDICT";
    final String hdict="__HDICT";
    final String phdict = "__PHDICT";
    final String pedict = "__PEDICT";
    final String nhdict = "__NHDICT";
    final String nedict = "__NEDICT";
    final String start = "__START";
    final String end = "__END";
    
    boolean Hprep;
    boolean Eprep;
    boolean Punc;
    boolean Digt;
    boolean Capt;
    boolean Edict;
    boolean Hdict;
    boolean Phdict;
    boolean Pedict;
    boolean Nhdict;
    boolean Nedict;
    boolean Start;
    boolean End;
    
    
    public ExtractFeaures(Pipe pipe, DictionaryWords english, DictionaryWords  hindi) {
        this.pipe = pipe;
        this.english = english;
        this.hindi = hindi;
        Hprep = true;
        Eprep = true;
        Punc = true;
        Digt = true;
        Capt = true;
        Edict = true;
        Hdict = true;
        Phdict = true;
        Pedict = true;
        Nhdict = true;
        Nedict = true;
        Start = true;
        End = true;
    }
    
    public List<Instance> createFeatureVectors (File inputFile) {
        List<Instance> features = new ArrayList<>();
        
        CommonWords hinPrep = new CommonWords(new File("F:\\Eclipse Workspace\\Retrieval\\HindiPreposition.txt"));
        CommonWords engPrep = new CommonWords(new File("F:\\Eclipse Workspace\\Retrieval\\EnglishPreposition.txt"));
        
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                String lines;
                while((lines = reader.readLine()) != null)
                {
                   //System.out.println(line);
                   String[] linearray = lines.split("\\\\O");
                   for(String line: linearray) {
                        Pattern p = Pattern.compile("([a-zA-Z0-9]+)\\\\([EH])");
                        Matcher m = p.matcher(line);

                            List<String> words=new ArrayList<>();
                            List<String> labels=new ArrayList<>();
                            ArrayList<ArrayList<String>> feat=new ArrayList<>();

                            boolean ePrep=false;
                            boolean hPrep=false;

                        while(m.find()) {
                            String word = m.group(1);
                            String label = m.group(2);
                            words.add(word);
                            labels.add(label);
                        }

                        for(int i=0; i<words.size(); i++) {
                                ArrayList<String> f = new ArrayList<>();
                                int ng = 1;
                                while(ng <= 5 && ng <= words.get(i).length()){
                                    f.addAll(CharNGramExtractor.getGrams(words.get(i), ng));
                                    ng++;
                                }
                                if(words.get(i).length() > 5){
                                    f.add(words.get(i));
                                }

                                if(i!=0) {
                                    if(english.contains(words.get(i-1)) && Pedict) {
                                        f.add(pedict);
                                    }
                                    if(hindi.contains(words.get(i-1)) && Phdict) {
                                        f.add(phdict);
                                    }
                                }
                                else {
                                    if(Start)
                                        f.add(start);
                                }
                                if(i!=words.size()-1) {
                                    if(english.contains(words.get(i+1)) && Nedict) {
                                        f.add(nedict);
                                    }
                                    if(hindi.contains(words.get(i+1)) && Nhdict) {
                                        f.add(nhdict);
                                    }
                                }
                                else {
                                    if(End)
                                        f.add(end);
                                }

                                if(words.get(i).matches(".*\\d.*") && Digt) {
                                        f.add(digt);
                                }

                                if(!words.get(i).equals(words.get(i).toLowerCase()) && Capt) {
                                        f.add(capt);
                                }

                                if(english.contains(words.get(i)) && Edict) {
                                        f.add(edict);
                                }

                                if(hindi.contains(words.get(i)) && Hdict) {
                                        f.add(hdict);
                                }

                                feat.add(f);
                                if(hinPrep.contains(words.get(i)) && Hprep) {
                                        hPrep=true;
                                }
                                if(engPrep.contains(words.get(i)) && Eprep) {
                                        ePrep=true;
                                }
                        }
                        for(int i=0; i<words.size(); i++) {
                                ArrayList<String> f = feat.get(i);
                                if(hPrep) {
                                        f.add(hprep);
                                }
                                if(ePrep) {
                                        f.add(eprep);
                                }

                            StringBuilder featur = new StringBuilder();
                            for(String t: f) {
                                featur.append(t);
                                featur.append(" ");
                            }
                            String feature = featur.toString();
                            //System.out.println(feature);
                            features.add(new Instance(feature, labels.get(i), words.get(i), null));
                        }
                   }
                }
            }
            return features;
        } catch (IOException ex) {
            Logger.getLogger(ExtractFeaures.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
       
    public List<Instance> createTestFeatureVectors(File inputFile) {
    	System.out.println("hello");
    	List<Instance> features = new ArrayList<>();
        
        CommonWords hinPrep = new CommonWords(new File("F:\\Eclipse Workspace\\Retrieval\\HindiPreposition.txt"));
        CommonWords engPrep = new CommonWords(new File("F:\\Eclipse Workspace\\Retrieval\\EnglishPreposition.txt"));
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String lines;
            while((lines = reader.readLine()) != null)
            {
               //System.out.println(line);
               String[] linearray = lines.split("[\\\\.\\\\?\\\\!] ");
               for(String line: linearray) {
                    Pattern p = Pattern.compile("([a-zA-Z0-9]+)(\\\\[EH])?");
                    Matcher m = p.matcher(line);

                        List<String> words=new ArrayList<>();
        //        	   List<String> labels=new ArrayList<>();
                        ArrayList<ArrayList<String>> feat=new ArrayList<>();

                        boolean ePrep=false;
                        boolean hPrep=false;

                    while(m.find()) {
                        String word = m.group(1);
        //                   String label = m.group(2);
                        words.add(word);
        //                   labels.add(label);
                    }

                    for(int i=0; i<words.size(); i++) {
                        ArrayList<String> f = new ArrayList<>();
                        int ng = 1;
                        while(ng <= 5 && ng <= words.get(i).length()){
                            f.addAll(CharNGramExtractor.getGrams(words.get(i), ng));
                            ng++;
                        }
                        if(words.get(i).length() > 5){
                            f.add(words.get(i));
                        }
                        if(i!=0) {
                            if(english.contains(words.get(i-1)) && Pedict) {
                                f.add(pedict);
                            }
                            if(hindi.contains(words.get(i-1)) && Phdict) {
                                f.add(phdict);
                            }
                        }
                        else {
                            if(Start)
                                f.add(start);
                        }
                        if(i!=words.size()-1) {
                            if(english.contains(words.get(i+1)) && Nedict) {
                                f.add(nedict);
                            }
                            if(hindi.contains(words.get(i+1)) && Nhdict) {
                                f.add(nhdict);
                            }
                        }
                        else {
                            if(End)
                                f.add(end);
                        }

                        if(words.get(i).matches(".*\\d.*") && Digt) {
                                f.add(digt);
                        }

                        if(!words.get(i).equals(words.get(i).toLowerCase()) && Capt) {
                                f.add(capt);
                        }

                        if(english.contains(words.get(i)) && Edict) {
                                f.add(edict);
                        }

                        if(hindi.contains(words.get(i)) && Hdict) {
                                f.add(hdict);
                        }

                        feat.add(f);
                        if(hinPrep.contains(words.get(i)) && Hprep) {
                                hPrep=true;
                        }
                        if(engPrep.contains(words.get(i)) && Eprep) {
                                ePrep=true;
                        }
                            
                    }
                    for(int i=0; i<words.size(); i++) {
                        ArrayList<String> f = feat.get(i);
                        if(hPrep) {
                                f.add(hprep);
                        }
                        if(ePrep) {
                                f.add(eprep);
                        }

                        StringBuilder featur = new StringBuilder();
                        for(String t: f) {
                            featur.append(t);
                            featur.append(" ");
                        }
                        String feature = featur.toString();
                        //System.out.println(feature);
                        features.add(new Instance(feature, "E", words.get(i), null));
                    }
               }
            }
            reader.close();
            return features;
        } catch (IOException ex) {
            Logger.getLogger(ExtractFeaures.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Map<String, Map<String, Integer> > labeledFeatureCounts(File inputFile) {
        BufferedReader reader = null;
        CommonWords hinPrep = new CommonWords(new File("F:\\Eclipse Workspace\\Retrieval\\HindiPreposition.txt"));
        CommonWords engPrep = new CommonWords(new File("F:\\Eclipse Workspace\\Retrieval\\EnglishPreposition.txt"));
        try {
            Map<String, Integer> lab1 = new HashMap<>();
            Map<String, Integer> lab2 = new HashMap<>();
            reader = new BufferedReader(new FileReader(inputFile));
            String lines;
            while((lines = reader.readLine()) != null) {
                
                String[] linearray = lines.split("\\\\O");
                
                for(String line: linearray) {
                    Pattern p = Pattern.compile("([a-zA-Z0-9]+)\\\\([EH])");
                    Matcher m = p.matcher(line);

                    List<String> words=new ArrayList<>();
                    List<String> labels=new ArrayList<>();
                    ArrayList<ArrayList<String>> feat=new ArrayList<>();

                    boolean ePrep=false;
                    boolean hPrep=false;

                    while(m.find()) {
                        String word = m.group(1);
                        String label = m.group(2);
                        words.add(word);
                        labels.add(label);
                    }

                    for(int i=0; i<words.size(); i++) {
                            ArrayList<String> f = new ArrayList<>();
                            int ng = 1;
                            while(ng <= 5 && ng <= words.get(i).length()){
                                f.addAll(CharNGramExtractor.getGrams(words.get(i), ng));
                                ng++;
                            }
                            if(words.get(i).length() > 5){
                                f.add(words.get(i));
                            }

                            if(i!=0) {
                                if(english.contains(words.get(i-1)) && Pedict) {
                                    f.add(pedict);
                                }
                                if(hindi.contains(words.get(i-1)) && Phdict) {
                                    f.add(phdict);
                                }
                            }
                            else {
                                if(Start)
                                    f.add(start);
                            }
                            if(i!=words.size()-1) {
                                if(english.contains(words.get(i+1)) && Nedict) {
                                    f.add(nedict);
                                }
                                if(hindi.contains(words.get(i+1)) && Nhdict) {
                                    f.add(nhdict);
                                }
                            }
                            else {
                                if(End)
                                    f.add(end);
                            }

                            if(words.get(i).matches(".*\\d.*")) {
                                    f.add(digt);
                            }

                            if(!words.get(i).equals(words.get(i).toLowerCase()) && Capt) {
                                    f.add(capt);
                            }

                            if(english.contains(words.get(i)) && Edict) {
                                    f.add(edict);
                            }

                            if(hindi.contains(words.get(i)) && Hdict) {
                                    f.add(hdict);
                            }

                            feat.add(f);
                            if(hinPrep.contains(words.get(i)) && Hprep) {
                                    hPrep=true;
                            }
                            if(engPrep.contains(words.get(i)) && Eprep) {
                                    ePrep=true;
                            }
                    }
                    
                    for(int i=0; i<words.size(); i++) {
                        ArrayList<String> f = feat.get(i);
                        if(hPrep) {
                                f.add(hprep);
                        }
                        if(ePrep) {
                                f.add(eprep);
                        }
                        for(String featS: f) {
                            if(labels.get(i).compareTo("E") == 0) {
                                if(lab1.containsKey(featS)) {
                                    lab1.put(featS, lab1.get(featS)+1);
                                }
                                else {
                                    lab1.put(featS, 1);
                                }
                            }
                            else {
                                if(lab2.containsKey(featS)) {
                                    lab2.put(featS, lab2.get(featS)+1);
                                }
                                else {
                                    lab2.put(featS, 1);
                                }
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
        //System.out.println("hello");
    	List<Instance> features = new ArrayList<>();
        
        CommonWords hinPrep = new CommonWords(new File("F:\\Eclipse Workspace\\Retrieval\\HindiPreposition.txt"));
        CommonWords engPrep = new CommonWords(new File("F:\\Eclipse Workspace\\Retrieval\\EnglishPreposition.txt"));
        
        String[] linearray = testString.split("[\\\\.\\\\?!,] ");
        for(String line: linearray) {
            Pattern p = Pattern.compile("([a-zA-Z0-9\\\\-]+)(\\\\[EH])?");
            Matcher m = p.matcher(line);

                List<String> words=new ArrayList<>();
//        	   List<String> labels=new ArrayList<>();
                ArrayList<ArrayList<String>> feat=new ArrayList<>();

                boolean ePrep=false;
                boolean hPrep=false;

            while(m.find()) {
                String word = m.group(1);
//                   String label = m.group(2);
                words.add(word);
//                   labels.add(label);
            }
            
            for(int i=0; i<words.size(); i++) {
                ArrayList<String> f = new ArrayList<>();
                int ng = 1;
                while(ng <= 5 && ng <= words.get(i).length()){
                    f.addAll(CharNGramExtractor.getGrams(words.get(i), ng));
                    ng++;
                }
                if(words.get(i).length() > 5){
                    f.add(words.get(i));
                }
                if(i!=0) {
                    if(english.contains(words.get(i-1)) && Pedict) {
                        f.add(pedict);
                    }
                    if(hindi.contains(words.get(i-1)) && Phdict) {
                        f.add(phdict);
                    }
                }
                else {
                    if(Start)
                        f.add(start);
                }
                if(i!=words.size()-1) {
                    if(english.contains(words.get(i+1)) && Nedict) {
                        f.add(nedict);
                    }
                    if(hindi.contains(words.get(i+1)) && Nhdict) {
                        f.add(nhdict);
                    }
                }
                else {
                    if(End)
                        f.add(end);
                }

                if(words.get(i).matches(".*\\d.*") && Digt) {
                        f.add(digt);
                }

                if(!words.get(i).equals(words.get(i).toLowerCase()) && Capt) {
                        f.add(capt);
                }

                if(english.contains(words.get(i)) && Edict) {
                        f.add(edict);
                }

                if(hindi.contains(words.get(i)) && Hdict) {
                        f.add(hdict);
                }

                feat.add(f);
                if(hinPrep.contains(words.get(i)) && Hprep) {
                        hPrep=true;
                }
                if(engPrep.contains(words.get(i)) && Eprep) {
                        ePrep=true;
                }
            }
            for(int i=0; i<words.size(); i++) {
                ArrayList<String> f = feat.get(i);
                if(hPrep) {
                        f.add(hprep);
                }
                if(ePrep) {
                        f.add(eprep);
                }

                StringBuilder featur = new StringBuilder();
                for(String t: f) {
                    featur.append(t);
                    featur.append(" ");
                }
                String feature = featur.toString();
                //System.out.println(feature);
                features.add(new Instance(feature, "E", words.get(i), null));
            }
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
