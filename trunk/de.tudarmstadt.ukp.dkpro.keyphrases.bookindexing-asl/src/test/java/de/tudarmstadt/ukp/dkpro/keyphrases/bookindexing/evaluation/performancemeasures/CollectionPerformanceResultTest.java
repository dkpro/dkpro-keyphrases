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
