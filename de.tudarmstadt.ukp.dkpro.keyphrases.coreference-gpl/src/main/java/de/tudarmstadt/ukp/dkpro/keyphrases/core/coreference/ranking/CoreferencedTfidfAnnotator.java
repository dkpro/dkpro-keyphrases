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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.coreference.ranking;

import java.util.Map.Entry;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceLink;
import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathException;
import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathFactory;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotator;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.util.FreqDist;

public class CoreferencedTfidfAnnotator extends TfidfAnnotator {

    @Override
    protected FreqDist<String> getTermFrequencies(JCas jcas)
            throws AnalysisEngineProcessException {
        
        // count all terms with the given annotation
        FreqDist<String> termFrequencies = new FreqDist<String>();
        
        try {
            for(Entry<AnnotationFS, String> entry : FeaturePathFactory.select(jcas.getCas(), featurePath)){
                int occurrences = 1;
                for(CoreferenceLink link : JCasUtil.selectCovering(jcas, CoreferenceLink.class, entry.getKey())){
                    occurrences += getRemainingChainLength(link);
                }
                termFrequencies.count(entry.getValue(), occurrences);
            }
        }
        catch (FeaturePathException e) {
            throw new AnalysisEngineProcessException(e);
        }
        return termFrequencies;
    }

    private int getRemainingChainLength(CoreferenceLink link) {
        int counter = 0;
        while((link = link.getNext()) != null){
            counter++;
        }
        return counter;
    }
}
