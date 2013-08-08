package de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class RandomRankingTest
{

    @Test
    public void randomKeyphraseTest() throws Exception {

        System.out.println("Default settings. Maximum.");

        String testDocument = "example sentence funny. second example.";

        AnalysisEngine analysisEngine = AnalysisEngineFactory.createPrimitive(
                RandomRanking.class);

        JCas jcas = setup(testDocument, analysisEngine);
        analysisEngine.process(jcas);

        Set<String> expectedKeyphrases = new HashSet<String>();
        expectedKeyphrases.add("example sentence funny");
        expectedKeyphrases.add("second example");

        int i=0;
        for (Keyphrase k : JCasUtil.select(jcas, Keyphrase.class)) {
            System.out.println(k);
            assertTrue(k.getKeyphrase(), expectedKeyphrases.contains(k.getKeyphrase()));
            i++;
        }
        assertEquals(2,i);

        // I think there is no more left to test for a random annotator ...
    }

    private JCas setup(String testDocument, AnalysisEngine analysisEngine) throws ResourceInitializationException {
        JCas jcas;

        jcas = analysisEngine.newJCas();
        jcas.setDocumentText(testDocument);

        Keyphrase k1 = new Keyphrase(jcas, 0, 22);
        k1.setKeyphrase("example sentence funny");
        k1.addToIndexes();
        assertEquals("example sentence funny", k1.getCoveredText());

        Keyphrase k2 = new Keyphrase(jcas, 24, 38);
        k2.setKeyphrase("second example");
        k2.addToIndexes();
        assertEquals("second example", k2.getCoveredText());

        return jcas;
    }
}