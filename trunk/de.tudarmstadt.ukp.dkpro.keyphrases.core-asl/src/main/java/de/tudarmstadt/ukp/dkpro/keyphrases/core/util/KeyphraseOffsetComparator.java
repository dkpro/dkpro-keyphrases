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