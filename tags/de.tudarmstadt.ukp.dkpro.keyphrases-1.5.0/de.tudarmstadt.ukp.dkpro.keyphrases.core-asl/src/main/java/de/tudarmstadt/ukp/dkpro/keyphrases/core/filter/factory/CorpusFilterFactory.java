package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.factory;

import java.io.File;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.CorpusFilter;

public class CorpusFilterFactory
{

    public static AnalysisEngineDescription getCorpusFilter(String folder, String extension)
        throws ResourceInitializationException
    {
        return AnalysisEngineFactory.createEngineDescription(CorpusFilter.class,
                CorpusFilter.CORPUS_FOLDER, new File(folder), 
                CorpusFilter.FILE_EXTENSION, extension);
    }

}
