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

import java.io.File;
import java.io.IOException;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.KeyphraseEvaluator;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.KeyphraseEvaluator.EvaluatorType;
import de.tudarmstadt.ukp.dkpro.keyphrases.lab.KeyphraseEvaluatorLab;
import de.tudarmstadt.ukp.dkpro.lab.engine.TaskContext;
import de.tudarmstadt.ukp.dkpro.lab.storage.StorageService.AccessMode;
import de.tudarmstadt.ukp.dkpro.lab.task.Discriminator;
import de.tudarmstadt.ukp.dkpro.lab.uima.task.impl.UimaTaskBase;

public class EvaluationTask
extends UimaTaskBase
{
	public static final String KEY_INPUT_XMI = "INPUT_XMI";
	public static final String KEY_OUTPUT_XMI = "OUTPUT_XMI";

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
		return createEngine(createEngine(
				KeyphraseEvaluatorLab.class,
				KeyphraseEvaluatorLab.PARAM_MATCHING_TYPE, evaluationMatchingType.toString(),
				KeyphraseEvaluatorLab.PARAM_N, evaluationN,
				KeyphraseEvaluatorLab.PARAM_EVAL_TYPE, evaluatorType.toString(),
				KeyphraseEvaluatorLab.PARAM_REMOVE_GOLD_AFTER_MATCH, evaluationRemoveGoldAfterMatch,
				KeyphraseEvaluatorLab.PARAM_GOLD_SUFFIX, goldSuffix,
				KeyphraseEvaluatorLab.PARAM_REMOVE_KEYPHRASES_NOT_IN_TEXT, removeKeyphrasesNotInText));
}

}
