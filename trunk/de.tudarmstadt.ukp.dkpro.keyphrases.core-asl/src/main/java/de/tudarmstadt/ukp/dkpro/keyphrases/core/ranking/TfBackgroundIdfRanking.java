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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking;

import org.apache.commons.lang.StringUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.frequency.provider.FrequencyCountProvider;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * Ranks keyphrase annotations using the term frequency (tf) value of the keyphrase divided by a background frequency as the keyphrase's score.
 * In difference to the TfidfKeyphraseRanker it regards Candidates with more than one token as one entity and annotates candidate accordingly.
 *
 * It is not implemented as part of the TfidfKeyphraseRanker, as we do not need a separate data structure,
 *   if a background idf is required.
 *
 * @author erbs
 *
 */
public class TfBackgroundIdfRanking extends JCasAnnotator_ImplBase {

	public static final String FREQUENCY_COUNT_RESOURCE = "FrequencyProvider";
	@ExternalResource(key = FREQUENCY_COUNT_RESOURCE)
	private FrequencyCountProvider frequencyProvider;

	@Override
	public void initialize(final UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

	    for (Keyphrase k : JCasUtil.select(jcas, Keyphrase.class)) {
            double tfScore = StringUtils.countMatches(jcas.getDocumentText(), k.getKeyphrase());
            try {
                final Long frequency = frequencyProvider.getFrequency(k.getKeyphrase());
                if(frequency <= 0) {
                    k.setScore(0.0);
                } else {
                    k.setScore(tfScore/Math.log(1+frequency));
                }
			} catch (Exception e) {
				throw new AnalysisEngineProcessException(e);
			}
        }

	}
}