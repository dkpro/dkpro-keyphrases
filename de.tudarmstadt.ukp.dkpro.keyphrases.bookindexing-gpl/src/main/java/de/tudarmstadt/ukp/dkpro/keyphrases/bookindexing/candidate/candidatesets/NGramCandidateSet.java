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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.NGram;
import de.tudarmstadt.ukp.dkpro.core.ngrams.NGramAnnotator;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.base.CandidateSet;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.base.CandidateSet_BaseImpl;

/**
 * Creates an aggregate description used to produce a candidate annotation based
 * on ngrams.
 *
 * @author Mateusz Parzonka
 *
 */
public class NGramCandidateSet
	extends CandidateSet_BaseImpl
{
	private int n;

	@Override
	public Class<? extends Annotation> getType()
	{
		return NGram.class;
	}

	/**
	 * Sets the n in the ngram.
	 *
	 * @param n
	 *
	 * @return the candidate set with set n
	 */
	public CandidateSet setN(int n) {
		this.n = n;
		return this;
	}

	@Override
	public AnalysisEngineDescription createPreprocessingComponents(String language)
		throws ResourceInitializationException
	{
		AggregateBuilder builder = new AggregateBuilder();
		builder.add(getTokenizer(language));
		builder.add(getTagger(language));
		builder.add(createPrimitiveDescription(
				NGramAnnotator.class,
				NGramAnnotator.PARAM_N, n));
		builder.add(getCandidateAnnotator());

		return builder.createAggregateDescription();
	}

	public String getConfigurationDetails()
	{
		return "CandidateType: NGram" + LF + "N: " + n + LF;
	}

}
