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

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Assume;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerChunkerTT4J;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerPosLemmaTT4J;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerTT4JBase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate.CandidateAnnotatorFactory;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class StructureFilterTest
{
    @Test
    public void lengthFilterTestKeyphrase() throws Exception {

        // "a unusual long example sentence phrase structure" is too long and should not pass the filter
    	checkModelsAndBinary("en");
        String testDocument = "This is a unusual long example sentence phrase structure. This is a short example.";

        Set<String> expectedResults = new HashSet<String>();
        expectedResults.add("This");
        expectedResults.add("a short example");

        AnalysisEngineDescription aggregate = createEngineDescription(
        		createEngineDescription(BreakIteratorSegmenter.class),
                createEngineDescription(TreeTaggerPosLemmaTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                createEngineDescription(TreeTaggerChunkerTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                CandidateAnnotatorFactory.getKeyphraseCandidateAnnotator_nc(false),
                createEngineDescription(StructureFilter.class,
                        StructureFilter.PARAM_MIN_TOKENS, 1,
                        StructureFilter.PARAM_MAX_TOKENS, 4,
                        StructureFilter.PARAM_POS_PATTERNS, false)
        );

        AnalysisEngine engine = AnalysisEngineFactory.createEngine(aggregate);
        JCas aJCas = engine.newJCas();
        aJCas.setDocumentText(testDocument);

        engine.process(aJCas);

        int i=0;
        for (Keyphrase kc : JCasUtil.select(aJCas, Keyphrase.class)) {
            System.out.println(kc);
            assertTrue(kc.getKeyphrase(), expectedResults.contains(kc.getKeyphrase()));
            i++;
        }
        assertEquals(3,i);
    }

    @Test
    public void posStructureFilterTestKeyphrase() throws Exception {

        // "plug and play methodology" has structure V_C_V_N
    	checkModelsAndBinary("en");
        String testDocument = "Mr Smith knows plug and play methodology.";

        Set<String> expectedResults = new HashSet<String>();
        expectedResults.add("Mr Smith");
        expectedResults.add("plug and play methodology");

        AnalysisEngineDescription aggregate = createEngineDescription(
                createEngineDescription(BreakIteratorSegmenter.class),
                createEngineDescription(TreeTaggerPosLemmaTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                createEngineDescription(TreeTaggerChunkerTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                CandidateAnnotatorFactory.getKeyphraseCandidateAnnotator_nc(false),
                createEngineDescription(StructureFilter.class,
                        StructureFilter.PARAM_MIN_TOKENS, 1,
                        StructureFilter.PARAM_MAX_TOKENS, 4,
                        StructureFilter.PARAM_POS_PATTERNS, false)
        );

        AnalysisEngine engine = AnalysisEngineFactory.createEngine(aggregate);
        JCas aJCas = engine.newJCas();
        aJCas.setDocumentText(testDocument);

        engine.process(aJCas);

        int i=0;
        for (Keyphrase kc : JCasUtil.select(aJCas, Keyphrase.class)) {
            System.out.println(kc);
            assertTrue(kc.getKeyphrase(), expectedResults.contains(kc.getKeyphrase()));
            i++;
        }
        assertEquals(2,i);
    }

    @Test
    public void lengthFilterTestCandidate() throws Exception {
    	
        // "a unusual long example sentence phrase structure" is too long and should not pass the filter
    	checkModelsAndBinary("en");
        String testDocument = "This is a unusual long example sentence phrase structure. This is a short example.";

        Set<String> expectedResults = new HashSet<String>();
        expectedResults.add("This");
        expectedResults.add("a short example");

        AnalysisEngineDescription aggregate = createEngineDescription(
        		createEngineDescription(BreakIteratorSegmenter.class),
                createEngineDescription(TreeTaggerPosLemmaTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                createEngineDescription(TreeTaggerChunkerTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                CandidateAnnotatorFactory.getKeyphraseCandidateAnnotator_nc(false),
                createEngineDescription(StructureFilter.class,
                        StructureFilter.PARAM_MIN_TOKENS, 2,
                        StructureFilter.PARAM_MAX_TOKENS, 4,
                        StructureFilter.PARAM_POS_PATTERNS, false)
        );

        AnalysisEngine engine = AnalysisEngineFactory.createEngine(aggregate);
        JCas aJCas = engine.newJCas();
        aJCas.setDocumentText(testDocument);

        engine.process(aJCas);

        int i=0;
        for (Keyphrase kc : JCasUtil.select(aJCas, Keyphrase.class)) {
            System.out.println(kc);
            assertTrue(kc.getKeyphrase(), expectedResults.contains(kc.getKeyphrase()));
            i++;
        }
        assertEquals(1,i);
    }

    @Test
    public void posStructureFilterTestCandidate() throws Exception {

        // "plug and play methodology" has structure V_C_V_N
    	// this is not a valid pattern in our pos structure filter
    	checkModelsAndBinary("en");
        String testDocument = "Mr Smith knows plug and play methodology.";

        Set<String> expectedResults = new HashSet<String>();
        expectedResults.add("Mr Smith");

        AnalysisEngineDescription aggregate = createEngineDescription(
        		createEngineDescription(BreakIteratorSegmenter.class),
                createEngineDescription(TreeTaggerPosLemmaTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                createEngineDescription(TreeTaggerChunkerTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                CandidateAnnotatorFactory.getKeyphraseCandidateAnnotator_nc(false),
                createEngineDescription(StructureFilter.class,
                        StructureFilter.PARAM_MIN_TOKENS, 1,
                        StructureFilter.PARAM_MAX_TOKENS, 4,
                        StructureFilter.PARAM_POS_PATTERNS, true)
        );

        AnalysisEngine engine = AnalysisEngineFactory.createEngine(aggregate);
        JCas aJCas = engine.newJCas();
        aJCas.setDocumentText(testDocument);

        engine.process(aJCas);

        int i=0;
        for (Keyphrase kc : JCasUtil.select(aJCas, Keyphrase.class)) {
            System.out.println(kc);
            assertTrue(kc.getKeyphrase(), expectedResults.contains(kc.getKeyphrase()));
            i++;
        }
        assertEquals(0,i);
    }
    
    private void checkModelsAndBinary(String lang) {
		Assume.assumeTrue(getClass().getResource(
				"/de/tudarmstadt/ukp/dkpro/core/treetagger/lib/chunker-" + lang + "-little-endian.par") != null);

		Assume.assumeTrue(getClass().getResource(
				"/de/tudarmstadt/ukp/dkpro/core/treetagger/bin/LICENSE.txt") != null);
    }
    
}
