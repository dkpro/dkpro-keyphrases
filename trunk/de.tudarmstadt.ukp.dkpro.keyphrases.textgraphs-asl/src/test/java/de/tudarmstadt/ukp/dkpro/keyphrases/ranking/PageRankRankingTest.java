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
package de.tudarmstadt.ukp.dkpro.keyphrases.ranking;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createAggregateDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.posfilter.PosFilter;
import de.tudarmstadt.ukp.dkpro.core.snowball.SnowballStemmer;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerPosLemmaTT4J;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerTT4JBase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.CooccurrenceGraphFactory;

public class PageRankRankingTest
{
    // assertEquals on doubles needs an epsilon
    protected static final double EPSILON = 0.000001;

    @Test
    public void PosFilterTest() throws Exception {

        Map<String,Double> expectedResults = new HashMap<String,Double>();
        expectedResults.put("sentence", 1.6487752080270504);
        expectedResults.put("second", 0.8508165279819666);
        expectedResults.put("long", 0.5004082639909833);
        expectedResults.put("more", 0.5004082639909833);
        expectedResults.put("necessary", 0.8508165279819666);
        expectedResults.put("test", 1.6487752080270504);

        String testDocument = "This is a not so long test sentence. This is a longer second test sentence. More sentences are necessary for the tests.";

        AnalysisEngineDescription aggregate = createAggregateDescription(
        		createPrimitiveDescription(de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter.class),
                createPrimitiveDescription(TreeTaggerPosLemmaTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                createPrimitiveDescription(
                        PosFilter.class,
                        PosFilter.PARAM_TYPE_TO_REMOVE, Lemma.class.getName(),
                        PosFilter.PARAM_ADJ, true,
                        PosFilter.PARAM_N, true
                ),
                CooccurrenceGraphFactory.getCooccurrenceGraph_lemma(),
                createPrimitiveDescription(PageRankRanking.class,
                        PageRankRanking.PARAM_WEIGHTED, false));

        AnalysisEngine engine = AnalysisEngineFactory.createAggregate(aggregate);
        JCas aJCas = engine.newJCas();
        aJCas.setDocumentText(testDocument);

        engine.process(aJCas);

        int i=0;
        for (Keyphrase keyphrase : JCasUtil.select(aJCas, Keyphrase.class)) {
//                System.out.println("\"" + keyphrase.getKeyphrase() + "\", " + keyphrase.getScore());
            assertTrue(keyphrase.getKeyphrase(), expectedResults.containsKey(keyphrase.getKeyphrase()));
            assertEquals(keyphrase.getKeyphrase(), expectedResults.get(keyphrase.getKeyphrase()), keyphrase.getScore(), EPSILON);
            i++;
        }
        assertEquals(10,i);
    }

    @Test
    public void PosFilterWindowSizeTest() throws Exception {
        Map<String,Double> expectedResults = new HashMap<String,Double>();
        expectedResults.put("sentence", 1.2233253524500909);
        expectedResults.put("second", 0.9985807508947789);
        expectedResults.put("long", 0.7780938966551303);
        expectedResults.put("necessary", 0.7780938966551303);
        expectedResults.put("more", 0.9985807508947789);
        expectedResults.put("test", 1.2233253524500909);

        String testDocument = "This is a not so long test sentence. This is a longer second test sentence. More sentences are necessary for the tests.";

        AnalysisEngineDescription aggregate = createAggregateDescription(
        		createPrimitiveDescription(BreakIteratorSegmenter.class),
                createPrimitiveDescription(TreeTaggerPosLemmaTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                createPrimitiveDescription(
                        PosFilter.class,
                        PosFilter.PARAM_TYPE_TO_REMOVE, Lemma.class.getName(),
                        PosFilter.PARAM_ADJ, true,
                        PosFilter.PARAM_N, true
                ),
                CooccurrenceGraphFactory.getCooccurrenceGraph_lemma_windowSize5(),
                createPrimitiveDescription(PageRankRanking.class,
                        PageRankRanking.PARAM_WEIGHTED, false));

        AnalysisEngine engine = AnalysisEngineFactory.createAggregate(aggregate);
        JCas aJCas = engine.newJCas();
        aJCas.setDocumentText(testDocument);

        engine.process(aJCas);

        int i=0;
        for (Keyphrase keyphrase : JCasUtil.select(aJCas, Keyphrase.class)) {
//                System.out.println("\"" + keyphrase.getKeyphrase() + "\", " + keyphrase.getScore());
            assertTrue(keyphrase.getKeyphrase(), expectedResults.containsKey(keyphrase.getKeyphrase()));
            assertEquals(keyphrase.getKeyphrase(), expectedResults.get(keyphrase.getKeyphrase()), keyphrase.getScore(), EPSILON);
            i++;
        }
        assertEquals(8,i);
    }

    @Test
    public void PosFilterTokenTest() throws Exception {

        Map<String,Double> expectedResults = new HashMap<String,Double>();
        expectedResults.put("test", 1.389844507927117);
        expectedResults.put("necessary", 1.130446300811737);
        expectedResults.put("More", 0.9781026497450921);
        expectedResults.put("tests", 0.6305394317298283);
        expectedResults.put("long", 0.5437990437678338);
        expectedResults.put("second", 0.9273103124158457);
        expectedResults.put("sentences", 1.0462913625399426);
        expectedResults.put("sentence", 1.3536663910626023);

        String testDocument = "This is a not so long test sentence. This is a longer second test sentence. More sentences are necessary for the tests.";

        AnalysisEngineDescription aggregate = createAggregateDescription(
        		createPrimitiveDescription(BreakIteratorSegmenter.class),
                createPrimitiveDescription(TreeTaggerPosLemmaTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                createPrimitiveDescription(
                        PosFilter.class,
                        PosFilter.PARAM_TYPE_TO_REMOVE, Token.class.getName(),
                        PosFilter.PARAM_ADJ, true,
                        PosFilter.PARAM_N, true
                ),
                CooccurrenceGraphFactory.getCooccurrenceGraph_token(),
                createPrimitiveDescription(PageRankRanking.class,
                        PageRankRanking.PARAM_WEIGHTED, false));

        AnalysisEngine engine = AnalysisEngineFactory.createAggregate(aggregate);
        JCas aJCas = engine.newJCas();
        aJCas.setDocumentText(testDocument);

        engine.process(aJCas);

        int i=0;
        for (Keyphrase keyphrase : JCasUtil.select(aJCas, Keyphrase.class)) {
//                System.out.println("\"" + keyphrase.getKeyphrase() + "\", " + keyphrase.getScore());
            assertTrue(keyphrase.getKeyphrase(), expectedResults.containsKey(keyphrase.getKeyphrase()));
            assertEquals(keyphrase.getKeyphrase(), expectedResults.get(keyphrase.getKeyphrase()), keyphrase.getScore(), EPSILON);
            i++;
        }
        assertEquals(10,i);
    }

    @Test
    public void TestDefaults() throws Exception {

        String testDocument = "This is a not so long test sentence. This is a longer second test sentence. More sentences are necessary for the tests.";

        AnalysisEngineDescription aggregate = createAggregateDescription(
        		createPrimitiveDescription(BreakIteratorSegmenter.class),
                createPrimitiveDescription(
                        TreeTaggerPosLemmaTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"
                ),
                createPrimitiveDescription(
                        PosFilter.class,
                        PosFilter.PARAM_TYPE_TO_REMOVE, Token.class.getName()
                ),
                CooccurrenceGraphFactory.getCooccurrenceGraph_token(),
                createPrimitiveDescription(
                        PageRankRanking.class
                )
        );

        AnalysisEngine engine = AnalysisEngineFactory.createAggregate(aggregate);
        JCas aJCas = engine.newJCas();
        aJCas.setDocumentText(testDocument);

        engine.process(aJCas);

        int i=0;
        for (Keyphrase keyphrase : JCasUtil.select(aJCas, Keyphrase.class)) {
            System.out.println("\"" + keyphrase.getKeyphrase() + "\", " + keyphrase.getScore());
            i++;
        }
        assertEquals(0,i);
    }

    @Test
    public void UimaTokenTest() throws Exception {

        Map<String,Double> expectedResults = new HashMap<String,Double>();
        expectedResults.put(".", 1.661900603917403);
        expectedResults.put("test", 1.2836673859058836);
        expectedResults.put("are", 0.9620982432002869);
        expectedResults.put("sentences", 0.9431203792298113);
        expectedResults.put("so", 0.921031990159519);
        expectedResults.put("long", 0.9051287280811249);
        expectedResults.put("More", 0.9039247574403875);
        expectedResults.put("second", 0.8954061167643781);
        expectedResults.put("This", 0.8825146333163552);
        expectedResults.put("sentence", 0.8668209606573215);
        expectedResults.put("is", 0.8927104216342462);
        expectedResults.put("longer", 0.8981820352527301);
        expectedResults.put("tests", 0.9039247574403875);
        expectedResults.put("not", 0.9090578417656192);
        expectedResults.put("the", 0.9431203792298113);
        expectedResults.put("for", 0.9620982432002869);
        expectedResults.put("necessary", 0.9678483350255677);
        expectedResults.put("a", 1.2974441877788796);

        String testDocument = "This is a not so long test sentence. This is a longer second test sentence. More sentences are necessary for the tests.";

        AnalysisEngineDescription aggregate = createAggregateDescription(
        		createPrimitiveDescription(BreakIteratorSegmenter.class),
                createPrimitiveDescription(TreeTaggerPosLemmaTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                CooccurrenceGraphFactory.getCooccurrenceGraph_token(),
                createPrimitiveDescription(PageRankRanking.class,
                        PageRankRanking.PARAM_WEIGHTED, false));

        AnalysisEngine engine = AnalysisEngineFactory.createAggregate(aggregate);
        JCas aJCas = engine.newJCas();
        aJCas.setDocumentText(testDocument);

        engine.process(aJCas);

        int i=0;
        for (Keyphrase keyphrase : JCasUtil.select(aJCas, Keyphrase.class)) {
//            System.out.println("\"" + keyphrase.getKeyphrase() + "\", " + keyphrase.getScore());
            assertTrue(keyphrase.getKeyphrase(), expectedResults.containsKey(keyphrase.getKeyphrase()));
            assertEquals(keyphrase.getKeyphrase(), expectedResults.get(keyphrase.getKeyphrase()), keyphrase.getScore(), EPSILON);
            i++;
        }
        assertEquals(23,i);
    }

    @Test
    public void UimaStemTest() throws Exception {

        Map<String,Double> expectedResults = new HashMap<String,Double>();
        expectedResults.put("test", 1.8134305614665536);
        expectedResults.put(".", 1.4329982193841975);
        expectedResults.put("so", 0.8740762172963042);
        expectedResults.put("for", 0.8731390116788602);
        expectedResults.put("longer", 0.8508521426507418);
        expectedResults.put("long", 0.8297210900015667);
        expectedResults.put("are", 0.8259673863961844);
        expectedResults.put("This", 0.8153284095664003);
        expectedResults.put("More", 0.7598346171720322);
        expectedResults.put("second", 0.8199015271228548);
        expectedResults.put("the", 0.8293347813912294);
        expectedResults.put("is", 0.8488911967404977);
        expectedResults.put("necessari", 0.8720856407424997);
        expectedResults.put("not", 0.8738473387706528);
        expectedResults.put("a", 1.2438572442186149);
        expectedResults.put("sentenc", 1.4367346154008103);

        String testDocument = "This is a not so long test sentence. This is a longer second test sentence. More sentences are necessary for the tests.";

        AnalysisEngineDescription aggregate = createAggregateDescription(
        		createPrimitiveDescription(BreakIteratorSegmenter.class),
                createPrimitiveDescription(TreeTaggerPosLemmaTT4J.class,
                		TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
        		createPrimitiveDescription	(SnowballStemmer.class,
                		SnowballStemmer.PARAM_LANGUAGE, "en"),
                CooccurrenceGraphFactory.getCooccurrenceGraph_stem(),
                createPrimitiveDescription(PageRankRanking.class, PageRankRanking.PARAM_WEIGHTED, false)
        );

        AnalysisEngine engine = AnalysisEngineFactory.createAggregate(aggregate);
        JCas aJCas = engine.newJCas();
        aJCas.setDocumentText(testDocument);

        engine.process(aJCas);

        int i=0;
        for (Keyphrase keyphrase : JCasUtil.select(aJCas, Keyphrase.class)) {
//                System.out.println("\"" + keyphrase.getKeyphrase() + "\", " + keyphrase.getScore());
            assertTrue(keyphrase.getKeyphrase(), expectedResults.containsKey(keyphrase.getKeyphrase()));
            assertEquals(keyphrase.getKeyphrase(), expectedResults.get(keyphrase.getKeyphrase()), keyphrase.getScore(), EPSILON);
            i++;
        }
        assertEquals(23,i);
    }

//    // TODO find way to incorporate ESA indexes in a portable way
//    // currently ignored, as the test has dependencies to full ESA indexes.
//    // See Bug 145
//    @Ignore
//    @Test
//    public void PosFilterTestWeighted() throws Exception {
//
//        // this also tests whether the PosFilter works with LexSemGraphs (if it does not work, the resulting keyphrases will contain prepositions)
//
//        Map<String,Double> expectedResults = new HashMap<String,Double>();
//        expectedResults.put("long", 1.2594937874894585);
//        expectedResults.put("test", 0.8591668364701337);
//        expectedResults.put("sentence", 0.6547397950634422);
//        expectedResults.put("second", 1.2723503165002539);
//        expectedResults.put("more", 0.15000000000000002);
//        expectedResults.put("necessary", 0.954249264476713);
//
//        String testDocument = "This is a not so long test sentence. This is a longer second test sentence. More sentences are necessary for the tests.";
//
//        AnalysisEngineDescription lexSemGraph = createPrimitiveDescription(
//                LexSemGraph.class,
//                LexSemGraph.PARAM_FEATURE_PATH, KeyphraseCandidate.class.getName()
//        );
//
//        bindResource(
//                lexSemGraph,
//                LexSemGraph.SR_RESOURCE,
//                TestRelatednessResource.class
//        );
//
//        AnalysisEngineDescription aggregate = createAggregateDescription(
//                createPrimitiveDescription(BreakIteratorSegmenter.class),
//                createPrimitiveDescription(TreeTaggerPosLemmaTT4J.class,
//                        TreeTaggerTT4JBase.PARAM_LANGUAGE_CODE, "en"),
//                createPrimitiveDescription(
//                        PosFilter.class,
//                        PosFilter.PARAM__TYPE_TO_REMOVE, Token.class.getName(),
//                        PosFilter.PARAM_ADJ, true,
//                        PosFilter.PARAM_N, true
//                ),
//                CandidateAnnotatorFactory.getKeyphraseCandidateAnnotator_lemma(true),
//                lexSemGraph,
//                createPrimitiveDescription(
//                        PageRankRanking.class,
//                        PageRankRanking.PARAM_WEIGHTED, true
//                )
//        );
//
//        AnalysisEngine engine = AnalysisEngineFactory.createAggregate(aggregate);
//        JCas aJCas = engine.newJCas();
//        aJCas.setDocumentText(testDocument);
//
//        engine.process(aJCas);
//
//        int i=0;
//        for (Keyphrase keyphrase : JCasUtil.select(aJCas, Keyphrase.class)) {
//            System.out.println("\"" + keyphrase.getKeyphrase() + "\", " + keyphrase.getScore());
//            assertTrue(keyphrase.getKeyphrase(), expectedResults.containsKey(keyphrase.getKeyphrase()));
//            assertEquals(keyphrase.getKeyphrase(), expectedResults.get(keyphrase.getKeyphrase()), keyphrase.getScore(), EPSILON);
//            i++;
//        }
//        assertEquals(6,i);
//    }
}