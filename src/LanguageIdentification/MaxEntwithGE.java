package LanguageIdentification;

import java.util.ArrayList;

import cc.mallet.classify.MaxEnt;
import cc.mallet.classify.MaxEntGETrainer;
import cc.mallet.classify.Trial;
import cc.mallet.classify.constraints.ge.MaxEntGEConstraint;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

public class MaxEntwithGE
{
	public String maxEntWithGEStats(InstanceList testingDataNoLabels, InstanceList testingData, ArrayList<MaxEntGEConstraint> constraints)
	{
		MaxEntGETrainer trainer = new MaxEntGETrainer(constraints);
		trainer.train(testingDataNoLabels);
		
		MaxEnt classifier = trainer.getClassifier();
		Trial trial = new Trial(classifier, testingData);
                String stats = "Accuracy = "+trial.getAccuracy()+"\nPrecision (English) = "+trial.getPrecision("E") + "\nPrecision (Hindi) = "+trial.getPrecision("H") + "\nRecall (English)" + trial.getRecall("E") + "\nRecall (Hindi)" + trial.getRecall("H") + "\nF1-Score (English)" + trial.getF1("E") + "\nF1-Score (Hindi)" + trial.getF1("H");

                return stats;
	}
	
	public String getClassification(InstanceList testingDataNoLabels, InstanceList testingData, ArrayList<MaxEntGEConstraint> constraints)
	{
		MaxEntGETrainer trainer = new MaxEntGETrainer(constraints);
		trainer.train(testingDataNoLabels);
		
		MaxEnt classifier = trainer.getClassifier();
		String markedString = "";
		for(Instance i : testingData)
		{
			String bestClass = classifier.classify(i).getLabeling().getBestLabel().toString();
			markedString += i.getName() +"\\"+bestClass+" ";
		}
                //Trial test = new Trial(classifier, testingData);
                //System.out.println(test.getAccuracy()+" "+test.getPrecision("E") + " "+test.getRecall("E") + " " + test.getF1("E"));
                return markedString;
	}
}
