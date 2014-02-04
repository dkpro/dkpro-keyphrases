package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.factory;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.CharacterLengthFilter;

public class CharacterLengthFilterFactory
{

    public static AnalysisEngineDescription getCharacterLengthFilter(int minLength, int maxLength)
        throws ResourceInitializationException
    {
        return AnalysisEngineFactory.createEngineDescription(CharacterLengthFilter.class,
                CharacterLengthFilter.MIN_KEYPHRASE_LENGTH, minLength,
                CharacterLengthFilter.MAX_KEYPHRASE_LENGTH, maxLength);
    }

}
