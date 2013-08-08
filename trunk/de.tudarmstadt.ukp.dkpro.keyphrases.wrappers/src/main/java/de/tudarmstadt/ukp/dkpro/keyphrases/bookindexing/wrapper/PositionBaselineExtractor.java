package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createAggregateDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate.Candidate2KeyphraseConverter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking.PositionRanking;

/**
 * Provides a baseline for book-index-phrase-extraction by ranking the phrases
 * highest, that appear first in a segment.
 * 
 * @author Mateusz Parzonka
 * 
 */
public class PositionBaselineExtractor
	extends BookIndexPhraseExtractor_BaseImpl
{

	@Override
	protected AnalysisEngineDescription createRanker()
		throws ResourceInitializationException
	{
		return createAggregateDescription(
						createPrimitiveDescription(Candidate2KeyphraseConverter.class),
						createPrimitiveDescription(PositionRanking.class));
	}

}
