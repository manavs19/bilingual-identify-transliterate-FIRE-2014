package LanguageIdentification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class CharNGramExtractor
{
	public static List<String> getGrams(String word, int gramLength)
	{
		List<String> grams = new ArrayList<String>();
		if(gramLength > 1)
			word = "_" + word + "_";
		for(int i = 0; i <= word.length() - gramLength; ++i)
		{
			grams.add(word.substring(i, i+gramLength));
		}
		return grams;
	}
	
	public static Vector createNGramVector(String word, Set<Integer> gramLengths)
	{
		Vector v = new Vector();
		for(int gramLength : gramLengths)
		{
			List<String> grams = getGrams(word, gramLength);
			for(String gram : grams)
			{
				v.increment(gram);
			}
		}
		return v;
	}
}
