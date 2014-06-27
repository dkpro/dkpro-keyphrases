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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.File;
import java.io.IOException;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotator;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotator.WeightingModeIdf;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotator.WeightingModeTf;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking.TfidfRanking;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking.TfidfRanking.TfidfAggregate;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * Extractor for bookindex-phrases using a tfidf-model.
 * <p>
 * Based on
 * {@link de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.TfidfExtractor}
 * by Zesch
 *
 * @author Mateusz Parzonka
 *
 */
public class TfidfExtractor
	extends BookIndexPhraseExtractor_BaseImpl
{

	/**
	 * The way tf.idf values of multi-word components are aggregated, if there is
	 * no tf.idf annotation for the whole multi-word.
	 */
	private TfidfAggregate aggregateFunction = TfidfAggregate.max;

	/**
	 * The weighting mode for the term frequency (tf).
	 */
	private WeightingModeTf tfWeightingMode = WeightingModeTf.NORMAL;

	/**
	 * The weighting mode for the inverse document frequency (idf).
	 */
	private WeightingModeIdf idfWeightingMode = WeightingModeIdf.LOG;


	/**
	 * File with the tf.idf model.
	 */
	private File tfidfModelFile;

	/**
	 * Builds a tf.idf model from the input files found in inputDir. It writes the
	 * model to a temporary file and sets the corresponding field in the
	 * TfidfExtractor.
	 *
	 * @param inputDir
	 *          The input directory with the input files.
	 * @param suffix
	 *          The suffix of the valid input files.
	 * @throws IOException io exception
	 * @throws ResourceInitializationException resource initialization exception
	 */
	public void buildTfidfModel(File inputDir, String suffix)
		throws ResourceInitializationException
	{
		try {
			final DfModelBuilder dfModelBuilder = new DfModelBuilder(getLanguage(),
					inputDir, suffix);
			File tmpFile = File.createTempFile("tfidf_model_ukp", "tmp");
			dfModelBuilder.buildKeyphraseCandidateModel(getCandidateSet(), tmpFile);
			tfidfModelFile = tmpFile;
		}
		catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}

	public void setAggregateFunction(TfidfAggregate aggregateFunction)
	{
		this.aggregateFunction = aggregateFunction;
	}

	public TfidfAggregate getAggregateFunction()
	{
		return aggregateFunction;
	}

	public void setTfWeightingMode(WeightingModeTf tfWeightingMode)
	{
		this.tfWeightingMode = tfWeightingMode;
	}

	public WeightingModeTf getTfWeightingMode()
	{
		return tfWeightingMode;
	}

	public void setIdfWeightingMode(WeightingModeIdf idfWeightingMode)
	{
		this.idfWeightingMode = idfWeightingMode;
	}

	public WeightingModeIdf getIdfWeightingMode()
	{
		return idfWeightingMode;
	}

	public void setTfidfModelFile(File tfidfModelFile)
	{
		this.tfidfModelFile = tfidfModelFile;
	}

	public File getTfidfModelFile()
	{
		return tfidfModelFile;
	}

	@Override
	protected AnalysisEngineDescription createRanker()
		throws ResourceInitializationException
	{
		return createEngineDescription(
				createEngineDescription(
                    TfidfAnnotator.class,
                    TfidfAnnotator.PARAM_FEATURE_PATH, Keyphrase.class.getName(),
                    TfidfAnnotator.PARAM_TFDF_PATH, getTfidfModelFile().getAbsolutePath(),
                    TfidfAnnotator.PARAM_LOWERCASE, isConvertToLowercase(),
                    TfidfAnnotator.PARAM_TF_MODE, getTfWeightingMode().name(),
                    TfidfAnnotator.PARAM_IDF_MODE, getIdfWeightingMode().name()),
                createEngineDescription(
        		    TfidfRanking.class,
        		    TfidfRanking.PARAM_AGGREGATE,TfidfRanking.TfidfAggregate.max.name()));
	}

}