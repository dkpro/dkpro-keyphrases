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

import static org.junit.Assert.*;

import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.AbstractBookIndexingTest;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures.PerformanceMeasures;

/**
 * Test for {@link PerformanceMeasures}
 * 
 * @author Mateusz Parzonka
 * 
 */
public class PerformanceMeasuresTest
	extends AbstractBookIndexingTest
{

	@Test
	public void infRetrievalInstantiation()
	{
		// setup
		int retrievedCount = 523;
		int relevantCount = 42;
		int goldCount = 1984;

		// exercise
		PerformanceMeasures measures = new PerformanceMeasures(retrievedCount,
				relevantCount, goldCount);

		// verify
		assertEquals(retrievedCount, measures.getRetrievedCount());
		assertEquals(relevantCount, measures.getRelevantCount());
		assertEquals(relevantCount, measures.getCoveredCount());
		assertEquals(goldCount, measures.getGoldCount());
	}

	@Test
	public void approxMatchingInstantiation()
	{
		// setup
		int retrievedCount = 523;
		int relevantCount = 42;
		int coveredCount = 17;
		int goldCount = 1984;

		// exercise
		PerformanceMeasures measures = new PerformanceMeasures(retrievedCount,
				relevantCount, coveredCount, goldCount);

		// verify
		assertEquals(retrievedCount, measures.getRetrievedCount());
		assertEquals(relevantCount, measures.getRelevantCount());
		assertEquals(coveredCount, measures.getCoveredCount());
		assertEquals(goldCount, measures.getGoldCount());
	}

	@Test
	public void approxMatchingMeasuresA_1RetrievedItemCovers1GoldItem()
	{

		// setup
		int retrievedCount = 1;
		int relevantCount = 1;
		int coveredCount = 1;
		int goldCount = 1;

		// exercise
		PerformanceMeasures measures = new PerformanceMeasures(retrievedCount,
				relevantCount, coveredCount, goldCount);

		// verify
		assertEquals(1, measures.getPrecision(), EPSILON);
		assertEquals(1, measures.getRecall(), EPSILON);
		assertEquals(1, measures.getFMeasure(), EPSILON);
	}

	@Test
	public void approxMatchingMeasuresA_1RetrievedItemCovers0GoldItem()
	{

		// setup
		int retrievedCount = 1;
		int relevantCount = 0;
		int coveredCount = 0;
		int goldCount = 1;

		// exercise
		PerformanceMeasures measures = new PerformanceMeasures(retrievedCount,
				relevantCount, coveredCount, goldCount);

		// verify
		assertEquals(0, measures.getPrecision(), EPSILON);
		assertEquals(0, measures.getRecall(), EPSILON);
		assertEquals(0, measures.getFMeasure(), EPSILON);
	}

	@Test
	public void approxMatchingMeasuresB_1RetrievedItemCovers1GoldItem()
	{

		// setup
		int retrievedCount = 2;
		int relevantCount = 1;
		int coveredCount = 1;
		int goldCount = 2;

		// exercise
		PerformanceMeasures measures = new PerformanceMeasures(retrievedCount,
				relevantCount, coveredCount, goldCount);

		// verify
		assertEquals(0.5, measures.getPrecision(), EPSILON);
		assertEquals(0.5, measures.getRecall(), EPSILON);
		assertEquals(0.5, measures.getFMeasure(), EPSILON);
	}

	@Test
	public void approxMatchingMeasuresB_1RetrievedItemCovers2GoldItems()
	{

		// setup
		int retrievedCount = 2;
		int relevantCount = 1;
		int coveredCount = 2;
		int goldCount = 2;

		// exercise
		PerformanceMeasures measures = new PerformanceMeasures(retrievedCount,
				relevantCount, coveredCount, goldCount);

		// verify
		assertEquals(0.5, measures.getPrecision(), EPSILON);
		assertEquals(1, measures.getRecall(), EPSILON);
		assertEquals(2D / 3D, measures.getFMeasure(), EPSILON);
	}

	@Test(expected = IllegalArgumentException.class)
	public void approxMatchingMeasures_toMuchCovered_expectException()
	{

		// setup
		int retrievedCount = 1;
		int relevantCount = 1;
		// coveredCount may not be greater then goldCount
		int coveredCount = 100;
		int goldCount = 1;

		// exercise
		new PerformanceMeasures(retrievedCount, relevantCount, coveredCount,
				goldCount);
	}

	@Test(expected = IllegalArgumentException.class)
	public void approxMatchingMeasures_toMuchRelevant_expectException()
	{

		// setup
		int retrievedCount = 100;
		// relevant may not be greater then goldCount
		int relevantCount = 100;
		int coveredCount = 1;
		int goldCount = 1;

		// exercise
		new PerformanceMeasures(retrievedCount, relevantCount, coveredCount,
				goldCount);
	}

	@Test(expected = IllegalArgumentException.class)
	public void approxMatchingMeasures_toMuchRelevant2_expectException()
	{

		// setup
		int retrievedCount = 1;
		// relevant may not be greater then retrieved
		int relevantCount = 100;
		int coveredCount = 100;
		int goldCount = 100;

		// exercise
		new PerformanceMeasures(retrievedCount, relevantCount, coveredCount,
				goldCount);
	}

}
