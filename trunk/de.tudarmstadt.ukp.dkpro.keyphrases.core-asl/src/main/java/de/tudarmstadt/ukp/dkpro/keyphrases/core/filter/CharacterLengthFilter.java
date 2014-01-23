package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import java.util.LinkedList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate;

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

        List<KeyphraseCandidate> candidatesToBeRemoved = new LinkedList<KeyphraseCandidate>();
        for (KeyphraseCandidate kc : JCasUtil.select(aJCas, KeyphraseCandidate.class)) {
            if (kc.getCoveredText().length() < length) {
                candidatesToBeRemoved.add(kc);
            }
        }
        for (KeyphraseCandidate kc : candidatesToBeRemoved) {
            kc.removeFromIndexes(aJCas);
        }

    }

}
