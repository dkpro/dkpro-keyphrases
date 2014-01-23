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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.NGram;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.NC;

/**
 *
 * @author zesch, Mateusz Parzonka
 *
 */
public class CandidateAnnotatorFactory
{

	public static AnalysisEngineDescription getKeyphraseCandidateAnnotator_token(
			boolean resolve)
		throws ResourceInitializationException
	{
		return createEngineDescription(CandidateAnnotator.class,
				CandidateAnnotator.PARAM_FEATURE_PATH, Token.class.getName(),
				CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, resolve);
	}

	public static AnalysisEngineDescription getKeyphraseCandidateAnnotator_ngram(
			boolean resolve)
		throws ResourceInitializationException
	{
		return createEngineDescription(CandidateAnnotator.class,
				CandidateAnnotator.PARAM_FEATURE_PATH, NGram.class.getName(),
				CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, resolve);
	}

	public static AnalysisEngineDescription getKeyphraseCandidateAnnotator_lemma(
			boolean resolve)
		throws ResourceInitializationException
	{
		return createEngineDescription(CandidateAnnotator.class,
				CandidateAnnotator.PARAM_FEATURE_PATH,
				Lemma.class.getName() + "/value",
				CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, resolve);
	}

	public static AnalysisEngineDescription getKeyphraseCandidateAnnotator_nc(
			boolean resolve)
		throws ResourceInitializationException
	{
		return createEngineDescription(CandidateAnnotator.class,
				CandidateAnnotator.PARAM_FEATURE_PATH, NC.class.getName(),
				CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, resolve);
	}

	public static AnalysisEngineDescription getKeyphraseCandidateAnnotator_ne(
			boolean resolve)
		throws ResourceInitializationException
	{
		return createEngineDescription(CandidateAnnotator.class,
				CandidateAnnotator.PARAM_FEATURE_PATH, NamedEntity.class.getName(),
				CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, resolve);
	}

	public static AnalysisEngineDescription getKeyphraseCandidateAnnotator(
			String featurePath, boolean resolve)
		throws ResourceInitializationException
	{
		return createEngineDescription(CandidateAnnotator.class,
				CandidateAnnotator.PARAM_FEATURE_PATH, featurePath,
				CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, resolve);
	}

}
