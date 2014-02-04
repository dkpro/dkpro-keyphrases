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
package de.tudarmstadt.ukp.dkpro.keyphrases.lab.tasks;

import static de.tudarmstadt.ukp.dkpro.core.api.io.ResourceCollectionReaderBase.INCLUDE_PREFIX;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.File;
import java.io.IOException;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.io.bincas.BinaryCasReader;
import de.tudarmstadt.ukp.dkpro.core.io.bincas.BinaryCasWriter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate.CandidateAnnotator;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.postprocessing.KeyphraseMerger;
import de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.CooccurrenceGraph;
import de.tudarmstadt.ukp.dkpro.lab.engine.TaskContext;
import de.tudarmstadt.ukp.dkpro.lab.storage.StorageService.AccessMode;
import de.tudarmstadt.ukp.dkpro.lab.task.Discriminator;
import de.tudarmstadt.ukp.dkpro.lab.uima.task.impl.UimaTaskBase;

public class CandidateSelectionTask
    extends UimaTaskBase
{
    public static final String KEY_INPUT_BIN = "BIN";
    public static final String KEY_OUTPUT_BIN = "BIN";


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
        File inputRoot = aContext.getStorageLocation(KEY_INPUT_BIN, AccessMode.READONLY);
        return createReader(BinaryCasReader.class,
                BinaryCasReader.PARAM_SOURCE_LOCATION, inputRoot,
                BinaryCasReader.PARAM_PATTERNS, new String[] { INCLUDE_PREFIX + "**/*.bin" });
    }

    @Override
    public AnalysisEngineDescription getAnalysisEngineDescription(TaskContext aContext)
        throws ResourceInitializationException, IOException
    {

        AnalysisEngineDescription candidateAnnotator = createEngineDescription(
                CandidateAnnotator.class,
                CandidateAnnotator.PARAM_FEATURE_PATH, candidateFeaturePath,
                CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, shouldResolveOverlaps);

        AnalysisEngineDescription merger = createEngineDescription(
                KeyphraseMerger.class,
                KeyphraseMerger.PARAM_MAX_LENGTH, keyphraseMergerMaxTokens);

        AnalysisEngineDescription cooccurrenceGraph = createEngineDescription(
				CooccurrenceGraph.class,
				CooccurrenceGraph.PARAM_FEATURE_PATH, cooccurrenceGraphFeaturePath,
				CooccurrenceGraph.PARAM_WINDOW_SIZE, cooccurrenceGraphWindowSize);

        File outputRoot = aContext.getStorageLocation(KEY_OUTPUT_BIN, AccessMode.ADD_ONLY);
        AnalysisEngineDescription writer = createEngineDescription(
                BinaryCasWriter.class,
                BinaryCasWriter.PARAM_TARGET_LOCATION, outputRoot,
                BinaryCasWriter.PARAM_FORMAT, "4");

        return createEngine(
                candidateAnnotator,
                merger,
                cooccurrenceGraph,
                writer);
    }


}
