package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.frequency;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class FrequencyFilter
extends AbstractFrequencyFilter
{

    private Map<String, Long> keyphraseFrequencies;

    @Override
    public List<Keyphrase> filterCandidates(Collection<Keyphrase> keyphrases) throws AnalysisEngineProcessException
    {
        keyphraseFrequencies = getKeyphraseFrequencies(keyphrases);
        
        return super.filterCandidates(keyphrases);
    }
    
    private Map<String, Long> getKeyphraseFrequencies(Collection<Keyphrase> keyphrases)
    {
        Map<String, Long> keyphraseFrequencies = new HashMap<String, Long>();
        String keyphraseText;
        for (Keyphrase keyphrase : keyphrases) {
            keyphraseText = keyphrase.getKeyphrase();
            if(!keyphraseFrequencies.containsKey(keyphraseText)){
                keyphraseFrequencies.put(keyphraseText, 0l);
            }
            keyphraseFrequencies.put(keyphraseText, keyphraseFrequencies.get(keyphraseText) + 1);
        }
        return keyphraseFrequencies;
    }

    @Override
    protected long getFrequency(String keyphrase){
        if(!keyphraseFrequencies.containsKey(keyphrase)){
            return 0l;
        }
        return keyphraseFrequencies.get(keyphrase);
    }

}
