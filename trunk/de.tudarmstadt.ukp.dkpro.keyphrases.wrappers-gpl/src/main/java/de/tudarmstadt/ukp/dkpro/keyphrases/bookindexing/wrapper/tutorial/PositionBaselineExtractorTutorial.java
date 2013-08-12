package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.tutorial;

import java.io.File;
import java.util.List;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.TokenCandidateSet;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.base.CandidateSet.PosType;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.PositionBaselineExtractor;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.BookIndexPhraseExtractor_BaseImpl.SegmentationType;

public class PositionBaselineExtractorTutorial
{

	public final static File BOOK_FILE = new File(
	// "src/main/resources/books/fulltext/Bramble-Bees and Others.txt");
			"src/main/resources/books/excerpts/excerpt1.txt");

	public static void main(String... args)
	{

		PositionBaselineExtractor extractor = new PositionBaselineExtractor();
		TokenCandidateSet candidate = new TokenCandidateSet();
		candidate.setPosToKeep(PosType.N);
		extractor.setCandidateSet(candidate);

		extractor.setSegmentation(SegmentationType.TOKENS, 300);
		extractor.setConvertToLowercase(true);

		System.out.println(extractor.toString());

		List<String> bookIndexPhrases = extractor.extract(BOOK_FILE);

		int nrOfTopBookIndexPhrases = 10;
		for (String string : bookIndexPhrases.subList(0, nrOfTopBookIndexPhrases)) {
			System.out.println(string);
		}

	}


}
