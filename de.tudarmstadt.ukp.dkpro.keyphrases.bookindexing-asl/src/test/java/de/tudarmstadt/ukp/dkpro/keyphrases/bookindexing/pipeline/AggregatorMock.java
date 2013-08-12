package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.pipeline;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.Segment;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * All {@link Keyphrase}s associated to the covering {@link Segment} are printed
 * to the logger.
 *
 * @author Mateusz Parzonka
 *
 */
public class AggregatorMock
	extends JCasAnnotator_ImplBase
{

	private int jcasNr = 0;

	@Override
	public void process(JCas jcas)
		throws AnalysisEngineProcessException
	{

		getContext().getLogger().log(Level.INFO,
				"JCas " + jcasNr++ + " after segment processing: " + jcas);

		assert jcas.getAnnotationIndex(Segment.type).size() > 0 : "JCas must contain at least one Segment annotation.";

		int segNr = 0;
		for (Segment segment : JCasUtil.select(jcas, Segment.class)) {
			for (Keyphrase k : JCasUtil.selectCovered(Keyphrase.class, segment)) {
				getContext().getLogger().log(Level.INFO,
						"SegNr.:" + segNr + " " + k.toString());
			}
			segNr++;
		}

	}

}
