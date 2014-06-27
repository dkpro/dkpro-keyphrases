package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.ngrams.NGramAnnotator;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate.CandidateAnnotatorFactory;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.factory.PosSequenceFilterFactory;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class PosSequenceFilterTest
{

    private final static String testDocument = "Mr Smith knows plug and play methodology.";

    @Test
    public void test()
        throws ResourceInitializationException, AnalysisEngineProcessException
    {

        AnalysisEngine engine = AnalysisEngineFactory.createEngine(
                AnalysisEngineFactory.createEngineDescription(
                    AnalysisEngineFactory.createEngineDescription(BreakIteratorSegmenter.class),
                    AnalysisEngineFactory.createEngineDescription(StanfordPosTagger.class),
                    AnalysisEngineFactory.createEngineDescription(StanfordLemmatizer.class),
                    AnalysisEngineFactory.createEngineDescription(NGramAnnotator.class,
                            NGramAnnotator.PARAM_N, 4),
                    CandidateAnnotatorFactory.getKeyphraseCandidateAnnotator_ngram(false),
                    PosSequenceFilterFactory.createPosSequenceFilter(new String[]{"N_C_N","N_N"})));
        JCas jcas = engine.newJCas();
        jcas.setDocumentLanguage("en");
        jcas.setDocumentText(testDocument);
        engine.process(jcas);
        List<String> expectedResults = new ArrayList<String>();
        expectedResults.add("Mr Smith");
        expectedResults.add("plug and play");
        expectedResults.add("play methodology");

        int i = 0;
        for (Keyphrase kc : JCasUtil.select(jcas, Keyphrase.class)) {
            System.out.println(kc);
            assertTrue(expectedResults.contains(kc.getKeyphrase()));
            i++;
        }
        assertEquals(3, i);
    }

    @Test
    public void testDefaultSetting()
        throws ResourceInitializationException, AnalysisEngineProcessException
    {

        AnalysisEngine engine = AnalysisEngineFactory.createEngine(
                createEngineDescription(
                    createEngineDescription(BreakIteratorSegmenter.class),
                    AnalysisEngineFactory.createEngineDescription(StanfordPosTagger.class),
                    AnalysisEngineFactory.createEngineDescription(StanfordLemmatizer.class),
                    createEngineDescription(NGramAnnotator.class,
                            NGramAnnotator.PARAM_N, 4),
                    CandidateAnnotatorFactory.getKeyphraseCandidateAnnotator_ngram(false),
                    createEngineDescription(PosSequenceFilter.class)));
        JCas jcas = engine.newJCas();
        jcas.setDocumentLanguage("en");
        jcas.setDocumentText(testDocument);
        engine.process(jcas);
        List<String> expectedResults = new ArrayList<String>();
        expectedResults.add("Mr Smith");
        expectedResults.add("Smith");
        expectedResults.add("Smith knows");
        expectedResults.add("knows plug");
        expectedResults.add("Mr");
        expectedResults.add("plug");
        expectedResults.add("play");
        expectedResults.add("play methodology");
        expectedResults.add("methodology");

        int i = 0;
        for (Keyphrase kc : JCasUtil.select(jcas, Keyphrase.class)) {
            System.out.println(kc);
            assertTrue(expectedResults.contains(kc.getKeyphrase()));
            i++;
        }
        assertEquals(9, i);
    }

}
