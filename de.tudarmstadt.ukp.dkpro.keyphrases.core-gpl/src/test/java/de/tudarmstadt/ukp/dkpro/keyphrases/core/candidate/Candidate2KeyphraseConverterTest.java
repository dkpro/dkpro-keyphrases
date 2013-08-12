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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createAggregateDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.ngrams.NGramAnnotator;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public
class Candidate2KeyphraseConverterTest
{
    @Test
    public
    void simpleKeyphraseAnnoatorTest()
    throws Exception
    {
        String testDocument = "A b c d. E f g h i.";

        List<String> keyphrases = new ArrayList<String>();

        AnalysisEngineDescription aggregate = createAggregateDescription(
                createPrimitiveDescription(BreakIteratorSegmenter.class),
                createPrimitiveDescription(NGramAnnotator.class,
                        NGramAnnotator.PARAM_N, 3),
                CandidateAnnotatorFactory.getKeyphraseCandidateAnnotator_token(false),
                createPrimitiveDescription(Candidate2KeyphraseConverter.class)
        );

        AnalysisEngine engine = AnalysisEngineFactory.createAggregate(aggregate);
        JCas aJCas = engine.newJCas();
        aJCas.setDocumentText(testDocument);

        engine.process(aJCas);

        AnalysisEngineDescription aggregate2 = createAggregateDescription(
        		createPrimitiveDescription(BreakIteratorSegmenter.class),
        		createPrimitiveDescription(NGramAnnotator.class,
                        NGramAnnotator.PARAM_N, 3),
                CandidateAnnotatorFactory.getKeyphraseCandidateAnnotator_ngram(false),
                createPrimitiveDescription(Candidate2KeyphraseConverter.class)
        );

        AnalysisEngine engine2 = AnalysisEngineFactory.createAggregate(aggregate2);
        engine2.process(aJCas);

        for (Keyphrase keyphrase : JCasUtil.select(aJCas, Keyphrase.class)) {
            keyphrases.add(keyphrase.getCoveredText());
            System.out.println(keyphrase.getCoveredText());
        }

        assertEquals(36, keyphrases.size());
    }
}
