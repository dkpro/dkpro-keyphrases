package de.tudarmstadt.ukp.dkpro.keyphrases.ranking;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createAggregateDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.posfilter.PosFilter;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerPosLemmaTT4J;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerTT4JBase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.CooccurrenceGraphFactory;

public class NodeDegreeRankingTest
{

    @Test
    public final void test()
        throws ResourceInitializationException, AnalysisEngineProcessException
    {

        final String testDocument = "This is a not so long test sentence. This is a longer second "
                + "test sentence. More sentences are necessary for the tests.";
        final AnalysisEngineDescription aggregate = createAggregateDescription(
                createPrimitiveDescription(de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter.class),
                createPrimitiveDescription(TreeTaggerPosLemmaTT4J.class,
                        TreeTaggerTT4JBase.PARAM_LANGUAGE, "en"),
                createPrimitiveDescription(PosFilter.class, PosFilter.PARAM_TYPE_TO_REMOVE,
                        Lemma.class.getName(), PosFilter.PARAM_ADJ, true, PosFilter.PARAM_N, true),
                CooccurrenceGraphFactory.getCooccurrenceGraph_lemma(),
                NodeDegreeRankingFactory.getNodeDegreeRanking_weighted());
        final AnalysisEngine engine = AnalysisEngineFactory.createAggregate(aggregate);
        final JCas aJCas = engine.newJCas();
        aJCas.setDocumentText(testDocument);

        engine.process(aJCas);

        final String[] expectedArray = new String[] { "More", "second", "test", "sentence",
                "necessary", "long" };
        final Set<String> expectedKeyphr = new HashSet<String>();
        for (String string : expectedArray) {
            expectedKeyphr.add(string);
        }
        final Set<String> resultKeyphr = new HashSet<String>();
        final Map<String, Double> map = new HashMap<String, Double>();

        for (Keyphrase keyphrase : JCasUtil.select(aJCas, Keyphrase.class)) {
            resultKeyphr.add(keyphrase.getCoveredText());
            map.put(keyphrase.getCoveredText(), keyphrase.getScore());
        }

        Assert.assertThat(resultKeyphr, CoreMatchers.is(expectedKeyphr));
        final ValueComparator valueComparator = new ValueComparator(map);
        final TreeMap<String, Double> sortedMap = new TreeMap<String, Double>(valueComparator);
        sortedMap.putAll(map);
        final Set<String> orderedSet = sortedMap.keySet();
        final String[] orderedArray = orderedSet.toArray(new String[orderedSet.size()]);
        Assert.assertThat(orderedArray[0], CoreMatchers.is("sentence"));
        Assert.assertThat(orderedArray[1], CoreMatchers.is("test"));

    }

    private class ValueComparator
        implements Comparator<String>
    {

        private final Map<String, Double> base;

        public ValueComparator(final Map<String, Double> base)
        {
            this.base = base;
        }

        @Override
        public int compare(final String first, final String second)
        {
            int result;
            if (base.get(first) >= base.get(second)) {
                result = -1;
            }
            else {
                result = 1;
            }
            return result;
        }
    }

}
