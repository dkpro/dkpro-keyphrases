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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.coreference.ranking;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.util.JCasUtil.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Ignore;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceChain;
import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceLink;
import de.tudarmstadt.ukp.dkpro.core.api.frequency.tfidf.type.Tfidf;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotator;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotator.WeightingModeIdf;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotator.WeightingModeTf;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotatorTest;

public class CoreferencedTfidfAnnotatorTest extends TfidfAnnotatorTest{

    private JCas getJCas(AnalysisEngine engine) throws ResourceInitializationException{

        JCas jcas = engine.newJCas();

        //Add text
        jcas.setDocumentText("George Bush is nice. He is tall.");

        //Add tokens, lemmas, chunks, entities
        Token token = new Token(jcas, 0, 6);
        token.addToIndexes();
        Lemma lemma = new Lemma(jcas, 0, 6);
        lemma.setValue("George");
        lemma.addToIndexes();
        
        token = new Token(jcas, 7, 11);
        token.addToIndexes();
        lemma = new Lemma(jcas, 7, 11);
        lemma.setValue("Bush");
        lemma.addToIndexes();
        Chunk chunk = new Chunk(jcas, 0, 11);
        chunk.addToIndexes();
        NamedEntity entity = new NamedEntity(jcas, 0, 11);
        entity.addToIndexes();

        token = new Token(jcas, 12, 14);
        token.addToIndexes();
        lemma = new Lemma(jcas, 12, 14);
        lemma.setValue("be");
        lemma.addToIndexes();

        //Add coreference annotations
        CoreferenceLink link1 = new CoreferenceLink(jcas, 22, 24);
        link1.addToIndexes();
        CoreferenceLink link0 = new CoreferenceLink(jcas, 0, 11);
        link0.setNext(link1);
        link0.addToIndexes();
        CoreferenceChain chain = new CoreferenceChain(jcas);
        chain.setFirst(link0);
        chain.addToIndexes();

        return jcas;
    }

    @Test
    public void tfidfTestToken()
            throws Exception
            {
        AnalysisEngine tfidfAnnotator = createEngine(CoreferencedTfidfAnnotator.class,
                TfidfAnnotator.PARAM_FEATURE_PATH, Token.class,
                TfidfAnnotator.PARAM_TFDF_PATH, model,
                TfidfAnnotator.PARAM_TF_MODE, WeightingModeTf.NORMAL, 
                TfidfAnnotator.PARAM_IDF_MODE, WeightingModeIdf.CONSTANT_ONE);

        JCas jcas = getJCas(tfidfAnnotator);

        tfidfAnnotator.process(jcas);

        Map<String, Double> expectedFrequencies = new HashMap<String, Double>();
        expectedFrequencies.put("George", 2.0);
        expectedFrequencies.put("Bush", 2.0);
        expectedFrequencies.put("is", 1.0);
        expectedFrequencies.put("He", 1.0);

        for(Tfidf tfidf : JCasUtil.select(jcas, Tfidf.class)){
            assertTrue(expectedFrequencies.containsKey(tfidf.getTerm()));
            assertEquals(expectedFrequencies.get(tfidf.getTerm()), tfidf.getTfidfValue(), EPSILON);
        }
            }

    @Test
    public void tfidfTestLemma()
            throws Exception
            {
        AnalysisEngine tfidfAnnotator = createEngine(CoreferencedTfidfAnnotator.class,
                TfidfAnnotator.PARAM_FEATURE_PATH, Lemma.class.getName() + "/value",
                TfidfAnnotator.PARAM_TFDF_PATH, model,
                TfidfAnnotator.PARAM_TF_MODE, WeightingModeTf.NORMAL, 
                TfidfAnnotator.PARAM_IDF_MODE, WeightingModeIdf.CONSTANT_ONE);

        JCas jcas = getJCas(tfidfAnnotator);

        tfidfAnnotator.process(jcas);

        Map<String, Double> expectedFrequencies = new HashMap<String, Double>();
        expectedFrequencies.put("George", 2.0);
        expectedFrequencies.put("Bush", 2.0);
        expectedFrequencies.put("be", 1.0);
        expectedFrequencies.put("He", 1.0);

        for(Tfidf tfidf : JCasUtil.select(jcas, Tfidf.class)){
            assertTrue(expectedFrequencies.containsKey(tfidf.getTerm()));
            assertEquals(expectedFrequencies.get(tfidf.getTerm()), tfidf.getTfidfValue(), EPSILON);
        }
            }

    @Test
    public void tfidfTestChunk()
            throws Exception
            {
        AnalysisEngine tfidfAnnotator = createEngine(CoreferencedTfidfAnnotator.class,
                TfidfAnnotator.PARAM_FEATURE_PATH, Chunk.class,
                TfidfAnnotator.PARAM_TFDF_PATH, model,
                TfidfAnnotator.PARAM_TF_MODE, WeightingModeTf.NORMAL, 
                TfidfAnnotator.PARAM_IDF_MODE, WeightingModeIdf.CONSTANT_ONE);

        JCas jcas = getJCas(tfidfAnnotator);

        tfidfAnnotator.process(jcas);

        Map<String, Double> expectedFrequencies = new HashMap<String, Double>();
        expectedFrequencies.put("George Bush", 2.0);
        expectedFrequencies.put("is", 1.0);

        for(Tfidf tfidf : JCasUtil.select(jcas, Tfidf.class)){
            System.out.println(tfidf.getTerm());
            assertTrue(expectedFrequencies.containsKey(tfidf.getTerm()));
            assertEquals(expectedFrequencies.get(tfidf.getTerm()), tfidf.getTfidfValue(), EPSILON);
        }
            }

    @Test
    public void tfidfTestEntity()
            throws Exception
            {
        AnalysisEngine tfidfAnnotator = createEngine(CoreferencedTfidfAnnotator.class,
                TfidfAnnotator.PARAM_FEATURE_PATH, NamedEntity.class,
                TfidfAnnotator.PARAM_TFDF_PATH, model,
                TfidfAnnotator.PARAM_TF_MODE, WeightingModeTf.NORMAL, 
                TfidfAnnotator.PARAM_IDF_MODE, WeightingModeIdf.CONSTANT_ONE);

        JCas jcas = getJCas(tfidfAnnotator);

        tfidfAnnotator.process(jcas);

        Map<String, Double> expectedFrequencies = new HashMap<String, Double>();
        expectedFrequencies.put("George Bush", 2.0);

        for(Tfidf tfidf : JCasUtil.select(jcas, Tfidf.class)){
            assertTrue(expectedFrequencies.containsKey(tfidf.getTerm()));
            assertEquals(expectedFrequencies.get(tfidf.getTerm()), tfidf.getTfidfValue(), EPSILON);
        }
            }
    
    @Test
    @Ignore
    public void tfidfTest_normal_constantOne()
        throws Exception
    {
    }
    
    @Test
    @Ignore
    public void tfidfTest_binary_binary()
        throws Exception
    {
    }
    
    @Test
    @Ignore
    public void tfidfTest_normal_log()
        throws Exception
    {
    }
}
