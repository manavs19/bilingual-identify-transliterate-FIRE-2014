package LanguageIdentification;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author in dia
 */
public class Main {
    
    

//    public static void main(String[] args) {
//        TrainerTester tt = new TrainerTester("F:\\Eclipse Workspace\\Retrieval\\HindiEnglishTrain\\train.txt", "maxent", "F:\\Eclipse Workspace\\Retrieval\\Dict\\English.txt", "F:\\Eclipse Workspace\\Retrieval\\Dict\\Hindi.txt");
//        tt.train();
//        tt.writeClassification("F:\\Eclipse Workspace\\Retrieval\\HindiEnglishTest.txt", "F:\\Eclipse Workspace\\Retrieval\\sid.txt");
//        Scanner s = new Scanner(System.in);
//        while(true) {
//            String query = s.nextLine();
//            System.out.println(tt.ClassifyQuery(query));
//        }
//    }
//        if(args[1].startsWith("-h") || args[1].startsWith("--h"))
//        {
//                System.out.println("Usage: [maxent|maxent-ge|hmm|crf] [lang_id:lang_sample]+ <file_to_label>");
//        }
//        
//        List<String> labels = new ArrayList<>();
//        Map<String, File> languageSampleFiles = new HashMap<>();
//        //File fileToLabel = new File("");
//
//        String method = args[1];
//        labels.add(args[2]);
//        labels.add(args[3]);
//        String fileName = args[4];
//        String EdictPath = args[5];
//        String HdictPath = args[6];
//        
//        DictionaryWords englishDict = new DictionaryWords(EdictPath);
//        DictionaryWords hindiDict = new DictionaryWords(HdictPath);
//
//        List<Pipe> pipes = new ArrayList<>();
//        pipes.add(new Input2CharSequence("UTF-8"));
//        pipes.add(new CharSequence2TokenSequence("[\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]\\p{Nd}\\-_]+"));
//        pipes.add(new TokenSequence2FeatureSequence());
//        pipes.add(new Target2Label());
//        pipes.add(new FeatureSequence2FeatureVector());
//        Pipe pipe = new SerialPipes(pipes);
//
//        ExtractFeaures makeFeatures = new ExtractFeaures(pipe, englishDict, hindiDict);
//        InstanceList trainingData = makeFeatures.getInstanceList(fileName, true);
//        
//        if(method.compareTo("maxent") == 0){    
//            
//            if(args[0].compareTo("train") == 0) {
//                MaxEntTrainer met = new MaxEntTrainer();
//                met.train(trainingData);
//                MaxEnt metclassifier = met.getClassifier();
//                ClassifierIO.save(metclassifier, "trainResults/maxent/metClassifier");
//            }
//            else {
//                String testString = "juda hoke bhi tu mujhme kahi baki hai, IIT-JEE results release data";
//                String testString2 = "hell .. i dnt y ppl make assumptns ... thrgh dressing style ... Saath mein summary bhi likh det";
//                String testString3 = "anil k amar chand ko kya maloom chahta he use koi chkore wo to bechara door se dekhe kre me hu fan me hu fan ka sore";
//                String testFileName = args[7];
//                InstanceList test = makeFeatures.getInstanceList(testFileName, true);
//                InstanceList testRemoveLabels = removeLabels(test, pipe);
//                MaxEnt metclassifier = (MaxEnt)ClassifierIO.read("trainResults/maxent/metClassifier");
//                String markedString = "";
//                for(Instance i: testRemoveLabels) {
//                    boolean ee = englishDict.contains((String)i.getName());
//                    boolean yy = hindiDict.contains((String)i.getName());
//                        markedString += i.getName()+"\\"+metclassifier.classify(i).getLabeling().getBestLabel().toString()+" ";
//                    
//                }
//                Trial trial = new Trial(metclassifier, test);
//                System.out.println(trial.getAccuracy()+" "+trial.getPrecision("E") + " "+trial.getRecall("E") + " " + trial.getF1("E"));
//                System.out.println(markedString);
//            }
//        }
//
//        else if(method.compareTo("maxent-ge") == 0) {
//            
//            if(args[0].compareTo("train") == 0) {
//                MaxEntTrainer met = new MaxEntTrainer();
//                met.train(trainingData);
//                MaxEnt metclassifier = met.getClassifier();
//                ClassifierIO.save(metclassifier, "trainResults/maxent/metClassifier");
//            }
//            else {
//                String testString = "juda hoke bhi tu mujhme kahi baki hai, IIT-JEE results release data";
//                String testString2 = "hell .. i dnt y ppl make assumptns ... thrgh dressing style ... Saath mein summary bhi likh det";
//                String testString3 = "pal pal dil ke saath, lives dipayan, who is a good banda, aur ek acha student";
//                String testFileName = args[7];
//                InstanceList test = makeFeatures.getInstanceList(testString);
//                
//                MaxEnt metclassifier = (MaxEnt)ClassifierIO.read("trainResults/maxent/metClassifier");
//                Map<String, Integer> labelCounts = new HashMap<>();
//
//                for(Instance i : test){
//                    String bestClass = metclassifier.classify(i).getLabeling().getBestLabel().toString();
//                    //System.out.println(i.getName()+"\\"+bestClass+" ");
//                    if(labelCounts.containsKey(bestClass))
//                            labelCounts.put(bestClass, labelCounts.get(bestClass)+1);
//                    else
//                            labelCounts.put(bestClass, 1);
//                }
//
//                Map<String, Double> proportions = new HashMap<>();
//                for(String label :labelCounts.keySet()) {
//                    proportions.put(label, (double)labelCounts.get(label)/test.size());
//                }
//                
//                Map<String, Map<String, Integer>> labeledFeatureCounts = makeFeatures.labeledFeatureCounts(new File(fileName));
//                ConstraintBuilder constraintBuilder = new ConstraintBuilder(pipe.getDataAlphabet(), pipe.getTargetAlphabet());
//                ArrayList<MaxEntGEConstraint> constraints = constraintBuilder.buildForMaxEnt(proportions, labeledFeatureCounts);
//                InstanceList testRemoveLabels = removeLabels(test, pipe);
//                MaxEntwithGE maxEntWithGE = new MaxEntwithGE();
//                String markedString = maxEntWithGE.getClassification(testRemoveLabels, test, constraints);
//                System.out.println(markedString);      
//            }
//        }               
//    }
//    public static InstanceList removeLabels(InstanceList il, Pipe pipe)
//    {
//            InstanceList ul = new InstanceList(new Noop());
//            for(Instance i : il)
//            {
//                    Instance j = new Instance(i.getData(), null, i.getName(), null);
//                    ul.add(j);
//            }
//            ul.setPipe(pipe);
//            return ul;
//    }
}
