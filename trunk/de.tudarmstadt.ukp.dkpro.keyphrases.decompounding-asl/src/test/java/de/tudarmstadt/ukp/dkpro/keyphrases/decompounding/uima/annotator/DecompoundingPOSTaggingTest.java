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
package de.tudarmstadt.ukp.dkpro.keyphrases.decompounding.uima.annotator;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;

import java.io.File;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.BeforeClass;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Compound.CompoundSplitLevel;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.decompounding.uima.annotator.CompoundAnnotator;
import de.tudarmstadt.ukp.dkpro.core.decompounding.uima.resource.AsvToolboxSplitterResource;
import de.tudarmstadt.ukp.dkpro.core.decompounding.uima.resource.FrequencyRankerResource;
import de.tudarmstadt.ukp.dkpro.core.decompounding.uima.resource.RankerResource;
import de.tudarmstadt.ukp.dkpro.core.decompounding.uima.resource.SharedDictionary;
import de.tudarmstadt.ukp.dkpro.core.decompounding.uima.resource.SharedFinder;
import de.tudarmstadt.ukp.dkpro.core.decompounding.uima.resource.SharedLinkingMorphemes;
import de.tudarmstadt.ukp.dkpro.core.decompounding.uima.resource.SplitterResource;
import de.tudarmstadt.ukp.dkpro.core.decompounding.web1t.LuceneIndexer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class DecompoundingPOSTaggingTest
{

    static File source = new File("src/test/resources/ranking/n-grams");
    static File index = new File("target/test/index");
    static String jWeb1TPath = "src/test/resources/web1t/de";
    static String indexPath = "target/test/index";


    @BeforeClass
    public static void createIndex()
        throws Exception
    {
        index.mkdirs();

        LuceneIndexer indexer = new LuceneIndexer(source, index);
        indexer.index();
    }

    @Test
    public void test()
        throws ResourceInitializationException, AnalysisEngineProcessException
    {

        String text = "Die Verpflegung der Bauarbeiter besteht überwiegend aus Grillfleisch und "
                + "Bier. Akademiker trinken gerne Rotwein und Lehrer trinken Schnaps.";

        AnalysisEngineDescription stanfordSegmenter = createPrimitiveDescription(
                StanfordSegmenter.class);

        AnalysisEngineDescription decompoudingAE = createPrimitiveDescription(
                CompoundAnnotator.class,
                CompoundAnnotator.PARAM_SPLITTING_ALGO,
                createExternalResourceDescription(
                        AsvToolboxSplitterResource.class,
                        SplitterResource.PARAM_DICT_RESOURCE,
                        createExternalResourceDescription(SharedDictionary.class),
                        SplitterResource.PARAM_MORPHEME_RESOURCE,
                        createExternalResourceDescription(SharedLinkingMorphemes.class)),
                CompoundAnnotator.PARAM_RANKING_ALGO,
                createExternalResourceDescription(
                        FrequencyRankerResource.class,
                        RankerResource.PARAM_FINDER_RESOURCE,
                        createExternalResourceDescription(SharedFinder.class,
                                SharedFinder.PARAM_INDEX_PATH, indexPath,
                                SharedFinder.PARAM_NGRAM_LOCATION, jWeb1TPath)));

        AnalysisEngineDescription compoundPartTokenizer = createPrimitiveDescription(
                CompoundPartTokenizer.class,
                CompoundPartTokenizer.PARAM_COMPOUND_SPLIT_LEVEL, CompoundSplitLevel.LOWEST);

        AnalysisEngineDescription stanfordPosTagger = createPrimitiveDescription(
                StanfordPosTagger.class);

        AnalysisEngineDescription aeDescription = AnalysisEngineFactory.createAggregateDescription(
                stanfordSegmenter, decompoudingAE, compoundPartTokenizer, stanfordPosTagger);
        AnalysisEngine ae = AnalysisEngineFactory.createAggregate(aeDescription);
        JCas jcas = ae.newJCas();
        jcas.setDocumentText(text);
        jcas.setDocumentLanguage("de");
        ae.process(jcas);

        for(Token token : JCasUtil.select(jcas, Token.class)){
            System.out.println(token.getCoveredText()+"\t"+token.getPos().getPosValue());
        }

    }

}
