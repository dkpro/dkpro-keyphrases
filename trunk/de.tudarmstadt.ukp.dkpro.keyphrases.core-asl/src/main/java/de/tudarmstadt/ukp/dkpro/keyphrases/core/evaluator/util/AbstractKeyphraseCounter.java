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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;

public abstract class AbstractKeyphraseCounter
{

    protected Map<String, FilePerformance> fileName2performanceMap = new HashMap<String, FilePerformance>();

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