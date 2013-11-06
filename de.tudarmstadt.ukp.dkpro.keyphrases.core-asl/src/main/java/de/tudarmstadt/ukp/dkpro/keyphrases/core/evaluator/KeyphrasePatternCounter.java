package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class KeyphrasePatternCounter
{

    private final Map<Integer, Map<String, Integer>> patternCounter;
    private final Map<Integer, Map<String, String>> examplePatterns;

    public KeyphrasePatternCounter()
    {
        patternCounter = new HashMap<Integer, Map<String, Integer>>();
        examplePatterns = new HashMap<Integer, Map<String, String>>();
    }

    public void updateStructureCounter(int size, String pattern, String example)
    {
        if (patternCounter.containsKey(size)) {
            Map<String, Integer> counterMap = patternCounter.get(size);
            if (counterMap.containsKey(pattern)) {
                counterMap.put(pattern, counterMap.get(pattern) + 1);
            }
            else {
                counterMap.put(pattern, 1);

            }
            patternCounter.put(size, counterMap);
        }
        else {
            Map<String, Integer> counterMap = new TreeMap<String, Integer>();
            counterMap.put(pattern, 1);
            patternCounter.put(size, counterMap);
        }

        if (examplePatterns.containsKey(size)) {
            Map<String, String> exampleMap = examplePatterns.get(size);
            exampleMap.put(pattern, example);
            examplePatterns.put(size, exampleMap);

        }
        else {
            Map<String, String> exampleMap = new TreeMap<String, String>();
            exampleMap.put(pattern, example);
            examplePatterns.put(size, exampleMap);
        }
    }

    public Map<String, Integer> getPhraseCounterMap(int size)
    {
        if (patternCounter.containsKey(size)) {
            return patternCounter.get(size);
        }
        else {
            return Collections.emptyMap();
        }
    }

    public Set<Integer> getSizes()
    {
        return patternCounter.keySet();
    }

    public Set<String> getPatterns(int size)
    {
        return patternCounter.get(size).keySet();
    }

    public int getPhrasePatternCount(int size, String pattern)
    {
        return patternCounter.get(size).get(pattern);
    }

    public String getPhrasePatternExample(int size, String pattern)
    {
        return examplePatterns.get(size).get(pattern);
    }
}
