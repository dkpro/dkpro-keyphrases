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

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

public class PageRankRankingFactory {

    /**
     * Method for generating AnalysisEngineDescriptor
     * With CooccurrenceGraph: Works and is sensible.
     * With LexicalSemanticGraph: Works but is not sensible.
     * @return Descriptor for annotator with weighted PageRank algorithm
     * @throws ResourceInitializationException
     */
    public static AnalysisEngineDescription getPageRankRanking_weighted()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                PageRankRanking.class,
                PageRankRanking.PARAM_WEIGHTED, true);
    }

    /**
     * Method for generating AnalysisEngineDescriptor
     * With CooccurrenceGraph: Works and is sensible. Information are lost and cooccurrences wit the frequency 1 are rated as high as those with a frequency of 100.
     * With LexicalSemanticGraph: Works but is not sensible. LSG are often fully connected.
     * @return Descriptor for annotator with unweighted PageRank algorithm
     * @throws ResourceInitializationException
     */
    public static AnalysisEngineDescription getPageRankRanking_unweighted()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                PageRankRanking.class,
                PageRankRanking.PARAM_WEIGHTED, false);
    }
}
