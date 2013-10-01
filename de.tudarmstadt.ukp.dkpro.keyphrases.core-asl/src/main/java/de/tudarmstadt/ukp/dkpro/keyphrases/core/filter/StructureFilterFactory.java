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

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

public class StructureFilterFactory {

    public static AnalysisEngineDescription getStructureFilter_1_4()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                StructureFilter.class,
                StructureFilter.PARAM_MIN_TOKENS, 1,
                StructureFilter.PARAM_MAX_TOKENS, 4);
    }

    public static AnalysisEngineDescription getStructureFilter_2_4()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                StructureFilter.class,
                StructureFilter.PARAM_MIN_TOKENS, 2,
                StructureFilter.PARAM_MAX_TOKENS, 4);
    }

    public static AnalysisEngineDescription getStructureFilter_1()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                StructureFilter.class,
                StructureFilter.PARAM_MIN_TOKENS, 1,
                StructureFilter.PARAM_MAX_TOKENS, 1);
    }

    public static AnalysisEngineDescription getStructureFilter_2()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                StructureFilter.class,
                StructureFilter.PARAM_MIN_TOKENS, 2,
                StructureFilter.PARAM_MAX_TOKENS, 2);
    }

    public static AnalysisEngineDescription getStructureFilter_3()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                StructureFilter.class,
                StructureFilter.PARAM_MIN_TOKENS, 3,
                StructureFilter.PARAM_MAX_TOKENS, 3);
    }

    public static AnalysisEngineDescription getStructureFilter_4()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                StructureFilter.class,
                StructureFilter.PARAM_MIN_TOKENS, 4,
                StructureFilter.PARAM_MAX_TOKENS, 4);
    }

    public static AnalysisEngineDescription getStructureFilter_2_4_posPatterns()
    throws ResourceInitializationException
    {
        return createPrimitiveDescription(
                StructureFilter.class,
                StructureFilter.PARAM_MIN_TOKENS, 2,
                StructureFilter.PARAM_MAX_TOKENS, 4,
                StructureFilter.PARAM_POS_PATTERNS, true);
    }
}
