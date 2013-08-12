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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.phrasematch;

import java.util.List;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

/**
 * Declares methods to retrieve strings associated with a jcas.
 *
 * @author Mateusz Parzonka
 *
 */
public interface StringReader
{

	/**
	 * Retrieves a set of strings associated with a document.
	 *
	 * @param jcas
	 *          containing a annotation where the set of strings can be derived
	 * @return a set of strings
	 * @throws AnalysisEngineProcessException
	 */
	public abstract Set<String> getSetOfStrings(JCas jcas)
		throws AnalysisEngineProcessException;

	/**
	 * Retrieves a list of strings associated with a document.
	 *
	 * @param jcas
	 *          containing a annotation where the list of strings can be derived
	 * @return a list of strings
	 * @throws AnalysisEngineProcessException
	 */
	public abstract List<String> getListOfStrings(JCas jcas)
		throws AnalysisEngineProcessException;

}