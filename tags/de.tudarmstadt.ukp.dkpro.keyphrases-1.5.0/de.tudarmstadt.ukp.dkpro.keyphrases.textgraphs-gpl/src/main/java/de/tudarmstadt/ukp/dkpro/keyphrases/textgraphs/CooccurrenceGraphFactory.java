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
package de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Stem;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

/**
 * Combination with ranking:
 * With NodeDegree weighted: Works and is sensible. Collocations may be rated very high.
 * With NodeDegree unweighted: Works and is sensible. Information are lost and cooccurrences wit the frequency 1 are rated as high as those with a frequency of 100.
 * With PageRank weighted: Works and is sensible.
 * With PageRank unweighted:  Works and is sensible. Information are lost and cooccurrences wit the frequency 1 are rated as high as those with a frequency of 100.
 */
public class CooccurrenceGraphFactory {

    public static AnalysisEngineDescription getCooccurrenceGraph_token()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                CooccurrenceGraph.class,
                CooccurrenceGraph.PARAM_FEATURE_PATH, Token.class.getName(),
                CooccurrenceGraph.PARAM_WINDOW_SIZE, 2);
    }

    public static AnalysisEngineDescription getCooccurrenceGraph_stem()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                CooccurrenceGraph.class,
                CooccurrenceGraph.PARAM_FEATURE_PATH, Stem.class.getName()+"/value",
                CooccurrenceGraph.PARAM_WINDOW_SIZE, 2);
    }

    public static AnalysisEngineDescription getCooccurrenceGraph_lemma()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                CooccurrenceGraph.class,
                CooccurrenceGraph.PARAM_FEATURE_PATH, Lemma.class.getName()+"/value",
                CooccurrenceGraph.PARAM_WINDOW_SIZE, 2);
    }

    public static AnalysisEngineDescription getCooccurrenceGraph_lemma_windowSize5()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                CooccurrenceGraph.class,
                CooccurrenceGraph.PARAM_FEATURE_PATH, Lemma.class.getName()+"/value",
                CooccurrenceGraph.PARAM_WINDOW_SIZE, 5);
    }

    public static AnalysisEngineDescription getCooccurrenceGraph_ngram()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                CooccurrenceGraph.class,
                CooccurrenceGraph.PARAM_FEATURE_PATH, "de.tudarmstadt.ukp.dkpro.core.type.NGram/",
                CooccurrenceGraph.PARAM_WINDOW_SIZE, 2);
    }


}