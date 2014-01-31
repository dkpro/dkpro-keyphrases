package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public abstract class AbstractCandidateFilter
    extends JCasAnnotator_ImplBase implements CandidateFilter
{

    /* (non-Javadoc)
     * @see de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.CandidateFilter#process(org.apache.uima.jcas.JCas)
     */
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
}
