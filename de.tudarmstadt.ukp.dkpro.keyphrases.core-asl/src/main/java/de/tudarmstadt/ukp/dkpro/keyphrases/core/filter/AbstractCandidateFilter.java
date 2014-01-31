package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import java.util.Collection;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public abstract class AbstractCandidateFilter
    extends JCasAnnotator_ImplBase
{

    @Override
    public void process(JCas aJCas)
        throws AnalysisEngineProcessException
    {
        try {
            for (Keyphrase keyphrase : filterCandidates(JCasUtil.select(aJCas, Keyphrase.class))) {
                keyphrase.removeFromIndexes();
            }
        }
        catch (CASException e) {
            throw new AnalysisEngineProcessException(e);
        }
    }

    protected abstract List<Keyphrase> filterCandidates(Collection<Keyphrase> keyphrases)
        throws CASException, AnalysisEngineProcessException;

}
