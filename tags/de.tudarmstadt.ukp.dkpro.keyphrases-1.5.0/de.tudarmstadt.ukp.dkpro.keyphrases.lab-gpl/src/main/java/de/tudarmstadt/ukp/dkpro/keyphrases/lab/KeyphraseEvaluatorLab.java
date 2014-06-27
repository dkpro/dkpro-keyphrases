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
                setMacroPrecisionAt5(getMacroPrecision(5)).
                setMacroPrecisionAt10(getMacroPrecision(10)).
                setMacroPrecisionAt15(getMacroPrecision(15)).
                setMacroRecall(getMacroRecall()).
                setMacroRecallAt5(getMacroRecall(5)).
                setMacroRecallAt10(getMacroRecall(10)).
                setMacroRecallAt15(getMacroRecall(15)).
                setMicroPrecision(getMicroPrecision()).
                setMicroPrecisionAt5(getMicroPrecision(5)).
                setMicroPrecisionAt10(getMicroPrecision(10)).
                setMicroPrecisionAt15(getMicroPrecision(15)).
                setMicroRecall(getMicroRecall()).
                setMicroRecallAt5(getMicroRecall(5)).
                setMicroRecallAt10(getMicroRecall(10)).
                setMicroRecallAt15(getMicroRecall(15)).
                setRPrecisionAll(getRPrecisionAll()).
                setMeanAveragePrecision(getMeanAveragePrecision()).
                setMaxMicroRecall(getMaxMicroRecall()).
                setMaxMacroRecall(getMaxMacroRecall());

        taskContext.getStorageService().storeBinary(
                taskContext.getId(), getClass().getSimpleName(), stream);
    }

}
