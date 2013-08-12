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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.base;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

/**
 * The candidate set expects a empty JCas and augments the CAS with the
 * specified annotation type.
 *
 * @author Mateusz Parzonka
 *
 */
public interface CandidateSet
{

	public final static String LF = System.getProperty("line.separator");

	public enum PosType
	{
		ADJ, ADV, ART, CARD, CONJ, N, O, PP, PR, PUNC, V
	}

	/**
	 * Returns the feature path to the string representation of the used candidate
	 * type.
	 *
	 * @return the feature path
	 */
	public String getFeaturePath();

	AnalysisEngineDescription createPreprocessingComponents(String language)
		throws ResourceInitializationException;

	public Class<? extends Annotation> getType();

}
