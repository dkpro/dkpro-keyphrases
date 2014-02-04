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

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class PosSequenceFilter
extends AbstractCandidateFilter
{

    public static final String PARAM_POS_PATTERNS = "posPatterns";
    @ConfigurationParameter(name = PARAM_POS_PATTERNS, mandatory = false)
    private String[] posPatterns;

    private List<String> allowedPosSequences;

    @Override
    public void initialize(final UimaContext context) throws ResourceInitializationException {
        super.initialize(context);

        if(posPatterns == null){
            posPatterns = getStandardPosPatterns();
        }

        allowedPosSequences = Arrays.asList(posPatterns);
    }

    @Override
    public List<Keyphrase> filterCandidates(Collection<Keyphrase> keyphrases)
            throws AnalysisEngineProcessException
            {
        List<Keyphrase> keyphrasesToBeRemoved = new LinkedList<Keyphrase>();

        for (Keyphrase keyphrase : keyphrases) {
            String posSequence = getPosSequence(JCasUtil.selectCovered(POS.class, keyphrase));

            if (!allowedPosSequences.contains(posSequence)) {
                //                System.out.println(keyphrase.getKeyphrase());
                //                System.out.println(posSequence);
                keyphrasesToBeRemoved.add(keyphrase);
            }
        }
        return keyphrasesToBeRemoved;
            }

    private String getPosSequence(Collection<POS> tags)
    {
        List<String> posSequence = new LinkedList<String>();
        for (POS tag : tags) {
            posSequence.add(tag.getPosValue().substring(0, 1));
        }

        return createSequence(posSequence);
    }

    public static String createSequence(List<String> posTags)
    {
        return StringUtils.join(posTags,"_");
    }

    public static String createSequence(String... posTags)
    {
        return createSequence(Arrays.asList(posTags));
    }

    public static String[] getStandardPosPatterns(){
        return new String[]{
                "N",
                "N_N",
                "A_N",
                "V_N",
                "N_V",
                "N_N_N",
                "A_N_N",
                "A_A_N",
                "V_N_N",
                "V_V_N",
                "N_P_N",
                "N_N_N_N",
                "A_N_N_N",
                "A_A_N_N",
                "A_A_A_N"
        };
    }

}