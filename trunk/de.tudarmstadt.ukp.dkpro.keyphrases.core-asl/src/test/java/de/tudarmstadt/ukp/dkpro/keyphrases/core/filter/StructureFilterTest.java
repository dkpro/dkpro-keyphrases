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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createAggregateDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;
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
import de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate.Candidate2KeyphraseConverter;
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

        AnalysisEngineDescription aggregate = createAggregateDescription(
        		createPrimitiveDescription(BreakIteratorSegmenter.class),
                createPrimitiveDescription(TreeTaggerPosLemmaTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                createPrimitiveDescription(TreeTaggerChunkerTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                CandidateAnnotatorFactory.getKeyphraseCandidateAnnotator_nc(false),
                createPrimitiveDescription(Candidate2KeyphraseConverter.class),
                createPrimitiveDescription(StructureFilter.class,
                        StructureFilter.PARAM_MIN_TOKENS, 1,
                        StructureFilter.PARAM_MAX_TOKENS, 4,
                        StructureFilter.PARAM_POS_PATTERNS, false)
        );

        AnalysisEngine engine = AnalysisEngineFactory.createAggregate(aggregate);
        JCas aJCas = engine.newJCas();
        aJCas.setDocumentText(testDocument);

        engine.process(aJCas);

        int i=0;
        for (Keyphrase k : JCasUtil.select(aJCas, Keyphrase.class)) {
            System.out.println(k);
            assertTrue(k.getKeyphrase(), expectedResults.contains(k.getKeyphrase()));
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

        AnalysisEngineDescription aggregate = createAggregateDescription(
                createPrimitiveDescription(BreakIteratorSegmenter.class),
                createPrimitiveDescription(TreeTaggerPosLemmaTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                createPrimitiveDescription(TreeTaggerChunkerTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                CandidateAnnotatorFactory.getKeyphraseCandidateAnnotator_nc(false),
                createPrimitiveDescription(Candidate2KeyphraseConverter.class),
                createPrimitiveDescription(StructureFilter.class,
                        StructureFilter.PARAM_MIN_TOKENS, 1,
                        StructureFilter.PARAM_MAX_TOKENS, 4,
                        StructureFilter.PARAM_POS_PATTERNS, false)
        );

        AnalysisEngine engine = AnalysisEngineFactory.createAggregate(aggregate);
        JCas aJCas = engine.newJCas();
        aJCas.setDocumentText(testDocument);

        engine.process(aJCas);

        int i=0;
        for (Keyphrase k : JCasUtil.select(aJCas, Keyphrase.class)) {
            System.out.println(k);
            assertTrue(k.getKeyphrase(), expectedResults.contains(k.getKeyphrase()));
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

        AnalysisEngineDescription aggregate = createAggregateDescription(
        		createPrimitiveDescription(BreakIteratorSegmenter.class),
                createPrimitiveDescription(TreeTaggerPosLemmaTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                createPrimitiveDescription(TreeTaggerChunkerTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                CandidateAnnotatorFactory.getKeyphraseCandidateAnnotator_nc(false),
                createPrimitiveDescription(Candidate2KeyphraseConverter.class),
                createPrimitiveDescription(StructureFilter.class,
                        StructureFilter.PARAM_MIN_TOKENS, 2,
                        StructureFilter.PARAM_MAX_TOKENS, 4,
                        StructureFilter.PARAM_POS_PATTERNS, false)
        );

        AnalysisEngine engine = AnalysisEngineFactory.createAggregate(aggregate);
        JCas aJCas = engine.newJCas();
        aJCas.setDocumentText(testDocument);

        engine.process(aJCas);

        int i=0;
        for (Keyphrase k : JCasUtil.select(aJCas, Keyphrase.class)) {
            System.out.println(k);
            assertTrue(k.getKeyphrase(), expectedResults.contains(k.getKeyphrase()));
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

        AnalysisEngineDescription aggregate = createAggregateDescription(
        		createPrimitiveDescription(BreakIteratorSegmenter.class),
                createPrimitiveDescription(TreeTaggerPosLemmaTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                createPrimitiveDescription(TreeTaggerChunkerTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                CandidateAnnotatorFactory.getKeyphraseCandidateAnnotator_nc(false),
                createPrimitiveDescription(Candidate2KeyphraseConverter.class),
                createPrimitiveDescription(StructureFilter.class,
                        StructureFilter.PARAM_MIN_TOKENS, 1,
                        StructureFilter.PARAM_MAX_TOKENS, 4,
                        StructureFilter.PARAM_POS_PATTERNS, true)
        );

        AnalysisEngine engine = AnalysisEngineFactory.createAggregate(aggregate);
        JCas aJCas = engine.newJCas();
        aJCas.setDocumentText(testDocument);

        engine.process(aJCas);

        int i=0;
        for (Keyphrase k : JCasUtil.select(aJCas, Keyphrase.class)) {
            System.out.println(k);
            assertTrue(k.getKeyphrase(), expectedResults.contains(k.getKeyphrase()));
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
