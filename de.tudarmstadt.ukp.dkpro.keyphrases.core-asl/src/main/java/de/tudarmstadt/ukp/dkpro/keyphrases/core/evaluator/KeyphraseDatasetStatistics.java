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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.util.EvaluatorUtils;
//import org.apache.commons.math.stat.correlation.PearsonsCorrelation;

/**
 * Outputs statists about a keyphrase dataset.
 * @author zesch
 *
 */
public class KeyphraseDatasetStatistics extends JCasAnnotator_ImplBase {

    // In some cases the capitalization of the gold standard keyphrases does not reflect capitalization in the text
    // As there are only rare cases where a keyphrase should be included in uppercase, but not in lowercase (e.g. apple vs. Apple) this seems acceptable.
    // The behavior can be overwritten by a parameter.
    public static final String PARAM_LOWERCASE   = "lowercase";
    @ConfigurationParameter(name=PARAM_LOWERCASE, mandatory=false, defaultValue="false")
    private boolean toLowercase;

    // The suffix of the gold keyphrase files. Default is "key".
    // Suffixes should always be given with the ".".
    public static final String PARAM_GOLD_SUFFIX = "GoldSuffix";
    @ConfigurationParameter(name=PARAM_GOLD_SUFFIX, mandatory=false, defaultValue=".key")
    private String goldSuffix;

    private static final String LF = System.getProperty("line.separator");

    private static int nrofDocuments;
    private static int nrofKeyphrases;
    private static int sumNrofTokens;
    private static int sumLengthOfKeyphrases;
    private static List<Integer> tokensPerKeyphrase;

    private static List<Integer> tokenSizeList;
    private static List<Integer> goldSizeList;

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);

        nrofDocuments = 0;
        nrofKeyphrases = 0;
        sumNrofTokens = 0;
        sumLengthOfKeyphrases = 0;
        tokensPerKeyphrase = new ArrayList<Integer>();

        tokenSizeList = new ArrayList<Integer>();
        goldSizeList  = new ArrayList<Integer>();
    }

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {

        nrofDocuments++;

        DocumentMetaData docMetaData = DocumentMetaData.get(jcas);
        String currentTitle = docMetaData.getDocumentTitle();
        getContext().getLogger().log(Level.INFO, "Document title: " + currentTitle);

        // get the gold keyphrases from the file system
        Set<String> goldKeyphrases = EvaluatorUtils.getGoldKeyphrases(
                docMetaData, goldSuffix, toLowercase
        );

        AnnotationIndex<Annotation> tokenIndex = jcas.getAnnotationIndex(Token.type);
        sumNrofTokens += tokenIndex.size();


        for (String goldKeyphrase : goldKeyphrases) {
            sumLengthOfKeyphrases += goldKeyphrase.length();
            tokensPerKeyphrase.add(goldKeyphrase.split(" ").length);

            nrofKeyphrases++;
        }

        tokenSizeList.add(tokenIndex.size());
        goldSizeList.add(goldKeyphrases.size());

    }


    @Override
    public void collectionProcessComplete() throws AnalysisEngineProcessException {
        double averageNrofTokens = (double) sumNrofTokens / nrofDocuments;
        double averageLengthOfKeyphrases = (double) sumLengthOfKeyphrases / nrofKeyphrases;
        double averageTokensPerKeyphrase = mean(tokensPerKeyphrase);
        double stdDevTokensPerKeyphrase = stdDev(tokensPerKeyphrase);
        double keyphrasesPerDocument = (double) nrofKeyphrases / nrofDocuments;

        double[] tokenSizeArray = listToArray(tokenSizeList);
        double[] goldSizeArray  = listToArray(goldSizeList);

        StringBuilder sb = new StringBuilder();
        sb.append(LF);
        sb.append("# Documents:               "); sb.append(nrofDocuments); sb.append(LF);
        sb.append("Tokens / Document:         "); sb.append(averageNrofTokens); sb.append(LF);
        sb.append("# Keyphrases:              "); sb.append(nrofKeyphrases); sb.append(LF);
        sb.append("Keyphrases / Document:     "); sb.append(keyphrasesPerDocument); sb.append(LF);
        sb.append("Characters / Keyphrase:    "); sb.append(averageLengthOfKeyphrases); sb.append(LF);
        sb.append("Tokens / Keyphrase:        "); sb.append(averageTokensPerKeyphrase); sb.append("(+/- "); sb.append(stdDevTokensPerKeyphrase); sb.append(")"); sb.append(LF);
        sb.append(LF);
        sb.append("Pearson Correlation between document size and the number of gold keyphrases:"); sb.append(LF);
//        sb.append(new PearsonsCorrelation().correlation(tokenSizeArray, goldSizeArray)); sb.append(LF);
        sb.append(LF);
        sb.append(getNrOfTokensPerKeyphraseHistogramm()); sb.append(LF);
        sb.append(LF);

        getContext().getLogger().log(Level.INFO, sb.toString());
    }

    private double[] listToArray(List<Integer> list) {
        double[] array = new double[list.size()];
        int i = 0;
        for (int value : list) {
            array[i] = value;
            i++;
        }
        return array;
    }

    private String getNrOfTokensPerKeyphraseHistogramm() {
        Map<Integer,Integer> tokensPerKeyphraseMap = new TreeMap<Integer,Integer>();
        for (Integer nrOfTokens : tokensPerKeyphrase) {
            if (tokensPerKeyphraseMap.containsKey(nrOfTokens)) {
                tokensPerKeyphraseMap.put(nrOfTokens, tokensPerKeyphraseMap.get(nrOfTokens) + 1);
            }
            else {
                tokensPerKeyphraseMap.put(nrOfTokens, 1);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (Integer nrOfTokens : tokensPerKeyphraseMap.keySet() ) {
            sb.append(nrOfTokens); sb.append(":"); sb.append(tokensPerKeyphraseMap.get(nrOfTokens)); sb.append(LF);
        }
        return sb.toString();
    }

    public double mean(List<Integer> values) {
        double mean = 0.0;
        if (values.size() > 0) {
            double sum = 0.0;
            for (Integer value : values) {
                sum += value;
            }

           mean = sum/values.size();
        }
        return mean;
    }

    public double stdDev(List<Integer> values) {
         double stdDev = 0.0;

        if (values.size() > 1) {
            double meanValue = mean(values);
            double sum = 0.0;

            for (Integer value : values) {
                double diff = value - meanValue;
                sum += diff*diff;
            }
            stdDev = Math.sqrt(sum/(values.size()));
        }
        return stdDev;
    }

    public class KeyphrasePatternCounter {

        private final Map<Integer,Map<String,Integer>> patternCounter;
        private final Map<Integer,Map<String,String>> examplePatterns;

        public KeyphrasePatternCounter() {
            patternCounter  = new HashMap<Integer,Map<String,Integer>>();
            examplePatterns = new HashMap<Integer,Map<String,String>>();
        }

        public void updateStructureCounter(int size, String pattern, String example) {
            if (patternCounter.containsKey(size)) {
                Map<String,Integer> counterMap = patternCounter.get(size);
                if (counterMap.containsKey(pattern)) {
                    counterMap.put(pattern, counterMap.get(pattern) + 1);
                }
                else {
                    counterMap.put(pattern, 1);

                }
                patternCounter.put(size, counterMap);
            }
            else {
                Map<String,Integer> counterMap = new TreeMap<String,Integer>();
                counterMap.put(pattern, 1);
                patternCounter.put(size, counterMap);
            }

            if (examplePatterns.containsKey(size)) {
                Map<String,String> exampleMap = examplePatterns.get(size);
                exampleMap.put(pattern, example);
                examplePatterns.put(size, exampleMap);

            }
            else {
                Map<String,String> exampleMap = new TreeMap<String,String>();
                exampleMap.put(pattern, example);
                examplePatterns.put(size, exampleMap);
            }
        }

        public Map<String,Integer> getPhraseCounterMap(int size) {
            if (patternCounter.containsKey(size)) {
                return patternCounter.get(size);
            }
            else {
                return Collections.emptyMap();
            }
        }

        public Set<Integer> getSizes() {
            return patternCounter.keySet();
        }

        public Set<String> getPatterns(int size) {
            return patternCounter.get(size).keySet();
        }

        public int getPhrasePatternCount(int size, String pattern) {
            return patternCounter.get(size).get(pattern);
        }

        public String getPhrasePatternExample(int size, String pattern) {
            return examplePatterns.get(size).get(pattern);
        }
    }

}