package de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking;

import static org.junit.Assert.assertEquals;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.frequency.tfidf.type.Tfidf;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking.TfidfRanking.TfidfAggregate;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class TfidfRankingTest
{
    // assertEquals on doubles needs an epsilon
    protected static final double EPSILON = 0.000001;

    @Test
    public void defaultTest() throws Exception {

        System.out.println("Default settings. Maximum.");

        String testDocument = "example sentence funny. second example.";

        AnalysisEngine analysisEngine = AnalysisEngineFactory.createPrimitive(
                TfidfRanking.class);

        JCas jcas = setup(testDocument, analysisEngine);
        analysisEngine.process(jcas);

        int i=0;
        for (Keyphrase k : JCasUtil.select(jcas, Keyphrase.class)) {
            System.out.println(k);
            if (i==0) {
                assertEquals("example sentence funny", k.getKeyphrase());
                assertEquals(1.0, k.getScore(), EPSILON);
            }
            if (i==1) {
                assertEquals("second example", k.getKeyphrase());
                assertEquals(0.5, k.getScore(), EPSILON);
            }
            i++;
        }
        assertEquals(2,i);
    }

    @Test
    public void averageTest() throws Exception {

        System.out.println("Average tfidf.");

        String testDocument = "example sentence funny. second example.";

        AnalysisEngine analysisEngine = AnalysisEngineFactory.createPrimitive(
                TfidfRanking.class,
                TfidfRanking.PARAM_AGGREGATE, TfidfAggregate.avg.toString()
        );

        JCas jcas = setup(testDocument, analysisEngine);

        analysisEngine.process(jcas);

        int i=0;
        for (Keyphrase k : JCasUtil.select(jcas, Keyphrase.class)) {
            System.out.println(k);
            if (i==0) {
                assertEquals("example sentence funny", k.getKeyphrase());
                assertEquals(0.6, k.getScore(), EPSILON);
            }
            if (i==1) {
                assertEquals("second example", k.getKeyphrase());
                assertEquals(0.45, k.getScore(), EPSILON);
            }
            i++;
        }
        assertEquals(2,i);
    }

	private JCas setup(String testDocument, AnalysisEngine analysisEngine)
		throws ResourceInitializationException
	{
        JCas jcas;

        jcas = analysisEngine.newJCas();
        jcas.setDocumentText(testDocument);

        Tfidf t1 = new Tfidf(jcas, 0, 7);
        t1.setTfidfValue(1.0);
        t1.addToIndexes();
        assertEquals("example", t1.getCoveredText());

        Tfidf t2 = new Tfidf(jcas, 8, 16);
        t2.setTfidfValue(0.5);
        t2.addToIndexes();
        assertEquals("sentence", t2.getCoveredText());

        Tfidf t3 = new Tfidf(jcas, 17, 22);
        t3.setTfidfValue(0.3);
        t3.addToIndexes();
        assertEquals("funny", t3.getCoveredText());

        Tfidf t4 = new Tfidf(jcas, 24, 30);
        t4.setTfidfValue(0.4);
        t4.addToIndexes();
        assertEquals("second", t4.getCoveredText());

        Tfidf t5 = new Tfidf(jcas, 31, 38);
        t5.setTfidfValue(0.5);
        t5.addToIndexes();
        assertEquals("example", t5.getCoveredText());

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