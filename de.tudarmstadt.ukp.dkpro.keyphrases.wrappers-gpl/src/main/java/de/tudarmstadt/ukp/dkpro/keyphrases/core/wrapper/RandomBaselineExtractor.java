package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking.RandomRanking;

public class RandomBaselineExtractor
	extends BaselineExtractor_ImplBase
{
	@Override
	protected AnalysisEngineDescription createRanker()
		throws ResourceInitializationException
	{
		return createPrimitiveDescription(RandomRanking.class);
	}
}
