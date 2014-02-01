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
import static org.apache.uima.fit.factory.ExternalResourceFactory.bindResource;

import java.io.File;
import java.io.IOException;

import org.apache.uima.analysis_component.AnalysisComponent;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;

import de.tudarmstadt.ukp.dkpro.core.frequency.resources.Web1TFrequencyCountResource;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotator;
import de.tudarmstadt.ukp.dkpro.core.io.bincas.BinaryCasReader;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.coreference.ranking.CoreferencedTfidfAnnotator;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.KeyphraseEvaluator;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.KeyphraseEvaluator.EvaluatorType;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking.TfBackgroundIdfRanking;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking.TfidfRanking;
import de.tudarmstadt.ukp.dkpro.keyphrases.lab.KeyphraseEvaluatorLab;
import de.tudarmstadt.ukp.dkpro.keyphrases.ranking.PageRankRanking;
import de.tudarmstadt.ukp.dkpro.lab.engine.TaskContext;
import de.tudarmstadt.ukp.dkpro.lab.storage.StorageService.AccessMode;
import de.tudarmstadt.ukp.dkpro.lab.task.Discriminator;
import de.tudarmstadt.ukp.dkpro.lab.uima.task.impl.UimaTaskBase;

public class KeyphraseRankingTask
extends UimaTaskBase
{
	public static final String KEY_INPUT_BIN = "INPUT_BIN";

	@Discriminator
	private Class<? extends AnalysisComponent> rankerClass;
	@Discriminator
	private TfidfRanking.TfidfAggregate tfidfAggregate;
	@Discriminator
	private boolean shouldLowercaseExtractedKeyphrases;
	@Discriminator
	private boolean shouldRemoveContainedKeyphrasesInExtractedKeyphrases;
	@Discriminator
	private String tfidfFeaturePath;
	@Discriminator
	private TfidfAnnotator.WeightingModeTf weightingModeTf;
	@Discriminator
	private TfidfAnnotator.WeightingModeIdf weightingModeIdf;
	@Discriminator
	private boolean shouldLowercaseCandidates;
	@Discriminator
	private String dfModelFile;
	@Discriminator
	private String nGramFolder;
	@Discriminator
    private boolean useCoreferenceCounts;
    @Discriminator
    private KeyphraseEvaluator.MatchingType evaluationMatchingType;
    @Discriminator
    private int evaluationN;
    @Discriminator
    private EvaluatorType evaluatorType;
    @Discriminator
    private boolean evaluationRemoveGoldAfterMatch;
    @Discriminator
    private boolean removeKeyphrasesNotInText;
    @Discriminator
    private String goldSuffix;
    @Discriminator
    private boolean evaluatorLowercase;


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

		AnalysisEngineDescription ranker;
		if(rankerClass.equals(TfidfRanking.class)){
		    
		    AnalysisEngineDescription tfidfAnnotator;
		    if(useCoreferenceCounts){
                tfidfAnnotator = createEngineDescription(
                        CoreferencedTfidfAnnotator.class,
                        TfidfAnnotator.PARAM_TFDF_PATH, dfModelFile,
                        TfidfAnnotator.PARAM_FEATURE_PATH, tfidfFeaturePath,
                        TfidfAnnotator.PARAM_TF_MODE, weightingModeTf.toString(),
                        TfidfAnnotator.PARAM_IDF_MODE, weightingModeIdf.toString(),
                        TfidfAnnotator.PARAM_LOWERCASE, shouldLowercaseCandidates);
		    }
		    else{
		        tfidfAnnotator = createEngineDescription(
	                    TfidfAnnotator.class,
	                    TfidfAnnotator.PARAM_TFDF_PATH, dfModelFile,
	                    TfidfAnnotator.PARAM_FEATURE_PATH, tfidfFeaturePath,
	                    TfidfAnnotator.PARAM_TF_MODE, weightingModeTf.toString(),
	                    TfidfAnnotator.PARAM_IDF_MODE, weightingModeIdf.toString(),
	                    TfidfAnnotator.PARAM_LOWERCASE, shouldLowercaseCandidates);
		    }

			AnalysisEngineDescription tfidfRanker = createEngineDescription(
					TfidfRanking.class,
					TfidfRanking.PARAM_AGGREGATE, tfidfAggregate.toString()
					);

			ranker = createEngineDescription(tfidfAnnotator, tfidfRanker);
		}
		else if(rankerClass.equals(PageRankRanking.class)){
			ranker = createEngineDescription(
					PageRankRanking.class,
					PageRankRanking.PARAM_WEIGHTED, true);
		}
		else if(rankerClass.equals(TfBackgroundIdfRanking.class)){
			ranker = createEngineDescription(
					rankerClass);
			try {
				bindResource(
						ranker,
						TfBackgroundIdfRanking.FREQUENCY_COUNT_RESOURCE,
						Web1TFrequencyCountResource.class,
						Web1TFrequencyCountResource.PARAM_MIN_NGRAM_LEVEL, "1",
						Web1TFrequencyCountResource.PARAM_MAX_NGRAM_LEVEL, "3",
						Web1TFrequencyCountResource.PARAM_INDEX_PATH, nGramFolder
						);
			} catch (InvalidXMLException e) {
				throw new IOException(e);
			}
		}


		else{
			ranker = createEngineDescription(
					rankerClass);
		}
		
		AnalysisEngineDescription evaluator = createEngineDescription(
                KeyphraseEvaluatorLab.class,
                KeyphraseEvaluatorLab.PARAM_MATCHING_TYPE, evaluationMatchingType.toString(),
                KeyphraseEvaluatorLab.PARAM_N, evaluationN,
                KeyphraseEvaluatorLab.PARAM_EVAL_TYPE, evaluatorType.toString(),
                KeyphraseEvaluatorLab.PARAM_REMOVE_GOLD_AFTER_MATCH, evaluationRemoveGoldAfterMatch,
                KeyphraseEvaluatorLab.PARAM_GOLD_SUFFIX, goldSuffix,
                KeyphraseEvaluatorLab.PARAM_REMOVE_KEYPHRASES_NOT_IN_TEXT, removeKeyphrasesNotInText,
                KeyphraseEvaluatorLab.PARAM_LOWERCASE, evaluatorLowercase);

		return createEngine(
				ranker,
				evaluator
				);
			}

}
