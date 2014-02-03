package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.factory;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.PosSequenceFilter;

public class PosSequenceFilterFactory
{

    public static AnalysisEngineDescription createPosSequenceFilter(String... posSequences)
        throws ResourceInitializationException
    {
        return AnalysisEngineFactory.createEngineDescription(
                PosSequenceFilter.class,
                PosSequenceFilter.PARAM_POS_PATTERNS, posSequences);
    }

}
