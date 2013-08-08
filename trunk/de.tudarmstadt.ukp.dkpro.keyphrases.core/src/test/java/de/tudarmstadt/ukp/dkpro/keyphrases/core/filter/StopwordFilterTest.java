package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate;

public class StopwordFilterTest{

    private final String testDocument = "against2 sentence2 above2. across2 against2. 1.";

    @Test
    public void FilterTestKeyphrases() throws Exception {

        AnalysisEngine analysisEngine = AnalysisEngineFactory.createPrimitive(
                StopwordFilter.class,
                StopwordFilter.PARAM_STOPWORD_LIST, "classpath:/stopwords/english_stopwords.txt");

        JCas jcas = setupKeyphrases(testDocument, analysisEngine);

        // as there are keyphrases,
        //   the stopword filter is applied to keyphrases, not to candidates
        Set<String> expectedResults = new HashSet<String>();
        expectedResults.add("sentence");

        Set<String> expectedCandidateResults = new HashSet<String>();
        expectedCandidateResults.add("against sentence above");
        expectedCandidateResults.add("across against");
        expectedCandidateResults.add("1");

        analysisEngine.process(jcas);

        int i=0;
        for (Keyphrase k : JCasUtil.select(jcas, Keyphrase.class)) {
            System.out.println(k);
            assertTrue(expectedResults.contains(k.getKeyphrase()));
            assertEquals(9, k.getBegin());
            assertEquals(18, k.getEnd());
            i++;
        }
        assertEquals(1,i);

        int j=0;
        FSIterator<Annotation> candidateIter = jcas.getAnnotationIndex(KeyphraseCandidate.type).iterator();
        while (candidateIter.hasNext()) {
            KeyphraseCandidate kc = (KeyphraseCandidate) candidateIter.next();
            System.out.println(kc);
            assertTrue(kc.getKeyphrase(), expectedCandidateResults.contains(kc.getKeyphrase()));
            j++;
        }
        assertEquals(3,j);
    }

    @Test
    public void FilterTestCandidates() throws Exception {
        AnalysisEngine analysisEngine = AnalysisEngineFactory.createPrimitive(
                StopwordFilter.class,
                StopwordFilter.PARAM_STOPWORD_LIST, "classpath:/stopwords/english_stopwords.txt");

        JCas jcas = setupCandidates(testDocument, analysisEngine);

        // as there are only candidates, the stopword filter is applied on them
        Set<String> expectedCandidateResults = new HashSet<String>();
        expectedCandidateResults.add("sentence");

        analysisEngine.process(jcas);

        int i=0;
        for (Keyphrase k : JCasUtil.select(jcas, Keyphrase.class)) {
            System.out.println(k);
            i++;
        }
        assertEquals(0,i);

        int j=0;
        for (KeyphraseCandidate kc : JCasUtil.select(jcas, KeyphraseCandidate.class)) {
            System.out.println(kc);
            assertTrue(kc.getKeyphrase(), expectedCandidateResults.contains(kc.getKeyphrase()));
            j++;
        }
        assertEquals(1,j);
    }

    private
    JCas setupCandidates(
    		String testDocument,
    		AnalysisEngine analysisEngine)
    throws IOException, InvalidXMLException, ResourceInitializationException
    {
        JCas jcas;

        jcas = analysisEngine.newJCas();
        jcas.setDocumentText(testDocument);

        setupLemmas(jcas);
        setupCandidates(jcas);

        return jcas;
    }

    private
    JCas setupKeyphrases(
    		String testDocument,
    		AnalysisEngine analysisEngine)
    throws IOException, InvalidXMLException, ResourceInitializationException
    {
        JCas jcas;

        jcas = analysisEngine.newJCas();
        jcas.setDocumentText(testDocument);

        setupLemmas(jcas);
        setupCandidates(jcas);
        setupKeyphrases(jcas);

        return jcas;
    }

    private void setupLemmas(JCas jcas) {
        Lemma l1 = new Lemma(jcas, 0, 8);
        l1.setValue("against");
        l1.addToIndexes();
        assertEquals("against2", l1.getCoveredText());

        Lemma l2 = new Lemma(jcas, 9, 18);
        l2.setValue("sentence");
        l2.addToIndexes();
        assertEquals("sentence2", l2.getCoveredText());

        Lemma l3 = new Lemma(jcas, 19, 25);
        l3.setValue("above");
        l3.addToIndexes();
        assertEquals("above2", l3.getCoveredText());

        Lemma l4 = new Lemma(jcas, 27, 34);
        l4.setValue("across");
        l4.addToIndexes();
        assertEquals("across2", l4.getCoveredText());

        Lemma l5 = new Lemma(jcas, 35, 43);
        l5.setValue("against");
        l5.addToIndexes();
        assertEquals("against2", l5.getCoveredText());

        Lemma l6 = new Lemma(jcas, 45, 46);
        l6.setValue("1");
        l6.addToIndexes();
        assertEquals("1", l6.getCoveredText());
    }

    private void setupCandidates(JCas jcas) {
        KeyphraseCandidate kc1 = new KeyphraseCandidate(jcas, 0, 25);
        kc1.setKeyphrase("against sentence above");
        kc1.addToIndexes();
        assertEquals("against2 sentence2 above2", kc1.getCoveredText());

        KeyphraseCandidate kc2 = new KeyphraseCandidate(jcas, 27, 43);
        kc2.setKeyphrase("across against");
        kc2.addToIndexes();
        assertEquals("across2 against2", kc2.getCoveredText());

        KeyphraseCandidate kc3 = new KeyphraseCandidate(jcas, 45, 46);
        kc3.setKeyphrase("1");
        kc3.addToIndexes();
        assertEquals("1", kc3.getCoveredText());
    }

    private void setupKeyphrases(JCas jcas) {
        Keyphrase k1 = new Keyphrase(jcas, 0, 25);
        k1.setKeyphrase("against sentence above");
        k1.addToIndexes();
        assertEquals("against2 sentence2 above2", k1.getCoveredText());

        Keyphrase k2 = new Keyphrase(jcas, 27, 43);
        k2.setKeyphrase("across against");
        k2.addToIndexes();
        assertEquals("across2 against2", k2.getCoveredText());

        Keyphrase k3 = new Keyphrase(jcas, 45, 46);
        k3.setKeyphrase("1");
        k3.addToIndexes();
        assertEquals("1", k3.getCoveredText());

    }
}
