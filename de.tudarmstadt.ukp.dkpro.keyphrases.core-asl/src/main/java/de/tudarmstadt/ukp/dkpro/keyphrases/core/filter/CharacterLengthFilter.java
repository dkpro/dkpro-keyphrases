package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.fit.descriptor.ConfigurationParameter;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class CharacterLengthFilter
    extends AbstractCandidateFilter
{

    public static final String CANDIDATE_LENGTH = "candidateLenght";
    @ConfigurationParameter(name = CANDIDATE_LENGTH)
    private int length;

    @Override
    public List<Keyphrase> filterCandidates(Collection<Keyphrase> keyphrases)
    {

        List<Keyphrase> candidatesToBeRemoved = new LinkedList<Keyphrase>();
        for (Keyphrase kc : keyphrases) {
            if (kc.getCoveredText().length() < length) {
                candidatesToBeRemoved.add(kc);
            }
        }
        return candidatesToBeRemoved;

    }

}
