package LanguageIdentification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cc.mallet.fst.CRF;
import cc.mallet.fst.TokenAccuracyEvaluator;
import cc.mallet.fst.semi_supervised.CRFTrainerByGE;
import cc.mallet.fst.semi_supervised.constraints.GEConstraint;
import cc.mallet.types.FeatureVectorSequence;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Sequence;

public class CRFwithGE
{
	public double crfWithGEAccuracy(InstanceList testingData, ArrayList<GEConstraint> constraints)
	{
		CRF crf = new CRF(testingData.getDataAlphabet(), testingData.getTargetAlphabet());
		crf.addFullyConnectedStatesForLabels();
		crf.setWeightsDimensionAsIn(testingData, false);
		CRFTrainerByGE trainer = new CRFTrainerByGE(crf, constraints);
		trainer.setGaussianPriorVariance(0.1);
		trainer.train(testingData);

		TokenAccuracyEvaluator evaluator = new TokenAccuracyEvaluator(testingData, "test");
		evaluator.evaluateInstanceList(trainer, testingData, "test");
		return evaluator.getAccuracy("test");
	}
	
	public void printClassification(InstanceList tokenInstances, InstanceList testingData, ArrayList<GEConstraint> constraints)
	{
		CRF crf = new CRF(testingData.getDataAlphabet(), testingData.getTargetAlphabet());
		crf.addFullyConnectedStatesForLabels();
		crf.setWeightsDimensionAsIn(testingData, false);
		CRFTrainerByGE trainer = new CRFTrainerByGE(crf, constraints);
		trainer.setGaussianPriorVariance(0.1);
		trainer.train(testingData);

		Sequence predictedOutput = trainer.getTransducer().transduce((FeatureVectorSequence)testingData.get(0).getData());
		String previousClass = "";
		for(int k=0; k<predictedOutput.size(); ++k)
		{
			String bestClass = (String)predictedOutput.get(k);
			if(bestClass.equals(previousClass))
			{
				System.out.print(" " + tokenInstances.get(k).getName());
			}
			else
			{
				System.out.println();
				System.out.print("[[" + bestClass + "]] " + tokenInstances.get(k).getName());
			}
			previousClass = bestClass;
		}
	}
	
	public Map<String, Map<Integer, Integer>> getSegmentLengthBarChart(InstanceList tokenInstances, InstanceList testingData, ArrayList<GEConstraint> constraints)
	{
		Map<String, Map<Integer, Integer>> barChartByLang = new HashMap<String, Map<Integer,Integer>>();
		CRF crf = new CRF(testingData.getDataAlphabet(), testingData.getTargetAlphabet());
		crf.addFullyConnectedStatesForLabels();
		crf.setWeightsDimensionAsIn(testingData, false);
		CRFTrainerByGE trainer = new CRFTrainerByGE(crf, constraints);
		trainer.setGaussianPriorVariance(0.1);
		trainer.train(testingData);

		Sequence predictedOutput = trainer.getTransducer().transduce((FeatureVectorSequence)testingData.get(0).getData());
		String previousClass = "";
		int currentSegmentLength = 1;
		for(int k=0; k<predictedOutput.size(); ++k)
		{
			String bestClass = (String)predictedOutput.get(k);
			if(bestClass.equals(previousClass))
			{
				currentSegmentLength++;
			}
			else if(previousClass.length() > 0)
			{
				if(!barChartByLang.containsKey(previousClass))
					barChartByLang.put(previousClass, new HashMap<Integer, Integer>());
				if(!barChartByLang.get(previousClass).containsKey(currentSegmentLength))
					barChartByLang.get(previousClass).put(currentSegmentLength, 0);
				barChartByLang.get(previousClass).put(currentSegmentLength, barChartByLang.get(previousClass).get(currentSegmentLength)+1);
				currentSegmentLength = 1;
			}
			previousClass = bestClass;
		}
		
		if(!barChartByLang.containsKey(previousClass))
			barChartByLang.put(previousClass, new HashMap<Integer, Integer>());
		if(!barChartByLang.get(previousClass).containsKey(currentSegmentLength))
			barChartByLang.get(previousClass).put(currentSegmentLength, 0);
		barChartByLang.get(previousClass).put(currentSegmentLength, barChartByLang.get(previousClass).get(currentSegmentLength)+1);
		
		return barChartByLang;
	}
}
