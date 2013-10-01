package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.util;

import java.util.HashMap;

public class MaxKeyphraseRecallCounter extends AbstractKeyphraseCounter
{
    
    public MaxKeyphraseRecallCounter()
    {
        this.fileName2performanceMap = new HashMap<String, FilePerformance>();
    }

    public double getMaxMicroRecall()
    {
        int tpCount = 0;
        int fnCount = 0;

        for (String fileName : fileName2performanceMap.keySet()) {
            tpCount += fileName2performanceMap.get(fileName).getMaxFileTPcount();
            fnCount += fileName2performanceMap.get(fileName).getMaxFileFNcount();
        }
        return (double) tpCount / (tpCount + fnCount);
    }
   
    public double getMaxMacroRecall()
    {
        double recall = 0.0;
        for (String fileName : fileName2performanceMap.keySet()) {
            recall += fileName2performanceMap.get(fileName).getMaxFileRecall();
        }
        return recall / getRegisteredFiles().size();
    }
}
