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

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * Ranks keyphrase annotations according to their position in the document. The more at the beginning, the better.
 * Normalizes the distance from the end with the total length
 *
 * @author zesch, erbs
 *
 */
public class PositionRanking extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		int totalLength = jcas.getDocumentText().length();

	    for (Keyphrase keyphrase : JCasUtil.select(jcas, Keyphrase.class)) {
	    	keyphrase.setScore((totalLength - keyphrase.getBegin())/(double)totalLength);
        }
	}
}