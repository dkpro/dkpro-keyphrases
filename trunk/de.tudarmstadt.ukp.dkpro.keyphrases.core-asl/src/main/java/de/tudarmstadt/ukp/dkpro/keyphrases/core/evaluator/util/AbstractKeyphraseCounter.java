package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.util;

import java.util.Map;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;

public abstract class AbstractKeyphraseCounter
{
    
    protected Map<String, FilePerformance> fileName2performanceMap;
    

    public void registerFile(String filename, int nrOfKeyphrasesRetrieved)
        throws AnalysisEngineProcessException
    {
        if (fileName2performanceMap.containsKey(filename)) {
            throw new AnalysisEngineProcessException(new Throwable("Filename '" + filename
                    + "' already registered."));
        }
        fileName2performanceMap.put(filename, new FilePerformance(nrOfKeyphrasesRetrieved));
    }

    public Set<String> getRegisteredFiles()
    {
        return fileName2performanceMap.keySet();
    }

    public void setFileTPcount(String fileName, int i, int count)
    {
        FilePerformance filePerformance = fileName2performanceMap.get(fileName);
        filePerformance.setTPcounter(i, count);
    }

    public void setFileFPcount(String fileName, int i, int count)
    {
        FilePerformance filePerformance = fileName2performanceMap.get(fileName);
        filePerformance.setFPcounter(i, count);
    }

    public void setFileFNcount(String fileName, int i, int count)
    {
        FilePerformance filePerformance = fileName2performanceMap.get(fileName);
        filePerformance.setFNcounter(i, count);
    }
    
}