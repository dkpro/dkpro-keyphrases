package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.NC;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.base.OverlappingCandidateSet_BaseImpl;

/**
 * Creates an aggregate description used to produce a candidate annotation based
 * on noun chunks.
 *
 * @author Mateusz Parzonka
 *
 */
public class NChunkCandidateSet
	extends OverlappingCandidateSet_BaseImpl
{

	@Override
	public Class<? extends Annotation> getType()
	{
		return NC.class;
	}

	@Override
	public AnalysisEngineDescription createPreprocessingComponents(String language)
		throws ResourceInitializationException
	{
		AggregateBuilder builder = new AggregateBuilder();
		builder.add(getTokenizer(language));
		builder.add(getTagger(language));
		builder.add(getChunker(language));
		builder.add(getCandidateAnnotator());

		return builder.createAggregateDescription();
	}

	@Override
	public String toString()
	{
		return "CandidateType: NChunk" + LF + super.toString();
	}

}