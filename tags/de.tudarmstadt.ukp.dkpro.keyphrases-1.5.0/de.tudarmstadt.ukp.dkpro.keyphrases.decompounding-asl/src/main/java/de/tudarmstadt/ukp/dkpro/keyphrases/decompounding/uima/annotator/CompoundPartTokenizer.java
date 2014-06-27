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

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Compound;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Compound.CompoundSplitLevel;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Split;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

@TypeCapability(inputs = { "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Compound",
        "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Split" }, outputs = { "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token" })
public class CompoundPartTokenizer
    extends JCasAnnotator_ImplBase
{

    /**
     * Specify the level for which tokens should be added for compounds
     */
    public static final String PARAM_COMPOUND_SPLIT_LEVEL = "CompoundingSplitLevel";
    @ConfigurationParameter(name = PARAM_COMPOUND_SPLIT_LEVEL, mandatory = true, defaultValue = { "ALL" })
    private CompoundSplitLevel compoundSplitLevel;

    @Override
    public void process(final JCas aJCas)
        throws AnalysisEngineProcessException
    {
        Token token;
        for (Compound compound : JCasUtil.select(aJCas, Compound.class)) {
            final Token compoundToken = JCasUtil.selectCovered(aJCas, Token.class,
                    compound.getBegin(), compound.getEnd()).get(0);
            for (Split compoundPart : compound.getSplitsWithoutMorpheme(compoundSplitLevel)) {
                token = new Token(aJCas);
                token.setBegin(compoundPart.getBegin());
                token.setEnd(compoundPart.getEnd());
                token.setPos(compoundToken.getPos());
                token.addToIndexes();
            }
        }

    }

}
