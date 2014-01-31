package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import java.util.LinkedList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class PositionFilter
    extends JCasAnnotator_ImplBase
{

    public static final String BEGIN_INDEX = "beginIndex";
    @ConfigurationParameter(name = BEGIN_INDEX, mandatory = true)
    private int beginIndex;

    public static final String END_INDEX = "endIndex";
    @ConfigurationParameter(name = END_INDEX, mandatory = true)
    private int endIndex;

    @Override
    public void process(JCas aJCas)
        throws AnalysisEngineProcessException
    {
        List<Keyphrase> candidatesToBeRemoved = new LinkedList<Keyphrase>();
        for (Keyphrase kc : JCasUtil.select(aJCas, Keyphrase.class)) {
            if ((kc.getBegin() >= beginIndex && kc.getBegin() <= endIndex) || 
                (kc.getEnd() >= beginIndex && kc.getEnd() <= endIndex)) {
                continue;
            }
            candidatesToBeRemoved.add(kc);
        }
        for (Keyphrase kc : candidatesToBeRemoved) {
            kc.removeFromIndexes(aJCas);
        }
    }

}
