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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.AbstractPhraseMatcherTest;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures.AveragedPerformanceMeasures;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures.CollectionPerformanceResult;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures.DocumentPerformanceResult;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures.PerformanceMeasures;

/**
 * Test of {@link CollectionPerformanceResult}
 *
 * @author Mateusz Parzonka
 *
 */
public class CollectionPerformanceResultTest
	extends AbstractPhraseMatcherTest
{

	@Test
	public void instantiationAndCalculation_OneDocument()
	{
		// setup
		DocumentPerformanceResult dpr1 = new DocumentPerformanceResult("d1", list(
				1, 1, 1, 1), 4);
		CollectionPerformanceResult collectionPerformanceResult = new CollectionPerformanceResult(
				list(dpr1));

		// exercise
		AveragedPerformanceMeasures actualByRetrievedCount = collectionPerformanceResult
				.getAveragedPerformanceMeasures(2);
		AveragedPerformanceMeasures actualByRetrievedPercentage = collectionPerformanceResult
				.getAveragedPerformanceMeasures(0.5D);

		// verify
		PerformanceMeasures pm1 = new PerformanceMeasures(2, 2, 4);
		AveragedPerformanceMeasures expected = new AveragedPerformanceMeasures(
				list(pm1));
		assertEquals(expected, actualByRetrievedCount);
		assertEquals(expected, actualByRetrievedPercentage);

	}

	@Test
	public void instantiationAndCalculation_TwoDocuments()
	{
		// setup
		DocumentPerformanceResult dpr1 = new DocumentPerformanceResult("d1", list(
				1, 1, 1, 1), 4);
		DocumentPerformanceResult dpr2 = new DocumentPerformanceResult("d2", list(
				1, 0), 2);
		CollectionPerformanceResult collectionPerformanceResult = new CollectionPerformanceResult(
				list(dpr1, dpr2));

		// exercise
		AveragedPerformanceMeasures actualByRetrievedCount = collectionPerformanceResult
				.getAveragedPerformanceMeasures(2);
		AveragedPerformanceMeasures actualByRetrievedPercentage = collectionPerformanceResult
				.getAveragedPerformanceMeasures(0.5D);

		// verify
		PerformanceMeasures pm1 = new PerformanceMeasures(2, 2, 4);
		PerformanceMeasures pm2ByCount = new PerformanceMeasures(2, 1, 2);
		PerformanceMeasures pm2ByPercentage = new PerformanceMeasures(1, 1, 2);
		AveragedPerformanceMeasures expectedByRetrievedCount = new AveragedPerformanceMeasures(
				list(pm1, pm2ByCount));
		AveragedPerformanceMeasures expectedByRetrievedPercentage = new AveragedPerformanceMeasures(
				list(pm1, pm2ByPercentage));
		assertEquals(expectedByRetrievedCount, actualByRetrievedCount);
		assertEquals(expectedByRetrievedPercentage, actualByRetrievedPercentage);
	}

}
