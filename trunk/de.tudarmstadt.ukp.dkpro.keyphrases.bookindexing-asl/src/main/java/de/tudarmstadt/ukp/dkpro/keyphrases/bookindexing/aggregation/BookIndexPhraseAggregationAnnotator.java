/*******************************************************************************
 * Copyright 2013
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
