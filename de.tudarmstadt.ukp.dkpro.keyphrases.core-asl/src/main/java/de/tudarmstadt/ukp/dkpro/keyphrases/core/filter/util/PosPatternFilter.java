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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class PosPatternFilter {
    
    private final int lower_length_threshold;
    private final int upper_length_threshold;
    
    private final Set<String> lengthOnePatterns = new HashSet<String>() {
        private static final long serialVersionUID = 1L;
        {
            add("N");
        }
    };
    private final Set<String> lengthTwoPatterns = new HashSet<String>() {
        private static final long serialVersionUID = 1L;
        {
            add("N_N");
            add("A_N");
            add("V_N");
            add("N_V");
        }
    };
    private final Set<String> lengthThreePatterns = new HashSet<String>() {
        private static final long serialVersionUID = 1L;
        {
            add("N_N_N");
            add("A_N_N");
            add("A_A_N");
            add("V_N_N");
            add("V_V_N");
            add("N_P_N");
        }
    };
    private final Set<String> lengthFourPatterns = new HashSet<String>() {
        private static final long serialVersionUID = 1L;
        {
            add("N_N_N_N");
            add("A_N_N_N");
            add("A_A_N_N");
            add("A_A_A_N");
        }
    };

    private final Map<Integer,Set<String>> posPatterns = new HashMap<Integer,Set<String>>() {
        private static final long serialVersionUID = 1L;
        {
            put(1, lengthOnePatterns);
            put(2, lengthTwoPatterns);
            put(3, lengthThreePatterns);
            put(4, lengthFourPatterns);
        }
    };

    public PosPatternFilter(int lower_length_threshold, int upper_length_threshold) {
        super();
        this.lower_length_threshold = lower_length_threshold;
        this.upper_length_threshold = upper_length_threshold;
    }

    public boolean isValidPosPattern(List<String> pos) {
        String joinedPos = StringUtils.join(pos, "_");
        
        if (pos.size() < lower_length_threshold || pos.size() > upper_length_threshold) {
            return false;
        }
     
        Set<String> patterns = posPatterns.get(pos.size());
        if (patterns.contains(joinedPos)) {
            return true;
        }
        else {
            return false;
        }
    }
}
