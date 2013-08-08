package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

public class KeyphraseMergerFactory {

    public static AnalysisEngineDescription getKeyphraseMerger_maxLength4()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                KeyphraseMerger.class,
                KeyphraseMerger.PARAM_MAX_LENGTH, 4);
    }
}
