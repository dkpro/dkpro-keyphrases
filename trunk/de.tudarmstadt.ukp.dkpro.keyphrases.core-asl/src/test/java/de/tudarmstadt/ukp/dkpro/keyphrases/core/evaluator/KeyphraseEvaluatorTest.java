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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator;

import static org.junit.Assert.fail;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.jcas.JCas;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.KeyphraseEvaluator.EvaluatorType;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class KeyphraseEvaluatorTest
{

	@Rule
	public TemporaryFolder workdir = new TemporaryFolder();

    private static final String resultFileName = "KeyphraseEvaluatorTest.txt";

    @Test
    public void testKeyphraseEvaluator() throws Exception {
        String documentText = "The added keyphrases should be found in this text. With the exception of one example.";

		File tmpFile = workdir.newFile(resultFileName);


        AnalysisEngine ae = AnalysisEngineFactory.createPrimitive(
                KeyphraseEvaluator.class,
                KeyphraseEvaluator.PARAM_N, 3,
                KeyphraseEvaluator.PARAM_RESULT_FILE, tmpFile.toString(),
                KeyphraseEvaluator.PARAM_EVAL_TYPE, EvaluatorType.Token.toString(),
                KeyphraseEvaluator.PARAM_REMOVE_GOLD_AFTER_MATCH, true);

        JCas jcas = ae.newJCas();

        jcas.setDocumentText(documentText);

        File testFile = new File("src/test/resources/keyphrase/evaluator/test.txt");
        DocumentMetaData meta = DocumentMetaData.create(jcas);
        meta.setDocumentUri(testFile.toURI().toString());

        Keyphrase k1 = new Keyphrase(jcas);
        k1.setKeyphrase("keyphrases");
        k1.setScore(0.7);
        k1.addToIndexes();

        Keyphrase k2 = new Keyphrase(jcas);
        k2.setKeyphrase("exception");
        k2.setScore(0.5);
        k2.addToIndexes();

        Keyphrase k3 = new Keyphrase(jcas);
        k3.setKeyphrase("not_a_valid_keyphrase");
        k3.setScore(0.3);
        k3.addToIndexes();

        ae.process(jcas);
        ae.collectionProcessComplete();

        if (!tmpFile.exists()) {
            fail("Result file not correctly written.");
        }
        System.out.println(FileUtils.readFileToString(tmpFile, "UTF-8"));
   }
}