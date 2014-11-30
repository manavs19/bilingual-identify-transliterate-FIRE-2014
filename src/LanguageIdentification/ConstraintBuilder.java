package LanguageIdentification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cc.mallet.classify.constraints.ge.MaxEntGEConstraint;
import cc.mallet.classify.constraints.ge.MaxEntKLFLGEConstraints;
import cc.mallet.fst.semi_supervised.constraints.GEConstraint;
import cc.mallet.fst.semi_supervised.constraints.OneLabelKLGEConstraints;
import cc.mallet.types.Alphabet;

public class ConstraintBuilder
{
	Alphabet featureAlphabet;
	Alphabet labelAlphabet;
	
	

	public ConstraintBuilder(Alphabet featureAlphabet, Alphabet labelAlphabet)
	{
		this.featureAlphabet = featureAlphabet;
		this.labelAlphabet = labelAlphabet;
	}


	/*
	 * Label counts represent expectations under a uniform distribution of labels.  If we have an estimate of the label distribution,
	 * we can make a better guess at the proper constraints. 
	 */
	public ArrayList<MaxEntGEConstraint> buildForMaxEnt(Map<String, Double> labelProportions, Map<String, Map<String, Integer>> labeledFeatureCounts)
	{
		Set<String> allRepresentedFeatures = allRepresentedFeatures(labeledFeatureCounts);
		
		MaxEntKLFLGEConstraints constraints = new MaxEntKLFLGEConstraints(featureAlphabet.size(), labelAlphabet.size(), false);
		for(String feature : allRepresentedFeatures)
		{
			Map<String, Integer> featureCountsByLabel = new HashMap<String, Integer>();
			for(String label : labeledFeatureCounts.keySet())
				featureCountsByLabel.put(label, labeledFeatureCounts.get(label).containsKey(feature) ? labeledFeatureCounts.get(label).get(feature) : 0);
			
			double[] expectations = getAdjustedConstraint(labelProportions, featureCountsByLabel);
			
			constraints.addConstraint(featureAlphabet.lookupIndex(feature, false), expectations, 1.0);
		}
		
		ArrayList<MaxEntGEConstraint> constraintList = new ArrayList<MaxEntGEConstraint>();
		constraintList.add(constraints);
		
		return constraintList;
	}

	public ArrayList<GEConstraint> build(Map<String, Double> labelProportions, Map<String, Map<String, Integer>> labeledFeatureCounts)
	{
		Set<String> allRepresentedFeatures = allRepresentedFeatures(labeledFeatureCounts);
		
		OneLabelKLGEConstraints constraints = new OneLabelKLGEConstraints();
		for(String feature : allRepresentedFeatures)
		{
			Map<String, Integer> featureCountsByLabel = new HashMap<String, Integer>();
			for(String label : labeledFeatureCounts.keySet())
				featureCountsByLabel.put(label, labeledFeatureCounts.get(label).containsKey(feature) ? labeledFeatureCounts.get(label).get(feature) : 0);
			
			double[] expectations = getAdjustedConstraint(labelProportions, featureCountsByLabel);
			
			constraints.addConstraint(featureAlphabet.lookupIndex(feature, false), expectations, 1.0);
		}
		
		ArrayList<GEConstraint> constraintList = new ArrayList<GEConstraint>();
		constraintList.add(constraints);
		
		return constraintList;
	}
	
	public Map<String, Map<String, Double>> buildForTesting(Map<String, Double> labelProportions, Map<String, Map<String, Integer>> labeledFeatureCounts)
	{
		Set<String> allRepresentedFeatures = allRepresentedFeatures(labeledFeatureCounts);
		
		Map<String, Map<String, Double>> constraints = new HashMap<String, Map<String, Double>>();
		for(String feature : allRepresentedFeatures)
		{
			Map<String, Integer> featureCountsByLabel = new HashMap<String, Integer>();
			for(String label : labeledFeatureCounts.keySet())
				featureCountsByLabel.put(label, labeledFeatureCounts.get(label).containsKey(feature) ? labeledFeatureCounts.get(label).get(feature) : 0);
			
			Map<String, Double> expectations = getAdjustedConstraintMap(labelProportions, featureCountsByLabel);
			
			constraints.put(feature, expectations);
		}
		return constraints;
	}
	
	Set<String> allRepresentedFeatures(Map<String, Map<String, Integer>> labeledFeatureCounts)
	{
		Set<String> allRepresentedFeatures = new HashSet<String>();
		for(Map<String, Integer> featureCounts : labeledFeatureCounts.values())
			allRepresentedFeatures.addAll(featureCounts.keySet());
		return allRepresentedFeatures;
	}
	
	double[] getAdjustedConstraint(Map<String, Double> labelProportions, Map<String, Integer> featureCountsByLabel)
	{
		int numLabels = labelAlphabet.size() /*labelProportions.keySet().size()*/;
                //System.out.println(labelAlphabet.toArray());
		double[] expectations = new double[numLabels];
		double uniformLabelProbability = 1.0 / numLabels;
		double normalizingFactor = 0;
		
		for(String label : labelProportions.keySet())
		{
			// expected count under the marginal label distribution is scaled by [actual label prob]/[uniform label prob]
			int featureLabelCount = featureCountsByLabel.get(label);
			double expectation = labelProportions.get(label) / uniformLabelProbability * ((double)featureLabelCount + 0.5);
			
			int labelIndex = labelAlphabet.lookupIndex(label);
			expectations[labelIndex] = expectation;
			normalizingFactor += expectation;
		}
		
		// divide by the total weight and smooth slightly to get maximum likelihood estimates
		for(int k=0; k<expectations.length; ++k)
			expectations[k] = (expectations[k]/* + 0.01*/)/ (normalizingFactor/* + 0.01 * numLabels*/);
		
		return expectations;
	}
	
	Map<String, Double> getAdjustedConstraintMap(Map<String, Double> labelProportions, Map<String, Integer> featureCountsByLabel)
	{
		int numLabels = labelProportions.keySet().size();
		Map<String, Double> expectations = new HashMap<String, Double>();
		double uniformLabelProbability = 1.0 / numLabels;
		double normalizingFactor = 0;
		
		for(String label : labelProportions.keySet())
		{
			// expected count under the marginal label distribution is scaled by [actual label prob]/[uniform label prob]
			int featureLabelCount = featureCountsByLabel.get(label);
			double expectation = labelProportions.get(label) / uniformLabelProbability * ((double)featureLabelCount + 0.5);
			
			expectations.put(label, expectation);
			normalizingFactor += expectation;
		}
		
		// divide by the total weight and smooth slightly to get maximum likelihood estimates
		for(String label : expectations.keySet())
			expectations.put(label, (expectations.get(label) /* + 0.01*/)/ (normalizingFactor/* + 0.01 * numLabels*/));
		
		return expectations;
	}
}
