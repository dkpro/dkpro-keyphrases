package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.uima.fit.descriptor.ConfigurationParameter;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class FrequencyFilter
    extends AbstractCandidateFilter
{

    public static final String FREQUENCY = "frequency";
    @ConfigurationParameter(name = FREQUENCY)
    private int frequency;

    @Override
    public List<Keyphrase> filterCandidates(Collection<Keyphrase> keyphrases)
    {

        List<Keyphrase> keyphrasesToBeRemoved = new LinkedList<Keyphrase>();
        Map<String, Long> keyphrasesFrequency = new HashMap<String, Long>();
        for (Keyphrase kc : keyphrases) {
            String coveredText = kc.getCoveredText().toLowerCase();
            if (keyphrasesFrequency.containsKey(coveredText)) {
                long value = keyphrasesFrequency.get(coveredText);
                keyphrasesFrequency.put(coveredText, ++value);
            }
            else {
                keyphrasesFrequency.put(coveredText, new Long(1));
            }
        }
        for (Keyphrase kc : keyphrases) {
            if (keyphrasesFrequency.get(kc.getCoveredText().toLowerCase()) < frequency) {
                keyphrasesToBeRemoved.add(kc);
            }
        }
        return keyphrasesToBeRemoved;

    }

}
