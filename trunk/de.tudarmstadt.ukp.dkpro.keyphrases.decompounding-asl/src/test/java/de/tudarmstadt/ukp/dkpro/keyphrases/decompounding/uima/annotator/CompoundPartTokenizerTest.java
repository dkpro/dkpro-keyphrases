/*******************************************************************************
 * Copyright 2013
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.tudarmstadt.ukp.dkpro.keyphrases.decompounding.uima.annotator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasBuilder;
import org.apache.uima.fit.util.FSCollectionFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Assert;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Compound;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Compound.CompoundSplitLevel;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Split;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class CompoundPartTokenizerTest
{

    private static final String GETRANKAUTOMAT = "getränkautomat";
    private static final String GETRANK = "getränk";
    private static final Logger LOGGER = Logger.getLogger(CompoundPartTokenizerTest.class.getName());

    private JCas getJCas(final AnalysisEngine analysisEngine)
        throws ResourceInitializationException
    {
        final JCas jcas = analysisEngine.newJCas();
        final JCasBuilder jcasBuilder = new JCasBuilder(jcas);
        final int beginPosition = jcasBuilder.getPosition();
        final Split getrank = jcasBuilder.add(GETRANK, Split.class);
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
        final POS pos = new POS(jcas, beginPosition, jcasBuilder.getPosition());
        pos.setPosValue("NN");
        pos.addToIndexes();
        final Token token = new Token(jcas, beginPosition, jcasBuilder.getPosition());
        token.setPos(pos);
        token.addToIndexes();
        jcasBuilder.close();

        return jcas;

    }

    @Test
    public void testLowest()
        throws ResourceInitializationException, AnalysisEngineProcessException
    {

        final AnalysisEngine analysisEngine = AnalysisEngineFactory.createEngine(
                CompoundPartTokenizer.class, CompoundPartTokenizer.PARAM_COMPOUND_SPLIT_LEVEL,
                CompoundSplitLevel.LOWEST);

        final JCas jcas = getJCas(analysisEngine);

        analysisEngine.process(jcas);

        final String[] splitsList = new String[] { GETRANKAUTOMAT, GETRANK, "auto", "mat" };
        final String[] tokensList = new String[4];
        final String[] posList = new String[] { "NN", "NN", "NN", "NN" };
        final String[] obtainedPosList = new String[4];
        int index = 0;
        for (Token token : JCasUtil.select(jcas, Token.class)) {
            tokensList[index] = token.getCoveredText();
            obtainedPosList[index] = token.getPos().getPosValue();
            ++index;
        }
        assertThat(tokensList, is(splitsList));
        assertThat(obtainedPosList, is(posList));
    }

    @Test
    public void testNone()
        throws ResourceInitializationException, AnalysisEngineProcessException
    {

        final AnalysisEngine analysisEngine = AnalysisEngineFactory.createEngine(
                CompoundPartTokenizer.class, CompoundPartTokenizer.PARAM_COMPOUND_SPLIT_LEVEL,
                CompoundSplitLevel.NONE);

        final JCas jcas = getJCas(analysisEngine);

        analysisEngine.process(jcas);

        final String[] splitsList = new String[] { GETRANKAUTOMAT };
        final String[] tokensList = new String[1];
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

        final AnalysisEngine analysisEngine = AnalysisEngineFactory.createEngine(
                CompoundPartTokenizer.class, CompoundPartTokenizer.PARAM_COMPOUND_SPLIT_LEVEL,
                CompoundSplitLevel.HIGHEST);

        final JCas jcas = getJCas(analysisEngine);

        analysisEngine.process(jcas);

        final String[] splitsList = new String[] { GETRANKAUTOMAT, GETRANK, "automat" };
        final String[] tokensList = new String[3];
        final String[] posList = new String[] { "NN", "NN", "NN" };
        final String[] obtainedPosList = new String[3];
        int index = 0;
        for (Token token : JCasUtil.select(jcas, Token.class)) {
            tokensList[index] = token.getCoveredText();
            obtainedPosList[index] = token.getPos().getPosValue();
            ++index;
        }
        assertThat(tokensList, is(splitsList));
        assertThat(obtainedPosList, is(posList));
    }

    @Test
    public void testAll()
        throws ResourceInitializationException, AnalysisEngineProcessException
    {

        final AnalysisEngine analysisEngine = AnalysisEngineFactory.createEngine(
                CompoundPartTokenizer.class, CompoundPartTokenizer.PARAM_COMPOUND_SPLIT_LEVEL,
                CompoundSplitLevel.ALL);

        final JCas jcas = getJCas(analysisEngine);

        analysisEngine.process(jcas);

        final String[] splitsList = new String[] { GETRANKAUTOMAT, GETRANK, "automat", "auto",
                "mat" };
        final String[] tokensList = new String[5];
        final String[] posList = new String[] { "NN", "NN", "NN", "NN", "NN" };
        final String[] obtainedPosList = new String[5];
        int index = 0;
        for (Token token : JCasUtil.select(jcas, Token.class)) {
            tokensList[index] = token.getCoveredText();
            obtainedPosList[index] = token.getPos().getPosValue();
            ++index;
        }
        assertThat(tokensList, is(splitsList));
        assertThat(obtainedPosList, is(posList));
    }

    @Test
    public void testParameters()
    {

        try {
            final AnalysisEngine analysisEngine = AnalysisEngineFactory.createEngine(
                    CompoundPartTokenizer.class, CompoundPartTokenizer.PARAM_COMPOUND_SPLIT_LEVEL,
                    CompoundSplitLevel.ALL);
            analysisEngine.process(analysisEngine.newJCas());
        }
        catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.getMessage(), exception);
            Assert.fail("No exception should be thrown");
        }

    }

}
