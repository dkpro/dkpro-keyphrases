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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.base;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.snowball.SnowballStemmer;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerChunkerTT4J;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerPosLemmaTT4J;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerTT4JBase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate.CandidateAnnotator;

/**
 * Provides base implementation for all {@link CandidateSet}s.
 *
 * @author Mateusz Parzonka
 *
 */
public abstract class CandidateSet_BaseImpl
	implements CandidateSet
{

	/**
	 * @return the candidate annotation type
	 */
	@Override
	public abstract Class<? extends Annotation> getType();

	/**
	 * @param language
	 *          unused in the default implementation.
	 * @return the default implementation returns a {@link BreakIteratorSegmenter}
	 * @throws ResourceInitializationException
	 */
	protected AnalysisEngineDescription getTokenizer(String language)
		throws ResourceInitializationException
	{
		return createEngineDescription(BreakIteratorSegmenter.class);
	}

	/**
	 * @param language
	 *          element of {en, de, ru}
	 * @return The default implementation returns a {@link TreeTaggerPosLemmaTT4J}
	 *         .
	 * @throws ResourceInitializationException
	 */
	// TODO TreeTagger is now available for more languages
	protected AnalysisEngineDescription getTagger(String language)
		throws ResourceInitializationException
	{
		if (language.equals("en") || language.equals("de") || language.equals("ru")) {
			return createEngineDescription(
					TreeTaggerPosLemmaTT4J.class,
					TreeTaggerTT4JBase.PARAM_LANGUAGE, language);
		}
		else {
			throw new ResourceInitializationException(new Throwable(
					"No tagger available for language: " + language));
		}
	}

	/**
	 * @param language
	 * @return The default implementation returns a {@link SnowballStemmer}.
	 * @throws ResourceInitializationException
	 */
	protected AnalysisEngineDescription getStemmer(String language)
		throws ResourceInitializationException
	{
		return createEngineDescription(SnowballStemmer.class,
				SnowballStemmer.PARAM_LANGUAGE, language);
	}

	/**
	 * @param language
	 *          element of {en, de, ru}
	 * @return The default implementation returns a {@link TreeTaggerChunkerTT4J}
	 *         .
	 * @throws ResourceInitializationException
	 */
	protected AnalysisEngineDescription getChunker(String language)
		throws ResourceInitializationException
	{
		if (language.equals("en") || language.equals("de") || language.equals("ru")) {
			return createEngineDescription(TreeTaggerChunkerTT4J.class, TreeTaggerChunkerTT4J.PARAM_LANGUAGE,
					language);
		}
		else {
			throw new ResourceInitializationException(new Throwable("No chunker available for language: " + language));
		}
	}

	/**
	 * Default implementation adds {@link KeyphraseCandidate}-annotations.
	 *
	 * @return candidate annotator
	 * @throws ResourceInitializationException
	 */
	protected AnalysisEngineDescription getCandidateAnnotator()
		throws ResourceInitializationException
	{
		return createEngineDescription(CandidateAnnotator.class,
				CandidateAnnotator.PARAM_FEATURE_PATH, getFeaturePath(),
				CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, false);
	}

	/**
	 * The base implementation returns the name of the type as feature path.
	 */
	@Override
	public String getFeaturePath()
	{
		return getType().getName();
	}

}

