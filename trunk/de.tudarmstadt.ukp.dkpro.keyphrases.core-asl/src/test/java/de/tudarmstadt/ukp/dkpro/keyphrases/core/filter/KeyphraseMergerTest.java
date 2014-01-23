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

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class KeyphraseMergerTest {

    @Test
    public void MergerTest() throws Exception {
        AnalysisEngine analysisEngine = AnalysisEngineFactory.createEngine(
                KeyphraseMerger.class);

        String testDocument = "example sentence funny. second example.";
        JCas jcas = setup(testDocument, analysisEngine);

        Set<String> expectedResults = new HashSet<String>();
        expectedResults.add("example sentence funny");

        analysisEngine.process(jcas);


        int i = 0;
        for (Keyphrase k : JCasUtil.select(jcas, Keyphrase.class)) {
            System.out.println(k);
            assertTrue(expectedResults.contains(k.getKeyphrase()));
            i++;
        }
        assertEquals(1, i);
    }

    @Test
    public void MergerTest2() throws Exception {
        AnalysisEngine analysisEngine = AnalysisEngineFactory.createEngine(
                KeyphraseMerger.class,
                KeyphraseMerger.PARAM_MAX_LENGTH, 2);

        String testDocument = "example sentence funny. second example.";
        JCas jcas = setup(testDocument, analysisEngine);

        Set<String> expectedResults = new HashSet<String>();
        expectedResults.add("example");
        expectedResults.add("sentence funny");

        analysisEngine.process(jcas);

        int i = 0;
        for (Keyphrase k : JCasUtil.select(jcas, Keyphrase.class)) {
            System.out.println(k);
            assertTrue(k.getKeyphrase(), expectedResults.contains(k.getKeyphrase()));
            i++;
        }
        assertEquals(2, i);
    }

//    @Test
//    public void MergerTest3() throws Exception {
//        // TZ: I do not like this test, as the tokens do not have unique offsets.
//        AnalysisEngine analysisEngine = UimaUtils.getAnalysisEngine(UimaUtils.getDataPath(), resMgr.resolveRelativePath(DESC));
//
//        String testDocument = "example sentence funny. second example.";
//        JCas jcas = setup2(testDocument, analysisEngine);
//
//        Set<String> expectedResults = new HashSet<String>();
//        expectedResults.add("example sentence funny");
//        expectedResults.add("example sentence funn");
//        expectedResults.add("xample sentence funny");
//        expectedResults.add("xample sentence funn");
//
//        analysisEngine.process(jcas);
//
//        FSIterator keyphraseIter = jcas.getAnnotationIndex(Keyphrase.type).iterator();
//
//        int i = 0;
//        while (keyphraseIter.hasNext()) {
//            Keyphrase k = (Keyphrase) keyphraseIter.next();
//            System.out.println("-" + k);
//            assertTrue(k.getKeyphrase(), expectedResults.contains(k.getKeyphrase()));
//            i++;
//        }
//        assertEquals(4, i);
//    }

    // @Test
    // public void MergerTest4() {
    //
    // File descriptorFile = new
    // File("desc/annotator/keyphrases/KeyphraseMerger.xml");
    //
    // String testDocument = "example sentence funny second example.";
    //
    // JCas jcas = null;
    // try {
    // ResourceManager resMgr = UIMAFramework.newDefaultResourceManager();
    // resMgr.setDataPath(dataPathString);
    //
    // XMLInputSource xmlInput = new XMLInputSource(descriptorFile);
    // ResourceSpecifier specifier =
    // UIMAFramework.getXMLParser().parseResourceSpecifier(xmlInput);
    // AnalysisEngine analysisEngine =
    // UIMAFramework.produceAnalysisEngine(specifier, resMgr, null);
    //
    // jcas = setup3(testDocument, descriptorFile, analysisEngine);
    //
    // Set<String> expectedResults = new HashSet<String>();
    // expectedResults.add("example sentence funny");
    // expectedResults.add("example sentence funn");
    // expectedResults.add("xample sentence funny");
    // expectedResults.add("xample sentence funn");
    //
    // analysisEngine.process(jcas);
    //
    // FSIterator keyphraseIter =
    // jcas.getAnnotationIndex(Keyphrase.type).iterator();
    //
    // int i=0;
    // while (keyphraseIter.hasNext()) {
    // Keyphrase k = (Keyphrase) keyphraseIter.next();
    // System.out.println("-" + k);
    // // assertTrue(expectedResults.contains(k.getKeyphrase()));
    // i++;
    // }
    // assertEquals(4,i);
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // fail(e.getMessage());
    // }
    // }

    private JCas setup(String testDocument, AnalysisEngine analysisEngine) throws IOException, InvalidXMLException, ResourceInitializationException {
        JCas jcas;

        jcas = analysisEngine.newJCas();
        jcas.setDocumentText(testDocument);

        Token t1 = new Token(jcas, 0, 7);
        t1.addToIndexes();
        assertEquals("example", t1.getCoveredText());

        Token t2 = new Token(jcas, 8, 16);
        t2.addToIndexes();
        assertEquals("sentence", t2.getCoveredText());

        Token t3 = new Token(jcas, 17, 22);
        t3.addToIndexes();
        assertEquals("funny", t3.getCoveredText());

        Token t4 = new Token(jcas, 24, 30);
        t4.addToIndexes();
        assertEquals("second", t4.getCoveredText());

        Token t5 = new Token(jcas, 31, 38);
        t5.addToIndexes();
        assertEquals("example", t5.getCoveredText());

        Keyphrase k1 = new Keyphrase(jcas, 0, 7);
        k1.setScore(0.7);
        k1.setKeyphrase("example");
        k1.addToIndexes();
        assertEquals("example", k1.getCoveredText());

        Keyphrase k2 = new Keyphrase(jcas, 8, 22);
        k2.setScore(0.5);
        k2.setKeyphrase("sentence funny");
        k2.addToIndexes();
        assertEquals("sentence funny", k2.getCoveredText());

        return jcas;
    }

//    private JCas setup2(String testDocument, AnalysisEngine analysisEngine) throws IOException, InvalidXMLException, ResourceInitializationException {
//        JCas jcas;
//
//        jcas = analysisEngine.newJCas();
//        jcas.setDocumentText(testDocument);
//
//        Token t1 = new Token(jcas);
//        t1.setBegin(0);
//        t1.setEnd(7);
//        t1.addToIndexes();
//        assertEquals("example", t1.getCoveredText());
//
//        Token t1b = new Token(jcas);
//        t1b.setBegin(1);
//        t1b.setEnd(7);
//        t1b.addToIndexes();
//        assertEquals("xample", t1b.getCoveredText());
//
//        Token t2 = new Token(jcas);
//        t2.setBegin(8);
//        t2.setEnd(16);
//        t2.addToIndexes();
//        assertEquals("sentence", t2.getCoveredText());
//
//        Token t3 = new Token(jcas);
//        t3.setBegin(17);
//        t3.setEnd(22);
//        t3.addToIndexes();
//        assertEquals("funny", t3.getCoveredText());
//
//        Token t3b = new Token(jcas);
//        t3b.setBegin(17);
//        t3b.setEnd(21);
//        t3b.addToIndexes();
//        assertEquals("funn", t3b.getCoveredText());
//
//        Token t4 = new Token(jcas);
//        t4.setBegin(24);
//        t4.setEnd(30);
//        t4.addToIndexes();
//        assertEquals("second", t4.getCoveredText());
//
//        Token t5 = new Token(jcas);
//        t5.setBegin(31);
//        t5.setEnd(38);
//        t5.addToIndexes();
//        assertEquals("example", t5.getCoveredText());
//
//        Keyphrase k1 = new Keyphrase(jcas);
//        k1.setBegin(0);
//        k1.setEnd(16);
//        k1.setScore(0.7);
//        k1.setKeyphrase("example sentence");
//        k1.addToIndexes();
//        assertEquals("example sentence", k1.getCoveredText());
//
//        Keyphrase k1b = new Keyphrase(jcas);
//        k1b.setBegin(1);
//        k1b.setEnd(16);
//        k1b.setScore(0.7);
//        k1b.setKeyphrase("xample sentence");
//        k1b.addToIndexes();
//        assertEquals("xample sentence", k1b.getCoveredText());
//
//        Keyphrase k2 = new Keyphrase(jcas);
//        k2.setBegin(8);
//        k2.setEnd(22);
//        k2.setScore(0.5);
//        k2.setKeyphrase("sentence funny");
//        k2.addToIndexes();
//        assertEquals("sentence funny", k2.getCoveredText());
//
//        Keyphrase k2b = new Keyphrase(jcas);
//        k2b.setBegin(8);
//        k2b.setEnd(21);
//        k2b.setScore(0.5);
//        k2b.setKeyphrase("sentence funn");
//        k2b.addToIndexes();
//        assertEquals("sentence funn", k2b.getCoveredText());
//
//        return jcas;
//    }

    // private JCas setup3(String testDocument, File descriptorFile,
    // AnalysisEngine analysisEngine) throws IOException, InvalidXMLException,
    // ResourceInitializationException {
    // JCas jcas;
    //
    // jcas = analysisEngine.newJCas();
    // jcas.setDocumentText(testDocument);
    //
    // Token t1 = new Token(jcas);
    // t1.setBegin(0);
    // t1.setEnd(7);
    // t1.addToIndexes();
    // assertEquals("example", t1.getCoveredText());
    //
    // Token t1b = new Token(jcas);
    // t1b.setBegin(1);
    // t1b.setEnd(7);
    // t1b.addToIndexes();
    // assertEquals("xample", t1b.getCoveredText());
    //
    // Token t2 = new Token(jcas);
    // t2.setBegin(8);
    // t2.setEnd(16);
    // t2.addToIndexes();
    // assertEquals("sentence", t2.getCoveredText());
    //
    // Token t3 = new Token(jcas);
    // t3.setBegin(17);
    // t3.setEnd(22);
    // t3.addToIndexes();
    // assertEquals("funny", t3.getCoveredText());
    //
    // Token t3b = new Token(jcas);
    // t3b.setBegin(17);
    // t3b.setEnd(21);
    // t3b.addToIndexes();
    // assertEquals("funn", t3b.getCoveredText());
    //
    // Token t4 = new Token(jcas);
    // t4.setBegin(24);
    // t4.setEnd(30);
    // t4.addToIndexes();
    // assertEquals("second", t4.getCoveredText());
    //
    // Token t5 = new Token(jcas);
    // t5.setBegin(31);
    // t5.setEnd(38);
    // t5.addToIndexes();
    // assertEquals("example", t5.getCoveredText());
    //
    // Keyphrase k1 = new Keyphrase(jcas);
    // k1.setBegin(0);
    // k1.setEnd(7);
    // k1.setScore(0.7);
    // k1.setKeyphrase("example");
    // k1.addToIndexes();
    // assertEquals("example", k1.getCoveredText());
    //
    // Keyphrase k1b = new Keyphrase(jcas);
    // k1b.setBegin(8);
    // k1b.setEnd(16);
    // k1b.setScore(0.7);
    // k1b.setKeyphrase("sentence");
    // k1b.addToIndexes();
    // assertEquals("sentence", k1b.getCoveredText());
    //
    // Keyphrase k2 = new Keyphrase(jcas);
    // k2.setBegin(1);
    // k2.setEnd(16);
    // k2.setScore(0.7);
    // k2.setKeyphrase("xample sentence");
    // k2.addToIndexes();
    // assertEquals("xample sentence", k2.getCoveredText());
    //
    // Keyphrase k3 = new Keyphrase(jcas);
    // k3.setBegin(8);
    // k3.setEnd(22);
    // k3.setScore(0.5);
    // k3.setKeyphrase("sentence funny");
    // k3.addToIndexes();
    // assertEquals("sentence funny", k3.getCoveredText());
    //
    // Keyphrase k3b = new Keyphrase(jcas);
    // k3b.setBegin(8);
    // k3b.setEnd(21);
    // k3b.setScore(0.5);
    // k3b.setKeyphrase("sentence funn");
    // k3b.addToIndexes();
    // assertEquals("sentence funn", k3b.getCoveredText());
    //
    // return jcas;
    // }
}
