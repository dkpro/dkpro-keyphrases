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

@TypeCapability(
        inputs={"de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Compound",
                "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Split"},
        outputs={"de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token"})

public class CompoundPartTokenizer
    extends JCasAnnotator_ImplBase
{

	/**
	 * This annotator is type agnostic, so it is mandatory to specify the type
	 * of the working annotation and how to obtain the string representation
	 * with the feature path.
	 */
	public static final String PARAM_COMPOUND_SPLIT_LEVEL = "CompoundingSplitLevel";
	@ConfigurationParameter(name = PARAM_COMPOUND_SPLIT_LEVEL, mandatory = true, defaultValue={"ALL"})
	private CompoundSplitLevel compoundSplitLevel;

    @Override
    public void process(final JCas aJCas)
        throws AnalysisEngineProcessException
    {
        for (Compound compound : JCasUtil.select(aJCas, Compound.class)) {
            for (Split compoundPart : compound.getSplitsWithoutMorpheme(compoundSplitLevel)) {
                final Token token = new Token(aJCas);
                token.setBegin(compoundPart.getBegin());
                token.setEnd(compoundPart.getEnd());
                token.addToIndexes();
            }
        }

    }

}
