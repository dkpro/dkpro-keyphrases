package de.tudarmstadt.ukp.dkpro.keyphrases.ranking;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

public class NodeDegreeRankingFactory {

    /**
     * Method for generating AnalysisEngineDescriptor
     * With CooccurrenceGraph: Works and is sensible. Collocations may be rated very high.
     * With LexicalSemanticGraph: Works and is sensible.
     * @return Descriptor for annotator with weighted NodeDegree algorithm
     * @throws ResourceInitializationException
     */
	public static AnalysisEngineDescription getNodeDegreeRanking_weighted()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                NodeDegreeRanking.class,
                NodeDegreeRanking.PARAM_WEIGHTED, true);
    }

    /**
     * Method for generating AnalysisEngineDescriptor
     * With CooccurrenceGraph: Works and is sensible. Information are lost and cooccurrences wit the frequency 1 are rated as high as those with a frequency of 100.
     * With LexicalSemanticGraph: Works but is not sensible. LSG are often fully connected.
     * @return Descriptor for annotator with unweighted NodeDegree algorithm
     * @throws ResourceInitializationException
     */
    public static AnalysisEngineDescription getNodeDegreeRanking_unweighted()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                NodeDegreeRanking.class,
                NodeDegreeRanking.PARAM_WEIGHTED, false);
    }
}
