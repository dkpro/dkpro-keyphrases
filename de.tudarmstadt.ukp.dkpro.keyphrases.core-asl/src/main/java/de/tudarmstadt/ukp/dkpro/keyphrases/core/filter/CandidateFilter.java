package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import java.util.Collection;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * An interface for keyphrase filters
 * @author nico.erbs@gmail.com
 *
 */
public interface CandidateFilter
{

    public abstract void process(JCas aJCas)
        throws AnalysisEngineProcessException;
    
    public abstract List<Keyphrase> filterCandidates(Collection<Keyphrase> keyphrases)
            throws CASException, AnalysisEngineProcessException;

}