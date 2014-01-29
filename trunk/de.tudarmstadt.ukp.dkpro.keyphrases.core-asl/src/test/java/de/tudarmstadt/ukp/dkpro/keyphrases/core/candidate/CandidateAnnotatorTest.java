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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.N;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.NGram;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.NC;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerChunkerTT4J;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerPosLemmaTT4J;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate;

public class CandidateAnnotatorTest
{

    @Test
    public void KeyphraseCandidateTest1() throws Exception {
        System.out.println("Only tokens. Default settings");

        Set<String> expectedResults = new HashSet<String>();
        expectedResults.add("example");
        expectedResults.add("sentence");
        expectedResults.add("funny");
        expectedResults.add("second");

        AnalysisEngine analysisEngine = AnalysisEngineFactory.createEngine(
                CandidateAnnotator.class,
                CandidateAnnotator.PARAM_FEATURE_PATH, Token.class.getName(),
                CandidateAnnotator.PARAM_RESOLVE_OVERLAPS,      false);

        String testDocument = "example sentence funny. second example.";
        JCas jcas = setup(testDocument, analysisEngine);

        analysisEngine.process(jcas);

        int i=0;
        for (KeyphraseCandidate kc : JCasUtil.select(jcas, KeyphraseCandidate.class)) {
            System.out.println(kc);
            assertTrue(expectedResults.contains(kc.getKeyphrase()));
            i++;
        }
        assertEquals(5,i);
    }

    @Test
    public void KeyphraseCandidateTest2() throws Exception {
        System.out.println("Tokens and NGrams. NGram covers two tokens.");

        Set<String> expectedResults = new HashSet<String>();
        expectedResults.add("example sentence");
        expectedResults.add("funny");
        expectedResults.add("second");
        expectedResults.add("example");
        expectedResults.add("sentence");

        AnalysisEngine analysisEngine = AnalysisEngineFactory.createEngine(
                CandidateAnnotator.class,
                CandidateAnnotator.PARAM_FEATURE_PATH, Token.class.getName(),
                CandidateAnnotator.PARAM_RESOLVE_OVERLAPS,      true);

        AnalysisEngine analysisEngine2 = AnalysisEngineFactory.createEngine(
                CandidateAnnotator.class,
                CandidateAnnotator.PARAM_FEATURE_PATH, NGram.class.getName(),
                CandidateAnnotator.PARAM_RESOLVE_OVERLAPS,      true);

        String testDocument = "example sentence funny. second example.";

        JCas jcas =  setup(testDocument, analysisEngine);

        analysisEngine.process(jcas);
        analysisEngine2.process(jcas);

        int i=0;
        for (KeyphraseCandidate kc : JCasUtil.select(jcas, KeyphraseCandidate.class)) {
            System.out.println(kc);
            assertTrue(expectedResults.contains(kc.getKeyphrase()));
            i++;
        }
        assertEquals(6,i);
    }

    @Test
    public void KeyphraseCandidateTest3() throws Exception {
        System.out.println("Tokens and Chunks. Chunks equals token. Should only appear once.");

        Set<String> expectedResults = new HashSet<String>();
        expectedResults.add("example");
        expectedResults.add("sentence");
        expectedResults.add("funny");
        expectedResults.add("second");

        AnalysisEngine analysisEngine = AnalysisEngineFactory.createEngine(
                CandidateAnnotator.class,
                CandidateAnnotator.PARAM_FEATURE_PATH, Token.class.getName(),
                CandidateAnnotator.PARAM_RESOLVE_OVERLAPS,      false);

        AnalysisEngine analysisEngine2 = AnalysisEngineFactory.createEngine(
                CandidateAnnotator.class,
                CandidateAnnotator.PARAM_FEATURE_PATH, NC.class.getName(),
                CandidateAnnotator.PARAM_RESOLVE_OVERLAPS,      false);

        String testDocument = "example sentence funny. second example.";

        JCas jcas = setup(testDocument, analysisEngine);

        analysisEngine.process(jcas);
        analysisEngine2.process(jcas);

        int i=0;
        for (KeyphraseCandidate kc : JCasUtil.select(jcas, KeyphraseCandidate.class)) {
            System.out.println(kc);
            assertTrue(expectedResults.contains(kc.getKeyphrase()));
            i++;
        }
        assertEquals(5,i);
    }

    @Test
    public void KeyphraseCandidateTest4() throws Exception {
        System.out.println("Only ngrams and named entities.");

        Set<String> expectedResults = new HashSet<String>();
        expectedResults.add("example sentence");
        expectedResults.add("sentence funny");

        AnalysisEngine analysisEngine = AnalysisEngineFactory.createEngine(
                CandidateAnnotator.class,
                CandidateAnnotator.PARAM_FEATURE_PATH, NamedEntity.class.getName(),
                CandidateAnnotator.PARAM_RESOLVE_OVERLAPS,      false);

        AnalysisEngine analysisEngine2 = AnalysisEngineFactory.createEngine(
                CandidateAnnotator.class,
                CandidateAnnotator.PARAM_FEATURE_PATH, NGram.class.getName(),
                CandidateAnnotator.PARAM_RESOLVE_OVERLAPS,      false);

        String testDocument = "example sentence funny. second example.";

        JCas jcas = setup(testDocument, analysisEngine);

        analysisEngine.process(jcas);
        analysisEngine2.process(jcas);

        int i=0;
        for (KeyphraseCandidate kc : JCasUtil.select(jcas, KeyphraseCandidate.class)) {
            System.out.println(kc);
            assertTrue(expectedResults.contains(kc.getKeyphrase()));
            i++;
        }
        assertEquals(2,i);
    }

    @Test
    public void KeyphraseCandidateTest5() throws Exception {
        System.out.println("Only named entities. They partially overlap. Should give a single large candidate.");

        Set<String> expectedResults = new HashSet<String>();
        expectedResults.add("example sentence funny second example");

        AnalysisEngine analysisEngine = AnalysisEngineFactory.createEngine(
                CandidateAnnotator.class,
                CandidateAnnotator.PARAM_FEATURE_PATH, NamedEntity.class.getName(),
                CandidateAnnotator.PARAM_RESOLVE_OVERLAPS,      true);

        String testDocument2 = "example sentence funny second example.";

        JCas jcas = setup2(testDocument2, analysisEngine);

        analysisEngine.process(jcas);

        int i=0;
        for (KeyphraseCandidate kc : JCasUtil.select(jcas, KeyphraseCandidate.class)) {
            System.out.println(kc);
            assertTrue(expectedResults.contains(kc.getKeyphrase()));
            i++;
        }
        assertEquals(1,i);
    }

    @Test
    public void KeyphraseCandidateTest6() throws Exception {
        System.out.println("Only named entities. They partially overlap, but no resolve parameter is set.");

        Set<String> expectedResults = new HashSet<String>();
        expectedResults.add("example sentence funny");
        expectedResults.add("funny second example");

        AnalysisEngine analysisEngine = AnalysisEngineFactory.createEngine(
                CandidateAnnotator.class,
                CandidateAnnotator.PARAM_FEATURE_PATH, NamedEntity.class.getName(),
                CandidateAnnotator.PARAM_RESOLVE_OVERLAPS,      false);

        String testDocument2 = "example sentence funny second example.";

        JCas jcas = setup2(testDocument2, analysisEngine);

        analysisEngine.process(jcas);

        int i=0;
        for (KeyphraseCandidate kc : JCasUtil.select(jcas, KeyphraseCandidate.class)) {
            System.out.println(kc);
            assertTrue(expectedResults.contains(kc.getKeyphrase()));
            i++;
        }
        assertEquals(2,i);
    }
    
    @Test
    public void KeyphraseCandidateTest7() throws Exception {

        AnalysisEngine engine = AnalysisEngineFactory.createEngine(AnalysisEngineFactory.
                createEngineDescription(
                        AnalysisEngineFactory.createEngineDescription(BreakIteratorSegmenter.class),
                        AnalysisEngineFactory.createEngineDescription(TreeTaggerPosLemmaTT4J.class),
                        CandidateAnnotatorFactory. getKeyphraseCandidateAnnotator_lemma(false)));

        JCas jcas = engine.newJCas();
        jcas.setDocumentLanguage("en");
        jcas.setDocumentText("This is a sentence.");

        engine.process(jcas);

        List<String> expectedResults = new ArrayList<String>();
        expectedResults.add("this");
        expectedResults.add("be");
        expectedResults.add("a");
        expectedResults.add("sentence");
        expectedResults.add(".");
        
        int i=0;
        for (KeyphraseCandidate kc : JCasUtil.select(jcas, KeyphraseCandidate.class)) {
            System.out.println(kc);
            assertTrue(expectedResults.contains(kc.getKeyphrase()));
            i++;
        }
        assertEquals(5,i);
    }
    
    @Test
    public void KeyphraseCandidateTest9() throws Exception {

        AnalysisEngine engine = AnalysisEngineFactory.createEngine(AnalysisEngineFactory.
                createEngineDescription(
                        AnalysisEngineFactory.createEngineDescription(BreakIteratorSegmenter.class),
                        AnalysisEngineFactory.createEngineDescription(TreeTaggerPosLemmaTT4J.class),
                        CandidateAnnotatorFactory.getKeyphraseCandidateAnnotator(N.class.getName(), false)));

        JCas jcas = engine.newJCas();
        jcas.setDocumentLanguage("en");
        jcas.setDocumentText("A noun phrase.");

        engine.process(jcas);

        List<String> expectedResults = new ArrayList<String>();
        expectedResults.add("noun");
        expectedResults.add("phrase");
        
        int i=0;
        for (KeyphraseCandidate kc : JCasUtil.select(jcas, KeyphraseCandidate.class)) {
            System.out.println(kc);
            assertTrue(expectedResults.contains(kc.getKeyphrase()));
            i++;
        }
        assertEquals(2,i);
    }

	private JCas setup(String testDocument, AnalysisEngine analysisEngine)
		throws ResourceInitializationException
    {
        JCas jcas = analysisEngine.newJCas();
        jcas.setDocumentText(testDocument);

        Token t1 = new Token(jcas, 0, 7);
        t1.addToIndexes();
        assertEquals("example", t1.getCoveredText());

        Token t2 = new Token(jcas, 8, 16);
        t2.addToIndexes();
        assertEquals("sentence", t2.getCoveredText());

        NGram ng = new NGram(jcas, 0, 16);
        ng.addToIndexes();
        assertEquals("example sentence", ng.getCoveredText());

        Token t3 = new Token(jcas, 17, 22);
        t3.addToIndexes();
        assertEquals("funny", t3.getCoveredText());

        Token t4 = new Token(jcas, 24, 30);
        t4.addToIndexes();
        assertEquals("second", t4.getCoveredText());

        Token t5 = new Token(jcas, 31, 38);
        t5.addToIndexes();
        assertEquals("example", t5.getCoveredText());

        Chunk c = new NC(jcas, 24, 30);
        c.addToIndexes();
        assertEquals("second", c.getCoveredText());

        NamedEntity ne = new NamedEntity(jcas, 8, 22);
        ne.addToIndexes();
        assertEquals("sentence funny", ne.getCoveredText());

        return jcas;
    }

	private JCas setup2(String testDocument, AnalysisEngine analysisEngine)
		throws ResourceInitializationException
    {
        JCas jcas = analysisEngine.newJCas();
        jcas.setDocumentText(testDocument);

        NamedEntity ne = new NamedEntity(jcas, 0, 22);
        ne.addToIndexes();
        assertEquals("example sentence funny", ne.getCoveredText());

        NamedEntity ne2 = new NamedEntity(jcas, 17, 37);
        ne2.addToIndexes();
        assertEquals("funny second example", ne2.getCoveredText());

        return jcas;
    }
}
