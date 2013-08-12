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
