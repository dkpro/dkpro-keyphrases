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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.postprocessing;

import static org.apache.uima.fit.util.JCasUtil.selectCovered;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * If two keyphrases are directly adjacent, they can be joined to form a larger keyphrase.
 * They are not joined if there length exceeds MaxLength that is given by a parameter.
 * This length is measured in Tokens underlying the keyphrase.
 *
 * If KeepParts is true, the keyphrases that are used for merging are not removed from the index.
 *
 * @author zesch
 *
 */

public class KeyphraseMerger extends JCasAnnotator_ImplBase {

    /**
     * The maximum length of a merged keyphrase.
     * As normally stopword filtering is applied after this step,
     * the value should be larger than the one in the structure filter,
     * as trailing stopwords might be removed.
     */
    public static final String PARAM_MAX_LENGTH = "MaxLength";
    @ConfigurationParameter(name=PARAM_MAX_LENGTH, mandatory=true, defaultValue="6")
    private int maxLength;

    public static final String PARAM_KEEP_PARTS = "KeepParts";
    @ConfigurationParameter(name=PARAM_KEEP_PARTS, mandatory=true, defaultValue="false")
    private boolean keepParts;

    private KeyphraseOffsetMerger offsetMerger;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
        getContext().getLogger().log(Level.CONFIG, "Entering " + this.getClass().getSimpleName());

        // do not create in initialize (there has to be a new Merger for each document)
        this.offsetMerger = new KeyphraseOffsetMerger(jcas);
        offsetMerger.merge();
	}

    public class KeyphraseOffsetMerger {
        JCas jcas;
        Map<Integer,Set<Keyphrase>> keyphraseOffsetMap;
        Map<Keyphrase,Integer> keyphraseTokenMap;
        Map<Keyphrase,String> keyphraseStringMap;
        Set<Keyphrase> toRemove;
        Set<Keyphrase> toRegister;

        public KeyphraseOffsetMerger(JCas jcas) {
            this.jcas = jcas;
            toRemove = new HashSet<Keyphrase>();
            toRegister = new HashSet<Keyphrase>();
            keyphraseOffsetMap = new TreeMap<Integer,Set<Keyphrase>>();
            keyphraseStringMap = new HashMap<Keyphrase,String>();

            keyphraseTokenMap = getKeyphraseTokenMap();
            for (Keyphrase k : keyphraseTokenMap.keySet()) {
                registerKeyphrase(k);
                keyphraseStringMap.put(k, k.getKeyphrase());
            }
        }

        /**
         * Merges keyphrases.
         * @throws AnalysisEngineProcessException 
         *
         */
        public void merge() throws AnalysisEngineProcessException {
            boolean changes;
            do {
                changes = false;

                // iterate over the sorted (lowest first) start offsets and try to concatenate
                for (Integer startOffset : keyphraseOffsetMap.keySet()) {
                    if (concatenateKeyphrases(startOffset)) {
                        changes = true;
                        if (!keepParts) {
                            removeSubsumedKeyphrases();
                        }
                        else {
                            // TODO this does not really merge as expected
                            changes = false;
                        }
                        registerNewKeyphrases();
                    }
                }
            } while (changes);
        }

        private void removeSubsumedKeyphrases() {
            // remove keyphrases that are substituted by the merged keyphrase
            for (Keyphrase k : toRemove) {
                getContext().getLogger().log(Level.FINE, "Removing " + k.getKeyphrase());
                k.removeFromIndexes();
                keyphraseTokenMap.remove(k);
                keyphraseStringMap.remove(k);
                removeFromKeyphraseOffsetMap(k);
            }
            toRemove.clear();
        }

        private void registerNewKeyphrases() {
            for (Keyphrase k : toRegister) {
                getContext().getLogger().log(Level.FINE, "Registering " + k.getKeyphrase());
                registerKeyphrase(k);
            }
            toRegister.clear();
        }

        private void removeFromKeyphraseOffsetMap(Keyphrase k) {
            int tmpOffset = k.getBegin();
            Set<Keyphrase> keyphrases = keyphraseOffsetMap.get(tmpOffset);
            keyphrases.remove(k);
            keyphraseOffsetMap.put(tmpOffset, keyphrases);
        }

        // try to concatenate keyphrases at starting at startOffset with other keyphrases with subsequent offsets
        private boolean concatenateKeyphrases(int startOffset) throws AnalysisEngineProcessException {
            boolean changes = false;

            // concatenate keyphrases
            for (Keyphrase k : keyphraseOffsetMap.get(startOffset)) {
                // a keyphrase that can be merged with a keyphrase starting at k.getEnd() + 1
                int startOffsetNextKeyphrase = k.getEnd() + 1;
                if (keyphraseOffsetMap.containsKey(startOffsetNextKeyphrase)) {
                    // concatenate with all keyphrases in the list
                    if (concatenateWithSet(k, keyphraseOffsetMap.get(startOffsetNextKeyphrase))) {
                        changes = true;
                    }
                }
            }

            return changes;
        }

        private boolean concatenateWithSet(Keyphrase startKeyphrase, Set<Keyphrase> keyphraseSet) throws AnalysisEngineProcessException {
            boolean changes = false;

            if (!keyphraseTokenMap.containsKey(startKeyphrase)) {
                System.err.println(startKeyphrase);
                System.err.println(keyphraseTokenMap.keySet());
                throw new AnalysisEngineProcessException();
            }

            int startKeyphraseTokens = keyphraseTokenMap.get(startKeyphrase);

            for (Keyphrase endKeyphrase : keyphraseSet) {
                int endKeyphraseTokens = keyphraseTokenMap.get(endKeyphrase);

                if ((startKeyphraseTokens + endKeyphraseTokens) > maxLength) {
                    continue;
                }
                else {
                    changes = true;
                }

                StringBuilder mergedString = new StringBuilder();
                mergedString.append(startKeyphrase.getKeyphrase());
                mergedString.append(" ");
                mergedString.append(endKeyphrase.getKeyphrase());

                // create a new keyphrase
                Keyphrase mergedKeyphrase = new Keyphrase(jcas);
                mergedKeyphrase.setBegin(startKeyphrase.getBegin());
                mergedKeyphrase.setEnd(endKeyphrase.getEnd());
                mergedKeyphrase.setKeyphrase(mergedString.toString());

                setKeyphraseScore(mergedKeyphrase, startKeyphrase, endKeyphrase);

                mergedKeyphrase.addToIndexes();
                toRegister.add(mergedKeyphrase);
                keyphraseTokenMap.put(mergedKeyphrase, startKeyphraseTokens + endKeyphraseTokens);
                keyphraseStringMap.put(mergedKeyphrase, mergedString.toString());

                toRemove.add(startKeyphrase);
                toRemove.add(endKeyphrase);
            }
            return changes;
        }

        /**
         * Registers the keyphrase k within the OffsetMerger
         * @param k A keyphrase.
         */
        private void registerKeyphrase(Keyphrase k) {
            int startOffset = k.getBegin();
            Set<Keyphrase> keyphraseSet;
            if (keyphraseOffsetMap.containsKey(startOffset)) {
                keyphraseSet = keyphraseOffsetMap.get(startOffset);
            }
            else {
                keyphraseSet = new HashSet<Keyphrase>();
            }
            keyphraseSet.add(k);
            keyphraseOffsetMap.put(startOffset, keyphraseSet);
        }

        /**
         * @param tokenIndex
         * @return Returns a mapping between keyphrases and the number of underlying tokens.
         */
        private Map<Keyphrase,Integer> getKeyphraseTokenMap() {
            Map<Keyphrase,Integer> keyphraseTokenMap = new HashMap<Keyphrase,Integer>();
            for (Keyphrase keyphrase : JCasUtil.select(jcas, Keyphrase.class)) {
                keyphraseTokenMap.put(keyphrase, getNrofUnderlyingTokens(keyphrase));
            }
            return keyphraseTokenMap;
        }

        private int getNrofUnderlyingTokens(Keyphrase keyphrase) {
            return selectCovered(jcas, Token.class, keyphrase).size();
        }

        private void setKeyphraseScore(Keyphrase targetKeyphrase, Keyphrase k1, Keyphrase k2) {
            if (k1.getScore() >= k2.getScore()) {
                targetKeyphrase.setScore(k1.getScore());
            }
            else {
                targetKeyphrase.setScore(k2.getScore());
            }
        }
    }
}