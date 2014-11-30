package LanguageIdentification;

import java.util.HashSet;
import java.util.Set;

public class WordInContext
{
	String word;
	String label;
	
	boolean preceedingNumber = false;
	boolean proceedingNumber = false;
	Set<String> preceedingPunctuation = new HashSet<String>();
	Set<String> proceedingPunctuation = new HashSet<String>();
	
	public WordInContext(String word, String label)
	{
		this.word = word;
		if(label != null)
			this.label = label;
		else
			this.label = "";
	}

	public String getLabel()
	{
		return label;
	}

	public String getWord()
	{
		return word;
	}
	
	public boolean hasPreceedingPunctuation(String punctuationMark)
	{
		return preceedingPunctuation.contains(punctuationMark);
	}
	
	public boolean hasProceedingPunctuation(String punctuationMark)
	{
		return proceedingPunctuation.contains(punctuationMark);
	}
	
	public Set<String> getAllPreceedingPunctuation()
	{
		return preceedingPunctuation;
	}
	
	public Set<String> getAllProceedingPunctuation()
	{
		return proceedingPunctuation;
	}
	
	public void addPreceedingString(String str)
	{
		for(int k=0; k<str.length(); ++k)
		{
			String character = str.substring(k, k+1);
			if(character.matches(".*[0-9].*"))
			{
				preceedingNumber = true;
				continue;
			}
			if(character.matches(".*[A-Za-z\\s].*"))
				continue;
			preceedingPunctuation.add(character);
		}
	}
	
	public void addProceedingString(String str)
	{
		for(int k=0; k<str.length(); ++k)
		{
			String character = str.substring(k, k+1);
			if(character.matches(".*[0-9].*"))
			{
				proceedingNumber = true;
				continue;
			}
			if(character.matches(".*[A-Za-z\\s].*"))
				continue;
			proceedingPunctuation.add(character);
		}
	}
}
