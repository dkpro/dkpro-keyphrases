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
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.frequency.resources.Web1TFrequencyCountResource;
import de.tudarmstadt.ukp.dkpro.core.io.bincas.BinaryCasReader;
import de.tudarmstadt.ukp.dkpro.core.io.bincas.BinaryCasWriter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate.CandidateAnnotator;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.CorpusFilter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.PosSequenceFilter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.PositionFilter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.factory.StopwordFilterFactory;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.frequency.ExternalFrequencyFilter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.frequency.FrequencyFilter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.length.CharacterLengthFilter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.length.TokenLengthFilter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.postprocessing.KeyphraseMerger;
import de.tudarmstadt.ukp.dkpro.lab.engine.TaskContext;
import de.tudarmstadt.ukp.dkpro.lab.storage.StorageService.AccessMode;
import de.tudarmstadt.ukp.dkpro.lab.task.Discriminator;
import de.tudarmstadt.ukp.dkpro.lab.uima.task.impl.UimaTaskBase;

public class KeyphraseFilteringTask
extends UimaTaskBase
{
    public static final String KEY_INPUT_BIN = "BIN";
    public static final String KEY_OUTPUT_BIN = "BIN";


    @Discriminator
    private String keyphraseFeaturePath;
    @Discriminator
    private boolean resolveOverlaps;
    @Discriminator
    private String nGramFolder;


    //Keyphrase merger
    @Discriminator
    private boolean runMergerBeforeFilter;
    @Discriminator
    private boolean runMergerAfterFilter;
    @Discriminator
    private int keyphraseMergerMaxTokens;
    @Discriminator
    private boolean keyphraseMergerKeepParts;
    
    //Filter
    //Length filter
    @Discriminator
    private boolean runCharacterLengthFilter;
    @Discriminator
    private int minCharacterLength;
    @Discriminator
    private int maxCharacterLength;
    @Discriminator
    private boolean runTokenLengthFilter;
    @Discriminator
    private int minTokenLength;
    @Discriminator
    private int maxTokenLength;

    //Frequency filter
    @Discriminator
    private boolean runFrequencyFilter;
    @Discriminator
    private int minFrequency;
    @Discriminator
    private int maxFrequency;
    @Discriminator
    private boolean runExternalFrequencyFilter;
    @Discriminator
    private long minExternalFrequency;
    @Discriminator
    private long maxExternalFrequency;

    //Corpus filter
    @Discriminator
    private boolean runCorpusFilter;
    @Discriminator
    private String corpusFilterPath;
    @Discriminator
    private String corpusFilterFileExtension;

    //Position filter
    @Discriminator
    private boolean runPositionFilter;
    @Discriminator
    private int beginIndexPositionFilter;
    @Discriminator
    private int endIndexPositionFilter;

    //PosSequence filter
    @Discriminator
    private boolean runPosSequenceFilter;
    @Discriminator
    private Set<String> posFilterPatterns;
    
    //Stopword filter
    @Discriminator
    private boolean runStopwordFilter;
    @Discriminator
    private Set<String> stopwordlists;
    

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

        ExternalResourceDescription frequencyProvider = createExternalResourceDescription(
                Web1TFrequencyCountResource.class,
                Web1TFrequencyCountResource.PARAM_MIN_NGRAM_LEVEL, "1",
                Web1TFrequencyCountResource.PARAM_MAX_NGRAM_LEVEL, "3",
                Web1TFrequencyCountResource.PARAM_INDEX_PATH, nGramFolder);

        List<AnalysisEngineDescription> engines = new LinkedList<AnalysisEngineDescription>();

        engines.add(createEngineDescription(
                CandidateAnnotator.class,
                CandidateAnnotator.PARAM_FEATURE_PATH, keyphraseFeaturePath,
                CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, resolveOverlaps));

        if(runMergerBeforeFilter){
            engines.add(createEngineDescription(
                    KeyphraseMerger.class,
                    KeyphraseMerger.PARAM_MAX_LENGTH, keyphraseMergerMaxTokens,
                    KeyphraseMerger.PARAM_KEEP_PARTS, keyphraseMergerKeepParts));
        }

        //Add filter here

        if(runMergerAfterFilter){
            engines.add(createEngineDescription(
                    KeyphraseMerger.class,
                    KeyphraseMerger.PARAM_MAX_LENGTH, keyphraseMergerMaxTokens));
        }

        if(runCharacterLengthFilter){
            engines.add(createEngineDescription(
                    CharacterLengthFilter.class,
                    CharacterLengthFilter.MIN_KEYPHRASE_LENGTH, minCharacterLength,
                    CharacterLengthFilter.MAX_KEYPHRASE_LENGTH, maxCharacterLength));
        }

        if(runTokenLengthFilter){
            engines.add(createEngineDescription(
                    TokenLengthFilter.class,
                    TokenLengthFilter.MIN_KEYPHRASE_LENGTH, minTokenLength,
                    TokenLengthFilter.MAX_KEYPHRASE_LENGTH, maxTokenLength));
        }

        if(runFrequencyFilter){
            engines.add(createEngineDescription(
                    FrequencyFilter.class,
                    FrequencyFilter.MIN_FREQUENCY, minFrequency,
                    FrequencyFilter.MAX_FREQUENCY, maxFrequency));
        }

        if(runExternalFrequencyFilter){
            engines.add(createEngineDescription(
                    ExternalFrequencyFilter.class,
                    ExternalFrequencyFilter.MIN_FREQUENCY, minExternalFrequency,
                    ExternalFrequencyFilter.MAX_FREQUENCY, maxExternalFrequency,
                    ExternalFrequencyFilter.FREQUENCY_COUNT_RESOURCE, frequencyProvider));
        }

        if(runCorpusFilter){
            engines.add(createEngineDescription(
                    CorpusFilter.class,
                    CorpusFilter.CORPUS_FOLDER, corpusFilterPath,
                    CorpusFilter.FILE_EXTENSION, corpusFilterFileExtension));
        }

        if(runPositionFilter){
            engines.add(createEngineDescription(
                    PositionFilter.class,
                    PositionFilter.BEGIN_INDEX, beginIndexPositionFilter,
                    PositionFilter.END_INDEX, endIndexPositionFilter));
        }

        if(runPosSequenceFilter){
            engines.add(createEngineDescription(
                    PosSequenceFilter.class,
                    PosSequenceFilter.PARAM_POS_PATTERNS, posFilterPatterns));
        }

        if(runStopwordFilter){
            engines.add(StopwordFilterFactory.getStopwordFilter(stopwordlists));
        }


        File outputRoot = aContext.getStorageLocation(KEY_OUTPUT_BIN, AccessMode.ADD_ONLY);        
        engines.add(createEngineDescription(
                BinaryCasWriter.class,
                BinaryCasWriter.PARAM_TARGET_LOCATION, outputRoot,
                BinaryCasWriter.PARAM_FORMAT, "4"));

        return createEngine(engines);
            }


}
