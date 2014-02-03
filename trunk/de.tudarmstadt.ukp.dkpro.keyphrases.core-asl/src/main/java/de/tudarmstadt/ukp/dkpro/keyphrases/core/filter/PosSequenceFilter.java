package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
    private String[] posPatterns;
    
    private List<String> allowedPosSequences;

    @Override
    public void initialize(final UimaContext context) throws ResourceInitializationException {
        super.initialize(context);

        allowedPosSequences = Arrays.asList(posPatterns);
//        System.out.println(allowedPosSequences);
    }

    @Override
    public List<Keyphrase> filterCandidates(Collection<Keyphrase> keyphrases)
            throws AnalysisEngineProcessException
            {
        List<Keyphrase> keyphrasesToBeRemoved = new LinkedList<Keyphrase>();
        for (Keyphrase keyphrase : keyphrases) {
            String posSequence = getPosSequence(JCasUtil.selectCovered(Token.class, keyphrase));
            
            if (!allowedPosSequences.contains(posSequence)) {
//                System.out.println(keyphrase.getKeyphrase());
//                System.out.println(posSequence);
                keyphrasesToBeRemoved.add(keyphrase);
            }
        }
        return keyphrasesToBeRemoved;
            }

    private String getPosSequence(Collection<Token> tokens)
    {
        List<String> posSequence = new LinkedList<String>();
        for (Token token : tokens) {
            posSequence.add(token.getPos().getClass().getSimpleName());
        }
        return createSequence(posSequence);
    }

    public static String createSequence(String... posTags)
    {
        return StringUtils.join(posTags,"-");
    }

    public static String createSequence(List<String> posTags)
    {
        return StringUtils.join(posTags,"-");
    }

}
