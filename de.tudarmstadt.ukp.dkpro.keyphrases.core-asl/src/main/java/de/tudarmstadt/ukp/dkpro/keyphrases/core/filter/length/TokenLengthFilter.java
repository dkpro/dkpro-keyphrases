package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.length;

import org.apache.uima.fit.util.JCasUtil;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class TokenLengthFilter
    extends AbstractLengthFilter
{

    @Override
    protected int getLength(Keyphrase keyphrase)
    {
        return JCasUtil.selectCovered(Token.class, keyphrase).size();
    }

}