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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createAggregateDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate.Candidate2KeyphraseConverter;

public abstract class BaselineExtractor_ImplBase
	extends KeyphraseExtractor_ImplBase
{

	protected abstract AnalysisEngineDescription createRanker()
		throws ResourceInitializationException;

	@Override
	protected AnalysisEngineDescription createKeyphraseExtractorAggregate()
		throws ResourceInitializationException
	{

		return createAggregateDescription(
				createPreprocessingComponents(getCandidate().getType()),
				createPrimitiveDescription(Candidate2KeyphraseConverter.class),
				createRanker(),
				createPostprocessingComponents());
	}
}
