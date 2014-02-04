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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.factory;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.stopwordremover.StopWordRemover;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class StopwordFilterFactory {

    public static AnalysisEngineDescription getStopwordFilter_english()
    throws ResourceInitializationException
    {
        return createEngineDescription(
                StopWordRemover.class,
                StopWordRemover.PARAM_STOP_WORD_LIST_FILE_NAMES, "[en]classpath:/stopwords/english_stopwords.txt",
                StopWordRemover.PARAM_PATHS, Keyphrase.class.getName()+"/keyphrase");
    }
    public static AnalysisEngineDescription getStopwordFilter(String... wordLists)
    throws ResourceInitializationException
    {
        return createEngineDescription(
                StopWordRemover.class,
                StopWordRemover.PARAM_STOP_WORD_LIST_FILE_NAMES, wordLists,
                StopWordRemover.PARAM_PATHS, Keyphrase.class.getName()+"/keyphrase");
    }
}
