package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.aggregator;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitive;
import static org.apache.uima.fit.factory.ExternalResourceFactory.bindResource;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.AbstractBookIndexingTest;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.aggregation.BookIndexPhraseAggregationAnnotator;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.aggregation.RankedPhraseAggregationAnnotator;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.aggregation.aggregationstrategy.AggregationStrategy;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.aggregation.aggregationstrategy.MaximumAggregation;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.BookIndexPhrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.Segment;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * Test for {@link BookIndexPhraseAggregationAnnotator}.
 *
 * @author Mateusz Parzonka
 *
 */
public class BookIndexPhraseAggregatorTest
	extends AbstractBookIndexingTest
{

	/**
	 * Sanity test using a single segment.
	 *
	 * @throws Exception
	 */
//	@Test
	public void allAggregationStrategies_1segment_expectCorrectRanking()
		throws Exception
	{
		String testDocument = "foo bar baz";

		List<Class<? extends AggregationStrategy>> aggregationStrategies = new ArrayList<Class<? extends AggregationStrategy>>();
		aggregationStrategies.add(MaximumAggregation.class);

		for (Class<? extends AggregationStrategy> aggregationStrategy : aggregationStrategies) {

			AnalysisEngineDescription aed = AnalysisEngineFactory
					.createPrimitiveDescription(BookIndexPhraseAggregationAnnotator.class);

			bindResource(aed, RankedPhraseAggregationAnnotator.AGGREGATION_STRATEGY,
					aggregationStrategy);

			AnalysisEngine ae = createPrimitive(aed);
			JCas jcas = setup_1segment(testDocument, ae);

			ae.process(jcas);

			List<String> expectedBookIndexPhrases = new ArrayList<String>();
			expectedBookIndexPhrases.add("bar");
			expectedBookIndexPhrases.add("foo");
			expectedBookIndexPhrases.add("baz");

			List<String> resultBookIndexPhrases = new ArrayList<String>();
			for (BookIndexPhrase b : JCasUtil.select(jcas, BookIndexPhrase.class)) {
				resultBookIndexPhrases.add(b.getPhrase());
			}

			assertEquals(expectedBookIndexPhrases, resultBookIndexPhrases);
		}
	}

	private JCas setup_1segment(String testDocument, AnalysisEngine ae)
		throws ResourceInitializationException
	{
		JCas jcas = ae.newJCas();
		jcas.setDocumentText(testDocument);

		Segment s = new Segment(jcas, 0, 10);
		s.addToIndexes();

		Keyphrase k1 = new Keyphrase(jcas, 0, 2);
		k1.setKeyphrase("foo");
		k1.setScore(0.5);
		k1.addToIndexes();

		Keyphrase k2 = new Keyphrase(jcas, 4, 6);
		k2.setKeyphrase("bar");
		k2.setScore(1);
		k2.addToIndexes();

		Keyphrase k3 = new Keyphrase(jcas, 8, 10);
		k3.setKeyphrase("baz");
		k3.setScore(0);
		k3.addToIndexes();

		return jcas;
	}
}
