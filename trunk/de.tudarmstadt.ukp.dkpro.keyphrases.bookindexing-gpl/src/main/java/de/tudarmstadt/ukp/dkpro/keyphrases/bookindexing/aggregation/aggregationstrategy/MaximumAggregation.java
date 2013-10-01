/*******************************************************************************
 * Copyright 2013

 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.0.txt
 ******************************************************************************/
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.aggregation.aggregationstrategy;

import java.util.*;

import org.apache.uima.util.Level;

import com.google.common.collect.Table;

/**
 * Takes the maximum score of ranked phrases contained in all segments.
 *
 * @author Mateusz Parzonka
 *
 */
public class MaximumAggregation
	extends AggregationStrategy
{

	@Override
	public List<RankedPhrase> aggregatePhrases(
			Table<String, Integer, Double> phraseSegmentTable)
	{

		List<RankedPhrase> rankedPhrases = new ArrayList<RankedPhrase>();

		if (getLogger() != null && getLogger().isLoggable(Level.CONFIG))
			getLogger().log(
					Level.CONFIG,
					String.format(Locale.US, "Using AggregationStrategy: "
							+ this.getClass().getSimpleName()));

		for (String phrase : phraseSegmentTable.rowKeySet()) {
			Collection<Double> segmentScores = phraseSegmentTable.row(phrase)
					.values();
			Double newScore = maximum(segmentScores);

			if (getLogger() != null && getLogger().isLoggable(Level.FINEST))
				getLogger().log(
						Level.FINEST,
						String.format(Locale.US,
								"Maximum score for phrase %s in %d segments was %.3f.", phrase,
								segmentScores.size(), newScore));

			rankedPhrases.add(new RankedPhrase(phrase, newScore));
		}
		return rankedPhrases;

	}

	/**
	 * Calculates the maximum double in a collection of doubles.
	 *
	 * @param scores
	 *          collection of doubles
	 * @return the maximum double
	 */
	private Double maximum(Collection<Double> scores)
	{
		double max = 0D;
		for (Double score : scores) {
			if (score > max)
				max = score;
		}
		return max;
	}

}