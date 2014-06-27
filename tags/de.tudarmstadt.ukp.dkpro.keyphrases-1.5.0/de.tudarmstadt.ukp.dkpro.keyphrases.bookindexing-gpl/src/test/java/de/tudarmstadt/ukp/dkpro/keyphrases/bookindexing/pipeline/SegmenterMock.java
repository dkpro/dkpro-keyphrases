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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.pipeline;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.Segment;

/**
 * Partitions the input text into {@link Segment}s of size 2.
 */
public class SegmenterMock
	extends JCasAnnotator_ImplBase
{

	@Override
	public void process(JCas jcas)
		throws AnalysisEngineProcessException
	{

		for (int i = 0; i < SegmentProcessingPipelineTest.text.length(); i = i + 2) {
			addSegment(i, jcas);
		}
	}

	private void addSegment(int i, JCas jcas)
	{
		Segment segment = new Segment(jcas);
		segment.setBegin(i);
		segment.setEnd(i + 2);
		segment.addToIndexes();
	}
}
