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