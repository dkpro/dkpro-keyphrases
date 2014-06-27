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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.segmentation;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.Segment;

/**
 * This segmenter annotates the whole document as a single segment.
 *
 * @author Mateusz Parzonka
 */

public class OneSegmentAnnotator extends JCasAnnotator_ImplBase {

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
	super.initialize(context);

    }

    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {

	int end = jCas.getDocumentText().length();

	getContext().getLogger().log(
		Level.CONFIG,
		"Entering " + getClass().getSimpleName() + ". Adding segment of length " + end
			+ ".");

	Segment segment = new Segment(jCas);
	segment.setBegin(0);
	segment.setEnd(end);
	segment.addToIndexes();
    }

}
