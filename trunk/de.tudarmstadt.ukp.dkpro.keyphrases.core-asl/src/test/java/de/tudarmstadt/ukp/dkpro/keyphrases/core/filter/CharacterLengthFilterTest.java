package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

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

import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate.CandidateAnnotatorFactory;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.factory.CharacterLengthFilterFactory;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class CharacterLengthFilterTest
{

    @Test
    public void test() throws ResourceInitializationException, AnalysisEngineProcessException
    {
        
        AnalysisEngine engine = AnalysisEngineFactory.createEngine(
                AnalysisEngineFactory.createEngineDescription(
                        AnalysisEngineFactory.createEngineDescription(BreakIteratorSegmenter.class),
                        CandidateAnnotatorFactory.getKeyphraseCandidateAnnotator_token(false),
                        CharacterLengthFilterFactory.getCharacterLengthFilter(4)));

        JCas jcas = engine.newJCas();
        jcas.setDocumentLanguage("en");
        jcas.setDocumentText("Give a man a name.");
        engine.process(jcas);
        List<String> expectedResults = new ArrayList<String>();
        expectedResults.add("Give");
        expectedResults.add("name");
        
        int i=0;
        for (Keyphrase kc : JCasUtil.select(jcas, Keyphrase.class)) {
            System.out.println(kc);
            assertTrue(expectedResults.contains(kc.getKeyphrase()));
            i++;
        }
        assertEquals(2,i);
        
    }

}
