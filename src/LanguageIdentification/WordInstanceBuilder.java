package LanguageIdentification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.Instance;

public class WordInstanceBuilder
{
	FeatureConfiguration features;
	Alphabet featureAlphabet;
	
	private static final Map<String, String> punctuationNames = initializePunctuationMap();
			
	private static Map<String, String> initializePunctuationMap()
	{
		Map<String, String> punct = new HashMap<String, String>();
		punct.put("(", "leftparenthesis");
		punct.put(")", "rightparenthesis");
		punct.put("-", "ndash");
		punct.put("/", "forwardslash");
		punct.put("\\", "backslash");
		punct.put(".", "period");
		punct.put(",", "comma");
		punct.put("<", "leftanglebracket");
		punct.put(">", "rightanglebracket");
		punct.put("?", "questionmark");
		punct.put(":", "colon");
		punct.put(";", "semicolon");
		punct.put("[", "leftsquarebracket");
		punct.put("]", "rightsquarebracket");
		punct.put("{", "leftcurlybrace");
		punct.put("}", "rightcurlybrace");
		punct.put("|", "verticalbar");
		punct.put("`", "backtick");
		punct.put("~", "tilde");
		punct.put("!", "exclamationpoint");
		punct.put("@", "atsign");
		punct.put("#", "hash");
		punct.put("$", "dollarsign");
		punct.put("%", "percentsign");
		punct.put("^", "carat");
		punct.put("&", "ampersand");
		punct.put("*", "star");
		punct.put("_", "underscore");
		punct.put("=", "equalssign");
		punct.put("+", "plussign");
		return punct;
	}
	
	public WordInstanceBuilder(FeatureConfiguration features, Alphabet featureAlphabet)
	{
		this.features = features;
		this.featureAlphabet = featureAlphabet;
	}
	
	public FeatureVector convertWordToFeatureVector(String word, String label)
	{
		WordInContext wic = new WordInContext(word, label);
		return convertWordToFeatureVector(wic);
	}
	
	public FeatureVector convertWordToFeatureVector(WordInContext wic)
	{
		if(wic.getWord().length() == 0)
			System.err.println("zero length word");
		
		List<String> allFeatureOccurences = getNGramsFeatureList(wic.getWord());
		allFeatureOccurences.addAll(getContextFeatureList(wic));
		allFeatureOccurences.addAll(getTokenFeatureList(wic));
		
		
		Map<String, Integer> featureCounts = new HashMap<String, Integer>();
		for(String feature : allFeatureOccurences)
		{
			if(featureCounts.containsKey(feature))
				featureCounts.put(feature, featureCounts.get(feature)+1);
			else
				featureCounts.put(feature, 1);
		}
		
		int[] featureIndices = new int[featureCounts.keySet().size()];
		double[] values = new double[featureCounts.keySet().size()];
		
		int count = 0;
		for(String feature : featureCounts.keySet())
		{
			featureIndices[count] = featureAlphabet.lookupIndex(feature, true);
			values[count] = featureCounts.get(feature);
			++count;
		}
		
		FeatureVector vector = new FeatureVector(featureAlphabet, featureIndices, values);
		return vector;
	}
	
	public Instance convertWordToInstance(String word, String label)
	{
		WordInContext wic = new WordInContext(word, label);
		return convertWordToInstance(wic);
	}

	public Instance convertWordToInstance(WordInContext wic)
	{
		if(wic.getWord().length() == 0)
			System.err.println("zero length word");
		
		List<String> allFeatures = getNGramsFeatureList(wic.getWord());
		if(features.useToken())
			allFeatures.addAll(getTokenFeatureList(wic));
		allFeatures.addAll(getContextFeatureList(wic));
		
		String data = concatList(allFeatures, " ");
		Instance i = new Instance(data, wic.getLabel(), wic.getWord(), null); 
		return i;
	}
	
	List<String> getNGramsFeatureList(String word)
	{
		List<String> grams = new ArrayList<String>();
		if(features.useUnigrams())
			grams.addAll(CharNGramExtractor.getGrams(word, 1));
		if(features.useBigrams())
			grams.addAll(CharNGramExtractor.getGrams(word, 2));
		if(features.useTrigrams())
			grams.addAll(CharNGramExtractor.getGrams(word, 3));
		if(features.useFourgrams())
			grams.addAll(CharNGramExtractor.getGrams(word, 4));
		if(features.useFivegrams())
			grams.addAll(CharNGramExtractor.getGrams(word, 5));
		return grams;
	}
	
	private List<String> getTokenFeatureList(WordInContext wic)
	{
		return getTokenFeatureList(wic.getWord());
	}
	
	private List<String> getTokenFeatureList(String word)
	{
		List<String> feats = new ArrayList<String>();
		if(word.length() == 1 && !features.useUnigrams())
			feats.add(word);
		if(word.length() == 2 && !features.useBigrams())
			feats.add(word);
		if(word.length() == 3 && !features.useTrigrams())
			feats.add(word);
		if(word.length() == 4 && !features.useFourgrams())
			feats.add(word);
		if(word.length() == 5 && !features.useFivegrams())
			feats.add(word);
		if(word.length() > 5)
			feats.add(word);
		return feats;
	}
	
	List<String> getContextFeatureList(WordInContext wic)
	{
		List<String> feats = new ArrayList<String>();
		for(String preceeding : wic.getAllPreceedingPunctuation())
			feats.add("pre" + punctuationNames.get(preceeding));
		for(String proceeding : wic.getAllProceedingPunctuation())
			feats.add("pro" + punctuationNames.get(proceeding));
				
		if(wic.preceedingNumber)
			feats.add("predigit");
		if(wic.proceedingNumber)
			feats.add("prodigit");
		
		return feats;
	}

	public List<String> featureList(String word)
	{
		List<String> feats = new ArrayList<String>();
		feats.addAll(getNGramsFeatureList(word));
		feats.addAll(getTokenFeatureList(word));
		return feats;
	}
	
	public List<String> featureListBinary(String word)
	{
		Set<String> feats = new HashSet<String>();
		feats.addAll(getNGramsFeatureList(word));
		feats.addAll(getTokenFeatureList(word));
		List<String> ret = new ArrayList<String>();
		ret.addAll(feats);
		return ret;
	}
    private String concatList(List<String> sList, String separator)
    {
        Iterator<String> iter = sList.iterator();
        StringBuilder sb = new StringBuilder();

        while (iter.hasNext())
        {
            sb.append(iter.next()).append( iter.hasNext() ? separator : "");
        }
        return sb.toString();
    }
}