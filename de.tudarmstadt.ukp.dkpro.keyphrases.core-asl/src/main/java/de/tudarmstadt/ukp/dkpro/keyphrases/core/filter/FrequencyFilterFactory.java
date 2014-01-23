package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.resource.ResourceInitializationException;

public class FrequencyFilterFactory
{

    public static AnalysisEngineDescription getFrequencyFilter(int n)
        throws ResourceInitializationException
    {
        return AnalysisEngineFactory.createEngineDescription(FrequencyFilter.class,
                FrequencyFilter.FREQUENCY, n);

    }

}
