package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.resource.ResourceInitializationException;

public class CharacterLengthFilterFactory
{

    public static AnalysisEngineDescription getCharacterLengthFilter(int length)
        throws ResourceInitializationException
    {
        return AnalysisEngineFactory.createEngineDescription(CharacterLengthFilter.class,
                CharacterLengthFilter.CANDIDATE_LENGTH, length);
    }

}
