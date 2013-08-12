package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.util.KeyphraseOffsetComparator;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;


public class KeyphraseExtractorUtils {

    /**
     * @param keyphrases The set of extracted keyphrases.
     * @param n How many keyphrases should be returned.
     * 
     * @return Returns a sorted list of the n top-ranked keyphrases.
     */
    public static List<Keyphrase> getTopRankedKeyphrases(List<Keyphrase> keyphrases, int n) {
        if (keyphrases.size() <= n) {
            return keyphrases;
        }
        else {
            return keyphrases.subList(0, n);
        }
    }

    /**
     * @param keyphrases The set of extracted keyphrases.
     * @param n How many keyphrases should be returned.
     * 
     * @return Returns a sorted list of the n top-ranked unique keyphrases.
     */
    public static List<Keyphrase> getTopRankedUniqueKeyphrases(List<Keyphrase> keyphrases, int n) {
        List<Keyphrase> uniqueKeyphrases = new ArrayList<Keyphrase>();
        Set<String> keyphraseStrings = new HashSet<String>();
        for (Keyphrase keyphrase : keyphrases) {
            if (!keyphraseStrings.contains(keyphrase.getKeyphrase())) {
                keyphraseStrings.add(keyphrase.getKeyphrase());
                uniqueKeyphrases.add(keyphrase);
            }
        }
        
        if (uniqueKeyphrases.size() <= n) {
            return uniqueKeyphrases;
        }
        else {
            return uniqueKeyphrases.subList(0, n);
        }
    }

    /**
     * @param keyphrases A list of keyphrases.
     * @return The list of keyphrases in the order they appear in the document.
     */
    public static List<Keyphrase> getKeyphrasesInDocumentOrder(List<Keyphrase> keyphrases) {
        Collections.sort(keyphrases, new KeyphraseOffsetComparator());
        return keyphrases;
    }

    public static List<String> keyphraseList2StringList(List<Keyphrase> keyphrases) {
        List<String> strings = new ArrayList<String>();
        for (Keyphrase k : keyphrases) {
            strings.add(k.getKeyphrase());
        }
        
        return strings;
    }
    
}
