package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.matchingstrategy;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.fit.component.Resource_ImplBase;
import org.apache.uima.util.Level;

/**
 *
 * @author Mateusz Parzonka
 *
 */
public abstract class MatchingStrategy
	extends Resource_ImplBase
{

	/**
	 * Returns a set containing the gold phrases that are covered by the given
	 * phrase.
	 *
	 * @param phrase
	 * @return a set of matches. When no matches had been found, then the set is
	 *         empty.
	 */
	public Set<String> getCovered(String phrase, Set<String> goldSet)
	{
		Set<String> matches = new HashSet<String>();
		for (String goldPhrase : goldSet) {
			if (isMatch(phrase, goldPhrase)) {
				if (getUimaContext().getLogger().isLoggable(Level.FINEST)) {
                    getUimaContext().getLogger().log(Level.FINEST, String.format("[%s] and [%s] match.", phrase, goldPhrase));
                }
				matches.add(goldPhrase);
			}
			else {
				if (getUimaContext().getLogger().isLoggable(Level.FINEST)) {
                    getUimaContext().getLogger().log(Level.FINEST, String.format("[%s] and [%s] match NOT.", phrase, goldPhrase));
                }
			}
		}
		return matches;
	}

	public abstract boolean isMatch(String phrase, String goldPhrase);

	@Override
	public String toString()
	{
		return getClass().getSimpleName();
	}

}
