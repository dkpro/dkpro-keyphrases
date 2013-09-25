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
package de.tudarmstadt.ukp.dkpro.keyphrases.lab;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.ExternalResource;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.KeyphraseEvaluator;
import de.tudarmstadt.ukp.dkpro.lab.engine.TaskContext;
import de.tudarmstadt.ukp.dkpro.lab.uima.task.TaskContextProvider;

public class KeyphraseEvaluatorLab
    extends KeyphraseEvaluator
{
    // This external resource is be automatically injected by Lab
    @ExternalResource(api = TaskContextProvider.class)
    private TaskContext taskContext;


    @Override
    public void collectionProcessComplete()
        throws AnalysisEngineProcessException
    {
        super.collectionProcessComplete();

        EvaluationResultStream stream = new EvaluationResultStream().
                setMacroPrecision(getMacroPrecision()).
                setMacroRecall(getMacroRecall()).
                setMicroPrecision(getMicroPrecision()).
                setMicroRecall(getMicroRecall()).
                setRPrecisionAll(getRPrecisionAll()).
                setMeanAveragePrecision(getMeanAveragePrecision()).
                setMaxMicroRecall(getMaxMicroRecall()).
                setMaxMacroRecall(getMaxMacroRecall());

        taskContext.getStorageService().storeBinary(
                taskContext.getId(), getClass().getSimpleName(), stream);
    }

}
