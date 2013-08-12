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
package de.tudarmstadt.ukp.dkpro.keyphrases.lab.tasks;

import static de.tudarmstadt.ukp.dkpro.core.api.io.ResourceCollectionReaderBase.INCLUDE_PREFIX;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import java.io.File;
import java.io.IOException;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.tudarmstadt.ukp.dkpro.core.stopwordremover.StopWordRemover;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate.Candidate2KeyphraseConverter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate.CandidateAnnotator;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.KeyphraseMerger;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.StructureFilter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate;
import de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.CooccurrenceGraph;
import de.tudarmstadt.ukp.dkpro.lab.engine.TaskContext;
import de.tudarmstadt.ukp.dkpro.lab.storage.StorageService.AccessMode;
import de.tudarmstadt.ukp.dkpro.lab.task.Discriminator;
import de.tudarmstadt.ukp.dkpro.lab.uima.task.impl.UimaTaskBase;

public class CandidateSelectionTask
    extends UimaTaskBase
{
    public static final String KEY_INPUT_XMI = "INPUT_XMI";
    public static final String KEY_OUTPUT_XMI = "OUTPUT_XMI";


    @Discriminator
    private String candidateFeaturePath;
    @Discriminator
    private boolean shouldResolveOverlaps;
    @Discriminator
    private int keyphraseMergerMaxTokens;
    @Discriminator
    private int structureFilterMinTokens;
    @Discriminator
    private int structureFilterMaxTokens;
    @Discriminator
    private String cooccurrenceGraphFeaturePath;
    @Discriminator
    private int cooccurrenceGraphWindowSize;
    @Discriminator
    private boolean structureFilterPosPatterns;


    @Override
    public CollectionReaderDescription getCollectionReaderDescription(TaskContext aContext)
        throws ResourceInitializationException, IOException
    {
        File inputXmiRoot = aContext.getStorageLocation(KEY_INPUT_XMI, AccessMode.READONLY);
        return createReader(XmiReader.class,
                XmiReader.PARAM_PATH, inputXmiRoot,
                XmiReader.PARAM_PATTERNS, new String[] { INCLUDE_PREFIX + "**/*.xmi.gz" });
    }

    @Override
    public AnalysisEngineDescription getAnalysisEngineDescription(TaskContext aContext)
        throws ResourceInitializationException, IOException
    {

        AnalysisEngineDescription candidateAnnotator = createPrimitiveDescription(
                CandidateAnnotator.class,
                CandidateAnnotator.PARAM_FEATURE_PATH, candidateFeaturePath,
                CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, shouldResolveOverlaps);

        AnalysisEngineDescription converter = createPrimitiveDescription(
                Candidate2KeyphraseConverter.class);

        AnalysisEngineDescription merger = createPrimitiveDescription(
                KeyphraseMerger.class,
                KeyphraseMerger.PARAM_MAX_LENGTH, keyphraseMergerMaxTokens);

        AnalysisEngineDescription structureFilter = createPrimitiveDescription(
                StructureFilter.class,
                StructureFilter.PARAM_MIN_TOKENS, structureFilterMinTokens,
                StructureFilter.PARAM_MAX_TOKENS, structureFilterMaxTokens,
                StructureFilter.PARAM_POS_PATTERNS, structureFilterPosPatterns);

        AnalysisEngineDescription cooccurrenceGraph = createPrimitiveDescription(
				CooccurrenceGraph.class,
				CooccurrenceGraph.PARAM_FEATURE_PATH, cooccurrenceGraphFeaturePath,
				CooccurrenceGraph.PARAM_WINDOW_SIZE, cooccurrenceGraphWindowSize);

        File outputXmiRoot = aContext.getStorageLocation(KEY_OUTPUT_XMI, AccessMode.ADD_ONLY);
        AnalysisEngineDescription xmiWriter = createPrimitiveDescription(
                XmiWriter.class,
                XmiWriter.PARAM_TARGET_LOCATION, outputXmiRoot,
                XmiWriter.PARAM_COMPRESSION, "GZIP"
                );

        return createEngine(
                candidateAnnotator,
                converter,
                merger,
                structureFilter,
                cooccurrenceGraph,
                xmiWriter);
    }


}
