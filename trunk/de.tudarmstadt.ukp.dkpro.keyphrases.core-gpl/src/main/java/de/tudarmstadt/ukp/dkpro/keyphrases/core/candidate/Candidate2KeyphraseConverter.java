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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate;

/**
 * This simple keyphrase annotator takes all keyphrase candidates and turns them into keyphrase annotations.
 * It is mainly used for retrieving the maximum recall that one of these methods can reach, and for the baselines.
 *
 * @author zesch
 *
 */
public class Candidate2KeyphraseConverter extends JCasAnnotator_ImplBase {
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

        for (KeyphraseCandidate kc : JCasUtil.select(jcas, KeyphraseCandidate.class)) {
            Keyphrase keyphrase = new Keyphrase(jcas);
            keyphrase.setKeyphrase(kc.getKeyphrase());
            keyphrase.setBegin(kc.getBegin());
            keyphrase.setEnd(kc.getEnd());

            keyphrase.addToIndexes();
        }
	}
}