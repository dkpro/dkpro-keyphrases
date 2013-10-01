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

import static org.junit.Assert.assertEquals;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class PositionRankingTest
{
    // assertEquals on doubles needs an epsilon
    protected static final double EPSILON = 0.000001;

    @Test
    public void PositionKeyphraseTest() throws Exception {

        String testDocument = "example sentence funny. second example.";

        AnalysisEngine analysisEngine = AnalysisEngineFactory.createPrimitive(
                PositionRanking.class);

        JCas jcas = setup(testDocument, analysisEngine);
        analysisEngine.process(jcas);

        int i=0;
        for (Keyphrase k : JCasUtil.select(jcas, Keyphrase.class)) {
            System.out.println(k);
            if (i==0) {
                assertEquals("example", k.getKeyphrase());
                assertEquals(1.0, k.getScore(), EPSILON);
            }
            if (i==1) {
                assertEquals("second", k.getKeyphrase());
                assertEquals(0.384615385, k.getScore(), EPSILON);
            }
            i++;
        }
        assertEquals(2,i);
    }

    private JCas setup(String testDocument, AnalysisEngine analysisEngine) throws ResourceInitializationException {
        JCas jcas;

        jcas = analysisEngine.newJCas();
        jcas.setDocumentText(testDocument);

        Keyphrase k1 = new Keyphrase(jcas, 0, 7);
        k1.setKeyphrase("example");
        k1.addToIndexes();
        assertEquals("example", k1.getCoveredText());

        Keyphrase k2 = new Keyphrase(jcas, 24, 30);
        k2.setKeyphrase("second");
        k2.addToIndexes();
        assertEquals("second", k2.getCoveredText());

        return jcas;
    }
}