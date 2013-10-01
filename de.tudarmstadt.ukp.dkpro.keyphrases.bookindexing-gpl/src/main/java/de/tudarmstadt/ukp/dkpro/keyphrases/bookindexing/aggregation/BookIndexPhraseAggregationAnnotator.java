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
