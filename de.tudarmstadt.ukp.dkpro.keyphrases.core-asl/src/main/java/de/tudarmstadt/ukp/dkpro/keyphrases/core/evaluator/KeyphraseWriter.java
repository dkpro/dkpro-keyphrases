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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * Simply outputs the document text and up to <code>n</code> extracted keyphrases.
 *
 * @author zesch
 *
 */
public class KeyphraseWriter extends JCasAnnotator_ImplBase {
    private static final String LF = System.getProperty("line.separator");

    public static final String PARAM_N = "n";
    @ConfigurationParameter(name=PARAM_N, mandatory=false, defaultValue="5")
    private int n;

    public static final String PARAM_LOWERCASE = "lowercase";
    @ConfigurationParameter(name=PARAM_LOWERCASE, mandatory=true, defaultValue="false")
    private boolean lowercase;

    /**
     * Whether should remove keyphrase which is already covered by other keyphrases.
     *
     * For example, "education" is covered by "college education". For true value of
     * this parameter, "education" will be removed.
     *
     */
    public static final String PARAM_REMOVE_CONTAINED = "removeFullyContainedKeyphrases";
    @ConfigurationParameter(name=PARAM_REMOVE_CONTAINED, mandatory=true, defaultValue="false")
    private boolean removeContained;

    public static final String PARAM_SHOULD_WRITE_DOCUMENT = "shouldWriteDocument";
    @ConfigurationParameter(name=PARAM_SHOULD_WRITE_DOCUMENT, mandatory=true, defaultValue="true")
    private boolean shouldWriteDocument;


    private JCas jcas;

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);
//        n=5;
        StringBuilder sb = new StringBuilder();
        sb.append("Lowercase: "); sb.append(lowercase); sb.append(LF);
        sb.append("Remove fully contained keyphrases: "); sb.append(removeContained); sb.append(LF);
        sb.append("N: "); sb.append(n); sb.append(LF);
        sb.append(LF);
        getContext().getLogger().log(Level.INFO, sb.toString());
    }

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {

        getContext().getLogger().log(Level.INFO, "Processing document: " + DocumentMetaData.get(jcas).getDocumentId());

        this.jcas = jcas;

        StringBuilder sb = new StringBuilder();

        if (shouldWriteDocument) {
            DocumentMetaData metaData = DocumentMetaData.get(jcas);
            sb.append("----------Document------------"); sb.append(LF);
            sb.append(metaData.getDocumentId()); sb.append(LF);
            sb.append(jcas.getDocumentText()); sb.append(LF);
        }

        // sort the keyphrases by score, filter duplicates,
        sb.append("----------Keyphrases------------"); sb.append(LF);
        List<Keyphrase> keyphrases = filterAndSortKeyphrases();
        int iterateTo = Math.min(keyphrases.size(), this.n);
        for (int i=0; i<iterateTo; i++) {
            sb.append(keyphrases.get(i).getKeyphrase()); sb.append(LF);
        }
        getContext().getLogger().log(Level.INFO, sb.toString());
    }

    /**
     * Sorts the keyphrases by score.
     * Filters duplicate keyphrases. The keyphrase with the higher score is kept.
     *
     * @return A list of unique keyphrases in order of descending scores.
     */
    private List<Keyphrase> filterAndSortKeyphrases() {

        // get a set of all keyphrases
        List<Keyphrase> keyphrases = new ArrayList<Keyphrase>();
        for (Keyphrase keyphrase : JCasUtil.select(jcas, Keyphrase.class)) {
            keyphrases.add(keyphrase);
        }

        // sort the keyphrases
        Collections.sort(keyphrases, new KeyphraseComparator());

        // filter the keyphrases
        List<Keyphrase> filteredKeyphrases = new ArrayList<Keyphrase>();
        Set<String> uniqueKeyphrases = new HashSet<String>();
        for (Keyphrase keyphrase : keyphrases) {
            String keyphraseString = keyphrase.getKeyphrase();

            if (keyphraseString.length() == 0) {
                continue;
            }

            if (lowercase) {
                keyphraseString = keyphraseString.toLowerCase();
            }
            if (!uniqueKeyphrases.contains(keyphraseString)) {
                uniqueKeyphrases.add(keyphraseString);
                filteredKeyphrases.add(keyphrase);
            }
        }

        if (removeContained) {
            filteredKeyphrases = removeFullyContained(filteredKeyphrases);
        }

        return filteredKeyphrases;
    }

    private List<Keyphrase> removeFullyContained(List<Keyphrase> keyphrases) {
        List<Keyphrase> filteredKeyphrases = new ArrayList<Keyphrase>();

        List<String> keyphraseStrings = new ArrayList<String>();
        for (Keyphrase keyphrase : keyphrases) {
            keyphraseStrings.add(keyphrase.getKeyphrase());
        }
        for (int i=0; i<keyphraseStrings.size(); i++) {
            boolean contained = false;
            for (int j=0; j<keyphraseStrings.size(); j++) {
                if (i!=j && isContained(keyphraseStrings.get(i), keyphraseStrings.get(j))) {
                    contained = true;
                }
            }
            if (!contained) {
                filteredKeyphrases.add(keyphrases.get(i));
            }
        }

        return filteredKeyphrases;
    }

    private boolean isContained(String source, String target) {
        return target.contains(source);
    }

    private class KeyphraseComparator implements Comparator<Keyphrase> {
        @Override
        public int compare(Keyphrase k1, Keyphrase k2) {
            if (k1.getScore() < k2.getScore()) {
                return 1;
            }
            else if (k1.getScore() > k2.getScore()) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }
}