package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.aggregation;

import java.util.List;

import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.aggregation.aggregationstrategy.AggregationStrategy.RankedPhrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.BookIndexPhrase;

/**
 * Aggregates ranked phrases and stores them as BookIndexPhrases in the CAS.
 *
 * @author Mateusz Parzonka
 *
 */
public class BookIndexPhraseAggregationAnnotator
	extends RankedPhraseAggregationAnnotator
{

	/**
	 * Stores {@link RankedPhrase}s in CAS as {@link BookIndexPhrase}s. Note that
	 * the BookIndexPhrases are not associated with a position in the CAS.
	 *
	 * @param aggregatedPhrases
	 * @param jcas
	 */
	@Override
	protected void storePhrasesInCas(List<RankedPhrase> aggregatedPhrases,
			JCas jcas)
	{

		for (RankedPhrase rankedPhrase : aggregatedPhrases) {
			BookIndexPhrase k = new BookIndexPhrase(jcas);
			k.setPhrase(rankedPhrase.getPhrase());
			k.setScore(rankedPhrase.getScore());
			k.addToIndexes();
		}

	}

}
