package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.aggregation.aggregationstrategy;

import java.util.List;

import org.apache.uima.fit.component.Resource_ImplBase;

import com.google.common.collect.Table;

/**
 * The aggregation strategy defines the method of extracting and merging ranked
 * phrases from segments. Segments are modeled as columns in tables.
 *
 * @author Mateusz Parzonka
 *
 */
public abstract class AggregationStrategy
	extends Resource_ImplBase
{

	/**
	 * The phrases are aggregated and extracted from the Segments and added to the
	 * document.
	 *
	 * @param phrasesPerSegmentTable
	 * @param document
	 */
	public abstract List<RankedPhrase> aggregatePhrases(
			Table<String, Integer, Double> phrasesPerSegmentTable);

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName();
	}

	public final static class RankedPhrase
	{

		private final String phrase;
		private final Double score;

		public RankedPhrase(final String phrase, final Double score)
		{
			super();
			this.phrase = phrase;
			this.score = score;
		}

		/**
		 * @return the phrase
		 */
		public String getPhrase()
		{
			return phrase;
		}

		/**
		 * @return the score
		 */
		public Double getScore()
		{
			return score;
		}

	}

}
