package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking.PositionRanking;

public class PositionBaselineExtractor
	extends BaselineExtractor_ImplBase
{

	@Override
	protected AnalysisEngineDescription createRanker()
		throws ResourceInitializationException
	{
		return createPrimitiveDescription(PositionRanking.class);
	}
}
