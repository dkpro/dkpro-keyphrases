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
