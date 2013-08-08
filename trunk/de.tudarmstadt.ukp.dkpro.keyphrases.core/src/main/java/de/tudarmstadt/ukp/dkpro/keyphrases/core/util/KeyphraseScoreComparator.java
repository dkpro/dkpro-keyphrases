package de.tudarmstadt.ukp.dkpro.keyphrases.core.util;

import java.util.Comparator;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * Sorts descending by score.
 */
public class KeyphraseScoreComparator implements Comparator<Keyphrase> {

    @Override
		public int compare(Keyphrase k1, Keyphrase k2) {
			return -1 * Double.compare(k1.getScore(), k2.getScore());
    }
}