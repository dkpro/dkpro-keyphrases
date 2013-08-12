/*******************************************************************************
 * Copyright 2013
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.ranking;

import java.util.Map.Entry;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathException;
import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathFactory;
import de.tudarmstadt.ukp.dkpro.core.api.frequency.provider.FrequencyCountProvider;
import de.tudarmstadt.ukp.dkpro.core.api.frequency.util.FrequencyDistribution;
import de.tudarmstadt.ukp.dkpro.core.api.frequency.util.FrequencyUtils;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * This component adds {@link Keyphrase} annotations consisting of a term and
 * double value calculated as the Rayson and Garside log-likelihood. <br>
 * <p>
 * Based on {@link de.tudarmstadt.ukp.dkpro.semantics.annotator.TfidfAnnotator}
 * by zesch & n_erbs.
 *
 * @author Mateusz Parzonka
 *
 */
public class LoglikelihoodAnnotator
	extends JCasAnnotator_ImplBase
{

	/**
	 * This annotator is type agnostic, so it is mandatory to specify the type of
	 * the working annotation and how to obtain its string representation with the
	 * feature path.
	 */
	public static final String PARAM_FEATURE_PATH = "FeaturePath";
	@ConfigurationParameter(name = PARAM_FEATURE_PATH, mandatory = true)
	private String featurePath;

	public static final String FREQUENCY_COUNT_PROVIDER_KEY = "FrequencyCountProvider";
	@ExternalResource(key = FREQUENCY_COUNT_PROVIDER_KEY)
	private FrequencyCountProvider frequencyCountProvider;

	@Override
	public void initialize(UimaContext context)
		throws ResourceInitializationException
	{
		super.initialize(context);

		String LF = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		sb.append(PARAM_FEATURE_PATH + ": " + this.featurePath + LF);

		getContext().getLogger().log(
				Level.CONFIG,
				"Initializing " + this.getClass().getSimpleName()
						+ " with configuration: " + LF + sb.toString());
	}

	@Override
	public void process(JCas jcas)
		throws AnalysisEngineProcessException
	{

		getContext().getLogger().log(Level.CONFIG,
				"Entering " + getClass().getSimpleName());

		FrequencyDistribution<String> documentCounts = getDocumentCounts(jcas);

		long documentSize = documentCounts.getN();
		long corpusSize = getCorpusSize();

		logDocumentSize(DocumentMetaData.get(jcas).getDocumentTitle(), documentSize);

		// iterate through all annotations and create a keyphrase ranked with
		// log-likelihood based on corpus.
		try {
			for (Entry<AnnotationFS, String> entry : FeaturePathFactory.select(jcas
					.getCas(), featurePath)) {
				String ngram = entry.getValue();
				AnnotationFS annotation = entry.getKey();

				long ngramCountInDocument = documentCounts.getCount(ngram);
				long ngramCountInCorpus = frequencyCountProvider
						.getFrequency(ngram);

				if (ngramCountInDocument > 0L) {

					double loglikelihood = FrequencyUtils.loglikelihood(
							ngramCountInDocument, documentSize, ngramCountInCorpus,
							corpusSize);

					logLoglikelihood(ngram, loglikelihood);

					Keyphrase keyphrase = new Keyphrase(jcas);
					keyphrase.setKeyphrase(ngram);
					keyphrase.setScore(loglikelihood);
					keyphrase.setBegin(annotation.getBegin());
					keyphrase.setEnd(annotation.getEnd());
					keyphrase.addToIndexes();

				}
			}
		}
		catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}

	}

	private long getCorpusSize()
		throws AnalysisEngineProcessException
	{
		long corpusSize;
		try {
			corpusSize = frequencyCountProvider.getNrOfTokens();
		}
		catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
		return corpusSize;
	}

	private FrequencyDistribution<String> getDocumentCounts(JCas jcas)
		throws AnalysisEngineProcessException
	{
		FrequencyDistribution<String> documentCounts = new FrequencyDistribution<String>();
		try {
			for (Entry<AnnotationFS, String> entry : FeaturePathFactory.select(jcas
					.getCas(), featurePath)) {
				documentCounts.inc(entry.getValue());
			}
		}
		catch (FeaturePathException e) {
			throw new AnalysisEngineProcessException(e);
		}
		return documentCounts;
	}

	private void logDocumentSize(String currentDocumentName, long documentSize)
	{
		if (getContext().getLogger().isLoggable(Level.INFO)) {
			getContext().getLogger().log(
					Level.INFO,
					"Document " + currentDocumentName + " consists of " + documentSize
							+ " ngrams.");
		}
	}

	private void logLoglikelihood(String ngram, double loglikelihood)
	{
		if (getContext().getLogger().isLoggable(Level.FINEST)) {
			getContext().getLogger().log(
					Level.FINEST,
					"Adding keyphrase " + ngram + " with loglikelihood " + loglikelihood
							+ ".");
		}
	}
}
