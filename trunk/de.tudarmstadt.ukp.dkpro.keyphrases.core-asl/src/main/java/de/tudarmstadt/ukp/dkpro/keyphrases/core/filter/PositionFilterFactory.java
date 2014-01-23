package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.resource.ResourceInitializationException;

public class PositionFilterFactory
{

    public static AnalysisEngineDescription getPositionFilter(int beginIndex, int endIndex)
        throws ResourceInitializationException
    {
        return AnalysisEngineFactory.createEngineDescription(PositionFilter.class,
                PositionFilter.BEGIN_INDEX, beginIndex, PositionFilter.END_INDEX, endIndex);
    }

}
