package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Stem;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.base.PosFilteredCandidateSet_BaseImpl;

/**
 * Creates an aggregate description used to produce a candidate annotation based
 * on stems.
 *
 * @author Mateusz Parzonka
 *
 */
public class StemCandidateSet
	extends PosFilteredCandidateSet_BaseImpl
{

	@Override
	public Class<? extends Annotation> getType()
	{
		return Stem.class;
	}

	@Override
	public AnalysisEngineDescription createPreprocessingComponents(String language)
		throws ResourceInitializationException
	{
		AggregateBuilder builder = new AggregateBuilder();
		builder.add(getTokenizer(language));
		builder.add(getTagger(language));
		builder.add(getStemmer(language));
		if (getPosToKeep().size() > 0) {
            builder.add(getPosFilter());
        }
		return builder.createAggregateDescription();
	}

	@Override
	public String toString()
	{
		return "CandidateType: Stem" + LF + super.toString();
	}

}