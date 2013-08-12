package de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.bindResource;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasBuilder;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.frequency.resources.Web1TFrequencyCountResource;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class TfBackgroundIdfRankingTest
{

    final private static String EXAMPLE_STRING = "example";
    final private static String SECOND_STRING = "second";

    @Test
    public void test()
        throws InvalidXMLException, ResourceInitializationException, AnalysisEngineProcessException
    {

        final AnalysisEngineDescription ranker = createPrimitiveDescription(TfBackgroundIdfRanking.class);
        bindResource(ranker, TfBackgroundIdfRanking.FREQUENCY_COUNT_RESOURCE,
                Web1TFrequencyCountResource.class,
                Web1TFrequencyCountResource.PARAM_MIN_NGRAM_LEVEL, "1",
                Web1TFrequencyCountResource.PARAM_MAX_NGRAM_LEVEL, "3",
                Web1TFrequencyCountResource.PARAM_INDEX_PATH, "src/test/resources/jweb1t");

        final AnalysisEngine rankerAE = AnalysisEngineFactory.createAggregate(ranker);

        final JCas jcas = rankerAE.newJCas();
        final JCasBuilder jcasBuilder = new JCasBuilder(jcas);

        final Keyphrase keyphrase1 = jcasBuilder.add(EXAMPLE_STRING, Keyphrase.class);
        keyphrase1.setKeyphrase(EXAMPLE_STRING);
        jcasBuilder.add(" sentence funny. ");

        final Keyphrase keyphrase2 = jcasBuilder.add(SECOND_STRING, Keyphrase.class);
        keyphrase2.setKeyphrase(SECOND_STRING);

        jcasBuilder.add(" example.");

        final Keyphrase keyphrase3 = jcasBuilder.add("iaejgi", Keyphrase.class);

        jcasBuilder.close();

        Assert.assertThat(keyphrase1.getCoveredText(), CoreMatchers.is(EXAMPLE_STRING));
        Assert.assertThat(keyphrase2.getCoveredText(), CoreMatchers.is(SECOND_STRING));
        Assert.assertThat(keyphrase3.getCoveredText(), CoreMatchers.is("iaejgi"));

        rankerAE.process(jcas);

        for (Keyphrase keyphrase : JCasUtil.select(jcas, Keyphrase.class)) {
            if (keyphrase.getCoveredText().equals(EXAMPLE_STRING)) {
                Assert.assertThat(keyphrase.getScore(), CoreMatchers.is(2.0 / Math.log(2.0)));
            }
            else if (keyphrase.getCoveredText().equals(SECOND_STRING)) {
                Assert.assertThat(keyphrase.getScore(), CoreMatchers.is(1.0 / Math.log(4.0)));
            }
            else if (keyphrase.getCoveredText().equals("iaejgi")) {
                Assert.assertThat(keyphrase.getScore(), CoreMatchers.is(0.0));
            }
        }

    }

}
