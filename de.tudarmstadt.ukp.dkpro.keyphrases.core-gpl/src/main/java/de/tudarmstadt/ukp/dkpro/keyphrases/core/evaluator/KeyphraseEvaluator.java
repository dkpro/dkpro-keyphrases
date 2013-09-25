/*******************************************************************************
 * Copyright 2013
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.0.txt
 ******************************************************************************/
package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.util.EvaluatorUtils;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.util.KeyphrasePerformanceCounter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.util.Matchings;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.util.MaxKeyphraseRecallCounter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * This consumer evaluates the annotated keyphrases against the gold standard in KeyphraseMetaData.
 * It outputs precision/recall/f-measure and some statistics.
 *
 * These measures depend on the number of returned keyphrases. Most keyphrase extractors are tuned to return the full ranking.
 * This consumer outputs performance for each #keyphrase threshold from 1 to max(N,#keyphrases in document), where n is N configuration parameter.
 *
 * If the rank at a threshold is tied, all these tied keyphrases are taken into account.
 *
 * @author zesch
 *
 */
public class KeyphraseEvaluator extends JCasConsumer_ImplBase {
    private static final String LF = System.getProperty("line.separator");

    public enum EvaluatorType {
        Token,
        Stem,
        Lemma
    }

    public enum MatchingType {
        Exact,
        Approximate
    }

    // The maximum number of keyphrases to consider for evaluation.
    // The KeyphraseEvaluator then iterates from 1 to n and outputs performance values for each of these thresholds.
    // O means take as many keyphrases as are there.
    public static final String PARAM_N = "N";
    @ConfigurationParameter(name=PARAM_N, mandatory=true, defaultValue="0")
	private int n;

    // In some cases the capitalization of the gold standard keyphrases does not reflect capitalization in the text
    // As there are only rare cases where a keyphrase should be included in uppercase, but not in lowercase (e.g. apple vs. Apple) this seems acceptable.
    // The behavior can be overwritten by a parameter.
    public static final String PARAM_LOWERCASE   = "lowercase";
    @ConfigurationParameter(name=PARAM_LOWERCASE, mandatory=true, defaultValue="false")
	private boolean lowercase;

    // The evaluation results are written to that file if specified.
    public static final String PARAM_RESULT_FILE = "ResultFile";
    @ConfigurationParameter(name=PARAM_RESULT_FILE, mandatory=false)
	private String resultFileName;

    public static final String PARAM_EVAL_TYPE = "EvaluationType";
    @ConfigurationParameter(name=PARAM_EVAL_TYPE, mandatory=true, defaultValue="Token")
    private EvaluatorType evalType;

    public static final String PARAM_MATCHING_TYPE = "MatchingType";
    @ConfigurationParameter(name=PARAM_MATCHING_TYPE, mandatory=true, defaultValue="Exact")
    private MatchingType matchingType;

    // The suffix of the gold keyphrase files. Default is "key".
    // Suffixes should always be given with the ".".
    public static final String PARAM_GOLD_SUFFIX = "GoldSuffix";
    @ConfigurationParameter(name=PARAM_GOLD_SUFFIX, mandatory=false, defaultValue=".key")
    private String goldSuffix;

    public static final String PARAM_REMOVE_KEYPHRASES_NOT_IN_TEXT = "RemoveKeyphrasesNotInText";
    @ConfigurationParameter(name=PARAM_REMOVE_KEYPHRASES_NOT_IN_TEXT, mandatory=false, defaultValue="true")
    private boolean removeKeyphrasesNotInText;

    // Whether gold keyphrases are removed from the list after a match.
    // If true, each keyphrase can only be matched once.
    // Default should be true.
    // Should only be set to false, if you know what you do :)
    public static final String PARAM_REMOVE_GOLD_AFTER_MATCH = "RemoveGoldKeyphraseAfterMatch";
    @ConfigurationParameter(name=PARAM_REMOVE_GOLD_AFTER_MATCH, mandatory=true, defaultValue="true")
    private boolean removeGoldKeyphraseAfterMatch;

    private static final String allowableStringDifferences = "s-/_ ";

    private int nrofGoldKeyphrases = 0;
    private int nrofDeletedGoldKeyphrases = 0;

    private int maxIterateTo = 0;

    private int docCounter = 1;

	private double rPrecisionAll;
	private double ratioFoundGoldKeyphrases;

    private KeyphrasePerformanceCounter performanceCounterAll;
    private MaxKeyphraseRecallCounter maxRecallCounter;


    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);

        StringBuilder sb = new StringBuilder();
        sb.append("Lowercase:    "); sb.append(lowercase);    sb.append(LF);
        sb.append("N:            "); sb.append(n);            sb.append(LF);
        sb.append("EvalType:     "); sb.append(evalType);     sb.append(LF);
        sb.append("MatchingType: "); sb.append(matchingType); sb.append(LF);
        sb.append(LF);
        getContext().getLogger().log(Level.INFO, sb.toString());
        
        performanceCounterAll = new KeyphrasePerformanceCounter();
        maxRecallCounter = new MaxKeyphraseRecallCounter();
    }

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {

        getContext().getLogger().log(Level.INFO, "Processing document: " + docCounter);
        docCounter++;

        // get the meta data of the document
        DocumentMetaData docMetaData = DocumentMetaData.get(jcas);
        String currentTitle = docMetaData.getDocumentTitle();
        getContext().getLogger().log(Level.INFO, "Document title: " + currentTitle);

        // get the gold keyphrases from the file system
        Set<String> goldKeyphrases = EvaluatorUtils.getGoldKeyphrases(docMetaData, goldSuffix, lowercase);
        int originalGoldKeyphrasesSize = goldKeyphrases.size();

        // remove gold keyphrases that are not in the text
        if(removeKeyphrasesNotInText){
        	goldKeyphrases = removeGoldKeyphrasesNotInDocument(jcas.getDocumentText(), goldKeyphrases);
        }
        
        

        nrofGoldKeyphrases += goldKeyphrases.size();
        nrofDeletedGoldKeyphrases += originalGoldKeyphrasesSize - goldKeyphrases.size();
        getContext().getLogger().log(Level.INFO, "GOLD-KEYS:"+goldKeyphrases);

        // if there are no gold keyphrases left, ignore that document in evaluation
        if (goldKeyphrases.size() == 0) {
            return;
        }

        List<Keyphrase> keyphrases = EvaluatorUtils.filterAndSortKeyphrases(JCasUtil.select(jcas, Keyphrase.class), lowercase);

        int iterateTo = getIterateTo(Math.max(keyphrases.size(), goldKeyphrases.size()));

        if (iterateTo > maxIterateTo) {
            maxIterateTo = iterateTo;
        }

        performanceCounterAll.registerFile(currentTitle, iterateTo);
        maxRecallCounter.registerFile(currentTitle, keyphrases.size());

        // get performance results for each i up to n
        for (int i=1; i<=iterateTo; i++) {
            computeThresholdPerformanceResults(i, iterateTo, keyphrases, goldKeyphrases, currentTitle);
        }
        
        computeMaxRecall(keyphrases, goldKeyphrases, currentTitle);
        
        
    }


    private void computeMaxRecall(List<Keyphrase> keyphrases, Set<String> goldKeyphrases, String title) throws AnalysisEngineProcessException
    {
        int tp = 0;
        int fp = 0;

        List<String> keyphrasesToConsider = getKeyphrasesToConsider(keyphrases, keyphrases.size());

        int nrOfGoldKeyphrases = goldKeyphrases.size();

        // get the set of matching goldkeyphrases and equivalent extracted keyphrases
        Matchings matchings = getMatchings(new HashSet<String>(goldKeyphrases), keyphrasesToConsider);

        tp = matchings.getNumberOfMatchings();
        fp = keyphrasesToConsider.size() - tp;

        // sanity check
        if (tp > nrOfGoldKeyphrases) {
            throw new AnalysisEngineProcessException(new Throwable("More true positives than gold standard keyphrases."));
        }

        performanceCounterAll.setFileTPcount(title, keyphrasesToConsider.size(), tp);
        performanceCounterAll.setFileFPcount(title, keyphrasesToConsider.size(), fp);
        performanceCounterAll.setFileFNcount(title, keyphrasesToConsider.size(), nrOfGoldKeyphrases - tp);
        
    }

    @Override
    public void collectionProcessComplete() throws AnalysisEngineProcessException {

        StringBuilder sb = new StringBuilder();
        sb.append(
            performanceCounterAll.getFilePerformanceOverview(maxIterateTo)
        );

        ratioFoundGoldKeyphrases = 1 - new Double(nrofDeletedGoldKeyphrases) / (nrofGoldKeyphrases + nrofDeletedGoldKeyphrases);
        rPrecisionAll = performanceCounterAll.getAverageRPrecision();

        sb.append(LF);
        sb.append("# gold keyphrases:           "); sb.append(nrofGoldKeyphrases + nrofDeletedGoldKeyphrases); sb.append(LF);
        sb.append("# gold keyphrases (deleted): "); sb.append(nrofDeletedGoldKeyphrases); sb.append(LF);
        sb.append("ratio:                       "); sb.append(ratioFoundGoldKeyphrases); sb.append(LF);
        sb.append(LF);
        sb.append("avg. R-Precision (All):      "); sb.append(rPrecisionAll); sb.append(LF);
        sb.append(LF);
        sb.append(LF);

        sb.append( performanceCounterAll.getMicroPrfOverview() );
        sb.append( performanceCounterAll.getMacroPrfOverview() );

        sb.append(
            performanceCounterAll.getMicroPerformanceOverview(maxIterateTo)
        );

        getContext().getLogger().log(Level.INFO, sb.toString());

        if (resultFileName != null) {
            try {
                FileUtils.writeStringToFile(new File(resultFileName), sb.toString(), "UTF-8");
            } catch (IOException e) {
                throw new AnalysisEngineProcessException(e);
            }
        }
    }

    private void computeThresholdPerformanceResults(int i, int n, List<Keyphrase> keyphrases, Set<String> goldKeyphrases, String title) throws AnalysisEngineProcessException {

        List<String> keyphrasesToConsider = getKeyphrasesToConsider(keyphrases, i);

        // update true positives and false positives
        int tp = 0;
        int fp = 0;

        int nrOfGoldKeyphrases = goldKeyphrases.size();

        // get the set of matching goldkeyphrases and equivalent extracted keyphrases
        Matchings matchings = getMatchings(new HashSet<String>(goldKeyphrases), keyphrasesToConsider);

        tp = matchings.getNumberOfMatchings();
        fp = keyphrasesToConsider.size() - tp;

        // sanity check
        if (tp > nrOfGoldKeyphrases) {
            throw new AnalysisEngineProcessException(new Throwable("More true positives than gold standard keyphrases."));
        }

        // do not show more than the first 100 keyphrases candidates
        if (i==n && i<100) {
            getContext().getLogger().log(Level.INFO, "KEYPHRASES:"+keyphrasesToConsider);
            getContext().getLogger().log(Level.INFO, matchings.toString());
        }
        else if (i==n) {
            getContext().getLogger().log(Level.INFO, "KEYPHRASES: " + i + " keyphrases retrieved. Too much to display.");
            getContext().getLogger().log(Level.INFO, matchings.toString());
        }

        performanceCounterAll.setFileTPcount(title, i, tp);
        performanceCounterAll.setFileFPcount(title, i, fp);
        performanceCounterAll.setFileFNcount(title, i, nrOfGoldKeyphrases - tp);
        if (i==nrOfGoldKeyphrases) {
            performanceCounterAll.setRPrecision(title, (double) tp / nrOfGoldKeyphrases);
        }
    }

    /**
     * Removes gold keyphrases that do not appear in the document text from the set of gold keyphrases.
     * @param documentView
     * @param goldKeyphrases
     */
    protected Set<String> removeGoldKeyphrasesNotInDocument(String documentText, Set<String> goldKeyphrases) {

        // if the document text is empty, no gold keyphrases can be found
        if (documentText.isEmpty()) {
            return Collections.emptySet();
        }

        documentText = lowercase ? documentText.toLowerCase() : documentText;

        Iterator<String> goldIter = goldKeyphrases.iterator();
        while (goldIter.hasNext()) {
            String goldKeyphrase = lowercase ? goldIter.next().toLowerCase() : goldIter.next();

            if (!documentText.contains(goldKeyphrase)) {
                getContext().getLogger().log(Level.FINE, "Removing gold keyphrase: " + goldKeyphrase);
                goldIter.remove();
            }
        }

        return goldKeyphrases;
    }

    /**
     * Sets the max iteration. It's either n or the maximum number of keyphrases smaller than n.
     * @param size The number of retrieved keyphrase candidates.
     * @return The upper bound of iterations for this file. Either n or the number of keyphrases (if number of keyphrases < n).
     */
    protected int getIterateTo(int size) {
    	// n==0 means => take all keyphrases
        int iterateTo = n;
        if (n == 0 || size < n) {
            iterateTo = size;  // if there are less than n keyphrases, limit iterating to keyphrases
        }

        return iterateTo;
    }

    /**
     * Returns a list of the n top-ranked keyphrases.
     *
     * @param keyphrases A list of keyphrases sorted in descending order by score.
     * @param n Indicates how many of the top-ranked keyphrases should be returned.
     * @return A list of the n top ranked keyphrases.
     */
    private List<String> getKeyphrasesToConsider(List<Keyphrase> keyphrases, int n) {
        List<String> keyphraseStrings = new ArrayList<String>();

        int i=0;
        while (i<n && i<keyphrases.size()) {
            String keyphrase = lowercase ? keyphrases.get(i).getKeyphrase().toLowerCase() : keyphrases.get(i).getKeyphrase();
            keyphraseStrings.add(keyphrase);
            i++;
        }

        return keyphraseStrings;
    }

    private Matchings getMatchings(Set<String> goldKeyphrases, List<String> keyphrases) {
        Matchings matchings = new Matchings();

        for (String keyphrase : keyphrases) {
                if (goldKeyphrases.contains(keyphrase)) {
                    matchings.addMatching(keyphrase, keyphrase);
                    if (removeGoldKeyphraseAfterMatch) {
                        goldKeyphrases.remove(keyphrase);
                    }
                }
            else {
                if (matchingType.equals(MatchingType.Approximate)) {
                    String matchingGoldKeyphrase = getApproximateMatchingGoldKeyphrase(goldKeyphrases, keyphrase);
                    if (matchingGoldKeyphrase != null) {
                        matchings.addMatching(matchingGoldKeyphrase, keyphrase);
                        if (removeGoldKeyphraseAfterMatch) {
                            goldKeyphrases.remove(matchingGoldKeyphrase);
                        }
                    }
                }
            }
        }

        return matchings;
    }

    /**
     * @param goldKeyphrases
     * @param keyphrases
     * @return The first matching gold keyphrase or null, if no match is found.
     */
    private String getApproximateMatchingGoldKeyphrase(Set<String> goldKeyphrases, String keyphrase) {

        for (String goldKeyphrase : goldKeyphrases) {
            if (isRelaxedMatch(goldKeyphrase,keyphrase)) {
                return goldKeyphrase;
            }
        }

        return null;
    }

    private boolean isRelaxedMatch(String goldKeyphrase, String keyphrase) {
//// TZ: deactivated for ACL09, as human agreement with these matchings is very low
//        if (fullyIncluded(goldKeyphrase, keyphrase)) {
//            return true;
//        }

        if (fullyIncluded(keyphrase, goldKeyphrase)) {
            return true;
        }
        else if (smallLevenshtein(goldKeyphrase, keyphrase)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * The comparison is made wrt to tokens.
     * E.g. "economic principles" is fully included in "basic economic principles".
     * However, "economic" is not fully included in "economics".
     * @param s1
     * @param s2
     * @return True if s2 is fully included in s1.
     */
    private boolean fullyIncluded(String s1, String s2) {
        if (!s1.contains(s2)) {
            return false;
        }

        int start = s1.indexOf(s2);
        int end = start + s2.length()-1;

        if ((start == 0 || start > 0 && s1.charAt(start-1) == ' ') && (end == s1.length()-1 || end < s1.length()-1 && s1.charAt(end+1) == ' ')) {
            return true;
        }

        return false;
    }

    /**
     * @param s1
     * @param s2
     * @return True, if s1 has a tolerable small Levenshtein distance to s2.
     */
    private boolean smallLevenshtein(String s1, String s2) {
        if (Math.abs(s1.length()-s2.length()) > 1) {
            return false;
        }

        String difference = org.apache.commons.lang.StringUtils.difference(s1, s2);
        if (difference.length() == 1) {
            if (org.apache.commons.lang.StringUtils.containsAny(difference, allowableStringDifferences)) {
                return true;
            }
        }

        return false;
    }

    protected double getRPrecisionAll()
    {
    	return rPrecisionAll;
    }

    protected String getGoldSuffix()
    {
        return goldSuffix;
    }

    protected boolean shouldLowercase()
    {
        return lowercase;
    }

    protected double getMacroPrecision()
    {
        return performanceCounterAll.getMacroPrecision(n);
    }

    protected double getMacroRecall()
    {
        return performanceCounterAll.getMacroRecall(n);
    }

    protected double getMicroPrecision()
    {
        return performanceCounterAll.getMicroPrecision(n);
    }

    protected double getMicroRecall()
    {
        return performanceCounterAll.getMicroRecall(n);
    }

    protected double getMeanAveragePrecision(){
        return performanceCounterAll.getMeanAveragePrecision();
    }

    protected double getMaxMicroRecall()
    {
        return maxRecallCounter.getMaxMicroRecall();
    }

    protected double getMaxMacroRecall()
    {
        return maxRecallCounter.getMaxMacroRecall();
    }


}