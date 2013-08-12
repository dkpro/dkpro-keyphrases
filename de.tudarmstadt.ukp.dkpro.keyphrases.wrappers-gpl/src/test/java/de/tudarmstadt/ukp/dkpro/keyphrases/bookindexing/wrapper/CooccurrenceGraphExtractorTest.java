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

import static org.junit.Assert.*;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import java.io.File;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Ignore;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.AbstractBookIndexingTestBase;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.TokenCandidateSet;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.segmentation.OneSegmentAnnotator;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.CooccurrenceGraphExtractor;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.TfidfExtractor;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.BookIndexPhraseExtractor_BaseImpl.SegmentationType;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.CooccurrenceGraphExtractor.RankingMode;

/**
 * Test for {@link TfidfExtractor}
 *
 * @author Mateusz Parzonka
 *
 */
public class CooccurrenceGraphExtractorTest
	extends AbstractBookIndexingTestBase
{

	private final static String TEXT_FILE = "src/test/resources/CooccurrenceExtractor/Cooccurrence.txt";

	@Test
	@Ignore
	public void extractTokens()
		throws Exception
	{

		CooccurrenceGraphExtractor extractor = new CooccurrenceGraphExtractor() {

				@Override
				protected AnalysisEngineDescription createSegmenter()
					throws ResourceInitializationException
				{
					return createPrimitiveDescription(OneSegmentAnnotator.class);
				}

		};

		extractor.setCandidateSet(new TokenCandidateSet());
		extractor.setWindowSize(3);
		extractor.setSegmentation(SegmentationType.SEGMENTS, 1);
		extractor.setRankingMode(RankingMode.NodeDegreeWeighted);
		extractor.setMergePhrases(true);
		extractor.setPhraseLengthRange(1, 8);

		// exercise
		List<String> actual = extractor.extract(new File(TEXT_FILE));

		// verify
		List<String> expected = list("foo bar", "quuz");
		assertEquals(expected, actual);
	}


}
