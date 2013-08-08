package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.ranking;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createAggregateDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.bindResource;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.frequency.FrequencyCountResourceBase;
import de.tudarmstadt.ukp.dkpro.core.api.io.ResourceCollectionReaderBase;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.AbstractBookIndexingTest;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * Test for {@link LoglikelihoodAnnotator}
 *
 * @author Mateusz Parzonka
 *
 */
public class LoglikelihoodAnnotatorTest
	extends AbstractBookIndexingTest

{

	/**
	 * The text-file:
	 * "threetimes onetime threetimes twotimes threetimes twotimes fourtimes fourtimes fourtimes fourtimes"
	 */
	final static String COLLECTION_PATH = "src/test/resources/LoglikelihoodAnnotator";
	final static String COLLECTION_SUFFIX = "*.txt";
	final static double EPSILON = 0.05D;

	static Map<String, Double> expectedScores;

	@Test
	public void processCollection()
		throws ResourceInitializationException, InvalidXMLException, UIMAException,
		IOException
	{

		// setup
		AnalysisEngineDescription breakIteratorSegmenter = createPrimitiveDescription(BreakIteratorSegmenter.class);

		AnalysisEngineDescription logLikelihoodAnnotator = createPrimitiveDescription(
				LoglikelihoodAnnotator_Testable.class,
				LoglikelihoodAnnotator.PARAM_FEATURE_PATH, Token.class.getName());

		AnalysisEngineDescription aed = createAggregateDescription(
				breakIteratorSegmenter, logLikelihoodAnnotator);

		bindResource(aed, LoglikelihoodAnnotator.FREQUENCY_COUNT_PROVIDER_KEY,
				FrequencyCountProviderMock.class);

		// setup verifier
		expectedScores = new HashMap<String, Double>();
		expectedScores.put("onetime", 2.73D);
		expectedScores.put("twotimes", 7.85D);
		expectedScores.put("threetimes", 13.84D);
		expectedScores.put("fourtimes", 20.37D);

		// exercise
		SimplePipeline.runPipeline(createReader(), aed);

	}

	/**
	 * Extends the unit under test with a verifier.
	 */
	public static class LoglikelihoodAnnotator_Testable
		extends LoglikelihoodAnnotator
	{

		@Override
		public void process(JCas jcas)
			throws AnalysisEngineProcessException
		{
			// exercise
			super.process(jcas);

			// verify
			final Collection<Keyphrase> actual = JCasUtil.select(jcas,
					Keyphrase.class);
			assertEquals(10, actual.size());
			for (Keyphrase keyphrase : actual) {
				final String phrase = keyphrase.getKeyphrase();
				final Double expectedScore = expectedScores.get(phrase);
				assertEquals("[" + phrase + "] must have expected score.",
						expectedScore, keyphrase.getScore(), EPSILON);
			}
		}

	}

	/**
	 * Reads a collection of textfiles.
	 *
	 * @return
	 * @throws ResourceInitializationException
	 */
	public CollectionReaderDescription createReader()
		throws ResourceInitializationException
	{
		return createDescription(TextReader.class,
				ResourceCollectionReaderBase.PARAM_PATH, new File(COLLECTION_PATH)
						.getAbsolutePath(), ResourceCollectionReaderBase.PARAM_PATTERNS,
				new String[] { ResourceCollectionReaderBase.INCLUDE_PREFIX
						+ COLLECTION_SUFFIX });
	}

	/**
	 * The background-corpus. Is configured to return a background-corpus
	 * frequency of 10 for each term and a background-corpus size of 1000.
	 */
	public static final class FrequencyCountProviderMock
		extends FrequencyCountResourceBase
	{

		@Override
		public long getFrequency(String phrase)
			throws Exception
		{
			return 10;
		}

		@Override
		public long getNrOfTokens()
			throws Exception
		{
			return 1000;
		}

	}

}
