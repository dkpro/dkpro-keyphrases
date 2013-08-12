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
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.Segment;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate;

/**
 * For all {@link KeyphraseCandidate}s in the covering {@link Segment} a
 * {@link Keyphrase} is created.
 *
 * @author Mateusz Parzonka
 *
 */
public class KeyphraseAnnotatorMock
	extends JCasAnnotator_ImplBase
{

	private int subCasNr = 0;

	@Override
	public void process(JCas jcas)
		throws AnalysisEngineProcessException
	{

		for (KeyphraseCandidate kc : JCasUtil
				.select(jcas, KeyphraseCandidate.class)) {
			Keyphrase keyphrase = new Keyphrase(jcas);
			keyphrase.setBegin(kc.getBegin());
			keyphrase.setEnd(kc.getEnd());
			keyphrase.addToIndexes();
			keyphrase.setKeyphrase("I was contained in subCasNr" + subCasNr);
		}
		subCasNr++;
	}

}
