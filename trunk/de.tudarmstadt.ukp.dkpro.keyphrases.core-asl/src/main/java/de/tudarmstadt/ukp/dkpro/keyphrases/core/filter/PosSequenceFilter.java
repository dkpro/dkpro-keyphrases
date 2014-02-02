package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class PosSequenceFilter
    extends AbstractCandidateFilter
{

    public static final String PARAM_POS_PATTERNS = "posPatterns";
    @ConfigurationParameter(name = PARAM_POS_PATTERNS, mandatory = true)
    private List<String[]> posPatterns;
    private String[][] splitPosPatterns;

    @Override
    public void initialize(UimaContext context)
        throws ResourceInitializationException
    {
        super.initialize(context);
        splitPosPatterns = new String[posPatterns.size()][];
        for (int i =0; i<posPatterns.size(); i++) {
            splitPosPatterns[i] = posPatterns.get(i)[0].split(",");
        }
    }

    @Override
    public List<Keyphrase> filterCandidates(Collection<Keyphrase> keyphrases)
        throws AnalysisEngineProcessException
    {
        List<Keyphrase> keyphrasesToBeRemoved = new LinkedList<Keyphrase>();
        for (Keyphrase keyphrase : keyphrases) {
            Collection<Token> tokens = JCasUtil.selectCovered(Token.class, keyphrase);
            String[] posTags = new String[tokens.size()];
            collectPosTags(tokens, posTags);
            try {
                if (!keyphraseMatchPattern(posTags)) {
                    keyphrasesToBeRemoved.add(keyphrase);
                }
            }
            catch (ClassNotFoundException e) {
                throw new AnalysisEngineProcessException(e);
            }
        }
        return keyphrasesToBeRemoved;
    }

    private void collectPosTags(Collection<Token> tokens, String[] posTags)
    {
        int i = 0;
        for (Token token : tokens) {
            posTags[i] = token.getPos().getClass().getCanonicalName();
            ++i;
        }
    }

    private boolean keyphraseMatchPattern(String[] posTags)
        throws ClassNotFoundException
    {
        for (String[] posPattern : splitPosPatterns) {
            if (matchPatterns(posTags, posPattern)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchPatterns(String[] posTags, String[] posPattern)
        throws ClassNotFoundException
    {
        if (posTags.length != posPattern.length) {
            return false;
        }
        for (int i = 0; i < posTags.length; ++i) {
            Class<?> tagClass = Class.forName(posTags[i]);
            Class<?> patternClass = Class.forName(posPattern[i]);
            if (!posTags[i].equals(posPattern[i]) && !patternClass.isAssignableFrom(tagClass)) {
                return false;
            }
        }
        return true;
    }

}
