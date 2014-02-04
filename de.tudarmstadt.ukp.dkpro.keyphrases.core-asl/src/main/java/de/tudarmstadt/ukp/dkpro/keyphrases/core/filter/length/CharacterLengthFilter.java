package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.length;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;


public class CharacterLengthFilter
    extends AbstractLengthFilter
{

    @Override
    protected int getLength(Keyphrase keyphrase)
    {
        return keyphrase.getKeyphrase().length();
    }
}