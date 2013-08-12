package de.tudarmstadt.ukp.dkpro.keyphrases.decompounding.uima.annotator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasBuilder;
import org.apache.uima.fit.util.FSCollectionFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Compound;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Compound.CompoundSplitLevel;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Split;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class CompoundPartTokenizerTest
{

	private JCas getJCas(AnalysisEngine analysisEngine) throws ResourceInitializationException{
        final JCas jcas = analysisEngine.newJCas();
        final JCasBuilder jcasBuilder = new JCasBuilder(jcas);
        final int beginPosition = jcasBuilder.getPosition();
        final Split getrank = jcasBuilder.add("getränk", Split.class);
        final int secondPosition = jcasBuilder.getPosition();
        final Split auto = jcasBuilder.add("auto", Split.class);
        final Split mat = jcasBuilder.add("mat", Split.class);
        final Split automat = new Split(jcas, secondPosition, jcasBuilder.getPosition());
        final List<Split> splits = new ArrayList<Split>();
        splits.add(auto);
        splits.add(mat);
        automat.setSplits(FSCollectionFactory.createFSArray(jcas, splits));
        automat.addToIndexes();
        final Compound compound = new Compound(jcas, beginPosition, jcasBuilder.getPosition());
        splits.clear();
        splits.add(getrank);
        splits.add(automat);
        compound.setSplits(FSCollectionFactory.createFSArray(jcas, splits));
        compound.addToIndexes();
        jcasBuilder.close();

        return jcas;


	}

    @Test
    public void testLowest()
        throws ResourceInitializationException, AnalysisEngineProcessException
    {

        final AnalysisEngine analysisEngine = AnalysisEngineFactory
                .createPrimitive(
                		CompoundPartTokenizer.class,
                		CompoundPartTokenizer.PARAM_COMPOUND_SPLIT_LEVEL, CompoundSplitLevel.LOWEST);

        JCas jcas = getJCas(analysisEngine);

        analysisEngine.process(jcas);

        final String[] splitsList = new String[] { "getränk", "auto", "mat" };
        final String[] tokensList = new String[3];
        int index = 0;
        for (Token token : JCasUtil.select(jcas, Token.class)) {
            tokensList[index] = token.getCoveredText();
            ++index;
        }
        assertThat(tokensList, is(splitsList));
    }

    @Test
    public void testNone()
        throws ResourceInitializationException, AnalysisEngineProcessException
    {

        final AnalysisEngine analysisEngine = AnalysisEngineFactory
                .createPrimitive(
                		CompoundPartTokenizer.class,
                		CompoundPartTokenizer.PARAM_COMPOUND_SPLIT_LEVEL, CompoundSplitLevel.NONE);

        JCas jcas = getJCas(analysisEngine);

        analysisEngine.process(jcas);

        final String[] splitsList = new String[] {  };
        final String[] tokensList = new String[0];
        int index = 0;
        for (Token token : JCasUtil.select(jcas, Token.class)) {
            tokensList[index] = token.getCoveredText();
            ++index;
        }
        assertThat(tokensList, is(splitsList));
    }

    @Test
    public void testHighest()
        throws ResourceInitializationException, AnalysisEngineProcessException
    {

        final AnalysisEngine analysisEngine = AnalysisEngineFactory
                .createPrimitive(
                		CompoundPartTokenizer.class,
                		CompoundPartTokenizer.PARAM_COMPOUND_SPLIT_LEVEL, CompoundSplitLevel.HIGHEST);

        JCas jcas = getJCas(analysisEngine);

        analysisEngine.process(jcas);

        final String[] splitsList = new String[] { "getränk", "automat" };
        final String[] tokensList = new String[2];
        int index = 0;
        for (Token token : JCasUtil.select(jcas, Token.class)) {
            tokensList[index] = token.getCoveredText();
            ++index;
        }
        assertThat(tokensList, is(splitsList));
    }

    @Test
    public void testAll()
        throws ResourceInitializationException, AnalysisEngineProcessException
    {

        final AnalysisEngine analysisEngine = AnalysisEngineFactory
                .createPrimitive(
                		CompoundPartTokenizer.class,
                		CompoundPartTokenizer.PARAM_COMPOUND_SPLIT_LEVEL, CompoundSplitLevel.ALL);

        JCas jcas = getJCas(analysisEngine);

        analysisEngine.process(jcas);

        final String[] splitsList = new String[] { "getränk", "automat", "auto", "mat" };
        final String[] tokensList = new String[4];
        int index = 0;
        for (Token token : JCasUtil.select(jcas, Token.class)) {
            tokensList[index] = token.getCoveredText();
            ++index;
        }
        assertThat(tokensList, is(splitsList));
    }

   @Test
    public void testParameters()
            throws ResourceInitializationException, AnalysisEngineProcessException
        {
        AnalysisEngine analysisEngine = AnalysisEngineFactory.createPrimitive(
                		CompoundPartTokenizer.class,
                		CompoundPartTokenizer.PARAM_COMPOUND_SPLIT_LEVEL, CompoundSplitLevel.ALL);
        analysisEngine.process(analysisEngine.newJCas());


        }


}
