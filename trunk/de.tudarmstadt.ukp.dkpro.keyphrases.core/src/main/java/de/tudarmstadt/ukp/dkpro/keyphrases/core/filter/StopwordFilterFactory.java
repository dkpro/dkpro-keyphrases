package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

public class StopwordFilterFactory {

    public static AnalysisEngineDescription getStopwordFilter_english()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                StopwordFilter.class,
                StopwordFilter.PARAM_STOPWORD_LIST, "classpath:/stopwords/english_stopwords.txt");
    }
}
