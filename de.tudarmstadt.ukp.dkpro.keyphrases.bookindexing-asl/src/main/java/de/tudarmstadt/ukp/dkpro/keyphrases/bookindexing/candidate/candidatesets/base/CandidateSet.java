package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.base;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

/**
 * The candidate set expects a empty JCas and augments the CAS with the
 * specified annotation type.
 *
 * @author Mateusz Parzonka
 *
 */
public interface CandidateSet
{

	public final static String LF = System.getProperty("line.separator");

	public enum PosType
	{
		ADJ, ADV, ART, CARD, CONJ, N, O, PP, PR, PUNC, V
	}

	/**
	 * Returns the feature path to the string representation of the used candidate
	 * type.
	 *
	 * @return the feature path
	 */
	public String getFeaturePath();

	AnalysisEngineDescription createPreprocessingComponents(String language)
		throws ResourceInitializationException;

	public Class<? extends Annotation> getType();

}
