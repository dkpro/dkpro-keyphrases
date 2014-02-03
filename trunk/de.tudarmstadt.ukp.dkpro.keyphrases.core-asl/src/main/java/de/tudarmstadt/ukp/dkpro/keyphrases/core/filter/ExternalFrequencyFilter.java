package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import java.io.IOException;
import org.apache.uima.fit.descriptor.ExternalResource;

import de.tudarmstadt.ukp.dkpro.core.api.frequency.provider.FrequencyCountProvider;

public class ExternalFrequencyFilter
    extends AbstractFrequencyFilter
{
    
    public static final String FREQUENCY_COUNT_RESOURCE = "FrequencyProvider";
    @ExternalResource(key = FREQUENCY_COUNT_RESOURCE, mandatory=true)
    private FrequencyCountProvider frequencyProvider;
    
    protected long getFrequency(String keyphrase) throws IOException{
        return frequencyProvider.getFrequency(keyphrase);
    }

}