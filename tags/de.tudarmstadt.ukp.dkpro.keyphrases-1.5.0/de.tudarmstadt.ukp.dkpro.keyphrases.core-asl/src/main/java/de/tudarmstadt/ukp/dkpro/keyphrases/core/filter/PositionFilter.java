package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.fit.descriptor.ConfigurationParameter;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class PositionFilter
    extends AbstractKeyphraseFilter
{

    public static final String BEGIN_INDEX = "beginIndex";
    @ConfigurationParameter(name = BEGIN_INDEX, mandatory = true)
    private int beginIndex;

    public static final String END_INDEX = "endIndex";
    @ConfigurationParameter(name = END_INDEX, mandatory = true)
    private int endIndex;

    @Override
    public List<Keyphrase> filterCandidates(Collection<Keyphrase> keyphrases)
    {
        List<Keyphrase> candidatesToBeRemoved = new LinkedList<Keyphrase>();
        for (Keyphrase kc : keyphrases) {
            if ((kc.getBegin() >= beginIndex && kc.getBegin() <= endIndex) || 
                (kc.getEnd() >= beginIndex && kc.getEnd() <= endIndex)) {
                continue;
            }
            candidatesToBeRemoved.add(kc);
        }
        return candidatesToBeRemoved;
    }

}
