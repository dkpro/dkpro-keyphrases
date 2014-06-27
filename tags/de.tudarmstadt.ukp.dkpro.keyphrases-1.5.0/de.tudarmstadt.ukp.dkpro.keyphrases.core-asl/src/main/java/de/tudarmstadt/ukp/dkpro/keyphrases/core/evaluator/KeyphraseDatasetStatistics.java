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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
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

    // In some cases the capitalization of the gold standard keyphrases
    // does not reflect capitalization in the text
    // As there are only rare cases where a keyphrase should be
    // included in uppercase, but not in lowercase (e.g. apple vs. Apple)
    // this seems acceptable. The behavior can be overwritten by a parameter.
    public static final String PARAM_LOWERCASE   = "lowercase";
    @ConfigurationParameter(name=PARAM_LOWERCASE, mandatory=false, defaultValue="false")
    private boolean toLowercase;

    // The suffix of the gold keyphrase files. Default is "key".
    // Suffixes should always be given with the ".".
    public static final String PARAM_GOLD_SUFFIX = "GoldSuffix";
    @ConfigurationParameter(name=PARAM_GOLD_SUFFIX, mandatory=false, defaultValue=".key")
    private String goldSuffix;

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String MORE_LESS_LITERAL = "(+/- ";
    private static final int MIN_STD_DEV_SIZE = 1;

    private static int nrofDocuments;
    private static int nrofKeyphrases;
    private static int sumNrofTokens;
    private static int sumLgthKeyphrs;
    private static List<Integer> tknsPerKeyphr;
    private static List<Integer> charsPerKeyphr;
    private static List<Integer> tokensPerDocument;
    private static List<Integer> keyphrsPerDoc;

    private static List<Integer> tokenSizeList;
    private static List<Integer> goldSizeList;

    @Override
    public void initialize(final UimaContext context) throws ResourceInitializationException {
        super.initialize(context);

        nrofDocuments = 0;
        nrofKeyphrases = 0;
        sumNrofTokens = 0;
        sumLgthKeyphrs = 0;
        tknsPerKeyphr = new ArrayList<Integer>();
        charsPerKeyphr = new ArrayList<Integer>();
        tokensPerDocument = new ArrayList<Integer>();
        keyphrsPerDoc = new ArrayList<Integer>();

        tokenSizeList = new ArrayList<Integer>();
        goldSizeList  = new ArrayList<Integer>();
    }

    @Override
    public void process(final JCas jcas) throws AnalysisEngineProcessException {

        nrofDocuments++;

        final DocumentMetaData docMetaData = DocumentMetaData.get(jcas);
        final String currentTitle = docMetaData.getDocumentTitle();
        getContext().getLogger().log(Level.INFO, "Document title: " + currentTitle);

        // get the gold keyphrases from the file system
        final Set<String> goldKeyphrases = EvaluatorUtils.getGoldKeyphrases(
                docMetaData, goldSuffix, toLowercase
        );

        final AnnotationIndex<Annotation> tokenIndex = jcas.getAnnotationIndex(Token.type);
        sumNrofTokens += tokenIndex.size();
        tokensPerDocument.add(tokenIndex.size());


        for (final String goldKeyphrase : goldKeyphrases) {
            sumLgthKeyphrs += goldKeyphrase.length();
            tknsPerKeyphr.add(goldKeyphrase.split("\\s+").length);
            charsPerKeyphr.add(goldKeyphrase.length());

            nrofKeyphrases++;
        }

        keyphrsPerDoc.add(goldKeyphrases.size());

        tokenSizeList.add(tokenIndex.size());
        goldSizeList.add(goldKeyphrases.size());

    }


    @Override
    public void collectionProcessComplete() throws AnalysisEngineProcessException {
        final double averageNrofTokens = (double) sumNrofTokens / nrofDocuments;
        final double stdDevAvgNrTokens = stdDev(tokensPerDocument);
        final double avgLngthKeyphrs = (double) sumLgthKeyphrs / nrofKeyphrases;
        final double stdDevAvgLnthKphr = stdDev(charsPerKeyphr);
        final double avgToksKeyphrs = mean(tknsPerKeyphr);
        final double stdDevToksKeyphr = stdDev(tknsPerKeyphr);
        final double avgKeyphrPerDoc = (double) nrofKeyphrases / nrofDocuments;
        final double stdDevKeyphrDoc = stdDev(keyphrsPerDoc);


//        double[] tokenSizeArray = listToArray(tokenSizeList);
//        double[] goldSizeArray  = listToArray(goldSizeList);

        final StringBuilder stringBuilder = new StringBuilder(272);
        stringBuilder.append(LINE_SEPARATOR);
        stringBuilder.append("# Documents:               "); stringBuilder.append(nrofDocuments); stringBuilder.append(LINE_SEPARATOR);
        stringBuilder.append("Tokens / Document:         "); stringBuilder.append(averageNrofTokens); stringBuilder.append(MORE_LESS_LITERAL); stringBuilder.append(stdDevAvgNrTokens); stringBuilder.append(") Median: ");  stringBuilder.append(median(tokensPerDocument)); stringBuilder.append(')'); stringBuilder.append(LINE_SEPARATOR);
        stringBuilder.append("# Keyphrases:              "); stringBuilder.append(nrofKeyphrases); stringBuilder.append(LINE_SEPARATOR);
        stringBuilder.append("Keyphrases / Document:     "); stringBuilder.append(avgKeyphrPerDoc); stringBuilder.append(MORE_LESS_LITERAL); stringBuilder.append(stdDevKeyphrDoc); stringBuilder.append(')'); stringBuilder.append(LINE_SEPARATOR);
        stringBuilder.append("Characters / Keyphrase:    "); stringBuilder.append(avgLngthKeyphrs); stringBuilder.append(MORE_LESS_LITERAL); stringBuilder.append(stdDevAvgLnthKphr); stringBuilder.append(')'); stringBuilder.append(LINE_SEPARATOR);
        stringBuilder.append("Tokens / Keyphrase:        "); stringBuilder.append(avgToksKeyphrs); stringBuilder.append(MORE_LESS_LITERAL); stringBuilder.append(stdDevToksKeyphr); stringBuilder.append(')'); stringBuilder.append(LINE_SEPARATOR);
        stringBuilder.append(LINE_SEPARATOR);
        stringBuilder.append("Pearson Correlation between document size and the number of gold keyphrases:"); stringBuilder.append(LINE_SEPARATOR);
//        sb.append(new PearsonsCorrelation().correlation(
//        tokenSizeArray, goldSizeArray));
//        sb.append(LF);
        stringBuilder.append(LINE_SEPARATOR);
        stringBuilder.append(getNrOfTokensPerKeyphraseHistogramm()); stringBuilder.append(LINE_SEPARATOR);
        stringBuilder.append(LINE_SEPARATOR);
        getContext().getLogger().setOutputStream(System.out);
        getContext().getLogger().log(Level.INFO, stringBuilder.toString());
    }

//    private double[] listToArray(List<Integer> list) {
//        double[] array = new double[list.size()];
//        int i = 0;
//        for (int value : list) {
//            array[i] = value;
//            i++;
//        }
//        return array;
//    }

    private String getNrOfTokensPerKeyphraseHistogramm() {
        final Map<Integer,Integer> tksPerKeyphrMap = new TreeMap<Integer,Integer>();
        for (final Integer nrOfTokens : tknsPerKeyphr) {
            if (tksPerKeyphrMap.containsKey(nrOfTokens)) {
                tksPerKeyphrMap.put(nrOfTokens, tksPerKeyphrMap.get(nrOfTokens) + 1);
            }
            else {
                tksPerKeyphrMap.put(nrOfTokens, 1);
            }
        }

        final StringBuilder stringBuilder = new StringBuilder();
        for (final Integer nrOfTokens : tksPerKeyphrMap.keySet() ) {
            stringBuilder.append(nrOfTokens); stringBuilder.append(':'); stringBuilder.append(tksPerKeyphrMap.get(nrOfTokens)); stringBuilder.append(LINE_SEPARATOR);
        }
        return stringBuilder.toString();
    }

    public double mean(final List<Integer> values) {
        double mean = 0.0;
        if (!values.isEmpty()) {
            double sum = 0.0;
            for (final Integer value : values) {
                sum += value;
            }

           mean = sum/values.size();
        }
        return mean;
    }

    public double median(final List<Integer> values) {
    	final DescriptiveStatistics stats = new DescriptiveStatistics();
    	for(final Integer value : values){
    		stats.addValue(value);
    	}
    	return stats.getPercentile(0.5);


    }

    public double stdDev(final List<Integer> values) {
         double stdDev = 0.0;

        if (values.size() > MIN_STD_DEV_SIZE) {
            final double meanValue = mean(values);
            double sum = 0.0;

            for (final Integer value : values) {
                final double diff = value - meanValue;
                sum += diff*diff;
            }
            stdDev = Math.sqrt(sum/(values.size()));
        }
        return stdDev;
    }

}