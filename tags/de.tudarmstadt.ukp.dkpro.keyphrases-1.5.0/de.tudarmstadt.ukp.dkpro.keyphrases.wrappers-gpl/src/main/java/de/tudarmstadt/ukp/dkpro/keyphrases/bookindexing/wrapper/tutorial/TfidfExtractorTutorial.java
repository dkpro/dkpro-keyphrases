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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.tutorial;

import java.io.File;
import java.util.List;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.NChunkCandidateSet;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.TfidfExtractor;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.BookIndexPhraseExtractor_BaseImpl.SegmentationType;

/**
 * Tutorial for {@link TfidfExtractor}
 *
 * @author Mateusz Parzonka
 * 
 */
public class TfidfExtractorTutorial
{

	public final static File TFIDF_MODEL_FILE = new File(
			"src/main/resources/books/dfmodels/tokens.dfmodel");

	public final static File BOOK_FILE = new File(
			"src/main/resources/books/excerpts/excerpt1.txt");

	public static void main(String... args)
	{

		TfidfExtractor extractor = new TfidfExtractor();
		extractor.setCandidateSet(new NChunkCandidateSet());

		extractor.setTfidfModelFile(TFIDF_MODEL_FILE);
		extractor.setSegmentation(SegmentationType.TOKENS, 300);
		extractor.setConvertToLowercase(true);

		// models can also be build using this command:
		// tfidfExtractor.buildTfidfModel(inputDir, suffix);

		System.out.println(extractor.toString());

		List<String> bookIndexPhrases = extractor.extract(BOOK_FILE);

		int nrOfTopBookIndexPhrases = 10;
		for (String string : bookIndexPhrases.subList(0, nrOfTopBookIndexPhrases)) {
			System.out.println(string);
		}

	}


}
