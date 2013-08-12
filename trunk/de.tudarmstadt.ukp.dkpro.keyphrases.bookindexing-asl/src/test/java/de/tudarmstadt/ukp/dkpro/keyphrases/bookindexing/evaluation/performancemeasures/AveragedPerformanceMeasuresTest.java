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
