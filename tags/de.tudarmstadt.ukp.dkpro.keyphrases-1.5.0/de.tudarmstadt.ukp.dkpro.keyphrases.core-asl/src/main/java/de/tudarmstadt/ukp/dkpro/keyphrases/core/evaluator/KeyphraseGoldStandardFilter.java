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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
//import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

/**
 * @author erbs
 *
 */
public class KeyphraseGoldStandardFilter extends JCasAnnotator_ImplBase {

    // The suffix of the gold keyphrase files. Default is "key".
    // Suffixes should always be given with the ".".
    public static final String PARAM_GOLD_SUFFIX = "GoldSuffix";
    @ConfigurationParameter(name=PARAM_GOLD_SUFFIX, mandatory=false, defaultValue=".key")
    private String goldSuffix;

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        
        DocumentMetaData md = DocumentMetaData.get(jcas);
        
        String goldFile = md.getDocumentUri().substring(0, md.getDocumentUri().lastIndexOf(".")).replace("file:", "") + goldSuffix;
        System.out.println("Loading gold standard from " + goldFile);
        
        try {
            List<String> keyphrases = FileUtils.readLines(new File(goldFile));
            List<String> filteredKeyphrases = new ArrayList<String>();
            
            List<String> lemmas = new ArrayList<String>();
            for(Lemma lemma : JCasUtil.select(jcas, Lemma.class)){
                lemmas.add(lemma.getValue().toLowerCase());
            }
            List<String> tokens = new ArrayList<String>();
            for(Token token : JCasUtil.select(jcas, Token.class)){
                tokens.add(token.getCoveredText().toLowerCase());
            }
                        
            for(String keyphrase : keyphrases){
                if(tokens.contains(keyphrase.toLowerCase()) || lemmas.contains(keyphrase.toLowerCase())){
                    filteredKeyphrases.add(keyphrase);
                }
            }
            
            FileUtils.writeLines(new File(goldFile + ".filtered"), filteredKeyphrases);
            
        }
        catch (IOException e) {
            throw new AnalysisEngineProcessException(e);
        }

    }
}