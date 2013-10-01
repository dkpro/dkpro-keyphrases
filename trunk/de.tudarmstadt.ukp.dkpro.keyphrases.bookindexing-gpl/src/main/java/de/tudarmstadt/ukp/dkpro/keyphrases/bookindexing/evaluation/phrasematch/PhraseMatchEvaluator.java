/*******************************************************************************
 * Copyright 2013

 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.0.txt
 ******************************************************************************/
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.phrasematch;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.matchingstrategy.MatchingStrategy;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures.AveragedPerformanceMeasures;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures.CollectionPerformanceResult;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures.DocumentPerformanceResult;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures.PerformanceMeasures;

/**
 * Generic type-system agnostic evaluator measuring matchings between a list of
 * ranked phrases and a set of gold items. Produces (averaged) performance
 * measures for each document, which are collected in
 * {@link CollectionPerformanceResult}s.<br>
 *
 * @author Mateusz Parzonka
 *
 * @see AveragedPerformanceMeasures
 * @see PerformanceMeasures
 *
 */
public abstract class PhraseMatchEvaluator

	extends JCasAnnotator_ImplBase
{

	/**
	 * Specifies the resource inheriting from {@link MatchingStrategy} responsible
	 * for testing the match between two strings.
	 */
	public final static String RESOURCE_MATCHING_STRATEGY = "MatchingStrategy";
	@ExternalResource(key = RESOURCE_MATCHING_STRATEGY)
	private MatchingStrategy matchingStrategy;

	private List<DocumentPerformanceResult> documentPerformanceResults;
	private int goldCount;

	@Override
	public void initialize(UimaContext context)
		throws ResourceInitializationException
	{
		super.initialize(context);
		getContext().getLogger().log(Level.CONFIG, "Initializing " + this.getClass().getSimpleName());
		documentPerformanceResults = new ArrayList<DocumentPerformanceResult>();
	}

	@Override
	public void process(JCas jcas)
		throws AnalysisEngineProcessException
	{

		getContext().getLogger().log(Level.CONFIG, "Entering " + getClass().getSimpleName());

		String documentBaseName = getDocumentBaseName(jcas);
		Set<String> goldSet = getGoldSet(jcas);
		goldCount = goldSet.size();
		List<Integer> truePositiveCounts = new ArrayList<Integer>();

		List<String> phrases = getRetrievedPhrases(jcas);

		for (String phrase : phrases) {

			// a phrase can cover 0 - n gold items
			final Set<String> coveredGoldPhrases = matchingStrategy.getCovered(phrase, goldSet);
			truePositiveCounts.add(coveredGoldPhrases.size());

			if (getContext().getLogger().isLoggable(Level.FINEST)) {
                getContext().getLogger().log(Level.FINEST,
						String.format("Phrase [%s] covered gold phrases: %s", phrase, coveredGoldPhrases.toString()));
            }

			// every gold phrase can be matched only once, such that 0 - n phrases
			// match 0 - 1 gold items
			for (String coveredGoldPhrase : coveredGoldPhrases) {
                goldSet.remove(coveredGoldPhrase);
            }
		}


	// store the result of each document
		DocumentPerformanceResult result = new DocumentPerformanceResult(documentBaseName, truePositiveCounts, goldCount);
		documentPerformanceResults.add(result);

		handleDocumentResult(result, phrases);

		if (getContext().getLogger().isLoggable(Level.INFO)) {
			logPhrasesAndGoldSet(jcas, documentBaseName, truePositiveCounts, phrases);
		}
	}

	/**
	 * Subclasses can overwrite this method to implement per-document-logging /-reporting.
	 *
	 * @param documentPerformanceResult
	 * @param extractedPhrases
	 */
	protected void handleDocumentResult(DocumentPerformanceResult documentPerformanceResult, List<String> extractedPhrases)
	{
		// base implementation does nothing.
	}

	/**
	 * @param jcas containing a annotation where the list of strings can be derived
	 * @return a list of strings which will be matched with the gold phrases. A
	 *         unique gold phrase can be matched only once, so duplicates in the
	 *         retrieved phrases will lead to a degrade performance.
	 */
	protected abstract List<String> getRetrievedPhrases(JCas jcas);

	@Override
	public void collectionProcessComplete()
		throws AnalysisEngineProcessException
	{
		super.collectionProcessComplete();
		handleCollectionResult(new CollectionPerformanceResult(documentPerformanceResults));
	}

	/**
	 * @param jcas containing information where the set of gold phrases can be derived
	 * @return a set of gold phrases
	 * @throws AnalysisEngineProcessException
	 */
	protected abstract Set<String> getGoldSet(JCas jcas) throws AnalysisEngineProcessException;

	/**
	 * Consumes and processes the result of the collection evaluation.
	 *
	 * @param collectionPerformanceResult
	 * @throws AnalysisEngineProcessException
	 */
	protected abstract void handleCollectionResult(CollectionPerformanceResult collectionPerformanceResult)
		throws AnalysisEngineProcessException;

	public int getGoldCount()
	{
		return goldCount;
	}

	/**
	 * @param jcas
	 * @return the document basename from the parsed document-URI-path.
	 * @throws AnalysisEngineProcessException
	 */
	private static String getDocumentBaseName(JCas jcas)
		throws AnalysisEngineProcessException
	{
		try {
			URI uri = new URI(DocumentMetaData.get(jcas).getDocumentUri());
			return FilenameUtils.getBaseName(uri.getPath());
		}
		catch (URISyntaxException e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

	private void logPhrasesAndGoldSet(JCas jcas, String documentBaseName, List<Integer> truePositiveCounts,
			List<String> phrases)
		throws AnalysisEngineProcessException
	{
		final String LF = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		sb.append("Retrieved ordered phrases and goldset for document [").append(documentBaseName).append("]").append(LF);
		sb.append("Phrases [phrase (#coveredGoldPhrases)]: [");
		int i = 0;
		if (!phrases.isEmpty()) {
			Iterator<String> phraseIter = phrases.iterator();
			sb.append(phraseIter.next()).append("(").append(truePositiveCounts.get(i++)).append(")");
			while (phraseIter.hasNext()) {
                sb.append(", ").append(phraseIter.next()).append("(").append(truePositiveCounts.get(i++)).append(")");
            }
		}
		sb.append("]").append(LF);
		sb.append("Goldset: ").append(getGoldSet(jcas));
		getContext().getLogger().log(Level.INFO, sb.toString());
	}

}
