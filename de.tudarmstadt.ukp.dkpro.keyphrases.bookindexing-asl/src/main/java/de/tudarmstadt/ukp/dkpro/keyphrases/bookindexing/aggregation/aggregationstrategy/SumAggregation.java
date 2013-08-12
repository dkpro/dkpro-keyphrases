package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.aggregation.aggregationstrategy;

import java.util.*;

import org.apache.uima.util.Level;

import com.google.common.collect.Table;

/**
 * Takes for every phrase the sum of all scores in all segments it occurs.
 *
 * @author Mateusz Parzonka
 *
 */
public class SumAggregation
	extends AggregationStrategy
{

	@Override
	public List<RankedPhrase> aggregatePhrases(
			Table<String, Integer, Double> phraseSegmentTable)
	{

		List<RankedPhrase> rankedPhrases = new ArrayList<RankedPhrase>();

		for (String phrase : phraseSegmentTable.rowKeySet()) {
			Collection<Double> segmentScores = phraseSegmentTable.row(phrase)
					.values();
			Double newScore = sum(segmentScores);
			if (getLogger() != null && getLogger().isLoggable(Level.FINEST))
				getLogger().log(
						Level.FINEST,
						String.format(Locale.US,
								"Summed score for phrase %s in %d segments was %.3f.", phrase,
								segmentScores.size(), newScore));

			rankedPhrases.add(new RankedPhrase(phrase, newScore));

		}
		return rankedPhrases;

	}

	private Double sum(Collection<Double> scores)
	{
		double sum = 0D;
		for (Double score : scores) {
			sum += score;
		}
		return sum;
	}

}
