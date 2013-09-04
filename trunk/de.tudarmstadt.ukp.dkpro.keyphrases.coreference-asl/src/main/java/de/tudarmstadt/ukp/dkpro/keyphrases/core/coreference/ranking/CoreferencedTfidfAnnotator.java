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

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceChain;
import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceLink;
import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathFactory;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Compound.CompoundSplitLevel;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotator;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.util.FreqDist;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.util.TermIterator;

public class CoreferencedTfidfAnnotator extends TfidfAnnotator {

    public static final String PARAM_COMPOUND_SPLIT_LEVEL = "CompoundingSplitLevel";
    @ConfigurationParameter(name = PARAM_COMPOUND_SPLIT_LEVEL, mandatory = true, defaultValue={"ALL"})
    private CompoundSplitLevel compoundSplitLevel;



    @Override
    protected FreqDist<String> getTermFrequencies(JCas jcas)
            throws AnalysisEngineProcessException {
        // count all terms with the given annotation
        FreqDist<String> termFrequencies = super.getTermFrequencies(jcas);;


        for(CoreferenceChain chain : JCasUtil.select(jcas, CoreferenceChain.class)){
            //TODO get lemma or token or whatever is needed


            for (String term : TermIterator.create(jcas, featurePath, lowercase)) {
                termFrequencies.count(chain.getFirst().getCoveredText(), getChainLength(chain));
                termFrequencies.count(chain.getFirst().getCoveredText(), getChainLength(chain));

            }


        }
        return termFrequencies;
    }

    int getChainLength(CoreferenceChain chain) {
        int counter = 1;
        CoreferenceLink link =chain.getFirst();
        while((link = link.getNext()) != null){
            counter++;
        }
        return counter;
    }
}
