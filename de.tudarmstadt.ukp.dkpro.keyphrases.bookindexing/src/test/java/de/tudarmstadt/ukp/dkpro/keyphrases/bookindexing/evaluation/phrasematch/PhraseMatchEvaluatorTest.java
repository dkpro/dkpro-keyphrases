package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.phrasematch;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createAggregateDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.bindResource;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.io.ResourceCollectionReaderBase;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.AbstractPhraseMatcherTest;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.matchingstrategy.MatchingStrategy;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures.CollectionPerformanceResult;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures.DocumentPerformanceResult;

/**
 * Test for {@link PhraseMatchEvaluator}
 *
 * @author Mateusz Parzonka
 *
 */
public class PhraseMatchEvaluatorTest
	extends AbstractPhraseMatcherTest
{

	final static Class<Token> CLASS = Token.class;
	final static String COLLECTION_PATH = "src/test/resources/PhraseMatchEvaluator/";
	final static String COLLECTION_SUFFIX = "*.txt";
	final static String GOLD_PATH = "src/test/resources/PhraseMatchEvaluator/gold";
	final static String GOLD_SUFFIX = ".gold";

	final static String COLLECTION_PATH_WITH_SPACES = "src/test/resources/PhraseMatchEvaluator/path with spaces/";
	final static String GOLD_PATH_WITH_SPACES = "src/test/resources/PhraseMatchEvaluator/path with spaces/gold";

	/**
	 * tokens 1.txt = foo bar bar / tokens 1.gold = foo bar<br>
	 * tokens 2.txt = foo bar baz / tokens 2.gold = foo foos quuz
	 */
	private static CollectionPerformanceResult actual;

	@Test
	public void evaluateTwoDocumentsContainingTokens()
		throws ResourceInitializationException, InvalidXMLException, UIMAException,
		IOException
	{

		// setup
		AnalysisEngineDescription breakIteratorSegmenter = createPrimitiveDescription(BreakIteratorSegmenter.class);

		AnalysisEngineDescription phraseMatchEvaluator = createPrimitiveDescription(
				PhraseMatchEvaluator_Testable.class,
				PhraseMatchEvaluator_Testable.PARAM_GOLD_PATH, GOLD_PATH);
		bindResource(phraseMatchEvaluator, PhraseMatchEvaluator.RESOURCE_MATCHING_STRATEGY,
				MatchingStrategyMock.class);

		AnalysisEngineDescription aed = createAggregateDescription(
				breakIteratorSegmenter, phraseMatchEvaluator);

		// exercise
		SimplePipeline.runPipeline(createReader(COLLECTION_PATH), aed);

		// verify
		DocumentPerformanceResult dr1 = new DocumentPerformanceResult("tokens 1",
				list(1, 1, 0), 2);
		DocumentPerformanceResult dr2 = new DocumentPerformanceResult("tokens 2",
				list(2, 0, 0), 3);
		CollectionPerformanceResult expected = new CollectionPerformanceResult(
				list(dr1, dr2));
		assertEquals(expected, actual);

	}

	@Test
	public void evaluateTwoDocumentsContainingTokens_pathWithSpaces()
		throws ResourceInitializationException, InvalidXMLException, UIMAException,
		IOException
	{

		// setup
		AnalysisEngineDescription breakIteratorSegmenter = createPrimitiveDescription(BreakIteratorSegmenter.class);

		AnalysisEngineDescription phraseMatchEvaluator = createPrimitiveDescription(
				PhraseMatchEvaluator_Testable.class,
				PhraseMatchEvaluator_Testable.PARAM_GOLD_PATH, GOLD_PATH_WITH_SPACES);
		bindResource(phraseMatchEvaluator, PhraseMatchEvaluator.RESOURCE_MATCHING_STRATEGY,
				MatchingStrategyMock.class);

		AnalysisEngineDescription aed = createAggregateDescription(
				breakIteratorSegmenter, phraseMatchEvaluator);

		// exercise
		SimplePipeline.runPipeline(createReader(COLLECTION_PATH_WITH_SPACES), aed);

		// verify
		DocumentPerformanceResult dr1 = new DocumentPerformanceResult("tokens 1",
				list(1, 1, 0), 2);
		DocumentPerformanceResult dr2 = new DocumentPerformanceResult("tokens 2",
				list(2, 0, 0), 3);
		CollectionPerformanceResult expected = new CollectionPerformanceResult(
				list(dr1, dr2));
		assertEquals(expected, actual);

	}

	public CollectionReaderDescription createReader(String collectionPath)
		throws ResourceInitializationException
	{
		return createDescription(TextReader.class,
				ResourceCollectionReaderBase.PARAM_PATH, new File(collectionPath)
						.getAbsolutePath(), ResourceCollectionReaderBase.PARAM_PATTERNS,
				new String[] { ResourceCollectionReaderBase.INCLUDE_PREFIX
						+ COLLECTION_SUFFIX });
	}

	/**
	 * Behaves like the exact matching strategy with addition plural-recognition
	 * for "foo".
	 *
	 * @author Mateusz Parzonka
	 *
	 */
	public static class MatchingStrategyMock
		extends MatchingStrategy
	{

		@Override
		public boolean isMatch(String phrase, String goldPhrase)
		{
			if (phrase.equals("foo") && goldPhrase.equals("foos")) {
                return true;
            }
            else {
                return phrase.equals(goldPhrase);
            }
		}

	}

	/**
	 * By overwriting the handle result method, we can verify the result of the
	 * PhraseMatchEvaluator.
	 *
	 * @author Mateusz Parzonka
	 *
	 */
	public static class PhraseMatchEvaluator_Testable
		extends PhraseMatchEvaluator
	{

		public static final String PARAM_GOLD_PATH = "GoldPath";
		@ConfigurationParameter(name = PARAM_GOLD_PATH, mandatory = true, defaultValue = "")
		private String goldPath;

		@Override
		protected void handleCollectionResult(CollectionPerformanceResult collectionPerformanceResult)
			throws AnalysisEngineProcessException
		{
			actual = collectionPerformanceResult;
		}

		@Override
		protected List<String> getRetrievedPhrases(JCas jcas)
		{
			List<String> phrases = new ArrayList<String>();
			for (Token token : JCasUtil.select(jcas, Token.class)) {
				phrases.add(token.getCoveredText());
			}
			return phrases;
			}

		@Override
		protected Set<String> getGoldSet(JCas jcas)
			throws AnalysisEngineProcessException
		{
			return new TokenReader(goldPath, GOLD_SUFFIX, "\n", true).getSetOfStrings(jcas);
		}
	}
}

