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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.reader;

import static org.junit.Assert.assertEquals;

import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

public class KeyphraseReaderTest
{
    @Test
    public void keyphraseReaderTest() throws Exception {


        String expectedDocument = "Does classicism explain universality? " +
        		"Arguments against a pure classical component of mind One of the hallmarks of human " +
        		"cognition is the capacity to generalize over arbitrary constituents. " +
        		"Marcus (Cognition 66, p.153; Cognitive Psychology 37, p. 243, 1998) argued that this " +
        		"capacity, called \"universal generalization\" (universality), " +
        		"is not supported by connectionist models. " +
        		"Instead, universality is best explained by classical symbol systems, " +
        		"with connectionism as its implementation. Here it is argued that universality is also " +
        		"a problem for classicism in that the syntax-sensitive rules that are supposed to " +
        		"provide causal explanations of mental processes are either too strict, precluding " +
        		"possible generalizations; or too lax, providing no information as to the appropriate " +
        		"alternative. Consequently, universality is not explained by a classical theory ";


        CollectionReaderDescription reader = CollectionReaderFactory.createReaderDescription(
                KeyphraseReader.class,
                KeyphraseReader.PARAM_INPUTDIR, "src/test/resources/keyphrase/reader/",
                KeyphraseReader.PARAM_DATA_SUFFIX, "abstr");

        int i = 0;
        for (JCas jcas : new JCasIterable(reader)) {
            if (i==0) {
                String document = jcas.getDocumentText();
                assertEquals(expectedDocument, document);
            }

            i++;
        }

        // there are 8 documents in the test set
        assertEquals(8,i);

    }

}