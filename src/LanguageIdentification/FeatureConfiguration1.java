package LanguageIdentification;

public class FeatureConfiguration1
{
	boolean useUnigrams = true;
	boolean useBigrams = true;
	boolean useTrigrams = true;
	boolean useFourgrams = true;
	boolean useFivegrams = true;
	boolean useToken = true;
	
	boolean usePrePunctuation = false;
	boolean usePostPunctuation = false;
	
	public FeatureConfiguration1()
	{
		
	}
	
	public FeatureConfiguration1(boolean unigrams, boolean bigrams, boolean trigrams, boolean fourgrams, boolean fivegrams, boolean token)
	{
		this.useUnigrams = unigrams;
		this.useBigrams = bigrams;
		this.useTrigrams = trigrams;
		this.useFourgrams = fourgrams;
		this.useFivegrams = fivegrams;
		this.useToken = token;
	}

	public boolean useUnigrams()
	{
		return useUnigrams;
	}

	public boolean useBigrams()
	{
		return useBigrams;
	}

	public boolean useTrigrams()
	{
		return useTrigrams;
	}

	public boolean useFourgrams()
	{
		return useFourgrams;
	}

	public boolean useFivegrams()
	{
		return useFivegrams;
	}
	
	public boolean useToken()
	{
		return useToken;
	}

	public boolean usePrePunctuation()
	{
		return usePrePunctuation;
	}

	public void setUsePrePunctuation(boolean usePrePunctuation)
	{
		this.usePrePunctuation = usePrePunctuation;
	}

	public boolean usePostPunctuation()
	{
		return usePostPunctuation;
	}

	public void setUsePostPunctuation(boolean usePostPunctuation)
	{
		this.usePostPunctuation = usePostPunctuation;
	}
	
	
}	
