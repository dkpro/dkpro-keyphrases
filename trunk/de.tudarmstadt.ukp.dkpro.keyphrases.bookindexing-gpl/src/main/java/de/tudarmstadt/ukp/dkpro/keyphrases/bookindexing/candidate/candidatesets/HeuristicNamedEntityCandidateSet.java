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

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.HeuristicNamedEntityAnnotator;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.base.OverlappingCandidateSet_BaseImpl;

/**
 * Creates an aggregate description used to produce a candidate annotation based
 * on named entities using a simple heuristic working in the english language.
 *
 * @author Mateusz Parzonka
 *
 */
public class HeuristicNamedEntityCandidateSet
extends OverlappingCandidateSet_BaseImpl
{

	@Override
	public Class<? extends Annotation> getType()
	{
		return NamedEntity.class;
	}

	protected AnalysisEngineDescription getNamedEntityAnnotator()
		throws ResourceInitializationException
	{
		return createPrimitiveDescription(HeuristicNamedEntityAnnotator.class);
	}

	@Override
	public AnalysisEngineDescription createPreprocessingComponents(String language)
		throws ResourceInitializationException
	{
		if (!language.equals("en")) {
            throw new IllegalArgumentException(
					"HeuristicNamedEntityAnnotator works only with the english language!");
        }
		AggregateBuilder builder = new AggregateBuilder();
		builder.add(getTokenizer(language));
		builder.add(getTagger(language));
		builder.add(getNamedEntityAnnotator());
		builder.add(getCandidateAnnotator());
		return builder.createAggregateDescription();
	}

	@Override
	public String toString()
	{
		return "CandidateType: HeuristicNamedEntity" + LF + super.toString();
	}

}
