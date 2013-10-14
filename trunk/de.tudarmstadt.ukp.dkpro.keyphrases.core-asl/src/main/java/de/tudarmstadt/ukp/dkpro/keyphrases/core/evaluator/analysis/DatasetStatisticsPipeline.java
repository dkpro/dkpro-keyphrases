package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.analysis;

import static de.tudarmstadt.ukp.dkpro.core.api.io.ResourceCollectionReaderBase.INCLUDE_PREFIX;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.KeyphraseDatasetStatistics;

public class DatasetStatisticsPipeline
{

    public static void main(String[] args) throws UIMAException, IOException
    {

        String dkproHome = System.getenv("DKPRO_HOME");
        
        String dataset = dkproHome + "/corpora/MedForumCorpus_gold/";
        String text = "*.txt";
        String gold = ".txt.intersection";

        System.out.println(dataset);
        
        CollectionReader reader = createReader(
                TextReader.class,
                TextReader.PARAM_SOURCE_LOCATION, dataset,
                TextReader.PARAM_PATTERNS, 
                    new String[] { INCLUDE_PREFIX +  text},
                TextReader.PARAM_LANGUAGE, "de");
        
        AnalysisEngine segmenter = createEngine(BreakIteratorSegmenter.class);
        
        AnalysisEngine analyzer = createEngine(
                KeyphraseDatasetStatistics.class,
                KeyphraseDatasetStatistics.PARAM_LOWERCASE, true,
                KeyphraseDatasetStatistics.PARAM_GOLD_SUFFIX, gold);
        
        SimplePipeline.runPipeline(reader, segmenter, analyzer);
    }
    

}
