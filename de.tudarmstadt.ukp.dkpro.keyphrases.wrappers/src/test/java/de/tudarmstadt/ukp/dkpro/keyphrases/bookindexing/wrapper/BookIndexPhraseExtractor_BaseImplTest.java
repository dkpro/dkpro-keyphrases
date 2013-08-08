package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper;

import static org.junit.Assert.assertEquals;

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
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.BookIndexPhraseExtractor_BaseImpl;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate.Candidate2KeyphraseConverter;

public class BookIndexPhraseExtractor_BaseImplTest
	extends AbstractBookIndexingTestBase
{

	private final static String TEXT_FILE = "src/test/resources/TfidfExtractor/TfidfExtractorTestFile.txt";

	@Test
	@Ignore
	public void extract()
	{
		// setup
		BookIndexPhraseExtractor_BaseImpl extractor = new BookIndexPhraseExtractor_BaseImplTestable();
		extractor.setCandidateSet(new TokenCandidateSet());

		// exercise
		List<String> actual = extractor.extract(new File(TEXT_FILE));

		// verify
		List<String> expected = list("bar", "quuz", "foo");
		assertEquals(expected, actual);
	}

	/**
	 * Creates one segment and applies no ranking.
	 */
	public class BookIndexPhraseExtractor_BaseImplTestable
		extends BookIndexPhraseExtractor_BaseImpl
	{

		@Override
		protected AnalysisEngineDescription createSegmenter()
			throws ResourceInitializationException
		{
			return createPrimitiveDescription(OneSegmentAnnotator.class);
		}

		@Override
		protected AnalysisEngineDescription createRanker()
			throws ResourceInitializationException
		{
			return createPrimitiveDescription(Candidate2KeyphraseConverter.class);
		}

	}

}
