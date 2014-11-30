package LanguageIdentification;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import cc.mallet.classify.MaxEnt;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.classify.Trial;
import cc.mallet.classify.constraints.ge.MaxEntGEConstraint;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Noop;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
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
public class TrainerTester {
    String trainPath;
    String method;
    DictionaryWords englishDict;
    DictionaryWords hindiDict;
    Pipe pipe;
    ExtractFeaures makeFeatures;
    InstanceList trainingData;
    MaxEnt metclassifier;
    
    
    public TrainerTester(String trainPath, String method, String EdictPath, String HdictPath) {
        this.trainPath = trainPath;
        this.method = method;
        englishDict = new DictionaryWords(EdictPath);
        hindiDict = new DictionaryWords(HdictPath);
        metclassifier = null;
        
        List<Pipe> pipes = new ArrayList<>();
        pipes.add(new Input2CharSequence("UTF-8"));
        pipes.add(new CharSequence2TokenSequence("[\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]\\p{Nd}\\-_]+"));
        pipes.add(new TokenSequence2FeatureSequence());
        pipes.add(new Target2Label());
        pipes.add(new FeatureSequence2FeatureVector());
        pipe = new SerialPipes(pipes);
        
        makeFeatures = new ExtractFeaures(pipe, englishDict, hindiDict);
        trainingData = makeFeatures.getInstanceList(trainPath, true);
    }
    public void train() {
        if(method.compareTo("maxent") == 0 || method.compareTo("maxent-ge") == 0) {
            MaxEntTrainer met = new MaxEntTrainer();
            System.out.println("1");
            met.train(trainingData);
            System.out.println("2");
            metclassifier = met.getClassifier();
            System.out.println("3");
            ClassifierIO.save(metclassifier, "trainResults/maxent/metClassifier");
            System.out.println("Training is complete !");
        }
        
        else {
            System.err.println("Error: Incorrect method of training, can be either maxent or maxent-ge");
        }
    }
    
    public void writeClassification (String testPath, String outputPath) {
        if(method.compareTo("maxent") == 0) {
            InstanceList test = makeFeatures.getInstanceList(testPath, false);
            InstanceList testRemoveLabels = removeLabels(test);
            if(metclassifier == null)
                metclassifier = (MaxEnt)ClassifierIO.read("trainResults/maxent/metClassifier");
            String markedString = "";
            System.out.println("ssdfsdf " +test.size());
            for(Instance i: testRemoveLabels) {
                    markedString += i.getName()+"\\"+metclassifier.classify(i).getLabeling().getBestLabel().toString()+" ";

            }
            
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputPath)));
                //System.out.println(markedString);
                bw.write(markedString);
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(TrainerTester.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        else if(method.compareTo("maxent-ge") == 0) {
                
            InstanceList test = makeFeatures.getInstanceList(testPath, false);
            if(metclassifier == null)
                metclassifier = (MaxEnt)ClassifierIO.read("trainResults/maxent/metClassifier");
            Map<String, Integer> labelCounts = new HashMap<>();

            for(Instance i : test){
                String bestClass = metclassifier.classify(i).getLabeling().getBestLabel().toString();
                //System.out.println(i.getName()+"\\"+bestClass+" ");
                if(labelCounts.containsKey(bestClass))
                        labelCounts.put(bestClass, labelCounts.get(bestClass)+1);
                else
                        labelCounts.put(bestClass, 1);
            }

            Map<String, Double> proportions = new HashMap<>();
            for(String label :labelCounts.keySet()) {
                proportions.put(label, (double)labelCounts.get(label)/test.size());
            }

            Map<String, Map<String, Integer>> labeledFeatureCounts = makeFeatures.labeledFeatureCounts(new File(trainPath));
            ConstraintBuilder constraintBuilder = new ConstraintBuilder(pipe.getDataAlphabet(), pipe.getTargetAlphabet());
            ArrayList<MaxEntGEConstraint> constraints = constraintBuilder.buildForMaxEnt(proportions, labeledFeatureCounts);
            InstanceList testRemoveLabels = removeLabels(test);
            MaxEntwithGE maxEntWithGE = new MaxEntwithGE();
            String markedString = maxEntWithGE.getClassification(testRemoveLabels, test, constraints);
            
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputPath)));
                //System.out.println(markedString);
                bw.write(markedString);
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(TrainerTester.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        else {
            System.err.println("Error: Incorrect method of training. Should be either maxent or maxent-ge");
        }
    }
    
    public String ClassifyQuery (String query) {
        if(method.compareTo("maxent") == 0) {
            InstanceList test = makeFeatures.getInstanceList(query);
            InstanceList testRemoveLabels = removeLabels(test);
            if(metclassifier == null)
                metclassifier = (MaxEnt)ClassifierIO.read("trainResults/maxent/metClassifier");
            String markedString = "";
            for(Instance i: testRemoveLabels) {
                    markedString += i.getName()+"\\"+metclassifier.classify(i).getLabeling().getBestLabel().toString()+" ";

            }
            //System.out.println(markedString);
            return markedString;
        }
        
        else if(method.compareTo("maxent-ge") == 0) {
                
            InstanceList test = makeFeatures.getInstanceList(query);
            InstanceList testRemoveLabels = removeLabels(test);
            if(metclassifier == null)
                metclassifier = (MaxEnt)ClassifierIO.read("trainResults/maxent/metClassifier");
            Map<String, Integer> labelCounts = new HashMap<>();

            for(Instance i : testRemoveLabels){
                String bestClass = metclassifier.classify(i).getLabeling().getBestLabel().toString();
                //System.out.println(i.getName()+"\\"+bestClass+" ");
                if(labelCounts.containsKey(bestClass))
                        labelCounts.put(bestClass, labelCounts.get(bestClass)+1);
                else
                        labelCounts.put(bestClass, 1);
            }

            Map<String, Double> proportions = new HashMap<>();
           
            for(String label :labelCounts.keySet()) {
                proportions.put(label, (double)labelCounts.get(label)/testRemoveLabels.size());
            }

            Map<String, Map<String, Integer>> labeledFeatureCounts = makeFeatures.labeledFeatureCounts(new File(trainPath));
            ConstraintBuilder constraintBuilder = new ConstraintBuilder(pipe.getDataAlphabet(), pipe.getTargetAlphabet());
            ArrayList<MaxEntGEConstraint> constraints = constraintBuilder.buildForMaxEnt(proportions, labeledFeatureCounts);
            
            MaxEntwithGE maxEntWithGE = new MaxEntwithGE();
            String markedString = maxEntWithGE.getClassification(testRemoveLabels, test, constraints);
            //System.out.println(markedString);
            return markedString;
        }
        
        else {
            System.err.println("Error: Incorrect method of training. Should be either maxent or maxent-ge");
            return null;
        }
    }
    
    public String getClassificationStatistics (String testPath) {
        if(method.compareTo("maxent") == 0) {
            System.out.println("sfds");
            InstanceList test = makeFeatures.getInstanceList(testPath, true);
            InstanceList testRemoveLabels = removeLabels(test);
            if(metclassifier == null)
                metclassifier = (MaxEnt)ClassifierIO.read("trainResults/maxent/metClassifier");
            
            Trial trial = new Trial(metclassifier, test);
            String stats = "Accuracy = "+trial.getAccuracy()+"\nPrecision (English) = "+trial.getPrecision("E") + "\nPrecision (Hindi) = "+trial.getPrecision("H") + "\nRecall (English)" + trial.getRecall("E") + "\nRecall (Hindi)" + trial.getRecall("H") + "\nF1-Score (English)" + trial.getF1("E") + "\nF1-Score (Hindi)" + trial.getF1("H");
            
            return stats;
        }
        
        else if(method.compareTo("maxent-ge") == 0) {
                
            InstanceList test2 = makeFeatures.getInstanceList(testPath, true);
            InstanceList test = makeFeatures.getInstanceList(testPath, false);
            if(metclassifier == null)
                metclassifier = (MaxEnt)ClassifierIO.read("trainResults/maxent/metClassifier");
            Map<String, Integer> labelCounts = new HashMap<>();

            for(Instance i : test){
                String bestClass = metclassifier.classify(i).getLabeling().getBestLabel().toString();
                //System.out.println(i.getName()+"\\"+bestClass+" ");
                if(labelCounts.containsKey(bestClass))
                        labelCounts.put(bestClass, labelCounts.get(bestClass)+1);
                else
                        labelCounts.put(bestClass, 1);
            }

            Map<String, Double> proportions = new HashMap<>();
            for(String label :labelCounts.keySet()) {
                proportions.put(label, (double)labelCounts.get(label)/test.size());
            }

            Map<String, Map<String, Integer>> labeledFeatureCounts = makeFeatures.labeledFeatureCounts(new File(trainPath));
            ConstraintBuilder constraintBuilder = new ConstraintBuilder(pipe.getDataAlphabet(), pipe.getTargetAlphabet());
            ArrayList<MaxEntGEConstraint> constraints = constraintBuilder.buildForMaxEnt(proportions, labeledFeatureCounts);
            InstanceList testRemoveLabels = removeLabels(test);
            MaxEntwithGE maxEntWithGE = new MaxEntwithGE();
            String stats = maxEntWithGE.maxEntWithGEStats(testRemoveLabels, test2, constraints);
            return stats;
        }
        
        else {
            System.err.println("Error: Incorrect method of training. Should be either maxent or maxent-ge");
            return null;
        }
    }
    
    public InstanceList removeLabels (InstanceList il) {
        InstanceList ul = new InstanceList(new Noop());
        for(Instance i : il) {
                Instance j = new Instance(i.getData(), null, i.getName(), null);
                ul.add(j);
        }
        ul.setPipe(pipe);
        return ul;
    }
}
