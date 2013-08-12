package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createAggregateDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.bindResource;

import java.io.File;
import java.net.MalformedURLException;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.frequency.resources.Web1TFrequencyCountResource;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.ranking.LoglikelihoodAnnotator;

/**
 * Extractor for bookindex-phrases applying the Rayson and Garside
 * loglikelihood-metric on a background corpus.
 *
 * @author Mateusz Parzonka
 *
 */
public class LoglikelihoodExtractor
	extends BookIndexPhraseExtractor_BaseImpl
{

	private File ngramLookupModel;

	@Override
	protected AnalysisEngineDescription createRanker()
		throws ResourceInitializationException
	{
		AnalysisEngineDescription breakIteratorSegmenter = createPrimitiveDescription(BreakIteratorSegmenter.class);

		AnalysisEngineDescription logLikelihoodAnnotator = createPrimitiveDescription(
				LoglikelihoodAnnotator.class,
				LoglikelihoodAnnotator.PARAM_FEATURE_PATH, Token.class.getName());

		AnalysisEngineDescription aed = createAggregateDescription(
				breakIteratorSegmenter, logLikelihoodAnnotator);

		try {
			bindResource(aed, LoglikelihoodAnnotator.FREQUENCY_COUNT_PROVIDER_KEY,
					Web1TFrequencyCountResource.class, getNgramLookupModel().toURI()
							.toURL().toString());
		}
		catch (InvalidXMLException e) {
			throw new ResourceInitializationException(e);
		}
		catch (MalformedURLException e) {
			throw new ResourceInitializationException(e);
		}

		return aed;
	}

	public void setNgramLookupModel(File ngramLookupModel)
	{
		this.ngramLookupModel = ngramLookupModel;
	}

	public File getNgramLookupModel()
	{
		return ngramLookupModel;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("Ranker: LoglikelihoodExtractor").append(LF);
		sb.append("NgramLookupModel: ").append(ngramLookupModel.getAbsolutePath())
				.append(LF);
		return sb.toString();
	}

}
