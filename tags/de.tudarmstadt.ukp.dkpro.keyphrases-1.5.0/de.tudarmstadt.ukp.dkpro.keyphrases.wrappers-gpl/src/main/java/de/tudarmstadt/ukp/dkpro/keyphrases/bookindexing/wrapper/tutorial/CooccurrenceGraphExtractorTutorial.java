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

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.TokenCandidateSet;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.base.CandidateSet.PosType;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.CooccurrenceGraphExtractor;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.BookIndexPhraseExtractor_BaseImpl.SegmentationType;

public class CooccurrenceGraphExtractorTutorial
{

	public final static File BOOK_FILE = new File(
	// "src/main/resources/books/fulltext/Bramble-Bees and Others.txt");
			"src/main/resources/books/excerpts/excerpt1.txt");

	public static void main(String... args)
	{

		CooccurrenceGraphExtractor extractor = new CooccurrenceGraphExtractor();
		TokenCandidateSet candidate = new TokenCandidateSet();
		candidate.setPosToKeep(PosType.N, PosType.ADJ);
		extractor.setCandidateSet(candidate);

		extractor.setSegmentation(SegmentationType.TOKENS, 300);
		extractor.setConvertToLowercase(true);
		extractor.setMergePhrases(true);

		System.out.println(extractor.toString());

		List<String> bookIndexPhrases = extractor.extract(BOOK_FILE);

		int nrOfTopBookIndexPhrases = 10;
		for (String string : bookIndexPhrases.subList(0, nrOfTopBookIndexPhrases)) {
			System.out.println(string);
		}

	}


}
