package de.tudarmstadt.ukp.dkpro.keyphrases.ranking;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

public class PageRankRankingFactory {

    /**
     * Method for generating AnalysisEngineDescriptor
     * With CooccurrenceGraph: Works and is sensible.
     * With LexicalSemanticGraph: Works but is not sensible.
     * @return Descriptor for annotator with weighted PageRank algorithm
     * @throws ResourceInitializationException
     */
    public static AnalysisEngineDescription getPageRankRanking_weighted()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                PageRankRanking.class,
                PageRankRanking.PARAM_WEIGHTED, true);
    }

    /**
     * Method for generating AnalysisEngineDescriptor
     * With CooccurrenceGraph: Works and is sensible. Information are lost and cooccurrences wit the frequency 1 are rated as high as those with a frequency of 100.
     * With LexicalSemanticGraph: Works but is not sensible. LSG are often fully connected.
     * @return Descriptor for annotator with unweighted PageRank algorithm
     * @throws ResourceInitializationException
     */
    public static AnalysisEngineDescription getPageRankRanking_unweighted()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                PageRankRanking.class,
                PageRankRanking.PARAM_WEIGHTED, false);
    }
}
