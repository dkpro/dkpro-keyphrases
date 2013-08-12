package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.matchingstrategy;

/**
 *
 * @author Mateusz Parzonka
 * 
 */
public class MatchingExact
	extends MatchingStrategy
{

	@Override
	public boolean isMatch(String phrase, String goldPhrase)
	{
		return phrase.equals(goldPhrase);
	}

}
