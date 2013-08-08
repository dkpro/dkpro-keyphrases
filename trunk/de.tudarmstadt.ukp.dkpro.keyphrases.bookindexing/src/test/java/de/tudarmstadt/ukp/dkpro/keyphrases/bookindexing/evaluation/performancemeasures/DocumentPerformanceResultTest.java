package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.AbstractPhraseMatcherTest;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures.DocumentPerformanceResult;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures.PerformanceMeasures;

/**
 * Test of {@link DocumentPerformanceResult}
 *
 * @author Mateusz Parzonka
 *
 */
public class DocumentPerformanceResultTest
	extends AbstractPhraseMatcherTest
{

	@Test
	public void getPerfomanceMeasuresByCountA_for1RetrievedItem()
	{
		// setup
		DocumentPerformanceResult result = new DocumentPerformanceResult("d1",
				list(1, 0), 1);

		// exercise
		PerformanceMeasures measuresFor1RetrievedItem = result
				.getPerformanceMeasures(1);
		System.out.println(measuresFor1RetrievedItem);

		// verify
		assertEquals(1, measuresFor1RetrievedItem.getPrecision(), EPSILON);
		assertEquals(1, measuresFor1RetrievedItem.getRecall(), EPSILON);
		assertEquals(1, measuresFor1RetrievedItem.getFMeasure(), EPSILON);
	}

	@Test
	public void getPerfomanceMeasuresByCountB_for2RetrievedItems()
	{
		// setup
		DocumentPerformanceResult result = new DocumentPerformanceResult("d1",
				list(1, 1), 2);

		// exercise
		PerformanceMeasures measuresFor1RetrievedItem = result
				.getPerformanceMeasures(1);
		PerformanceMeasures measuresFor2RetrievedItems = result
				.getPerformanceMeasures(2);

		// verify
		assertEquals(1, measuresFor1RetrievedItem.getPrecision(), EPSILON);
		assertEquals(0.5, measuresFor1RetrievedItem.getRecall(), EPSILON);
		assertEquals(2D / 3D, measuresFor1RetrievedItem.getFMeasure(), EPSILON);

		assertEquals(1, measuresFor2RetrievedItems.getPrecision(), EPSILON);
		assertEquals(1, measuresFor2RetrievedItems.getRecall(), EPSILON);
		assertEquals(1, measuresFor2RetrievedItems.getFMeasure(), EPSILON);
	}

	@Test
	public void getPerfomanceMeasuresByPercentageB_for2RetrievedItems()
	{
		// setup
		String documentTitle = "someDocument";
		List<Integer> truePositives = new ArrayList<Integer>();
		truePositives.add(1);
		truePositives.add(1);
		int goldCount = 2;

		// exercise
		DocumentPerformanceResult result = new DocumentPerformanceResult(
				documentTitle, truePositives, goldCount);
		// same test as getPerfomanceMeasuresByCountB_for2RetrievedItems except we
		// use the getter with percentages
		PerformanceMeasures measuresFor1RetrievedItem = result
				.getPerformanceMeasures(0.5D);
		PerformanceMeasures measuresFor2RetrievedItems = result
				.getPerformanceMeasures(1D);

		// verify
		assertEquals(1, measuresFor1RetrievedItem.getPrecision(), EPSILON);
		assertEquals(0.5, measuresFor1RetrievedItem.getRecall(), EPSILON);
		assertEquals(2D / 3D, measuresFor1RetrievedItem.getFMeasure(), EPSILON);

		assertEquals(1, measuresFor2RetrievedItems.getPrecision(), EPSILON);
		assertEquals(1, measuresFor2RetrievedItems.getRecall(), EPSILON);
		assertEquals(1, measuresFor2RetrievedItems.getFMeasure(), EPSILON);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getPerfomanceMeasuresByCount_toManyTruePositives_expectException()
	{
		// setup
		String documentTitle = "someDocument";
		List<Integer> truePositives = new ArrayList<Integer>();
		truePositives.add(1);
		truePositives.add(1);
		int goldCount = 1;

		// exercise
		DocumentPerformanceResult result = new DocumentPerformanceResult(
				documentTitle, truePositives, goldCount);
		result.getPerformanceMeasures(1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getPerfomanceMeasuresByCount_toManyTruePositives2_expectException()
	{
		// setup
		String documentTitle = "someDocument";
		List<Integer> truePositives = new ArrayList<Integer>();
		truePositives.add(2);
		int goldCount = 1;

		// exercise
		DocumentPerformanceResult result = new DocumentPerformanceResult(
				documentTitle, truePositives, goldCount);
		result.getPerformanceMeasures(1);
	}

}
