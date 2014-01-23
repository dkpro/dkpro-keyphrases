package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate;

public class FrequencyFilter
    extends JCasAnnotator_ImplBase
{

    public static final String FREQUENCY = "frequency";
    @ConfigurationParameter(name = FREQUENCY)
    private int frequency;

    @Override
    public void process(JCas aJCas)
        throws AnalysisEngineProcessException
    {

        List<KeyphraseCandidate> keyphrasesToBeRemoved = new LinkedList<KeyphraseCandidate>();
        Map<String, Long> keyphrasesFrequency = new HashMap<String, Long>();
        for (KeyphraseCandidate kc : JCasUtil.select(aJCas, KeyphraseCandidate.class)) {
            String coveredText = kc.getCoveredText().toLowerCase();
            if (keyphrasesFrequency.containsKey(coveredText)) {
                long value = keyphrasesFrequency.get(coveredText);
                keyphrasesFrequency.put(coveredText, ++value);
            }
            else {
                keyphrasesFrequency.put(coveredText, new Long(1));
            }
        }
        for (KeyphraseCandidate kc : JCasUtil.select(aJCas, KeyphraseCandidate.class)) {
            if (keyphrasesFrequency.get(kc.getCoveredText().toLowerCase()) < frequency) {
                keyphrasesToBeRemoved.add(kc);
            }
        }
        for (KeyphraseCandidate kc : keyphrasesToBeRemoved) {
            kc.removeFromIndexes(aJCas);
        }

    }

}
