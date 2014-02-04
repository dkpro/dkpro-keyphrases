package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.length;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.fit.descriptor.ConfigurationParameter;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.AbstractKeyphraseFilter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public abstract class AbstractLengthFilter
    extends AbstractKeyphraseFilter
{

    public static final String MIN_KEYPHRASE_LENGTH = "MinKeyphraseLength";
    @ConfigurationParameter(name = MIN_KEYPHRASE_LENGTH, mandatory=true)
    private int minLength;
    
    public static final String MAX_KEYPHRASE_LENGTH = "MaxKeyphraseLength";
    @ConfigurationParameter(name = MAX_KEYPHRASE_LENGTH, mandatory=true)
    private int maxLength;

    @Override
    public List<Keyphrase> filterCandidates(Collection<Keyphrase> keyphrases)
    {
        List<Keyphrase> keyphrasesToBeRemoved = new LinkedList<Keyphrase>();
        for (Keyphrase keyphrase : keyphrases) {
            int length = getLength(keyphrase);
            if (length < minLength || length > maxLength) {
                keyphrasesToBeRemoved.add(keyphrase);
            }
        }
        return keyphrasesToBeRemoved;
    }
    
    abstract protected int getLength(Keyphrase keyphrase);

}