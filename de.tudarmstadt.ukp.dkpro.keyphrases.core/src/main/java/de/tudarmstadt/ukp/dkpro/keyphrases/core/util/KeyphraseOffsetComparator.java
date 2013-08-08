package de.tudarmstadt.ukp.dkpro.keyphrases.core.util;

import java.util.Comparator;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class KeyphraseOffsetComparator implements Comparator<Keyphrase> {

    public int compare(Keyphrase k1, Keyphrase k2) {
        if (k1.getBegin() < k2.getBegin()) {
            return -1;
        } else if (k1.getBegin() > k2.getBegin()) {
            return 1;
        } else {
            if (k1.getEnd() < k2.getEnd()) {
                return -1;
            }
            else if (k1.getEnd() > k2.getEnd()) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }
}