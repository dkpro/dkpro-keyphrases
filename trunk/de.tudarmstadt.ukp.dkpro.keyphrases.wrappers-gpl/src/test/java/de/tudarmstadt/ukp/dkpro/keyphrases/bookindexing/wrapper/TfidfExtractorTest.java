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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotator.WeightingModeIdf;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotator.WeightingModeTf;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.AbstractBookIndexingTestBase;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.LemmaCandidateSet;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.TokenCandidateSet;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.TfidfExtractor;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.BookIndexPhraseExtractor_BaseImpl.SegmentationType;

/**
 * Test for {@link TfidfExtractor}
 *
 * @author Mateusz Parzonka
 *
 */
public class TfidfExtractorTest
	extends AbstractBookIndexingTestBase
{

	private final static String TEXT_FILE = "src/test/resources/TfidfExtractor/TfidfExtractorTestFile.txt";
	private final static String COLLECTION = "src/test/resources/TfidfExtractor/TfidfExtractorModelCollection";

	@Test
	@Ignore
	public void extractTokens()
		throws Exception
	{

		TfidfExtractor extractor = new TfidfExtractor();

		extractor.setCandidateSet(new TokenCandidateSet());
		extractor.buildTfidfModel(new File(COLLECTION), "txt");
		extractor.setSegmentation(SegmentationType.SEGMENTS, 1);
		extractor.setTfWeightingMode(WeightingModeTf.NORMAL);
		extractor.setIdfWeightingMode(WeightingModeIdf.NORMAL);

		// exercise
		List<String> actual = extractor.extract(new File(TEXT_FILE));

		// verify
		List<String> expected = list("foo", "quuz", "bar");
		assertEquals(expected, actual);
	}

	@Test
	@Ignore
	public void extractLemmas_mergePhrases()
		throws Exception
	{

		// setup
		TfidfExtractor extractor = new TfidfExtractor();

		extractor.setCandidateSet(new LemmaCandidateSet());
		extractor.buildTfidfModel(new File(COLLECTION), "txt");
		extractor.setSegmentation(SegmentationType.SEGMENTS, 1);
		extractor.setTfWeightingMode(WeightingModeTf.NORMAL);
		extractor.setIdfWeightingMode(WeightingModeIdf.NORMAL);
		extractor.setMergePhrases(true);
		extractor.setPhraseLengthRange(2, 3);

		// exercise
		List<String> actual = extractor.extract(new File(TEXT_FILE));

		// verify
		List<String> expected = list("bar foo", "quuz foo", "bar quuz");
		assertEquals(expected, actual);
	}


}
