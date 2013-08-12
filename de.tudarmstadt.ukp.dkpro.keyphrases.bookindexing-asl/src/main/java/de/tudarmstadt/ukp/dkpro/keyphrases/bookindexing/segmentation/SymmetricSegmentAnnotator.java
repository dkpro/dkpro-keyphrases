package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.segmentation;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

/**
 * Devides the cas into n segments of same length.
 *
 * @author Mateusz Parzonka
 *
 */
public class SymmetricSegmentAnnotator
	extends PseudoSentenceSegmentAnnotator
{

	public static final String PARAM_SEGMENTATION_FACTOR = "SegmentationFactor";
	@ConfigurationParameter(name = PARAM_SEGMENTATION_FACTOR, mandatory = false, defaultValue = "1")
	private int segmentationFactor;

	@Override
	public void process(JCas jCas)
		throws AnalysisEngineProcessException
	{

		getContext().getLogger().log(Level.CONFIG,
				"Entering " + getClass().getSimpleName());

		int tokenCount = JCasUtil.select(jCas, Token.class).size();

		setN((tokenCount / segmentationFactor) + 1);
		super.process(jCas);
	}

}
