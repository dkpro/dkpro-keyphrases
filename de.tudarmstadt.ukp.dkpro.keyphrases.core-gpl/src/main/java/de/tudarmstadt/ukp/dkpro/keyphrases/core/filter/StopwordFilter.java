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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.apache.uima.fit.util.JCasUtil.selectCovered;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.core.api.resources.ResourceUtils;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate;

/**
 * Removes all candidate or keyphrase annotations that contain only stopwords.
 * In the rest of the candidates or keyphrases, it removes trailing stopwords.
 *
 * If there is a keyphrase index, it works on keyphrases. Otherwise, it works on candidates.
 *
 * Stopword lists must be lemmatized and in lowercase.
 *
 * @author zesch
 *
 */
public class StopwordFilter extends JCasAnnotator_ImplBase {

    public static final String PARAM_STOPWORD_LIST = "StopwordList";
    @ConfigurationParameter(name=PARAM_STOPWORD_LIST, mandatory=true)
    private String stopwordList;

    private Set<String> stopwords;

    // the mode is automatically determined from the indexes present in the CAS
    private enum Mode {
        Candidates,
        Keyphrases
    }
    private Mode mode;

    @Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);
        try {
        	InputStream is = null;
        	try {
        		is = ResourceUtils.resolveLocation(stopwordList, this, getContext()).openStream();
                stopwords = loadStopwords(is);
        	}
        	finally {
        		closeQuietly(is);
        	}
		}
        catch (IOException e) {
        	throw new ResourceInitializationException(e);
        }
    }

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
        getContext().getLogger().log(Level.CONFIG, "Entering " + this.getClass().getSimpleName());

        if (JCasUtil.select(jcas, Keyphrase.class).size() > 0) {
            mode = Mode.Keyphrases;
        }
        else {
            mode = Mode.Candidates;
        }

        FSIterator<Annotation> annotationIter = null;
        if (mode.equals(Mode.Candidates)) {
            annotationIter = jcas.getAnnotationIndex(KeyphraseCandidate.type).iterator();
        }
        else if (mode.equals(Mode.Keyphrases)) {
            annotationIter = jcas.getAnnotationIndex(Keyphrase.type).iterator();
        }

        // annotation that should be fully removed
        List<Annotation> toRemove = new ArrayList<Annotation>();

        // annotation that should be replaced by a version with deleted trailing stopwords
        Map<Annotation,Annotation> toReplace = new HashMap<Annotation,Annotation>();

        while (annotationIter.hasNext()) {
            Annotation a = annotationIter.next();

            // if the candidate was reduced to length 0, add it to the removal candidates
            if (a.getBegin() == a.getEnd()) {
                toRemove.add(a);
                continue;
            }

            List<Lemma> lemmas  = selectCovered(jcas, Lemma.class, a);

            if (lemmas.size() == 0) {
                getContext().getLogger().log(Level.CONFIG, "Could not get annotations for annotation: " + a.toString() + ". Skipping!");
                continue;
            }

            List<String> items = new ArrayList<String>();
            List<Integer[]> offsets = new ArrayList<Integer[]>();
            for (Annotation l : lemmas) {
                Integer[] offset = new Integer[2];
                offset[0] = l.getBegin();
                offset[1] = l.getEnd();
                offsets.add(offset);

                Lemma lemma = (Lemma) l;
                String term = lemma.getValue().toLowerCase();

                items.add(term);
            }

            // check whether they are stopwords
            List<Boolean> stopwordsFound = new ArrayList<Boolean>();
            for (String item : items) {
                if (isStopword(item)) {
                    stopwordsFound.add(true);
                }
                else {
                    stopwordsFound.add(false);
                }
            }

            // test whether a candidate contains only stopwords => if yes, add to remove
            if (allStopwords(stopwordsFound)) {
                toRemove.add(a);
                continue;
            }

            if (hasTrailingStopword(stopwordsFound)) {
                toReplace.put(a, getReplaceCandidate(jcas, a, items, offsets, stopwordsFound));
            }
        }

        // remove candidates
        for (Annotation a : toRemove) {
            a.removeFromIndexes();
        }

        // replace candidates
        for (Annotation a : toReplace.keySet()) {
            a.removeFromIndexes();
            toReplace.get(a).addToIndexes();
        }
	}

	/**
	 * Tests whether item is a stopword or not.
	 * @param item The string to test.
	 * @return True, if the item is a stopword. False, otherwise.
	 */
	private boolean isStopword(String item) {
	    assert item.length() > 0;

	    // item is in the stopword list
	    if (stopwords.contains(item)) {
            return true;
        }

	    // does not start with a letter
	    if (!firstCharacterIsLetter(item)) {
	        return true;
	    }

        return false;
	}

	private boolean firstCharacterIsLetter(String item) {
        if (item.matches("^[A-Za-z].+")) {
            return true;
        }
        else {
            return false;
        }
	}

    /**
     * @param stopwordsFound
     * @return True, if all items in the list are true. False, otherwise.
     */
    private boolean allStopwords(List<Boolean> stopwordsFound) {
        for (Boolean isStopword : stopwordsFound) {
            if (!isStopword) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param stopwordsFound
     * @return True if the the first or the last element in the list is true. False otherwise.
     */
    private boolean hasTrailingStopword(List<Boolean> stopwordsFound) {
        if (stopwordsFound.get(0) || stopwordsFound.get(stopwordsFound.size() - 1) == true) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @param jcas The jcas.
     * @param a  Either a candidate or a keyphrase annotation, depending on the mode variable.
     * @param items A list of the terms in this annotation.
     * @param offsets A list of the term offsets in this annotation.
     * @param stopwordsFound A list of boolean values indidcating whether the term in this position is a stopword or not.
     * @return An annotation with removed trailing stopwords.
     */
    private Annotation getReplaceCandidate(JCas jcas, Annotation a, List<String> items, List<Integer[]> offsets, List<Boolean> stopwordsFound) {

        Annotation replaceAnnotation = null;
        if (mode.equals(Mode.Candidates)) {
            replaceAnnotation = new KeyphraseCandidate(jcas);
        }
        else if (mode.equals(Mode.Keyphrases)) {
            replaceAnnotation = new Keyphrase(jcas);
        }

        // skip stopwords starting from the beginning
        int i = 0;
        while (stopwordsFound.get(i) && i < stopwordsFound.size()) {
            i++;
        }
        int startOffset = Integer.MAX_VALUE;
        if (i < offsets.size()) {
            startOffset = offsets.get(i)[0];
        }

        // skip stopwords starting from the end
        int j = stopwordsFound.size() - 1;
        while (stopwordsFound.get(j) && j >= 0) {
            j--;
        }
        int endOffset = Integer.MIN_VALUE;
        if (j > 0) {
            endOffset = offsets.get(j)[1];
        }

        if (startOffset < endOffset) {
            String replaceKeyphraseString = StringUtils.join(items.subList(i, j+1), " ");
            replaceAnnotation.setBegin(startOffset);
            replaceAnnotation.setEnd(endOffset);
            if (mode.equals(Mode.Candidates)) {
                ((KeyphraseCandidate) replaceAnnotation).setKeyphrase(replaceKeyphraseString);
            }
            else if (mode.equals(Mode.Keyphrases)) {
                ((Keyphrase) replaceAnnotation).setKeyphrase(replaceKeyphraseString);
                ((Keyphrase) replaceAnnotation).setScore(((Keyphrase) a).getScore());
            }

            return replaceAnnotation;
        }
        else {
            getContext().getLogger().log(Level.FINE, "No more terms left, when pruning trailing stopwords.");
            return a;
        }
    }

    private Set<String> loadStopwords(InputStream is)
	    throws ResourceInitializationException
	{
        Set<String> stopwords = new HashSet<String>();

        try {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            while ((line = br.readLine()) != null) {
                stopwords.add(line);
            }
        } catch (IOException e) {
            throw new ResourceInitializationException(e);
        }

        return stopwords;
    }
}