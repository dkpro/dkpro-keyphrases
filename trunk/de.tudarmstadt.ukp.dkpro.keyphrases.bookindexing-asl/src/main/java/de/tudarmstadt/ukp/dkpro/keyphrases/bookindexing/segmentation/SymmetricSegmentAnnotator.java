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

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

/**
 * Devides the cas into n segments of same length.
 *
 * @author Mateusz Parzonka
 *
 */
public class SymmetricSegmentAnnotator
	extends PseudoSentenceSegmentAnnotator
{

	public static final String PARAM_SEGMENTATION_FACTOR = "SegmentationFactor";
	@ConfigurationParameter(name = PARAM_SEGMENTATION_FACTOR, mandatory = false, defaultValue = "1")
	private int segmentationFactor;

	@Override
	public void process(JCas jCas)
		throws AnalysisEngineProcessException
	{

		getContext().getLogger().log(Level.CONFIG,
				"Entering " + getClass().getSimpleName());

		int tokenCount = JCasUtil.select(jCas, Token.class).size();

		setN((tokenCount / segmentationFactor) + 1);
		super.process(jCas);
	}

}
