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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.AbstractBookIndexingTest;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures.AveragedPerformanceMeasures;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures.PerformanceMeasures;

/**
 * Test of {@link AveragedPerformanceMeasures}
 * 
 * @author Mateusz Parzonka
 * 
 */
public class AveragedPerformanceMeasuresTest
	extends AbstractBookIndexingTest
{

	@Test
	public void calculateMeasuresA()
	{
		// setup
		List<PerformanceMeasures> listOfPerformanceMeasures = new ArrayList<PerformanceMeasures>();
		listOfPerformanceMeasures.add(new PerformanceMeasures(1, 1, 1, 1));
		listOfPerformanceMeasures.add(new PerformanceMeasures(1, 1, 1, 1));

		// exercise
		AveragedPerformanceMeasures measures = new AveragedPerformanceMeasures(
				listOfPerformanceMeasures);

		// verify
		assertEquals(1, measures.getMacroPrecision(), EPSILON);
		assertEquals(1, measures.getMacroRecall(), EPSILON);
		assertEquals(1, measures.getMacroFMeasure(), EPSILON);

		assertEquals(1, measures.getMacroPrecision(), EPSILON);
		assertEquals(1, measures.getMacroRecall(), EPSILON);
		assertEquals(1, measures.getMacroFMeasure(), EPSILON);
	}

	@Test
	public void calculateMeasuresB()
	{
		// setup
		List<PerformanceMeasures> listOfPerformanceMeasures = new ArrayList<PerformanceMeasures>();
		listOfPerformanceMeasures.add(new PerformanceMeasures(1, 1, 1, 1));
		listOfPerformanceMeasures.add(new PerformanceMeasures(1, 1, 1, 1));

		// exercise
		AveragedPerformanceMeasures measures = new AveragedPerformanceMeasures(
				listOfPerformanceMeasures);

		// verify
		assertEquals(1, measures.getMacroPrecision(), EPSILON);
		assertEquals(1, measures.getMacroRecall(), EPSILON);
		assertEquals(1, measures.getMacroFMeasure(), EPSILON);

		assertEquals(1, measures.getMacroPrecision(), EPSILON);
		assertEquals(1, measures.getMacroRecall(), EPSILON);
		assertEquals(1, measures.getMacroFMeasure(), EPSILON);
	}

}
