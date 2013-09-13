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
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking.TfBackgroundIdfRanking;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking.TfidfRanking;
import de.tudarmstadt.ukp.dkpro.keyphrases.ranking.PageRankRanking;
import de.tudarmstadt.ukp.dkpro.lab.engine.TaskContext;
import de.tudarmstadt.ukp.dkpro.lab.storage.StorageService.AccessMode;
import de.tudarmstadt.ukp.dkpro.lab.task.Discriminator;
import de.tudarmstadt.ukp.dkpro.lab.uima.task.impl.UimaTaskBase;

public class KeyphraseRankingTask
extends UimaTaskBase
{
	public static final String KEY_INPUT_XMI = "INPUT_XMI";
	public static final String KEY_OUTPUT_XMI = "OUTPUT_XMI";

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


	@Override
	public CollectionReaderDescription getCollectionReaderDescription(TaskContext aContext)
			throws ResourceInitializationException, IOException
			{
		File inputXmiRoot = aContext.getStorageLocation(KEY_INPUT_XMI, AccessMode.READONLY);
		return createReader(XmiReader.class, XmiReader.PARAM_SOURCE_LOCATION, inputXmiRoot,
				XmiReader.PARAM_PATTERNS, new String[] { INCLUDE_PREFIX + "**/*.xmi.gz" });
			}

	@Override
	public AnalysisEngineDescription getAnalysisEngineDescription(TaskContext aContext)
			throws ResourceInitializationException, IOException
			{

		AnalysisEngineDescription ranker;
		if(rankerClass.equals(TfidfRanking.class)){

			AnalysisEngineDescription tfidfAnnotator = createEngineDescription(
					TfidfAnnotator.class,
					TfidfAnnotator.PARAM_TFDF_PATH, dfModelFile,
					TfidfAnnotator.PARAM_FEATURE_PATH, tfidfFeaturePath,
					TfidfAnnotator.PARAM_TF_MODE, weightingModeTf.toString(),
					TfidfAnnotator.PARAM_IDF_MODE, weightingModeIdf.toString(),
					TfidfAnnotator.PARAM_LOWERCASE, shouldLowercaseCandidates);

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

		File xmiOutputPath = aContext.getStorageLocation(KEY_OUTPUT_XMI, AccessMode.ADD_ONLY);
		AnalysisEngineDescription xmiWriter = createEngineDescription(
				XmiWriter.class,
				XmiWriter.PARAM_TARGET_LOCATION, xmiOutputPath,
				XmiWriter.PARAM_COMPRESSION, "GZIP"
				);

		return createEngine(
				ranker,
				xmiWriter
				);
			}

}
