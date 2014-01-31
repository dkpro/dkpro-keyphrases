package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import java.util.LinkedList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class CharacterLengthFilter
    extends JCasAnnotator_ImplBase
{

    public static final String CANDIDATE_LENGTH = "candidateLenght";
    @ConfigurationParameter(name = CANDIDATE_LENGTH)
    private int length;

    @Override
    public void process(JCas aJCas)
        throws AnalysisEngineProcessException
    {

        List<Keyphrase> candidatesToBeRemoved = new LinkedList<Keyphrase>();
        for (Keyphrase kc : JCasUtil.select(aJCas, Keyphrase.class)) {
            if (kc.getCoveredText().length() < length) {
                candidatesToBeRemoved.add(kc);
            }
        }
        for (Keyphrase kc : candidatesToBeRemoved) {
            kc.removeFromIndexes(aJCas);
        }

    }

}
