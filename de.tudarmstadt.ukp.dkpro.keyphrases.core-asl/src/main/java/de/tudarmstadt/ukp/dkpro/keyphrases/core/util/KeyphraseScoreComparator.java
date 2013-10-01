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