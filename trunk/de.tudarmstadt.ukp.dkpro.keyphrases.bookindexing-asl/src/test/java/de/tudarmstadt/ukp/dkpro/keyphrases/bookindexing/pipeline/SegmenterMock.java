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
