package de.tudarmstadt.ukp.dkpro.keyphrases.wikipediafilter.filter;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class DictionaryFilterTest
{
    
    private JCas setUpJcas(AnalysisEngine engine) throws AnalysisEngineProcessException, ResourceInitializationException{
        JCas jcas = engine.newJCas();
        
        jcas.setDocumentLanguage("en");
        jcas.setDocumentText("George Washington, Lincoln and Bush are great presidents.");
        
        Keyphrase keyphrase0 = new Keyphrase(jcas, 0, 17);
        keyphrase0.setKeyphrase("George Washington");
        keyphrase0.addToIndexes();

        Keyphrase keyphrase1 = new Keyphrase(jcas, 7, 17);
        keyphrase1.setKeyphrase("Washington");
        keyphrase1.addToIndexes();

        Keyphrase keyphrase2 = new Keyphrase(jcas, 19, 26);
        keyphrase2.setKeyphrase("Lincoln");
        keyphrase2.addToIndexes();

        Keyphrase keyphrase3 = new Keyphrase(jcas, 31, 35);
        keyphrase3.setKeyphrase("Bush");
        keyphrase3.addToIndexes();
        
        Keyphrase keyphrase4 = new Keyphrase(jcas, 46, 56);
        keyphrase4.setKeyphrase("presidents");
        keyphrase4.addToIndexes();
        
        engine.process(jcas);
        
        return jcas;
    }

    @Test
    public void testFilterLowercased() throws ResourceInitializationException, AnalysisEngineProcessException
    {
        AnalysisEngine engine = createEngine(
                DictionaryFilter.class,
                DictionaryFilter.PARAM_DICTIONARY_FILE_NAMES, new String[] {
                    "[en]classpath:/wikipedia/articles.txt"
                },
                DictionaryFilter.PARAM_PATHS, Keyphrase.class.getName());
        
        JCas jcas = setUpJcas(engine);
        
        List<String> keyphrases = new ArrayList<String>();
        keyphrases.add("George Washington");
        keyphrases.add("Washington");
        keyphrases.add("Lincoln");
        keyphrases.add("presidents");
        
        int counter = 0;
        for(Keyphrase nonFilteredKeyphrase : JCasUtil.select(jcas, Keyphrase.class)){
            counter++;
            assertTrue(keyphrases.contains(nonFilteredKeyphrase.getCoveredText()));
        }
        assertEquals(4, counter);
    }

    @Test
    public void testFilterNotLowercased() throws ResourceInitializationException, AnalysisEngineProcessException
    {
        AnalysisEngine engine = createEngine(
                DictionaryFilter.class,
                DictionaryFilter.PARAM_DICTIONARY_FILE_NAMES, new String[] {
                    "[en]classpath:/wikipedia/articles.txt"
                },
                DictionaryFilter.PARAM_PATHS, Keyphrase.class.getName(),
                DictionaryFilter.PARAM_LOWERCASE, false);
        
        JCas jcas = setUpJcas(engine);
        
        List<String> keyphrases = new ArrayList<String>();
        keyphrases.add("George Washington");
        keyphrases.add("Washington");
        keyphrases.add("Lincoln");
        keyphrases.add("president");
        
        int counter = 0;
        for(Keyphrase nonFilteredKeyphrase : JCasUtil.select(jcas, Keyphrase.class)){
            counter++;
            assertTrue(keyphrases.contains(nonFilteredKeyphrase.getCoveredText()));
        }
        assertEquals(3, counter);
    }

}
