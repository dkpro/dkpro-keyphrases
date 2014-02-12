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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.bindResource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.frequency.resources.Web1TFrequencyCountResource;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class TfBackgroundIdfRankingTest
{

    // assertEquals on doubles needs an epsilon
    protected static final double EPSILON = 0.000001;

    @Test
    public void test()
        throws InvalidXMLException, ResourceInitializationException, AnalysisEngineProcessException, IOException
    {
        
        String testDocument = "example sentence funny. second example. hdjdl";


        final AnalysisEngineDescription ranker = createEngineDescription(
                TfBackgroundIdfRanking.class);
        bindResource(ranker, 
                TfBackgroundIdfRanking.FREQUENCY_COUNT_RESOURCE,
                Web1TFrequencyCountResource.class,
                Web1TFrequencyCountResource.PARAM_MIN_NGRAM_LEVEL, "1",
                Web1TFrequencyCountResource.PARAM_MAX_NGRAM_LEVEL, "3",
                Web1TFrequencyCountResource.PARAM_INDEX_PATH, "src/test/resources/jweb1t");

        final AnalysisEngine rankerAE = AnalysisEngineFactory.createEngine(ranker);

        JCas jcas = setup(testDocument, rankerAE);
        rankerAE.process(jcas);

        
        int n=0;
        boolean exampleContained = false;
        boolean secondContained = false;
        boolean hdjdlContained = false;
        for (Keyphrase keyphrase : JCasUtil.select(jcas, Keyphrase.class)) {
            n++;
            if (keyphrase.getCoveredText().equals("example")) {
                assertEquals(2.0 / Math.log(2.0), keyphrase.getScore(), EPSILON);
                exampleContained = true;
            }
            else if (keyphrase.getCoveredText().equals("second")) {
                assertEquals(1.0 / Math.log(4.0), keyphrase.getScore(), EPSILON);
                secondContained = true;
            }
            else if (keyphrase.getCoveredText().equals("hdjdl")) {
                assertEquals(0.0, keyphrase.getScore(), EPSILON);
                hdjdlContained = true;
            }
        }
        assertEquals(4, n);
        assertTrue(exampleContained);
        assertTrue(secondContained);
        assertTrue(hdjdlContained);

    }

    private JCas setup(String testDocument, AnalysisEngine analysisEngine) throws IOException, InvalidXMLException, ResourceInitializationException {
        JCas jcas;

        jcas = analysisEngine.newJCas();
        jcas.setDocumentText(testDocument);

        Token t1a = new Token(jcas, 0, 7);
        t1a.addToIndexes();
        assertEquals("example", t1a.getCoveredText());

        Token t1b = new Token(jcas, 31, 38);
        t1b.addToIndexes();
        assertEquals("example", t1b.getCoveredText());

        Token t2 = new Token(jcas, 24, 30);
        t2.addToIndexes();
        assertEquals("second", t2.getCoveredText());

        Keyphrase k1 = new Keyphrase(jcas, 0, 7);
        k1.setKeyphrase("example");
        k1.addToIndexes();
        assertEquals("example", k1.getCoveredText());

        Keyphrase k1b = new Keyphrase(jcas, 31, 38);
        k1b.setKeyphrase("example");
        k1b.addToIndexes();
        assertEquals("example", k1b.getCoveredText());

        Keyphrase k2 = new Keyphrase(jcas, 24, 30);
        k2.setKeyphrase("second");
        k2.addToIndexes();
        assertEquals("second", k2.getCoveredText());

        Keyphrase k3 = new Keyphrase(jcas, 40, 45);
        k3.setKeyphrase("hdjdl");
        k3.addToIndexes();
        assertEquals("hdjdl", k3.getCoveredText());

        return jcas;
    }

}
