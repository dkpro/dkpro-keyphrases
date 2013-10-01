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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate;

/**
 * This simple keyphrase annotator takes all keyphrase candidates and turns them into keyphrase annotations.
 * It is mainly used for retrieving the maximum recall that one of these methods can reach, and for the baselines.
 *
 * @author zesch
 *
 */
public class Candidate2KeyphraseConverter extends JCasAnnotator_ImplBase {
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

        for (KeyphraseCandidate kc : JCasUtil.select(jcas, KeyphraseCandidate.class)) {
            Keyphrase keyphrase = new Keyphrase(jcas);
            keyphrase.setKeyphrase(kc.getKeyphrase());
            keyphrase.setBegin(kc.getBegin());
            keyphrase.setEnd(kc.getEnd());

            keyphrase.addToIndexes();
        }
	}
}