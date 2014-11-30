package LanguageIdentification;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordInContextReader
{
	boolean hideLabels = false;
	
	public WordInContextReader() {}
	
	public WordInContextReader(boolean hideLabels)
	{
		this.hideLabels = hideLabels;
	}
	
	public List<WordInContext> getWordsInContext(String fullText)
	{
		List<WordInContext> wordsInContext = new ArrayList<WordInContext>();
		
		fullText = removeCommentLines(fullText);
		
		Matcher m = Pattern.compile("(\\[\\[([a-z]{3})\\]\\])|([^\\p{Punct}\\s]+)").matcher(fullText);
		
		WordInContext previousWord = null;
		String currentLabel = null;
		int previousEnd = 0;
		String previousContext = "";
		while(m.find())
		{
           // System.out.println(m.group(3));
			if(m.group(1) != null)
			{
                //System.out.println(m.group(1) +" "+ m.group(2));
				if(!hideLabels)
					currentLabel = m.group(2);
				int currentStart = m.start(2)-2;
				previousContext = fullText.substring(previousEnd, currentStart);
				previousEnd = m.end(2)+2;
			}
			else
			{
				String word = m.group(3);
				
				if(word.matches(".*\\d.*")) // don't count 1990s (e.g.) as a word
					continue;
				
				int currentStart = m.start(3); 
				previousContext += fullText.substring(previousEnd, currentStart);
				previousEnd = m.end(3);
				
				if(previousWord != null)
				{
					previousWord.addProceedingString(previousContext);
					wordsInContext.add(previousWord);
				}
				
				previousWord = new WordInContext(word, currentLabel);
				previousWord.addPreceedingString(previousContext);
				previousContext = "";
			}
		}
		
		String proceedingContext = fullText.substring(previousEnd, fullText.length());
		previousWord.addProceedingString(proceedingContext);
		wordsInContext.add(previousWord);
		
		return wordsInContext;
	}

	private String removeCommentLines(String fullText)
	{
		String withoutComments = "";
		for(String line : fullText.split("\\r?\\n"))
		{
			if(line.matches("^\\s*#.*$"))
				continue;
			withoutComments += line + " ";
		}
		return withoutComments;
	}
}
