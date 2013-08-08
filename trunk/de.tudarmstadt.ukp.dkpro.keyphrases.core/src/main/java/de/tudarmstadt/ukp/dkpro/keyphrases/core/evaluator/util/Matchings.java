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