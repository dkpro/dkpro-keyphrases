package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.phrasematch;

import java.util.List;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

/**
 * Declares methods to retrieve strings associated with a jcas.
 *
 * @author Mateusz Parzonka
 *
 */
public interface StringReader
{

	/**
	 * Retrieves a set of strings associated with a document.
	 *
	 * @param jcas
	 *          containing a annotation where the set of strings can be derived
	 * @return a set of strings
	 * @throws AnalysisEngineProcessException
	 */
	public abstract Set<String> getSetOfStrings(JCas jcas)
		throws AnalysisEngineProcessException;

	/**
	 * Retrieves a list of strings associated with a document.
	 *
	 * @param jcas
	 *          containing a annotation where the list of strings can be derived
	 * @return a list of strings
	 * @throws AnalysisEngineProcessException
	 */
	public abstract List<String> getListOfStrings(JCas jcas)
		throws AnalysisEngineProcessException;

}