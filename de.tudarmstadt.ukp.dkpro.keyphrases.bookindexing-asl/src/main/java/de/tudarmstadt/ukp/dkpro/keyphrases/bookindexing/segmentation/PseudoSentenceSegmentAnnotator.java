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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.segmentation;

import static org.apache.uima.fit.util.JCasUtil.select;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.Segment;

/**
 * Creates a segment each n tokens. These can be considered as pseudo-sentences,
 * as coined by Hulce in M.A. Hearst in her TextTiling-papers.
 *
 * @author Mateusz Parzonka
 */

public class PseudoSentenceSegmentAnnotator
	extends JCasAnnotator_ImplBase
{

	public static final String PARAM_NUMBER_OF_TOKENS_PER_SEGMENT = "NrOfTokensPerSegment";
	@ConfigurationParameter(name = PARAM_NUMBER_OF_TOKENS_PER_SEGMENT, mandatory = true, defaultValue = "50")
	private int n;

	@Override
	public void initialize(UimaContext context)
		throws ResourceInitializationException
	{
		super.initialize(context);
	}

	@Override
	public void process(JCas jCas)
		throws AnalysisEngineProcessException
	{

		boolean allSegmentsFinished = true;
		int begin = 0;
		int end = 0;
		int nrOfTokensInSegment = 0;
		for (Token token : select(jCas, Token.class)) {

			if (allSegmentsFinished) {
				begin = token.getBegin();
				allSegmentsFinished = false;
			}

			nrOfTokensInSegment++;

			// saved here for use outside of loop
			end = token.getEnd();

			if (nrOfTokensInSegment == n) {

				// finish segment
				Segment segment = new Segment(jCas);
				segment.setBegin(begin);
				segment.setEnd(end);
				segment.addToIndexes();
				nrOfTokensInSegment = 0;
				allSegmentsFinished = true;
			}

		}

		// last segment might not be finished yet
		if (!allSegmentsFinished) {
			Segment segment = new Segment(jCas);
			segment.setBegin(begin);
			segment.setEnd(end);
			segment.addToIndexes();
		}

	}

	protected void setN(int n)
	{
		this.n = n;
	}

}