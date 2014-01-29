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
