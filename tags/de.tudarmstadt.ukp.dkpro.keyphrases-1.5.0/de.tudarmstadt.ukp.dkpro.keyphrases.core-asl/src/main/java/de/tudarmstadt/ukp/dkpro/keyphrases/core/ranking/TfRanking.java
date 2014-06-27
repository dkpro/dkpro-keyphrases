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

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.util.FreqDist;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * Ranks keyphrase annotations using the term frequency (tf) value of the keyphrase as the keyphrase's score.
 * In difference to the TfidfKeyphraseRanker it regards Candidates with more than one token as one entity and annotates candidate accordingly.
 *
 * It is not implemented as part of the TfidfKeyphraseRanker, as we do not need a separate data structure,
 *   if no idf is required.
 *
 * @author zesch
 *
 */
public class TfRanking extends JCasAnnotator_ImplBase{
    
    protected FreqDist<String> termFrequencies;

    /**
     * This annotator is type agnostic, so it is mandatory to specify the type of the working
     * annotation and how to obtain the string representation with the feature path.
     */
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
	    
        termFrequencies = new FreqDist<String>();
        for(Keyphrase keyphrase : JCasUtil.select(jcas, Keyphrase.class)){
            termFrequencies.count(keyphrase.getKeyphrase());
        }
        
 	    for (Keyphrase keyphrase : JCasUtil.select(jcas, Keyphrase.class)) {
	        keyphrase.setScore(getScore(keyphrase));
        }
	}
	
	protected double getScore(Keyphrase keyphrase) throws AnalysisEngineProcessException{
	    return termFrequencies.getCount(keyphrase.getKeyphrase());
	}
}