/*******************************************************************************
 * Copyright 2013
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.factory.StopwordFilterFactory;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class StopwordFilterTest
{

    private final String testDocument = "This is a short text.";

    @Test
    public void filterKeyphrasesTestEnglish()
        throws Exception
    {

        AnalysisEngine analysisEngine = createEngine(StopwordFilterFactory.getStopwordFilter_english());

        JCas jcas = setupKeyphrases(testDocument, analysisEngine);
        jcas.setDocumentLanguage("en");

        Set<String> expectedResults = new HashSet<String>();
        expectedResults.add("short");
        expectedResults.add("text");

        analysisEngine.process(jcas);

        int i = 0;
        for (Keyphrase k : JCasUtil.select(jcas, Keyphrase.class)) {
            System.out.println(k);
            assertTrue(expectedResults.contains(k.getKeyphrase()));
            i++;
        }
        assertEquals(2, i);

    }

    @Test
    public void filterKeyphrasesTestGerman()
       throws Exception
    {

        AnalysisEngine analysisEngine = createEngine(StopwordFilterFactory.getStopwordFilter(
                "[en]classpath:/stopwords/english_stopwords.txt", "[de]classpath:/stopwords/GermanTestStopwords.txt"));

        JCas jcas = setupKeyphrases(testDocument, analysisEngine);
        jcas.setDocumentLanguage("de");

        Set<String> expectedResults = new HashSet<String>();
        expectedResults.add("text");

        analysisEngine.process(jcas);

        int i = 0;
        for (Keyphrase k : JCasUtil.select(jcas, Keyphrase.class)) {
            System.out.println(k);
            assertTrue(expectedResults.contains(k.getKeyphrase()));
            i++;
        }
        assertEquals(1, i);

    }

    private JCas setupKeyphrases(String testDocument, AnalysisEngine analysisEngine)
        throws IOException, InvalidXMLException, ResourceInitializationException
    {
        JCas jcas;

        jcas = analysisEngine.newJCas();
        jcas.setDocumentText(testDocument);

        setupKeyphrases(jcas);

        return jcas;
    }

    private void setupKeyphrases(JCas jcas)
    {
        Keyphrase k1 = new Keyphrase(jcas, 8, 9);
        k1.setKeyphrase("a");
        k1.addToIndexes();
        assertEquals("a", k1.getCoveredText());

        Keyphrase k2 = new Keyphrase(jcas, 10, 15);
        k2.setKeyphrase("short");
        k2.addToIndexes();
        assertEquals("short", k2.getCoveredText());

        Keyphrase k3 = new Keyphrase(jcas, 16, 20);
        k3.setKeyphrase("text");
        k3.addToIndexes();
        assertEquals("text", k3.getCoveredText());

    }
}
