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

import java.util.Random;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * Takes all keyphrase candidates and turns them into keyphrase annotations.
 * Randomly sets the keyphrase weights, so that a ranked list of keyphrases will be random.
 * It serves as a baseline.
 *
 * @author zesch
 *
 */
public class RandomRanking extends JCasAnnotator_ImplBase {
	private Random random;

	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		random = new Random();
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

        for (Keyphrase k : JCasUtil.select(jcas, Keyphrase.class)) {
            k.setScore( random.nextDouble() );
        }
	}
}