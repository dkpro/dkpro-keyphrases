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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.aggregation;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.aggregation.aggregationstrategy.AggregationStrategy;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.aggregation.aggregationstrategy.AggregationStrategy.RankedPhrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.Segment;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * This component provides the base implementation to adds annotations the CAS
 * which represent phrases associated with scores. As they can be ranked
 * according to their score they are called "ranked phrases".
 *
 * @author Mateusz Parzonka
 *
 */
public abstract class RankedPhraseAggregationAnnotator
	extends JCasAnnotator_ImplBase
{

	/**
	 * The aggregation strategy is specified with the (qualified) name of a class
	 * implementing {@link AggregationStrategy}. It provides the implementation
	 * how the ranked phrases in the segments are aggregated into a single list.
	 */
	public static final String AGGREGATION_STRATEGY = "AggregationStrategy";
	@ExternalResource(key = AGGREGATION_STRATEGY)
	private AggregationStrategy aggregationStrategy;

	/**
	 * If set to true, the whole text is handled in lower case.
	 */
	public static final String PARAM_LOWERCASE = "ConvertToLowercase";
	@ConfigurationParameter(name = PARAM_LOWERCASE, mandatory = false, defaultValue = "false")
	private boolean convertToLowercase;

	@Override
	public void initialize(UimaContext context)
		throws ResourceInitializationException
	{
		super.initialize(context);
		getContext().getLogger().log(Level.CONFIG,
				"Initializing " + this.getClass().getSimpleName());
	}

	@Override
	public void process(JCas aJCas)
		throws AnalysisEngineProcessException
	{
		// aggregate phrases and store them in CAS (as implemented in concrete
		// subclass).
		List<AggregationStrategy.RankedPhrase> aggregatedPhrases = aggregationStrategy
				.aggregatePhrases(getTable(aJCas));
		storePhrasesInCas(aggregatedPhrases, aJCas);
	}

	/**
	 * Iterates through {@link Segment}s and aggregates all {@link Keyphrase}s as
	 * specified in the concrete {@link AggregationStrategy}.
	 *
	 * @param jcas
	 * @return table representing phrases contained in segments
	 * @throws AnalysisEngineProcessException
	 */
	private Table<String, Integer, Double> getTable(JCas jcas)
		throws AnalysisEngineProcessException
	{
		Table<String, Integer, Double> phraseSegmentTable = TreeBasedTable.create(
				new Comparator<String>() {
					@Override
					public int compare(String o1, String o2)
					{ return o1.compareTo(o2);}},
				new Comparator<Integer>() {
					@Override
					public int compare(Integer o1, Integer o2)
					{ return o1.compareTo(o2); }});

		// there have to be segments in the JCas
		assert jcas.getAnnotationIndex(Segment.type).size() > 0;

		if (getContext().getLogger().isLoggable(Level.INFO)) {
            getContext().getLogger().log(
					Level.INFO,
					String.format("Found %d segments", jcas.getAnnotationIndex(
							Segment.type).size()));
        }

		// iterate through all segments and add all phrases with their score to
		// the table
		int segmentNr = 0;
		for (Segment segment : JCasUtil.select(jcas, Segment.class)) {

			// aggregate all (keyphrase, segment) -> score mappings in a table
			for (Keyphrase keyphrase : JCasUtil.selectCovered(Keyphrase.class,
					segment)) {

				String phrase = convertToLowercase ? keyphrase.getKeyphrase().toLowerCase()
						: keyphrase.getKeyphrase();

				if (getContext().getLogger().isLoggable(Level.FINEST)) {
                    getContext().getLogger().log(
							Level.FINEST,
							String.format(Locale.US,
									"(Phrase=[%s], SegNr=[%d]) -> Score=[%.3f]",
									phrase, segmentNr, keyphrase.getScore()));
                }

					phraseSegmentTable.put(phrase, segmentNr, keyphrase.getScore());
			}
			segmentNr++;
		}
		return phraseSegmentTable;
	}

	/**
	 * Stores {@link RankedPhrase}s in CAS. Subclasses have to implement this
	 * method to store the aggregated phrases using a specified UIMA-type.
	 *
	 * @param aggregatedPhrases
	 * @param jcas
	 */
	protected abstract void storePhrasesInCas(
			List<RankedPhrase> aggregatedPhrases, JCas jcas);

}
