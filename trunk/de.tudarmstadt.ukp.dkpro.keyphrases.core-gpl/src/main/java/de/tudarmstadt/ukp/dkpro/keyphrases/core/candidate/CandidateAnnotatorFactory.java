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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

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
		return createPrimitiveDescription(CandidateAnnotator.class,
				CandidateAnnotator.PARAM_FEATURE_PATH, Token.class.getName(),
				CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, resolve);
	}

	public static AnalysisEngineDescription getKeyphraseCandidateAnnotator_ngram(
			boolean resolve)
		throws ResourceInitializationException
	{
		return createPrimitiveDescription(CandidateAnnotator.class,
				CandidateAnnotator.PARAM_FEATURE_PATH, NGram.class.getName(),
				CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, resolve);
	}

	public static AnalysisEngineDescription getKeyphraseCandidateAnnotator_lemma(
			boolean resolve)
		throws ResourceInitializationException
	{
		return createPrimitiveDescription(CandidateAnnotator.class,
				CandidateAnnotator.PARAM_FEATURE_PATH,
				Lemma.class.getName() + "/value",
				CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, resolve);
	}

	public static AnalysisEngineDescription getKeyphraseCandidateAnnotator_nc(
			boolean resolve)
		throws ResourceInitializationException
	{
		return createPrimitiveDescription(CandidateAnnotator.class,
				CandidateAnnotator.PARAM_FEATURE_PATH, NC.class.getName(),
				CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, resolve);
	}

	public static AnalysisEngineDescription getKeyphraseCandidateAnnotator_ne(
			boolean resolve)
		throws ResourceInitializationException
	{
		return createPrimitiveDescription(CandidateAnnotator.class,
				CandidateAnnotator.PARAM_FEATURE_PATH, NamedEntity.class.getName(),
				CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, resolve);
	}

	public static AnalysisEngineDescription getKeyphraseCandidateAnnotator(
			String featurePath, boolean resolve)
		throws ResourceInitializationException
	{
		return createPrimitiveDescription(CandidateAnnotator.class,
				CandidateAnnotator.PARAM_FEATURE_PATH, featurePath,
				CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, resolve);
	}

}
