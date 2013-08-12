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

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Ignore;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.AbstractBookIndexingTest;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate;

/**
 * Unit test of {@link SegmentProcessingPipeline}.
 *
 * @author Mateusz Parzonka
 *
 */
public class SegmentProcessingPipelineTest
	extends AbstractBookIndexingTest

{

	public static final String text = "abcdef";

	@Test
	@Ignore
	public void mockSegmentProcessingPipeline()
		throws ResourceInitializationException, AnalysisEngineProcessException
	{

		AnalysisEngine ae = createSegmentProcessingAE();

		JCas jcas = ae.newJCas();

		setup(jcas);

		jcas.setDocumentText(text);
		DocumentMetaData.create(jcas);

		// since the segmentProcessingAE contains a demultiplier it produces a
		// new jcas which we retrieve
		jcas = ae.processAndOutputNewCASes(jcas).next();

		System.out.println("Keyphrases in demultiplied JCas:");
		for (Keyphrase keyphrase : JCasUtil.select(jcas, Keyphrase.class)) {
			System.out.println(keyphrase);
		}
	}

	private void setup(JCas jcas)
	{
		for (int i = 0; i < text.length(); i++) {
			KeyphraseCandidate kc = new KeyphraseCandidate(jcas);
			kc.setBegin(i);
			kc.setEnd(i + 1);
			kc.addToIndexes();
		}
	}

	/**
	 *
	 * @return
	 * @throws ResourceInitializationException
	 */
	private AnalysisEngine createSegmentProcessingAE()
		throws ResourceInitializationException
	{
		return AnalysisEngineFactory.createAggregate(SegmentProcessingPipeline
				.createSegmentProcessingAggregate(
						createPrimitiveDescription(SegmenterMock.class),
						createPrimitiveDescription(KeyphraseAnnotatorMock.class),
						createPrimitiveDescription(AggregatorMock.class)));
	}

}
