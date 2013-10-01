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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking;

import org.apache.commons.lang.StringUtils;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * Ranks keyphrase annotations using the term frequency (tf) value of the keyphrase as the keyphrase's score.
 * In difference to the TfidfKeyphraseRanker it regards Candidates with more than one token as one entity and annotates candidate accordingly.
 *
 * It is not implemented as part of the TfidfKeyphraseRanker, as we do not need a separate data structure,
 *   if no idf is required.
 *
 * @author zesch
 *
 */
public class TfRanking extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

	    for (Keyphrase k : JCasUtil.select(jcas, Keyphrase.class)) {
            double tfScore = StringUtils.countMatches(jcas.getDocumentText(), k.getKeyphrase());
            k.setScore(tfScore);
        }

	}
}