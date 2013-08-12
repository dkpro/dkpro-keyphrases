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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Matchings {
    private Map<String,Set<String>> matchingsMap;

    public Matchings() {
        super();
        this.matchingsMap = new HashMap<String,Set<String>>();
    }

    public void addMatching(String goldKeyphrase, String keyphrase) {
        Set<String> keyphrases;
        if (matchingsMap.containsKey(goldKeyphrase)) {
            keyphrases = matchingsMap.get(goldKeyphrase);
        }
        else {
            keyphrases = new HashSet<String>();
        }
        keyphrases.add(keyphrase);
        matchingsMap.put(goldKeyphrase, keyphrases);
    }
    
    public int getNumberOfMatchings() {
        return matchingsMap.size();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String goldKeyphrase : matchingsMap.keySet()) {
            sb.append(goldKeyphrase);
            sb.append(" : (");
            for (String matchedKeyphrase : matchingsMap.get(goldKeyphrase)) {
                sb.append(" ");
                sb.append(matchedKeyphrase);
                sb.append(";");
            }
            sb.append(")");
            sb.append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }
}