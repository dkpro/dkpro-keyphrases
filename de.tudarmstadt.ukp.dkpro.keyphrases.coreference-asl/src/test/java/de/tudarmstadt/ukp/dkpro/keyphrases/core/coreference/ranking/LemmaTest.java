package de.tudarmstadt.ukp.dkpro.keyphrases.core.coreference.ranking;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.junit.Assert.*;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;

public class LemmaTest
{

    @Test
    public void test() throws ResourceInitializationException
    {
        AnalysisEngine engine = createEngine(BreakIteratorSegmenter.class);
        JCas jcas = engine.newJCas();
        Token token = new Token(jcas, 0, 6);
        token.addToIndexes();
        Lemma lemma = new Lemma(jcas, 0, 6);
        lemma.setValue("George");
        lemma.addToIndexes();
        
        for(Lemma lemma0 : JCasUtil.select(jcas, Lemma.class)){
            System.out.println(lemma0.getValue());
        }
        
    }

}
