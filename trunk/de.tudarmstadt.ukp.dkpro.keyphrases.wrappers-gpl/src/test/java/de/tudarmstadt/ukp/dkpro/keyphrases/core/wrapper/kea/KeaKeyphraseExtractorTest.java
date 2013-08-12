/*******************************************************************************
 * Copyright 2013
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.0.txt
 ******************************************************************************/
package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.kea;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createAggregateDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Assume;

import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class KeaKeyphraseExtractorTest
{
//    @Test
    public void testKeaKeyphraseExtraction() throws Exception {

        Assume.assumeTrue(Runtime.getRuntime().maxMemory() > 400000000);

        Set<String> testPhrases = new TreeSet<String>();
        testPhrases.add("Halophytes");
        testPhrases.add("Karachi");
        testPhrases.add("Saline");
        testPhrases.add("saline water");
        testPhrases.add("Salt");
        testPhrases.add("Salt-Tolerant");
        testPhrases.add("Salt-Tolerant Plants");
        testPhrases.add("University of Karachi");
        testPhrases.add("dS/m");
        testPhrases.add("seawater");

        String testDocument = FileUtils.readFileToString(new File("src/main/resources/Kea/testdocs/en/test/bostid_b12sae.txt"), "UTF-8");

        AnalysisEngineDescription aggregate = createAggregateDescription(
                createPrimitiveDescription(BreakIteratorSegmenter.class),
                createPrimitiveDescription(KeaKeyphraseExtractorWrapper.class,
                        KeaKeyphraseExtractorWrapper.PARAM_KEA_PROPERTIES, "src/main/resources/Kea/models/kea_en.properties",
                        KeaKeyphraseExtractorWrapper.PARAM_LANG, "en",
                        KeaKeyphraseExtractorWrapper.PARAM_MAX_PHRASE_LENGTH, 8,
                        KeaKeyphraseExtractorWrapper.PARAM_MODEL, "src/main/resources/Kea/models/kea_en.model",
                        KeaKeyphraseExtractorWrapper.PARAM_NUMBER_PHRASES, 10)
        );

        AnalysisEngine engine = AnalysisEngineFactory.createAggregate(aggregate);
        JCas aJCas = engine.newJCas();
        aJCas.setDocumentText(testDocument);

        engine.process(aJCas);

        Set<String> keyphrases = new HashSet<String>();
        for (Keyphrase keyphrase : JCasUtil.select(aJCas, Keyphrase.class)) {
            keyphrases.add(keyphrase.getCoveredText());
        }

        assertEquals(testPhrases.size(), keyphrases.size());

        for (String item : testPhrases) {
            assertTrue(keyphrases.contains(item));
        }

    }


//    @Test
    public void testKeaKeyphraseExtraction2() throws Exception {

        Assume.assumeTrue(Runtime.getRuntime().maxMemory() > 400000000);

        Set<String> testPhrases = new TreeSet<String>();
        testPhrases.add("Halophytes");
        testPhrases.add("Karachi");
        testPhrases.add("Saline");
        testPhrases.add("Salt");
        testPhrases.add("Salt-Tolerant");
        testPhrases.add("dS/m");
        testPhrases.add("seawater");
        testPhrases.add("Atriplex");
        testPhrases.add("Australia");
        testPhrases.add("tolerance");

        String testDocument = FileUtils.readFileToString(new File("src/main/resources/Kea/testdocs/en/test/bostid_b12sae.txt"),"UTF-8");

        AnalysisEngineDescription aggregate = createAggregateDescription(
        		createPrimitiveDescription(BreakIteratorSegmenter.class),
        		createPrimitiveDescription(KeaKeyphraseExtractorWrapper.class,
                        KeaKeyphraseExtractorWrapper.PARAM_KEA_PROPERTIES, "src/main/resources/Kea/models/kea_en.properties",
                        KeaKeyphraseExtractorWrapper.PARAM_LANG, "en",
                        KeaKeyphraseExtractorWrapper.PARAM_MAX_PHRASE_LENGTH, 1,
                        KeaKeyphraseExtractorWrapper.PARAM_MODEL, "src/main/resources/Kea/models/kea_en.model",
                        KeaKeyphraseExtractorWrapper.PARAM_NUMBER_PHRASES, 10)
        );


        AnalysisEngine engine = AnalysisEngineFactory.createAggregate(aggregate);
        JCas aJCas = engine.newJCas();
        aJCas.setDocumentText(testDocument);

        engine.process(aJCas);

        Set<String> keyphrases = new HashSet<String>();
        for (Keyphrase keyphrase : JCasUtil.select(aJCas, Keyphrase.class)) {
            keyphrases.add(keyphrase.getCoveredText());
        }

        assertEquals(testPhrases.size(), keyphrases.size());

        for (String item : testPhrases) {
            assertTrue(keyphrases.contains(item));
        }

    }
}